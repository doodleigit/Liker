package com.doodle.Reply.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.CommentTextIndex;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.MentionItem;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.service.CommentService;
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

import static com.doodle.Tool.Tools.containsIllegalCharacters;
import static com.doodle.Tool.Tools.delayLoadComment;
import static com.doodle.Tool.Tools.dismissDialog;
import static com.doodle.Tool.Tools.extractMentionText;
import static com.doodle.Tool.Tools.extractMentionUser;
import static com.doodle.Tool.Tools.extractUrls;
import static com.doodle.Tool.Tools.isContain;
import static com.doodle.Tool.Tools.isNullOrEmpty;
import static com.doodle.Tool.Tools.showBlockUser;
import static java.lang.Integer.parseInt;

public class ReplyTextHolder extends RecyclerView.ViewHolder {


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


    //Comment
    Reply replyItem;


    private String commentText, commentUserName, commentUserImage, commentImage, commentTime;

    public ImageView imageCommentSettings, imgCommentLike;

    public TextView tvCommentUserName, tvCommentTime, tvCommentReply, tvCountCommentLike;

    private PopupMenu popupCommentMenu;


    //SHOW ALL COMMENTS
    private CommentService commentService;
    int limit = 10;
    int offset = 0;
    boolean networkOk;
    //   CircularProgressView progressView;

    //mention
    private String full_text;
    List<String> mentions;
    ArrayList<String> mList;
    private String text;

    //ReplyAllComment
    private ProgressBar mProgressBar;
    public static final String REPLY_KEY = "reply_key";
    public static final String COMMENT_ITEM_KEY = "comment_item_key";
    public static final String POST_ITEM_KEY = "post_item_key";


    PostItem postItem;
    List<Reply> replyList;


    //EDIT COMMENT
    ReplyListener listener;
    String replyId = "";
    Reply reply;


    boolean isFirstTimeShowReply;

    public static final String REASON_KEY = "reason_key";
    private String commentLike;
    private int commentLikeNumeric;
    private List<MentionItem> mentionNameList;
    private ClickableSpan clickableSpan;
    private MediaPlayer player;

    public interface ReplyListener {

        void onTitleClicked(int position, Reply reply);

        void commentDelete(int position, Reply reply);

        void replyToReply(int position, Reply reply);
    }

    public ReplyTextHolder(View itemView, Context context, final ReplyListener listener) {
        super(itemView);

        mContext = context;
        this.listener = listener;

        player = MediaPlayer.create(mContext, R.raw.post_like);
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        mentionNameList=new ArrayList<>();
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
        tvPostEmojiContent = itemView.findViewById(R.id.tvPostEmojiContent);
        imagePostUser = itemView.findViewById(R.id.imagePostUser);
        tvPostContent = itemView.findViewById(R.id.tvPostContent);

        imageCommentSettings = itemView.findViewById(R.id.imageCommentSettings);

        tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
        tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
        imgCommentLike = itemView.findViewById(R.id.imgCommentLike);
        tvCommentReply = itemView.findViewById(R.id.tvCommentReply);
        tvCountCommentLike = itemView.findViewById(R.id.tvCountCommentLike);

        tvCountCommentLike.setVisibility(View.GONE);


        //All comment post
        commentService = CommentService.mRetrofit.create(CommentService.class);
        networkOk = NetworkHelper.hasNetworkAccess(mContext);
        //progressView = (CircularProgressView) itemView.findViewById(R.id.progress_view);


        //mention
        mentions = new ArrayList<>();
        mList = new ArrayList<>();

        //commentAllReply
        mProgressBar = itemView.findViewById(R.id.ProgressBar);
        replyList = new ArrayList<>();


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

    public void setItem(Reply replyItem, PostItem postItem, int position) {

        this.replyItem = replyItem;
        this.postItem = postItem;
        this.position = position;


        //  userPostId = item.getPostId();
//        commentPostId = commentItem.getPostId();


        commentText = replyItem.getCommentText();
        commentUserName = replyItem.getFirstName() + " " + replyItem.getLastName();
        commentUserImage = replyItem.getUserPhoto();
        commentImage = replyItem.getCommentImage();
        commentTime = replyItem.getDateTime();
        goldStar = Integer.parseInt(replyItem.getUserGoldStars());
        silverStar = parseInt(replyItem.getUserSliverStars());


        tvCommentUserName.setText(commentUserName);
        tvCommentTime.setText(Tools.chatDateCompare(mContext, Long.valueOf(commentTime)));

        if (containsIllegalCharacters(commentText)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(commentText);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            tvPostContent.setText(commentText);
        }




        if (!isNullOrEmpty(replyItem.getId())) {
            commentLike = replyItem.getTotalLike();
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


        tvCommentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.setReplyItem(replyItem);
                listener.replyToReply(position, replyItem);
            }
        });

        if (replyItem.isIsLikeReplied()) {
            imgCommentLike.setImageResource(R.drawable.like_done);
        } else {
            imgCommentLike.setImageResource(R.drawable.like_normal);
        }

        imgCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isNullOrEmpty(replyItem.getId())) {

                    if (userIds.equalsIgnoreCase(replyItem.getUserId())) {
                        Tools.toast(mContext, "On Liker, you can't like your own reply. That would be cheating", R.drawable.ic_insert_emoticon_black_24dp);
                    } else {
                        if (replyItem.isIsLikeReplied()) {

                            if (NetworkHelper.hasNetworkAccess(mContext)) {
                                Call<String> call = commentService.unLikeCommentReply(deviceId, profileId, token, replyItem.getCommentId(), replyItem.getId(), postItem.getPostId(), profileId);
                                sendUnLikeCommentReplyRequest(call);
                            } else {
//                            Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                Tools.showNetworkDialogs(v);
                            }

                        } else {
                            if (NetworkHelper.hasNetworkAccess(mContext)) {
                                Call<String> call = commentService.likeCommentReply(deviceId, profileId, token, replyItem.getCommentId(), replyItem.getId(), postItem.getPostId(), profileId);
                                sendLikeCommentReplyRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());

                            }
                        }
                    }


                }


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


        //mention

        text = replyItem.getCommentText();
        StringBuilder nameBuilder = new StringBuilder();
        List<String> mentionUrl = extractUrls(replyItem.getCommentText());


        for (CommentTextIndex temp : replyItem.getCommentTextIndex()) {
            String postType = temp.getType();
            if (postType.equalsIgnoreCase("mention")) {
                String mentionUserName = extractMentionUser(temp.getText());
                String userName=temp.getUsername();
                String userId=temp.getUserId();
                mentionNameList.add(new MentionItem(mentionUserName,userName,userId));
                nameBuilder.append(mentionUserName);
                nameBuilder.append(" ");
            }

        }


        if (mentionUrl.size() > 0 && extractMentionText(replyItem).trim().length() > 0) {

            full_text = extractMentionText(replyItem).trim();

            if (containsIllegalCharacters(full_text)) {
                tvPostContent.setVisibility(View.GONE);
                tvPostEmojiContent.setVisibility(View.VISIBLE);

                String nameStr = nameBuilder.toString();
                String[] mentionArr = nameStr.split(" ");
                //split strings by space
                String[] splittedWords = full_text.split(" ");
                SpannableString str = new SpannableString(full_text);
                //Check the matching words

                for (int i = 0; i < mentionArr.length; i++) {
                    for (int j = 0; j < splittedWords.length; j++) {
                        if (mentionArr[i].equalsIgnoreCase(splittedWords[j])) {
                            mList.add(mentionArr[i]);
                        }
                    }
                }

                //make the words bold

                for (int k = 0; k < mList.size(); k++) {
                    int val = full_text.indexOf(mList.get(k));
                    String name=mList.get(k);
                    for (MentionItem temp:mentionNameList) {

                        if(temp.getMentionFullName().contains(name)){
                            clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View view) {

                                    mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", temp.getMentionUserId()).putExtra("user_name", temp.getMentionUserName()));
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {

                                    ds.setColor(ds.linkColor);    // you can use custom color
                                    ds.setUnderlineText(false);    // this remove the underline
                                }
                            };
                        }
                    }
                    if (val >= 0) {
                        str.setSpan(clickableSpan, val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                text = str.toString();
                tvPostEmojiContent.setText(str);


            } else {
                tvPostEmojiContent.setVisibility(View.GONE);
                tvPostContent.setVisibility(View.VISIBLE);
                String nameStr = nameBuilder.toString();
                String[] mentionArr = nameStr.split(" ");

                //split strings by space
                String[] splittedWords = full_text.split(" ");
                SpannableString str = new SpannableString(full_text);

                //Check the matching words
                for (int i = 0; i < mentionArr.length; i++) {
                    for (int j = 0; j < splittedWords.length; j++) {
                        if (mentionArr[i].equalsIgnoreCase(splittedWords[j])) {
                            mList.add(mentionArr[i]);
                        }
                    }
                }

                //make the words bold

                for (int k = 0; k < mList.size(); k++) {
                    int val = full_text.indexOf(mList.get(k));
                    String name=mList.get(k);
                    for (MentionItem temp:mentionNameList) {

                        if(temp.getMentionFullName().contains(name)){
                            clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View view) {

                                    mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", temp.getMentionUserId()).putExtra("user_name", temp.getMentionUserName()));
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {

                                    ds.setColor(ds.linkColor);    // you can use custom color
                                    ds.setUnderlineText(false);    // this remove the underline
                                }
                            };
                        }
                    }
                    if (val >= 0) {
                        str.setSpan(clickableSpan, val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        str.setSpan(new MyClickableSpan("mystring"), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }

                }
                text = str.toString();
                tvPostContent.setText(str);

            }
        }

        imageCommentSettings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popupCommentMenu = new PopupMenu(mContext, v);
                popupCommentMenu.getMenuInflater().inflate(R.menu.post_comment_menu, popupCommentMenu.getMenu());


                String commentUserId = replyItem.getUserId();


                if (userIds.equalsIgnoreCase(commentUserId)) {
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.editComment).setVisible(true);
                } else {
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.editComment).setVisible(false);
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

                            App.setReplyItem(replyItem);
                            App.setItem(postItem);
                            activity = (AppCompatActivity) v.getContext();
                            if (NetworkHelper.hasNetworkAccess(mContext)) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, replyItem.getUserId(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }

                        }

                        if (id == R.id.blockUser) {


                            if (!((Activity) mContext).isFinishing()) {
                                App.setReplyItem(replyItem);
                                showBlockUser(v);
                            } else {
                                dismissDialog();
                            }

                        }
                        if (id == R.id.editComment) {
                            App.setReplyItem(replyItem);
                            listener.onTitleClicked(position, replyItem);

                        }

                        if (id == R.id.deleteComment) {

                            App.setReplyItem(replyItem);
                            listener.commentDelete(position, replyItem);


                        }

                        return true;
                    }
                });

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
                                    player.start();
                                    Call<String> mCall = webService.sendBrowserNotification(
                                            deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                                            profileId,//"26444",
                                            token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                                            replyItem.getUserId(),//"26444",
                                            userIds,//"26444",
                                            postItem.getPostId(),
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

                                    imgCommentLike.setImageResource(R.drawable.like_done);

                                }
                            }

                            if (isContain(object, "error")) {
                                if ("You could not liked your own reply".equalsIgnoreCase(object.getString("error"))) {
                                    Tools.toast(mContext, "You could not liked your own post", R.drawable.ic_insert_emoticon_black_24dp);
                                } else {
                                    Call<String> mCall = commentService.unLikeCommentReply(deviceId, profileId, token, replyItem.getCommentId(), replyItem.getId(), postItem.getPostId(), profileId);
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

                                    imgCommentLike.setImageResource(R.drawable.like_normal);
                                }

                            }

                            if (isContain(object, "error")) {
                                Call<String> mCall = commentService.likeCommentReply(deviceId, profileId, token, replyItem.getCommentId(), replyItem.getId(), postItem.getPostId(), profileId);
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
                    PostItem item = new PostItem();
                    CommentItem commentItems = new CommentItem();
//                    Intent intent = new Intent(mContext, CommentPost.class);
//                    intent.putExtra(COMMENT_CHILD_KEY, (Parcelable) commentItem);
//                    intent.putExtra(COMMENT_KEY, (Parcelable) commentItems);
//                    intent.putExtra(ITEM_KEY, (Parcelable) item);
//                    intent.putExtra(REASON_KEY, (Parcelable) reportReason);
//                    mContext.startActivity(intent);
//                    Log.d("replyItem: ", replyItem.toString());
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


}
