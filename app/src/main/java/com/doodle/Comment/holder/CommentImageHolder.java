package com.doodle.Comment.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.doodle.App;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.service.CommentService;
import com.doodle.Post.view.fragment.MediaFullViewFragment;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.Reply.view.ReplyPost;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.PROFILE_IMAGE;
import static com.doodle.Tool.Tools.delayLoadComment;
import static com.doodle.Tool.Tools.dismissDialog;
import static com.doodle.Tool.Tools.isContain;
import static com.doodle.Tool.Tools.isNullOrEmpty;
import static com.doodle.Tool.Tools.showBlockUser;
import static java.lang.Integer.parseInt;

public class CommentImageHolder extends RecyclerView.ViewHolder {


    public CircleImageView imagePostUser;
    public ReadMoreTextView tvPostContent;
    public EmojiTextView tvPostEmojiContent;
    public ImageView star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    public LinearLayout postBodyLayer;
    private Drawable mDrawable;


    public HomeService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;
    public static final String ITEM_KEY = "item_key";


    //Comment
    Comment_ commentItem;

    private String commentPostId;

    private String commentText, commentUserName, commentUserImage, commentImage, commentTime;
    public EmojiTextView tvCommentMessage;
    public ImageView imagePostCommenting, imageCommentSettings, imgCommentLike;

    public TextView tvCommentUserName, tvCommentTime, tvCommentReply, tvSeeReply, tvCountCommentLike;
    private String userPostId;
    private PopupMenu popupCommentMenu;


    //SHOW ALL COMMENTS
    private CommentService commentService;
    int limit = 10;
    int offset = 0;
    boolean networkOk;

    ProgressBar mProgressView;

    //ReplyAllComment
    private ProgressBar mProgressBar;

    public static final String REPLY_KEY = "reply_key";
    public static final String COMMENT_ITEM_KEY = "comment_item_key";
    public static final String POST_ITEM_KEY = "post_item_key";
    PostItem postItem;
    Reply itemReply;
    List<Reply> replyItem;

    CommentListener listener;
    String replyId = "";
    Reply reply;

    public interface CommentListener {

        void onTitleClicked(Comment_ commentItem, int position, Reply reply);

        void commentDelete(Comment_ commentItem, int position, Reply reply);
    }

    private String commentLike;
    private int commentLikeNumeric;
    private MediaPlayer player;

    public CommentImageHolder(View itemView, Context context, final CommentListener listener) {
        super(itemView);

        mContext = context;
        this.listener = listener;

        player = MediaPlayer.create(mContext, R.raw.post_like);
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        itemReply = new Reply();
        replyItem = new ArrayList<>();
        //tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);

        star1 = itemView.findViewById(R.id.star1);
        star2 = itemView.findViewById(R.id.star2);
        star3 = itemView.findViewById(R.id.star3);
        star4 = itemView.findViewById(R.id.star4);
        star5 = itemView.findViewById(R.id.star5);
        star6 = itemView.findViewById(R.id.star6);
        star7 = itemView.findViewById(R.id.star7);
        star8 = itemView.findViewById(R.id.star8);
        star9 = itemView.findViewById(R.id.star9);
        star10 = itemView.findViewById(R.id.star10);
        star11 = itemView.findViewById(R.id.star11);
        star12 = itemView.findViewById(R.id.star12);
        star13 = itemView.findViewById(R.id.star13);
        star14 = itemView.findViewById(R.id.star14);
        star15 = itemView.findViewById(R.id.star15);
        star16 = itemView.findViewById(R.id.star16);

        //Comment
        tvCommentMessage = itemView.findViewById(R.id.tvCommentMessage);
        imagePostUser = itemView.findViewById(R.id.imagePostUser);
        mProgressView = itemView.findViewById(R.id.progress_bar);

        imagePostCommenting = itemView.findViewById(R.id.imagePostCommenting);

        imageCommentSettings = itemView.findViewById(R.id.imageCommentSettings);

        tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
        tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
        imgCommentLike = itemView.findViewById(R.id.imgCommentLike);
        tvCommentReply = itemView.findViewById(R.id.tvCommentReply);
        tvCountCommentLike = itemView.findViewById(R.id.tvCountCommentLike);
        tvSeeReply = itemView.findViewById(R.id.tvSeeReply);

        tvCountCommentLike.setVisibility(View.GONE);


        //All comment post
        commentService = CommentService.mRetrofit.create(CommentService.class);
        webService = HomeService.mRetrofit.create(HomeService.class);
        networkOk = NetworkHelper.hasNetworkAccess(mContext);

        mProgressBar = (ProgressBar) itemView.findViewById(R.id.ProgressBar);

    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mDrawable = new BitmapDrawable(App.getInstance().getResources(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    int goldStar;
    int silverStar;
    int position;
    AppCompatActivity activity;

    public void setItem(Comment_ commentItem, PostItem postItem, int position) {

        this.commentItem = commentItem;
        this.postItem = postItem;
        this.position = position;

        //  userPostId = item.getPostId();
        commentPostId = commentItem.getPostId();

        commentText = commentItem.getCommentText();
        commentUserName = commentItem.getUserFirstName() + " " + commentItem.getUserLastName();
        commentUserImage = commentItem.getUserPhoto();
        commentImage = commentItem.getCommentImage();
        commentTime = commentItem.getDateTime();
        goldStar = Integer.parseInt(commentItem.getUserGoldStars());
        silverStar = parseInt(commentItem.getUserSliverStars());


        tvCommentUserName.setText(commentUserName);
        if (!isNullOrEmpty(commentText)) {
            tvCommentMessage.setVisibility(View.VISIBLE);
            tvCommentMessage.setText(commentText);
        } else {
            tvCommentMessage.setVisibility(View.GONE);
        }
        tvCommentTime.setText(Tools.chatDateCompare(mContext, Long.valueOf(commentTime)));

        if (isNullOrEmpty(commentImage)) {
            imagePostCommenting.setVisibility(View.GONE);
        } else {
            imagePostCommenting.setVisibility(View.VISIBLE);
            imagePostCommenting.setVisibility(View.VISIBLE);
            String commentImageUrl = PROFILE_IMAGE + commentImage;
         /*   Glide.with(App.getAppContext())
                    .load(commentImageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .into(imagePostCommenting);*/

            Glide.with(App.getAppContext())
                    .load(commentImageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                            mProgressView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgressView.setVisibility(View.GONE);
                            return false;
                        }


                    })
                    .into(imagePostCommenting);
        }

        imagePostCommenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentImageUrl = PROFILE_IMAGE + commentImage;
                fullMediaView("1", commentImageUrl);
            }
        });


        if (!isNullOrEmpty(commentItem.getReplyId())) {
            commentLike = commentItem.getTotalReplyLike();
        } else {
            commentLike = commentItem.getTotalLike();
        }

        if (App.isRvCommentHeader()) {
            tvSeeReply.setVisibility(View.GONE);
            tvCommentReply.setVisibility(View.GONE);
        } else {
            if (commentItem.getTotalReply().equals("0")) {
                tvSeeReply.setVisibility(View.GONE);
            } else {
                tvSeeReply.setVisibility(View.VISIBLE);
            }
            tvCommentReply.setVisibility(View.VISIBLE);
        }

        if ("0".equalsIgnoreCase(commentLike)) {
            tvCountCommentLike.setText("");

            tvCountCommentLike.setVisibility(View.GONE);
        } else {
            SpannableString content = new SpannableString(commentLike);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            tvCountCommentLike.setVisibility(View.VISIBLE);
            tvCountCommentLike.setText(content);
        }

        if (commentItem.isLikeUserStatus()) {
            imgCommentLike.setImageResource(R.drawable.like_done);
        } else {
            imgCommentLike.setImageResource(R.drawable.like_normal);
        }

        imagePostUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", commentItem.getUserId()).putExtra("user_name", commentItem.getUserName()));
            }
        });

        tvCommentUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", commentItem.getUserId()).putExtra("user_name", commentItem.getUserName()));
            }
        });


        imgCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNullOrEmpty(commentItem.getReplyId())) {

                    if (commentItem.isLikeUserStatus()) {

                        if (NetworkHelper.hasNetworkAccess(mContext)) {
                            Call<String> call = commentService.unLikeCommentReply(deviceId, profileId, token, commentItem.getCommentId(), commentItem.getReplyId(), commentItem.getPostId(), profileId);
                            sendUnLikeCommentReplyRequest(call);
                        } else {
                            Tools.showNetworkDialog(activity.getSupportFragmentManager());
                        }

                    } else {
                        if (NetworkHelper.hasNetworkAccess(mContext)) {
                            Call<String> call = commentService.likeCommentReply(deviceId, profileId, token, commentItem.getCommentId(), commentItem.getReplyId(), commentItem.getPostId(), profileId);
                            sendLikeCommentReplyRequest(call);
                        } else {
                            Tools.showNetworkDialog(activity.getSupportFragmentManager());

                        }
                    }


                } else {

                    if (commentItem.isLikeUserStatus()) {

                        if (NetworkHelper.hasNetworkAccess(mContext)) {

                            Call<String> call = commentService.commentUnLike(deviceId, profileId, token, commentItem.getId(), profileId);
                            sendCommentUnLikeRequest(call);

                        } else {
                            Tools.showNetworkDialog(activity.getSupportFragmentManager());
                        }

                    } else {

                        if (NetworkHelper.hasNetworkAccess(mContext)) {

                            Call<String> call = commentService.commentLike(deviceId, profileId, token, commentItem.getId(), userIds);
                            sendCommentLikeRequest(call);

                        } else {
                            Tools.showNetworkDialog(activity.getSupportFragmentManager());
                        }
                    }

                }

                //commentLike

            }
        });


        if (silverStar > 8) {
            silverStar = 8;
        }
        switch (silverStar) {
            case 1:
                star9.setVisibility(View.VISIBLE);
                break;
            case 2:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                break;
            case 3:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                break;
            case 4:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                break;
            case 5:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                break;
            case 6:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                break;
            case 7:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                star15.setVisibility(View.VISIBLE);
                break;
            case 8:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                star15.setVisibility(View.VISIBLE);
                star16.setVisibility(View.VISIBLE);
                break;
            case 0:
                star9.setVisibility(View.GONE);
                star10.setVisibility(View.GONE);
                star11.setVisibility(View.GONE);
                star12.setVisibility(View.GONE);
                star13.setVisibility(View.GONE);
                star14.setVisibility(View.GONE);
                star15.setVisibility(View.GONE);
                star16.setVisibility(View.GONE);
                break;

        }
        if (goldStar > 8) {
            goldStar = 8;
        }
        switch (goldStar) {
            case 1:
                star1.setVisibility(View.VISIBLE);
                break;
            case 2:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                break;
            case 3:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                break;
            case 4:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                break;
            case 5:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                break;
            case 6:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                break;
            case 7:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                star7.setVisibility(View.VISIBLE);
                break;
            case 8:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                star7.setVisibility(View.VISIBLE);
                star8.setVisibility(View.VISIBLE);
                break;
            case 0:
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.GONE);
                star6.setVisibility(View.GONE);
                star7.setVisibility(View.GONE);
                star8.setVisibility(View.GONE);
                break;

        }

        String userImageUrl = AppConstants.PROFILE_IMAGE + commentUserImage;
        Glide.with(App.getAppContext())
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imagePostUser);

        imageCommentSettings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popupCommentMenu = new PopupMenu(mContext, v);
                popupCommentMenu.getMenuInflater().inflate(R.menu.post_comment_menu, popupCommentMenu.getMenu());
                String commentUserId = commentItem.getUserId();
                if (userIds.equalsIgnoreCase(commentUserId)) {
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.editComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(true);
                } else {
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.editComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(false);
                }

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupCommentMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupCommentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.reportComment) {

                            App.setCommentItem(commentItem);
                            activity = (AppCompatActivity) v.getContext();
                            if (NetworkHelper.hasNetworkAccess(mContext)) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, commentItem.getUserId(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }

                        }

                        if (id == R.id.blockUser) {
                            if (!((Activity) mContext).isFinishing()) {
                                App.setCommentItem(commentItem);
                                showBlockUser(v);
                            } else {
                                dismissDialog();
                            }
                        }
                        if (id == R.id.editComment) {

                            Reply replyItem = new Reply();
                            List<Reply> replyList = commentItem.getReplies();
                            if (replyList.size() == 0) {
                                Reply reply = new Reply();
                                reply.setId("1");
                                reply.setCommentId("2");
                                listener.onTitleClicked(commentItem, position, reply);
                            } else {
                                replyItem = replyList.get(0);
                                listener.onTitleClicked(commentItem, position, replyItem);
                            }

                        }

                        if (id == R.id.deleteComment) {

                            Reply replyItem = new Reply();
                            List<Reply> replyList = commentItem.getReplies();
                            if (replyList.size() == 0) {
                                Reply reply = new Reply();
                                reply.setId("1");
                                reply.setCommentId("2");
                                listener.commentDelete(commentItem, position, reply);
                            } else {
                                replyItem = replyList.get(0);
                                listener.commentDelete(commentItem, position, replyItem);
                            }


                        }

                        return true;
                    }
                });

            }
        });

        if (!isNullOrEmpty(commentItem.getTotalReply()) && Integer.parseInt(commentItem.getTotalReply()) > 0) {
            if (Integer.parseInt(commentItem.getTotalReply()) == 1) {
                tvSeeReply.setText(String.format("View %s reply", commentItem.getTotalReply()));
                // tvCommentReply.setText(String.format("%s Reply", commentItem.getTotalReply()));
            } else {
                // tvCommentReply.setText(String.format("%s Replies", commentItem.getTotalReply()));
                tvSeeReply.setText(String.format("View %s replies", commentItem.getTotalReply()));
            }
        }

        tvSeeReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                String commentReply = commentItem.getTotalReply();
                if (Integer.parseInt(commentReply) > 0) {
                    if (NetworkHelper.hasNetworkAccess(mContext)) {

                        Call<List<Reply>> call = commentService.getPostCommentReplyList(deviceId, profileId, token, commentItem.getId(), "false", limit, offset, commentItem.getPostId(), userIds);
                        sendAllCommentReplyListRequest(call);
                        delayLoadComment(mProgressBar);
                    } else {
                        Tools.showNetworkDialog(activity.getSupportFragmentManager());

                    }
                } else {
                    Intent intent = new Intent(mContext, ReplyPost.class);
                    intent.putExtra(COMMENT_ITEM_KEY, (Parcelable) commentItem);
                    intent.putExtra(POST_ITEM_KEY, (Parcelable) postItem);
                    intent.putParcelableArrayListExtra(REPLY_KEY, (ArrayList<? extends Parcelable>) replyItem);

                    mContext.startActivity(intent);
                }

            }
        });
        tvCommentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                String commentReply = commentItem.getTotalReply();
                if (Integer.parseInt(commentReply) > 0) {
                    if (networkOk) {

                        Call<List<Reply>> call = commentService.getPostCommentReplyList(deviceId, profileId, token, commentItem.getId(), "false", limit, offset, commentItem.getPostId(), userIds);
                        sendAllCommentReplyListRequest(call);
                        delayLoadComment(mProgressBar);
                    } else {
                        Tools.showNetworkDialog(activity.getSupportFragmentManager());

                    }
                } else {
                    Intent intent = new Intent(mContext, ReplyPost.class);
                    intent.putExtra(COMMENT_ITEM_KEY, (Parcelable) commentItem);
                    intent.putExtra(POST_ITEM_KEY, (Parcelable) postItem);
                    intent.putParcelableArrayListExtra(REPLY_KEY, (ArrayList<? extends Parcelable>) replyItem);

                    mContext.startActivity(intent);
                }

            }
        });

    }

    private void sendLikeCommentReplyRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (isContain(object, "status")) {
                                String status = object.getString("status");
                                if ("true".equalsIgnoreCase(status)) {

                                    Call<String> mCall = webService.sendBrowserNotification(
                                            deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                                            profileId,//"26444",
                                            token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                                            commentItem.getUserId(),//"26444",
                                            userIds,//"26444",
                                            commentItem.getPostId(),
                                            "like_on_reply"
                                    );
                                    sendBrowserNotificationRequest(mCall);

                                    commentLikeNumeric = Integer.parseInt(commentLike);
                                    commentLikeNumeric++;
                                    commentLike = String.valueOf(commentLikeNumeric);


                                    SpannableString content = new SpannableString(String.valueOf(commentLikeNumeric));
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                                    tvCountCommentLike.setVisibility(View.VISIBLE);
                                    tvCountCommentLike.setText(content);

                                }
                            }

                            if (isContain(object, "error")) {
                                if ("You could not liked your own reply".equalsIgnoreCase(object.getString("error"))) {
                                    Tools.toast(mContext, "You could not liked your own post", R.drawable.ic_insert_emoticon_black_24dp);
                                } else {
                                    Call<String> mCall = commentService.unLikeCommentReply(deviceId, profileId, token, commentItem.getCommentId(), commentItem.getReplyId(), commentItem.getPostId(), profileId);
                                    sendUnLikeCommentReplyRequest(mCall);
                                }

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

    private void sendUnLikeCommentReplyRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());

                            if (isContain(object, "status")) {
                                String status = object.getString("status");
                                if ("true".equalsIgnoreCase(status)) {
                                    commentLikeNumeric = Integer.parseInt(commentLike);
                                    commentLikeNumeric--;
                                    commentLike = String.valueOf(commentLikeNumeric);

                                    if (0 == commentLikeNumeric) {
                                        tvCountCommentLike.setText("");

                                        tvCountCommentLike.setVisibility(View.GONE);
                                    } else {
                                        SpannableString content = new SpannableString(String.valueOf(commentLikeNumeric));
                                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                                        tvCountCommentLike.setVisibility(View.VISIBLE);
                                        tvCountCommentLike.setText(content);
                                    }
                                }


                            }

                            if (isContain(object, "error")) {
                                Call<String> mCall = commentService.likeCommentReply(deviceId, profileId, token, commentItem.getCommentId(), commentItem.getReplyId(), commentItem.getPostId(), profileId);
                                sendLikeCommentReplyRequest(mCall);
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


    private void sendCommentUnLikeRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());

                            if (isContain(object, "status")) {
                                String status = object.getString("status");
                                if ("true".equalsIgnoreCase(status)) {
                                    commentLikeNumeric = Integer.parseInt(commentLike);
                                    commentLikeNumeric--;
                                    commentLike = String.valueOf(commentLikeNumeric);

                                    if (0 == commentLikeNumeric) {
                                        tvCountCommentLike.setText("");

                                        tvCountCommentLike.setVisibility(View.GONE);
                                    } else {
                                        SpannableString content = new SpannableString(String.valueOf(commentLikeNumeric));
                                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                                        tvCountCommentLike.setVisibility(View.VISIBLE);
                                        tvCountCommentLike.setText(content);
                                    }
                                }

                                imgCommentLike.setImageResource(R.drawable.like_normal);
                            }

                            if (isContain(object, "error")) {
                                Call<String> mCall = commentService.commentLike(deviceId, profileId, token, commentItem.getId(), userIds);
                                sendCommentLikeRequest(mCall);
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

    private void sendCommentLikeRequest(Call<String> call) {


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            if (isContain(object, "status")) {
                                String status = object.getString("status");
                                if ("true".equalsIgnoreCase(status)) {
                                    if (Tools.checkNormalModeIsOn(mContext))
                                        player.start();
                                    Call<String> mCall = webService.sendBrowserNotification(
                                            deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                                            profileId,//"26444",
                                            token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                                            commentItem.getUserId(),//"26444",
                                            userIds,//"26444",
                                            commentItem.getPostId(),
                                            "like_comment"
                                    );
                                    sendBrowserNotificationRequest(mCall);

                                    commentLikeNumeric = Integer.parseInt(commentLike);
                                    commentLikeNumeric++;
                                    commentLike = String.valueOf(commentLikeNumeric);


                                    SpannableString content = new SpannableString(String.valueOf(commentLikeNumeric));
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                                    tvCountCommentLike.setVisibility(View.VISIBLE);
                                    tvCountCommentLike.setText(content);
                                    imgCommentLike.setImageResource(R.drawable.like_done);
                                }
                            }

                            if (isContain(object, "error")) {
                                if ("You could not liked your own post".equalsIgnoreCase(object.getString("error"))) {
                                    Tools.toast(mContext, "You could not liked your own post", R.drawable.ic_insert_emoticon_black_24dp);
                                } else {
                                    Call<String> mCall = commentService.commentUnLike(deviceId, profileId, token, commentItem.getId(), profileId);
                                    sendCommentUnLikeRequest(mCall);
                                }

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

    private void sendBrowserNotificationRequest(Call<String> mCall) {


        mCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {


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
                    reportReasonSheet.show(activity.getSupportFragmentManager(), "ReportReasonSheet");
                }


            }

            @Override
            public void onFailure(Call<ReportReason> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });

    }

    private void sendAllCommentReplyListRequest(Call<List<Reply>> call) {

        call.enqueue(new Callback<List<Reply>>() {

            @Override
            public void onResponse(Call<List<Reply>> mCall, Response<List<Reply>> response) {


                if (response.body() != null) {
                    replyItem = response.body();

                    Intent intent = new Intent(mContext, ReplyPost.class);
                    intent.putExtra(COMMENT_ITEM_KEY, (Parcelable) commentItem);
                    intent.putExtra(POST_ITEM_KEY, (Parcelable) postItem);
                    intent.putParcelableArrayListExtra(REPLY_KEY, (ArrayList<? extends Parcelable>) replyItem);
                    mContext.startActivity(intent);

                } else {
                    Intent intent = new Intent(mContext, ReplyPost.class);
                    mContext.startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<List<Reply>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }


    private void fullMediaView(String postType, String url) {
        FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
        Fragment prev = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new MediaFullViewFragment();

        Bundle bundle = new Bundle();
        bundle.putString("post_type", postType);
        bundle.putString("url", url);
        dialogFragment.setArguments(bundle);

        dialogFragment.show(ft, "dialog");
    }
}
