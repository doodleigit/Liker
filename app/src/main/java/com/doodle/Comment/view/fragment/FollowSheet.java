package com.doodle.Comment.view.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentService;
import com.doodle.Home.model.PostItem;
import com.doodle.R;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.Tools.isEmpty;

public class FollowSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    public static final String COMMENT_key = "comment_key";
    private Comment_ commentItem;
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    private String toId;

    public static FollowSheet newInstance(String message, Comment_ commentItem) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putParcelable(ReportSendCategorySheet.COMMENT_key, commentItem);
        args.putString(FollowSheet.MESSAGE_key, message);

        FollowSheet fragment = new FollowSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private String message,personTitle,personSubTitle,personName;
    private String deviceId, profileId, token, userIds;
    private String reasonId;
    boolean networkOk;
    private CommentService commentService;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);

        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();

        commentService = CommentService.mRetrofit.create(CommentService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());

        Bundle argument = getArguments();
        commentItem = new Comment_();
        if (argument != null) {
            message = argument.getString(MESSAGE_key);
            commentItem = argument.getParcelable(COMMENT_key);
            if(!isEmpty(commentItem)){

                personName=commentItem.getUserFirstName()+" "+commentItem.getUserLastName();
            }else {
                PostItem item=new PostItem();
                item=App.getItem();
                personName=item.getUserFirstName()+" "+item.getUserLastName();
            }
            personTitle="Unfollow "+personName;
            personSubTitle="Are you sure that you want to unfollow "+ personName+" ?";
        }
    }



   TextView tvSubTitle,tvTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.follow, container, false);

        root.findViewById(R.id.tvCancel).setOnClickListener(this);
        root.findViewById(R.id.tvUnfollow).setOnClickListener(this);
        tvSubTitle=root.findViewById(R.id.tvSubTitle);
        tvTitle=root.findViewById(R.id.tvTitle);
        tvTitle.setText(personTitle);
        tvSubTitle.setText(personSubTitle);
        return root;
    }



    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.tvUnfollow:

                if(!isEmpty(commentItem)){
//                    commentId = commentItem.getId();
                    toId=commentItem.getUserId();
                    //postId = commentItem.getPostId();
                    //reportType="3";
                }else {
                    PostItem item=new PostItem();
                    item=App.getItem();
                    //commentId="";
                    toId=item.getPostUserid();
                  //  reportType="2";
                    //postId=item.getPostId();
                }

                Call<String> call = commentService.unfollow(deviceId, profileId, token, toId, userIds);
                sendUnfollowRequest(call);

                break;
            case R.id.tvCancel:
                dismiss();
                break;
        }


    }

    private void sendUnfollowRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");

                            if (status) {
                                // adapter.notifyDataSetChanged();
                                Tools.toast(getActivity(),"your message was successfully sent",R.drawable.icon_checked);
                                dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("message",t.getMessage());
            }
        });
    }

    public interface BottomSheetListener {
        void onUnfollowClicked(int image, String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


}

