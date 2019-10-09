package com.doodle.Post.view.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.doodle.App;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.fragment.BlockUserDialog;
import com.doodle.Comment.view.fragment.FollowSheet;
import com.doodle.Comment.view.fragment.ReportLikerMessageSheet;
import com.doodle.Comment.view.fragment.ReportPersonMessageSheet;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Comment.view.fragment.ReportSendCategorySheet;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.view.activity.Home;
import com.doodle.Post.view.fragment.MultipleMediaPopUpFragment;
import com.doodle.Post.view.fragment.NewMultipleMediaPopUpFragment;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.Tools.isEmpty;

public class PostPopup extends AppCompatActivity
        implements
        ReportReasonSheet.BottomSheetListener,
        ReportSendCategorySheet.BottomSheetListener,
        ReportPersonMessageSheet.BottomSheetListener,
        ReportLikerMessageSheet.BottomSheetListener,
        FollowSheet.BottomSheetListener,
        BlockUserDialog.BlockListener {

    PostItem postItem;
    private String reportId;
    private boolean hasFooter, isCommentAction;
    private int position, mediaPosition;
    private boolean networkOk;
    private String profileId;
    private String blockUserId;
    private PrefManager manager;
    private String deviceId, userId, userName;
    private String token;
    private CommentService commentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_popup);

        postItem = getIntent().getExtras().getParcelable(AppConstants.ITEM_KEY);
        hasFooter = getIntent().getBooleanExtra("has_footer", false);
        isCommentAction = getIntent().getBooleanExtra("is_comment_action", false);
        position = getIntent().getIntExtra("position", -1);
        mediaPosition = getIntent().getIntExtra("media_position", -1);

        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        userName = manager.getUserName();
        profileId = manager.getProfileId();
        token = manager.getToken();
        commentService = CommentService.mRetrofit.create(CommentService.class);

//        initialFragment(new NewMultipleMediaPopUpFragment());
        initialFragment(new MultipleMediaPopUpFragment());
    }

    private void initialFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.ITEM_KEY, postItem);
        bundle.putBoolean("has_footer", hasFooter);
        bundle.putBoolean("is_comment_action", isCommentAction);
        bundle.putInt("position", position);
        bundle.putInt("media_position", mediaPosition);
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onButtonClicked(int image, String reasonId) {
        this.reportId = reasonId;
        Comment_ commentChild = new Comment_();
        commentChild = App.getCommentItem();
        boolean isFollow = App.isIsFollow();
        ReportSendCategorySheet reportSendCategorySheet = ReportSendCategorySheet.newInstance(reportId, commentChild, isFollow);
        reportSendCategorySheet.show(getSupportFragmentManager(), "ReportSendCategorySheet");

    }

    @Override
    public void onFollowClicked(int image, String text) {
        String message = text;

        Comment_ commentChild = new Comment_();
        commentChild = App.getCommentItem();

        FollowSheet followSheet = FollowSheet.newInstance(reportId, commentChild);
        followSheet.show(getSupportFragmentManager(), "FollowSheet");
    }

    @Override
    public void onReportLikerClicked(int image, String text) {
        String message = text;
        Comment_ commentChild = new Comment_();
        commentChild = App.getCommentItem();
        ReportLikerMessageSheet reportLikerMessageSheet = ReportLikerMessageSheet.newInstance(reportId, commentChild);
        reportLikerMessageSheet.show(getSupportFragmentManager(), "ReportLikerMessageSheet");
    }

    @Override
    public void onPersonLikerClicked(int image, String text) {
        String message = text;
        Comment_ commentChild = new Comment_();
        commentChild=null;
        Reply reply=new Reply();
        ReportPersonMessageSheet reportPersonMessageSheet = ReportPersonMessageSheet.newInstance(reportId, commentChild,reply);
        reportPersonMessageSheet.show(getSupportFragmentManager(), "ReportPersonMessageSheet");
    }

    @Override
    public void onReportPersonMessageClicked(int image, String text) {

    }

    @Override
    public void onReportLikerMessageClicked(int image, String text) {

    }

    @Override
    public void onUnfollowClicked(int image, String text) {

    }


    @Override
    public void onBlockResult(DialogFragment dlg) {


        PostItem item = new PostItem();
        item = App.getItem();
        if (!isEmpty(item)) {

            blockUserId = item.getPostUserid();
        }


        if (networkOk) {
            Call<String> call = commentService.blockedUser(deviceId, profileId, token, blockUserId, userId);
            sendBlockUserRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
        }

    }

    private void sendBlockUserRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");

                            if (status) {
                                // Tools.toast(Home.this, "your message was successfully sent", R.drawable.icon_checked);
                                Intent intent = new Intent(PostPopup.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

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
                Log.d("message", t.getMessage());
            }
        });
    }

    @Override
    public void onCancelResult(DialogFragment dlg) {

    }
}
