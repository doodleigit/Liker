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
import android.widget.EditText;
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
import static com.doodle.Tool.Tools.isNullOrWhiteSpace;

public class ReportPersonMessageSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    public static final String COMMENT_key = "comment_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    private Comment_ commentItem;
    private CommentService commentService;
    private String commentId,postId,toId;
    private String reportType;


    public static ReportPersonMessageSheet newInstance(String message, Comment_ commentItem) {

        Bundle args = new Bundle();
        //args.putString(ExampleBottomSheetDialog.MESSAGE_key, resend);
//        args.putParcelable(ResendEmail.MESSAGE_key, message);
        args.putString(ReportPersonMessageSheet.MESSAGE_key, message);
        args.putParcelable(ReportSendCategorySheet.COMMENT_key, commentItem);
        ReportPersonMessageSheet fragment = new ReportPersonMessageSheet();
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

        commentItem = new Comment_();
        Bundle argument = getArguments();
        if (argument != null) {
            reasonId = argument.getString(MESSAGE_key);
            commentItem = argument.getParcelable(COMMENT_key);
            if(!isEmpty(commentItem)){

                personName=commentItem.getUserFirstName()+" "+commentItem.getUserLastName();
            }else {
                PostItem item=new PostItem();
                item=App.getItem();
                personName=item.getUserFirstName()+" "+item.getUserLastName();
            }
            personTitle="Send a Message to "+personName;
            personSubTitle="Letting "+ personName+" know what you think may help him make a better post next time.";

        }
    }


    private TextView tvReportPersonTitle,tvReportPersonSubTitle,tvReportPersonName;
    private EditText etReportPerson;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.report_person_message, container, false);

        root.findViewById(R.id.imgCloseReason).setOnClickListener(this);
        root.findViewById(R.id.btnReasonContinue).setOnClickListener(this);

        tvReportPersonTitle=root.findViewById(R.id.tvReportPersonTitle);
        tvReportPersonSubTitle=root.findViewById(R.id.tvReportPersonSubTitle);
        tvReportPersonName=root.findViewById(R.id.tvReportPersonName);

        etReportPerson=root.findViewById(R.id.etReportPerson);


        tvReportPersonTitle.setText(personTitle);
        tvReportPersonSubTitle.setText(personSubTitle);
        tvReportPersonName.setText(personName);

        return root;
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btnReasonContinue:
                if (networkOk) {

                    if(!isEmpty(commentItem)){
                         commentId = commentItem.getId();
                         toId=commentItem.getUserId();
                         postId = commentItem.getPostId();
                         reportType="3";
                    }else {
                        PostItem item=new PostItem();
                        item=App.getItem();
                        commentId="";
                        toId=item.getPostUserid();
                        reportType="2";
                        postId=item.getPostId();
                    }

                    message = etReportPerson.getText().toString();

                    if(!isNullOrWhiteSpace(message)){
                        Call<String> call = commentService.reportUser(deviceId, profileId, token, commentId, message, postId, reasonId, reportType, "1", toId, userIds);
                        sendReportUserRequest(call);
                    }else {
                        Tools.toast(getActivity(),"Message Required!",R.drawable.icon_checked);
                    }


                    // delayLoadComment(mProgressBar);
                } else {
                    Tools.showNetworkDialog(getActivity().getSupportFragmentManager());

                }
                break;
            case R.id.imgCloseReason:
                dismiss();
                break;
        }


    }

    private void sendReportUserRequest(Call<String> call) {

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
        void onReportPersonMessageClicked(int image, String text);
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

