package com.doodle.Home.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.fragment.BlockUserDialog;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.LoadCompleteListener;
import com.doodle.Home.view.activity.EditPost;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.Tools.sendNotificationRequest;
import static com.doodle.Tool.Tools.showBlockUser;

public class PostPermissionSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    public static final String POST_ITEM_POSITION_key = "post_item_position_key";
    public static final String ITEM_KEY = "item_key";
    private PrefManager manager;
    private BottomSheetBehavior mBehavior;
    private List<Reason> reasonList;

    private String deviceId, profileId, token, userIds;
    private CommentService commentService;
    private boolean networkOk;
    private PostItem mPostItem;
    private boolean notificationOff;
    int position;

    public static PostPermissionSheet newInstance(PostItem postItem, int position) {

        Bundle args = new Bundle();
        args.putParcelable(MESSAGE_key, postItem);
        args.putInt(POST_ITEM_POSITION_key, position);
        //  args.putString(ReportReasonSheet.MESSAGE_key, message);

        PostPermissionSheet fragment = new PostPermissionSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

//    private String message;

    private HomeService webService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        commentService = CommentService.mRetrofit.create(CommentService.class);
        webService = HomeService.mRetrofit.create(HomeService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getContext());
        mPostItem = new PostItem();
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        Bundle argument = getArguments();
        if (argument != null) {
            PostItem postItem = argument.getParcelable(MESSAGE_key);
            App.setItem(postItem);
            mPostItem = postItem;
            position = argument.getInt(POST_ITEM_POSITION_key);
            Log.d("", postItem.toString() + position);
        }

    }


    private RadioGroup radioGroupReason;
    private RadioButton radioNudity, radioViolence, radioHarassment, radioInjury, radioFalseNews, radioSpam, radioUnauthorized, radioHateSpeech, radioOthers;
    private String reasonId;
    private ViewGroup reportContainer, blockContainer, publicContainer, friendsContainer, privateContainer, notificationContainer, editContainer, deleteContainer;
    private String notificationTitle;
    private TextView tvNotificationTitle;
    private ImageView imageNotification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_permission_sheet, container, false);

        tvNotificationTitle = root.findViewById(R.id.tvNotificationTitle);
        imageNotification = root.findViewById(R.id.imageNotification);
        reportContainer = root.findViewById(R.id.reportContainer);
        reportContainer.setOnClickListener(this);
        blockContainer = root.findViewById(R.id.blockContainer);
        blockContainer.setOnClickListener(this);
        publicContainer = root.findViewById(R.id.publicContainer);
        publicContainer.setOnClickListener(this);
        friendsContainer = root.findViewById(R.id.friendsContainer);
        friendsContainer.setOnClickListener(this);
        privateContainer = root.findViewById(R.id.privateContainer);
        privateContainer.setOnClickListener(this);
        notificationContainer = root.findViewById(R.id.notificationContainer);
        notificationContainer.setOnClickListener(this);
        editContainer = root.findViewById(R.id.editContainer);
        editContainer.setOnClickListener(this);
        deleteContainer = root.findViewById(R.id.deleteContainer);
        deleteContainer.setOnClickListener(this);
        String postUserId = App.getItem().getPostUserid();
        if (userIds.equalsIgnoreCase(postUserId)) {
            reportContainer.setVisibility(View.GONE);
            blockContainer.setVisibility(View.GONE);
            publicContainer.setVisibility(View.VISIBLE);
            friendsContainer.setVisibility(View.VISIBLE);
            privateContainer.setVisibility(View.VISIBLE);
            //  notificationContainer.setVisibility(View.VISIBLE);
            editContainer.setVisibility(View.VISIBLE);
            deleteContainer.setVisibility(View.VISIBLE);
        } else {
            reportContainer.setVisibility(View.VISIBLE);
            blockContainer.setVisibility(View.VISIBLE);
            publicContainer.setVisibility(View.GONE);
            friendsContainer.setVisibility(View.GONE);
            privateContainer.setVisibility(View.GONE);
            //    notificationContainer.setVisibility(View.VISIBLE);
            editContainer.setVisibility(View.GONE);
            deleteContainer.setVisibility(View.GONE);
        }
        boolean isNotificationOff = mPostItem.isIsNotificationOff();


        if (App.isNotificationStatus()) {

            if (App.isNotificationOff()) {
                tvNotificationTitle.setText("Turn on notifications");
                imageNotification.setImageResource(R.drawable.ic_notifications_black_24dp);
                // popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);
            } else {
                tvNotificationTitle.setText("Turn off notifications");
                imageNotification.setImageResource(R.drawable.ic_notifications_off_black_24dp);
                // popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

            }


        } else {
            if (isNotificationOff) {
                tvNotificationTitle.setText("Turn on notifications");
                imageNotification.setImageResource(R.drawable.ic_notifications_black_24dp);
                //  popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);

            } else {
                tvNotificationTitle.setText("Turn off notifications");
                imageNotification.setImageResource(R.drawable.ic_notifications_off_black_24dp);
                //   popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

            }
        }


        return root;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.reportContainer:
                if (networkOk) {
                    Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, mPostItem.getPostUserid(), "2", userIds);
                    sendReportReason(call);
                } else {
                    Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                }

                break;
            case R.id.blockContainer:
                BlockUserDialog blockUserDialog = new BlockUserDialog();
                // TODO: Use setCancelable() to make the dialog non-cancelable
                blockUserDialog.setCancelable(false);
                blockUserDialog.show(getActivity().getSupportFragmentManager(), "BlockUserDialog");
                dismiss();
                break;
            case R.id.publicContainer:

                if (networkOk) {
                    Call<String> call = webService.postPermission(deviceId, profileId, token, "0", mPostItem.getPostId());
                    sendPostPermissionRequest(call, "0");
                } else {
                    Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                }
                dismiss();
                break;
            case R.id.friendsContainer:
                if (networkOk) {
                    Call<String> call = webService.postPermission(deviceId, profileId, token, "2", mPostItem.getPostId());
                    sendPostPermissionRequest(call, "2");
                } else {
                    Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                }
                dismiss();
                break;
            case R.id.privateContainer:
                if (networkOk) {
                    Call<String> call = webService.postPermission(deviceId, profileId, token, "1", mPostItem.getPostId());
                    sendPostPermissionRequest(call, "1");
                } else {
                    Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                }
                dismiss();
                break;
            case R.id.editContainer:
                Intent intent = new Intent(getActivity(), EditPost.class);
                App.setPosition(position);
                intent.putExtra(ITEM_KEY, (Parcelable) mPostItem);
                intent.putExtra("position", position);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                dismiss();
                break;

            case R.id.deleteContainer:
                deletePost(mPostItem,position);
                dismiss();
                break;

            case R.id.notificationContainer:
                notificationTitle = tvNotificationTitle.getText().toString();
                switch (notificationTitle) {
                    case "Turn off notifications":

                        App.setNotificationOff(true);
                        if (networkOk) {
                            Call<String> call = webService.postNotificationTurnOff(deviceId, profileId, token, userIds, mPostItem.getPostId());
                            sendNotificationRequest(call);
                        } else {
                            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                        }
                        break;
                    case "Turn on notifications":
                        App.setNotificationOff(false);
                        if (networkOk) {
                            Call<String> call = webService.postNotificationTurnOn(deviceId, profileId, token, userIds, mPostItem.getPostId());
                            sendNotificationRequest(call);
                        } else {
                            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                        }
                        break;

                }
                dismiss();
                break;
        }


    }


    private void deletePost(PostItem deletePostItem, int deletePosition) {
        new AlertDialog.Builder(getActivity())
                //  .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this post? You will permanently lose this post !")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (networkOk) {
                            Call<String> call = webService.postDelete(deviceId, profileId, token, userIds, deletePostItem.getPostId());
                            sendDeletePostRequest(call);
                        } else {
                            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                        }


                    }
                })
                .setNegativeButton(android.R.string.no, null)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void sendDeletePostRequest(Call<String> call) {


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
//                                postItemList.remove(deletePostItem);
//                                adapter.deleteItem(deletePosition);

                                App.getAppContext().sendBroadcast(new Intent(AppConstants.PERMISSION_CHANGE_BROADCAST).putExtra("post_item", (Parcelable) mPostItem).putExtra("position", position).putExtra("type", "delete"));

//                                postItemList.remove(deletePosition);
//                                adapter.notifyDataSetChanged();
//                                offset--;
//                                recyclerView.smoothScrollToPosition(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });


    }


    private void sendPostPermissionRequest(Call<String> call, String permission) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
//                                if ("1".equalsIgnoreCase(isShared)) {
                                    mPostItem.setPermission(permission);
                                    App.getAppContext().sendBroadcast(new Intent(AppConstants.PERMISSION_CHANGE_BROADCAST).putExtra("post_item", (Parcelable) mPostItem).putExtra("position", position).putExtra("type", "permission"));

//                                }
//                                else {
//                                    switch (postPermissions) {
//                                        case "Public":
//                                            mPostItem.setPermission("1");
//                                            imagePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
//                                            break;
//                                        case "Friends":
//                                            mPostItem.setPermission("2");
//                                            imagePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
//                                            break;
//                                        case "Only Me":
//                                            mPostItem.setPermission("0");
//                                            imagePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
//                                            getContext().sendBroadcast(new Intent(AppConstants.PERMISSION_CHANGE_BROADCAST).putExtra("post_item", (Parcelable) mPostItem).putExtra("position", 0));
//                                            break;
//                                    }
//
//                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void sendReportReason(Call<ReportReason> call) {
        call.enqueue(new Callback<ReportReason>() {

            @Override
            public void onResponse(Call<ReportReason> mCall, Response<ReportReason> response) {


                if (response.body() != null) {
                    ReportReason reportReason = response.body();
                    boolean isFollowed = reportReason.isFollowed();
                    App.setIsFollow(isFollowed);
                    List<Reason> reasonList = reportReason.getReason();
                    ReportReasonSheet reportReasonSheet = ReportReasonSheet.newInstance(reasonList);
                    reportReasonSheet.show(getActivity().getSupportFragmentManager(), "ReportReasonSheet");
                    dismiss();
                }


            }

            @Override
            public void onFailure(Call<ReportReason> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }

    public interface BottomSheetListener {
        void postPermissionEnable(int image, String reasonId);
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

