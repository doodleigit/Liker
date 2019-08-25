package com.doodle.Comment.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
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
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.view.activity.CommentPost;
import com.doodle.Comment.view.activity.ReplyPost;
import com.doodle.Comment.view.fragment.BlockUserDialog;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Home.service.TextHolder.COMMENT_KEY;
import static com.doodle.Home.service.TextHolder.ITEM_KEY;
import static com.doodle.utils.AppConstants.PROFILE_IMAGE;
import static com.doodle.utils.Utils.containsIllegalCharacters;
import static com.doodle.utils.Utils.delayLoadComment;
import static com.doodle.utils.Utils.dismissDialog;
import static com.doodle.utils.Utils.extractMentionText;
import static com.doodle.utils.Utils.extractMentionUser;
import static com.doodle.utils.Utils.extractUrls;
import static com.doodle.utils.Utils.getSpannableStringBuilder;
import static com.doodle.utils.Utils.isNullOrEmpty;
import static com.doodle.utils.Utils.showBlockUser;
import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class CommentTextHolder extends RecyclerView.ViewHolder  {


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
    Comment_ commentItem;


    private String commentText, commentUserName, commentUserImage, commentImage, commentTime;
    public EmojiTextView tvCommentMessage;
    public ImageView imagePostCommenting, imageCommentLikeThumb, imageCommentSettings;

    public TextView tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike;

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
    List<Reply> replyItem;


    //EDIT COMMENT
    CommentListener listener;
    String replyId = "";
    Reply reply;


    boolean isFirstTimeShowReply;

    public static final String REASON_KEY = "reason_key";




    public interface CommentListener {

        void onTitleClicked(Comment_ commentItem, int position, Reply reply);

        void commentDelete(Comment_ commentItem, int position, Reply reply);
    }

    public CommentTextHolder(View itemView, Context context, final CommentListener listener) {
        super(itemView);

        mContext = context;
        this.listener = listener;
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();


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

        imagePostCommenting = itemView.findViewById(R.id.imagePostCommenting);
        imageCommentLikeThumb = itemView.findViewById(R.id.imageCommentLikeThumb);
        imageCommentSettings = itemView.findViewById(R.id.imageCommentSettings);

        tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
        tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
        tvCommentLike = itemView.findViewById(R.id.tvCommentLike);
        tvCommentReply = itemView.findViewById(R.id.tvCommentReply);
        tvCountCommentLike = itemView.findViewById(R.id.tvCountCommentLike);
        imageCommentLikeThumb.setVisibility(View.GONE);
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
        replyItem = new ArrayList<>();


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
//        commentPostId = commentItem.getPostId();


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
        tvCommentTime.setText(Utils.chatDateCompare(mContext, Long.valueOf(commentTime)));

        if (isNullOrEmpty(commentImage)) {
            imagePostCommenting.setVisibility(View.GONE);
        } else {
            imagePostCommenting.setVisibility(View.VISIBLE);
            imagePostCommenting.setVisibility(View.VISIBLE);
            String commentImageUrl = PROFILE_IMAGE + commentImage;
            Glide.with(App.getAppContext())
                    .load(commentImageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .into(imagePostCommenting);
        }


        tvCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvCountCommentLike.getText().toString().isEmpty()) {
                    imageCommentLikeThumb.setVisibility(View.VISIBLE);
                    tvCountCommentLike.setVisibility(View.VISIBLE);
                    tvCountCommentLike.setText("1");
                } else {
                    tvCountCommentLike.setText("");
                    imageCommentLikeThumb.setVisibility(View.GONE);
                    tvCountCommentLike.setVisibility(View.GONE);
                }
            }
        });
        if (!isNullOrEmpty(commentItem.getTotalReply()) && Integer.parseInt(commentItem.getTotalReply()) > 0) {
            tvCommentReply.setText(String.format("%s Reply", commentItem.getTotalReply()));
        }

        tvCommentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                String commentReply = commentItem.getTotalReply();

                if (!isNullOrEmpty(commentReply)) {
                    if (Integer.parseInt(commentReply) > 0) {
                        if (networkOk) {

                            Call<List<Reply>> call = commentService.getPostCommentReplyList(deviceId, profileId, token, commentItem.getId(), "false", limit, offset, commentItem.getPostId(), userIds);
                            sendAllCommentReplyListRequest(call);
                            delayLoadComment(mProgressBar);
                        } else {
                            Utils.showNetworkDialog(activity.getSupportFragmentManager());

                        }
                    } else if (Integer.parseInt(commentReply) == 0) {
                        Intent intent = new Intent(mContext, ReplyPost.class);
                        intent.putExtra(COMMENT_ITEM_KEY, (Parcelable) commentItem);
                        intent.putExtra(POST_ITEM_KEY, (Parcelable) postItem);
                        intent.putParcelableArrayListExtra(REPLY_KEY, (ArrayList<? extends Parcelable>) replyItem);
                        mContext.startActivity(intent);
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


        text = commentItem.getCommentText();
        StringBuilder nameBuilder = new StringBuilder();
        List<String> mentionUrl = extractUrls(commentItem.getCommentText());


        for (CommentTextIndex temp : commentItem.getCommentTextIndex()) {
            String postType = temp.getType();
            if (postType.equalsIgnoreCase("mention")) {
                String mentionUserName = extractMentionUser(temp.getText());
                nameBuilder.append(mentionUserName);
                nameBuilder.append(" ");
            }

        }


        if (mentionUrl.size() > 0 && extractMentionText(commentItem).trim().length() > 0) {

            full_text = extractMentionText(commentItem).trim();

            if (containsIllegalCharacters(full_text)) {
                //  tvPostContent.setVisibility(View.GONE);
                // tvPostEmojiContent.setVisibility(View.VISIBLE);

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
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(App.getAppContext(), "\"You click the text.\"", Toast.LENGTH_SHORT).show();
                        }
                    };
                    if (val >= 0) {
                        str.setSpan(clickableSpan, val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                text = str.toString();
                tvPostEmojiContent.setText(str);


            } else {
//                tvPostEmojiContent.setVisibility(View.GONE);
//                tvPostContent.setVisibility(View.VISIBLE);
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
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(App.getAppContext(), "\"You click the text.\"", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            ds.setColor(ds.linkColor);    // you can use custom color
                            ds.setUnderlineText(false);    // this remove the underline
                        }
                    };
                    if (val >= 0) {
                        str.setSpan(clickableSpan, val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        str.setSpan(new MyClickableSpan("mystring"), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }

                }
                text = str.toString();
                tvCommentMessage.setText(str);


            }
        }


        imageCommentSettings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popupCommentMenu = new PopupMenu(mContext, v);
                popupCommentMenu.getMenuInflater().inflate(R.menu.post_comment_menu, popupCommentMenu.getMenu());


                String userId = postItem.getPostUserid();
                String commentUserId = commentItem.getUserId();


                if (userId.equalsIgnoreCase(commentUserId)) {
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
                            App.setCommentItem(commentItem);
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, commentItem.getUserId(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Utils.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }

                        if (id == R.id.blockUser) {

                            if (!((Activity) mContext).isFinishing()) {
                                App.setCommentItem(commentItem);
                                showBlockUser(v);
                            }else {
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


                            //  Utils.showNetworkDialog(activity.getSupportFragmentManager());
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


                }


            }

            @Override
            public void onFailure(Call<List<Reply>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }


}
