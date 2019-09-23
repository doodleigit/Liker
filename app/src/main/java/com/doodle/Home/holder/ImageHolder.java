package com.doodle.Home.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.view.activity.CommentPost;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Home.adapter.GalleryAdapter;
import com.doodle.Home.model.MediaFrame;
import com.doodle.Home.model.MediaViewHolder;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.SharedProfile;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.view.activity.EditPost;
import com.doodle.Home.view.activity.Home;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Home.view.fragment.LikerUserListFragment;
import com.doodle.Post.view.activity.PostPopup;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.Operation;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.FACEBOOK_SHARE;
import static com.doodle.Tool.Tools.containsIllegalCharacters;
import static com.doodle.Tool.Tools.delayLoadComment;
import static com.doodle.Tool.Tools.dismissDialog;
import static com.doodle.Tool.Tools.getSpannableStringBuilder;
import static com.doodle.Tool.Tools.isNullOrEmpty;
import static com.doodle.Tool.Tools.sendNotificationRequest;
import static com.doodle.Tool.Tools.showBlockUser;
import static java.lang.Integer.parseInt;

public class ImageHolder extends RecyclerView.ViewHolder {
    public TextView tvHeaderInfo, tvPostTime, tvPostUserName, tvImgShareCount, tvPostLikeCount, tvLinkScriptText, tvCommentCount;
    public CircleImageView imagePostUser;
    public ReadMoreTextView tvPostContent;
    public EmojiTextView tvPostEmojiContent;
    public ImageView star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    // public ImageView imageMedia;
    public ImageView imagePostPermission;
    public LinearLayout postBodyLayer;
    PostItem item;

    public ImageView imagePostShare, imagePermission;
    private PopupMenu popup, popupMenu;
    public HomeService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;
    public static final String ITEM_KEY = "item_key";
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    //Comment
    Comment commentItem;
    private List<Comment_> comments = new ArrayList<Comment_>();
    private String commentPostId;
    RelativeLayout commentHold;
    public FrameLayout dynamicMediaFrame, mediaVideoOne, mediaVideoTwo, mediaVideoThree, mediaVideoFour;
    private String commentText, commentUserName, commentUserImage, commentImage, commentTime;
    public EmojiTextView tvCommentMessage;
    public ImageView imagePostCommenting, imageCommentLikeThumb, imageMediaOne, imageMediaTwo, imageMediaThree, imageMediaFour;
    public ImageView mediaVideoOneThumbnail, mediaVideoTwoThumbnail, mediaVideoThreeThumbnail, mediaVideoFourThumbnail, mediaVideoOneVolumeControl,
            mediaVideoTwoVolumeControl, mediaVideoThreeVolumeControl, mediaVideoFourVolumeControl, mediaVideoOnePlay, mediaVideoTwoPlay, mediaVideoThreePlay, mediaVideoFourPlay;
    public ProgressBar mediaVideoOneProgressBar, mediaVideoTwoProgressBar, mediaVideoThreeProgressBar, mediaVideoFourProgressBar;

    public CircleImageView imageCommentUser;
    public TextView tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike, tvMediaCount;
    private String userPostId;
    private PopupMenu popupCommentMenu;

    private ArrayList<MediaViewHolder> mediaViewHolders;
    private ArrayList<MediaFrame> mediaFrames;
    private int[] videoMediaHolder;

    //SHOW ALL COMMENTS
    private CommentService commentService;
    int limit = 10;
    int offset = 0;
    boolean networkOk;
    ProgressBar mProgressBar;
    private ImageView imagePostComment;
    private LinearLayout commentContainer;
    LinearLayout commentBox;
    public static final String COMMENT_KEY = "comment_item_key";
    private String postPermissions;
    private boolean notificationOff;
    private RecyclerView singleImgRecyclerView;
    //Delete post
    public PostItemListener listener;
    private boolean isPopup;

    //POST LIKE
    public ImageView imgLike;
    private int postLikeNumeric;
    private String postLike;
    private int postTotalShare;



    //SHARE-POST-HEADER
    private String sharedFirstName;
    private String sharedLastName;
    private String sharedFullName;
    private String isShared;
    private String sharedDateTime;
    private String sharedUserProfileImage;
    private int sharedTotalStar;
    private String sharedPostPermission;
    private String sharedUserProfileLike;
    private String sharedPostText;
    private String sharedCategoryName;

    private LinearLayout containerHeaderShare;
    private CircleImageView imageSharePostUser;
    private ImageView imageSharePermission,imageSharePostPermission;
    private TextView tvSharePostUserName, tvSharePostTime,tvShareHeaderInfo,tvSharePostContent;


    public interface PostItemListener {
        void deletePost(PostItem postItem, int position);
    }

    public ImageHolder(View itemView, Context context, PostItemListener listener, boolean isPopup) {
        super(itemView);

        mContext = context;
        this.listener = listener;
        this.isPopup = isPopup;
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog((Activity) context);
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        imagePostShare = (ImageView) itemView.findViewById(R.id.imagePostShare);
        imagePermission = (ImageView) itemView.findViewById(R.id.imagePermission);

        tvPostUserName = (TextView) itemView.findViewById(R.id.tvPostUserName);
        imagePostUser = (CircleImageView) itemView.findViewById(R.id.imagePostUser);
        tvHeaderInfo = (TextView) itemView.findViewById(R.id.tvHeaderInfo);
        tvImgShareCount = (TextView) itemView.findViewById(R.id.tvImgShareCount);
        tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
        tvPostLikeCount = (TextView) itemView.findViewById(R.id.tvPostLikeCount);
        //tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
        tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
        tvLinkScriptText = (ReadMoreTextView) itemView.findViewById(R.id.tvLinkScriptText);
        tvPostEmojiContent = (EmojiTextView) itemView.findViewById(R.id.tvPostEmojiContent);
        postBodyLayer = (LinearLayout) itemView.findViewById(R.id.postBodyLayer);
        tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        dynamicMediaFrame = itemView.findViewById(R.id.dynamic_media_frame);


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
        containerHeaderShare = itemView.findViewById(R.id.containerHeaderShare);

        // imageMedia = itemView.findViewById(R.id.imageMedia);
        imagePostPermission = itemView.findViewById(R.id.imagePostPermission);

        //Comment
        tvCommentMessage = itemView.findViewById(R.id.tvCommentMessage);
        commentHold = (RelativeLayout) itemView.findViewById(R.id.commentHold);
        imagePostCommenting = itemView.findViewById(R.id.imagePostCommenting);
        imageCommentLikeThumb = itemView.findViewById(R.id.imageCommentLikeThumb);

        imageCommentUser = itemView.findViewById(R.id.imageCommentUser);

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
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.ProgressBar);
        imagePostComment = (ImageView) itemView.findViewById(R.id.imagePostComment);
        commentContainer =itemView.findViewById(R.id.commentContainer);
        singleImgRecyclerView = itemView.findViewById(R.id.singleImgRecyclerView);
        imgLike = itemView.findViewById(R.id.imgLike);


        mediaViewHolders = new ArrayList<>();
        mediaFrames = new ArrayList<>();
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_zero, 0));
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_one, 1));
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_two, 2));
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_three, 3));
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_four, 4));
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_five, 3));
        mediaFrames.add(new MediaFrame(R.layout.item_media_frame_six, 4));


        //SHARE_POST_HEADER
        containerHeaderShare = itemView.findViewById(R.id.containerHeaderShare);
        imageSharePostUser = itemView.findViewById(R.id.imageSharePostUser);
        tvSharePostUserName = itemView.findViewById(R.id.tvSharePostUserName);
        tvSharePostTime = itemView.findViewById(R.id.tvSharePostTime);
        tvShareHeaderInfo = itemView.findViewById(R.id.tvShareHeaderInfo);
        imageSharePermission = itemView.findViewById(R.id.imageSharePermission);
        imageSharePostPermission = itemView.findViewById(R.id.imageSharePostPermission);
        tvSharePostContent = itemView.findViewById(R.id.tvSharePostContent);

    }

    AppCompatActivity activity;
    int position;
    RecyclerView.ViewHolder viewHolder;

    public void setItem(PostItem item, int position, RecyclerView.ViewHolder viewHolder) {
        this.item = item;
        this.position = position;
        this.viewHolder = viewHolder;
        userPostId = item.getPostId();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        dynamicMediaFrame.removeAllViews();
        View wizardView = inflater
                .inflate(mediaFrames.get(item.getFrameNumber()).getLayout(), dynamicMediaFrame, false);
        dynamicMediaFrame.addView(wizardView);

        imageMediaOne = itemView.findViewById(R.id.media_image_one);
        imageMediaTwo = itemView.findViewById(R.id.media_image_two);
        imageMediaThree = itemView.findViewById(R.id.media_image_three);
        imageMediaFour = itemView.findViewById(R.id.media_image_four);

        mediaVideoOne = itemView.findViewById(R.id.media_video_one);
        mediaVideoTwo = itemView.findViewById(R.id.media_video_two);
        mediaVideoThree = itemView.findViewById(R.id.media_video_three);
        mediaVideoFour = itemView.findViewById(R.id.media_video_four);

        mediaVideoOneThumbnail = itemView.findViewById(R.id.media_video_one_thumbnail);
        mediaVideoTwoThumbnail = itemView.findViewById(R.id.media_video_two_thumbnail);
        mediaVideoThreeThumbnail = itemView.findViewById(R.id.media_video_three_thumbnail);
        mediaVideoFourThumbnail = itemView.findViewById(R.id.media_video_four_thumbnail);

        mediaVideoOneVolumeControl = itemView.findViewById(R.id.media_video_one_volume_control);
        mediaVideoTwoVolumeControl = itemView.findViewById(R.id.media_video_two_volume_control);
        mediaVideoThreeVolumeControl = itemView.findViewById(R.id.media_video_three_volume_control);
        mediaVideoFourVolumeControl = itemView.findViewById(R.id.media_video_four_volume_control);

        mediaVideoOnePlay = itemView.findViewById(R.id.media_video_one_play);
        mediaVideoTwoPlay = itemView.findViewById(R.id.media_video_two_play);
        mediaVideoThreePlay = itemView.findViewById(R.id.media_video_three_play);
        mediaVideoFourPlay = itemView.findViewById(R.id.media_video_four_play);

        mediaVideoOneProgressBar = itemView.findViewById(R.id.media_video_one_progressBar);
        mediaVideoTwoProgressBar = itemView.findViewById(R.id.media_video_two_progressBar);
        mediaVideoThreeProgressBar = itemView.findViewById(R.id.media_video_three_progressBar);
        mediaVideoFourProgressBar = itemView.findViewById(R.id.media_video_four_progressBar);

        tvMediaCount = itemView.findViewById(R.id.media_count);
        mediaViewHolders.clear();
        mediaViewHolders.add(new MediaViewHolder(mediaVideoOne, imageMediaOne, mediaVideoOneThumbnail));
        mediaViewHolders.add(new MediaViewHolder(mediaVideoTwo, imageMediaTwo, mediaVideoTwoThumbnail));
        mediaViewHolders.add(new MediaViewHolder(mediaVideoThree, imageMediaThree, mediaVideoThreeThumbnail));
        mediaViewHolders.add(new MediaViewHolder(mediaVideoFour, imageMediaFour, mediaVideoFourThumbnail));

        String postPermission = item.getPermission();

        switch (postPermission) {
            case "0":
                imagePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                break;
            case "1":
                imagePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                break;
            case "2":
                imagePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                break;
        }




        isShared = item.getIsShared();

        if ("1".equalsIgnoreCase(isShared)) {
            containerHeaderShare.setVisibility(View.VISIBLE);
            imagePermission.setVisibility(View.GONE);

            SharedProfile itemSharedProfile = item.getSharedProfile();
            sharedFirstName = itemSharedProfile.getUserFirstName();
            sharedLastName = itemSharedProfile.getUserLastName();
            sharedFullName = sharedFirstName + " " + sharedLastName;
            sharedDateTime = itemSharedProfile.getDateTime();
            sharedUserProfileImage = itemSharedProfile.getUesrProfileImg();
            sharedTotalStar = Integer.parseInt(itemSharedProfile.getUserGoldStars()) + Integer.parseInt(itemSharedProfile.getUserSilverStars());
            sharedPostPermission = itemSharedProfile.getPermission();
            sharedUserProfileLike = itemSharedProfile.getUserProfileLikes();
            sharedPostText=item.getSharedPostText();
            sharedCategoryName=item.getCatName();
            SpannableStringBuilder builder = getSpannableStringBuilder(sharedUserProfileLike, "", sharedTotalStar, sharedCategoryName);
            long myMillis = Long.parseLong(sharedDateTime) * 1000;
            String postDate = Operation.getFormattedDateFromTimestamp(myMillis);
            //    tvSharePostTime.setText(chatDateCompare(mContext,myMillis));
            tvSharePostUserName.setText(sharedFullName);
            tvShareHeaderInfo.setText(builder);
            tvSharePostTime.setText(postDate);
            tvSharePostContent.setText(sharedPostText);

            switch (sharedPostPermission) {
                case "0":
                    imageSharePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                    break;
                case "1":
                    imageSharePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                    break;
                case "2":
                    imageSharePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                    break;
            }

        } else {
            containerHeaderShare.setVisibility(View.GONE);
            imagePermission.setVisibility(View.VISIBLE);
        }




        dynamicMediaFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PostPopup.class);
                intent.putExtra(ITEM_KEY, (Parcelable) item);
                intent.putExtra("has_footer", true);
                intent.putExtra("position", position);
                App.setIsImagePopup(true);
                mContext.startActivity(intent);
//                ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
            }
        });

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userIds.equalsIgnoreCase(item.getPostUserid())) {
                    Tools.toast(mContext, "On Liker, you can't like your own posts. That would be cheating ", R.drawable.ic_info_outline_blue_24dp);
                } else {
                    PostFooter postFooters = item.getPostFooter();
                    if (postFooters.isLikeUserStatus()) {
                        Call<String> call = webService.postUnlike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                        sendPostUnLikeRequest(call);
                    } else {
                        Call<String> call = webService.postLike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                        sendPostLikeRequest(call);
                    }

                }
            }
        });


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


        String contentUrl = FACEBOOK_SHARE + item.getSharedPostId();
        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(item.getPostText());
            Linkify.addLinks(tvPostEmojiContent, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostEmojiContent);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            tvPostContent.setText(item.getPostText());
            Linkify.addLinks(tvPostContent, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostContent);

        }

        String likes = item.getUserProfileLikes();
        String followers = item.getUserTotalFollowers();
        int silverStar = parseInt(item.getUserSilverStars());
        int goldStar = parseInt(item.getUserGoldStars());

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

        int totalStars = silverStar + goldStar;
        String categoryName = item.getCatName();

        SpannableStringBuilder builder = getSpannableStringBuilder(likes, followers, totalStars, categoryName);

        tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        long myMillis = Long.parseLong(item.getDateTime()) * 1000;
        String postDate = Operation.getFormattedDateFromTimestamp(myMillis);
        tvPostTime.setText(postDate);
        tvHeaderInfo.setText(builder);

        PostFooter postFooter = item.getPostFooter();
        postLike = postFooter.getPostTotalLike();
        postTotalShare = postFooter.getPostTotalShare();
        tvImgShareCount.setText(String.valueOf(postTotalShare));
        if ("0".equalsIgnoreCase(postLike)) {
            tvPostLikeCount.setVisibility(View.GONE);
        } else {
            SpannableString content = new SpannableString(postLike);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvPostLikeCount.setVisibility(View.VISIBLE);
            tvPostLikeCount.setText(content);
        }

        String userImageUrl = AppConstants.PROFILE_IMAGE + item.getUesrProfileImg();
        Glide.with(App.getAppContext())
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
                .into(imagePostUser);

        String shareUserImageUrl = AppConstants.PROFILE_IMAGE + sharedUserProfileImage;
        Glide.with(App.getAppContext())
                .load(shareUserImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imageSharePostUser);


        if (!isNullOrEmpty(item.getTotalComment()) && !"0".equalsIgnoreCase(item.getTotalComment())) {
            tvCommentCount.setText(item.getTotalComment());
        } else {
            tvCommentCount.setText("");
        }

        if (item.getPostFiles().size() <= 4) {
            tvMediaCount.setVisibility(View.GONE);
        } else {
            tvMediaCount.setVisibility(View.VISIBLE);
            tvMediaCount.setText(getCountText(item.getPostFiles().size()));
        }

        for (int i = 0; i < mediaFrames.get(item.getFrameNumber()).getItemCount(); i++) {
            if (item.getPostFiles().get(i).getPostType().equals("2")) {
                String imageUrl = AppConstants.POST_VIDEOS_THUMBNAIL + item.getPostFiles().get(i).getImageName();
                mediaViewHolders.get(i).getMediaVideoLayout().setVisibility(View.VISIBLE);
                mediaViewHolders.get(i).getMediaImage().setVisibility(View.GONE);
                Glide.with(App.getAppContext())
                        .load(imageUrl)
                        .error(R.drawable.post_image_background)
                        .dontAnimate()
                        .into(mediaViewHolders.get(i).getMediaThumbnail());
                Glide.with(App.getAppContext())
                        .load(imageUrl)
                        .error(R.drawable.post_image_background)
                        .centerCrop()
                        .dontAnimate()
                        .into(mediaViewHolders.get(i).getMediaImage());
            } else {
                String imageUrl = AppConstants.POST_IMAGES + item.getPostFiles().get(i).getImageName();
                mediaViewHolders.get(i).getMediaVideoLayout().setVisibility(View.GONE);
                mediaViewHolders.get(i).getMediaImage().setVisibility(View.VISIBLE);
                Glide.with(App.getAppContext())
                        .load(imageUrl)
                        .error(R.drawable.post_image_background)
                        .centerCrop()
                        .dontAnimate()
                        .into(mediaViewHolders.get(i).getMediaImage());
                Glide.with(App.getAppContext())
                        .load(imageUrl)
                        .error(R.drawable.post_image_background)
                        .dontAnimate()
                        .into(mediaViewHolders.get(i).getMediaThumbnail());
            }
        }

        if (item.getPostFooter().isLikeUserStatus()) {
            imgLike.setImageResource(R.drawable.like_done);
        } else {
            imgLike.setImageResource(R.drawable.like_normal);
        }

        tvPostLikeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = ((Home) mContext).getSupportFragmentManager().beginTransaction();
                Fragment prev = ((Home) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new LikerUserListFragment();

                Bundle bundle = new Bundle();
                bundle.putString("post_id", item.getPostId());
                bundle.putString("total_likes", item.getPostFooter().getPostTotalLike());
                dialogFragment.setArguments(bundle);

                dialogFragment.show(ft, "dialog");
            }
        });

        tvPostUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", item.getPostUserid()).putExtra("user_name", item.getPostUsername()));
            }
        });

        imagePostUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ProfileActivity.class).putExtra("user_id", item.getPostUserid()).putExtra("user_name", item.getPostUsername()));
            }
        });

        imagePostShare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.share_menu, popup.getMenu());

                if (App.isIsPostShare()) {
                    postTotalShare++;
                    popup.getMenu().add(1, R.id.shareAsPost, 1, "Share as a Post (" + postTotalShare + ")").setIcon(R.drawable.like_done);
                } else {
                    popup.getMenu().add(1, R.id.shareAsPost, 1, "Share as a Post (" + postTotalShare + ")").setIcon(R.drawable.like_normal);
                }


//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popup.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.shareAsPost) {
                            String postId = item.getSharedPostId();
                            Call<PostShareItem> call = webService.getPostDetails(deviceId, profileId, token, userIds, postId);
                            sendShareItemRequest(call);

                        }

                        if (id == R.id.shareFacebook) {

                            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                @Override
                                public void onSuccess(Sharer.Result result) {

                                    Toast.makeText(mContext, "Share successFull", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(mContext, "Share cancel", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(FacebookException error) {
                                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                            if (!isNullOrEmpty(contentUrl)) {
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentUrl(Uri.parse(contentUrl))
                                        .setQuote("")
                                        .build();
                                if (ShareDialog.canShow(ShareLinkContent.class)) {

                                    shareDialog.show(linkContent);
                                }
                            }


                        }
                        if (id == R.id.shareTwitter) {
                            String url = "http://www.twitter.com/intent/tweet?url=" + contentUrl + "&text=" + text;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            mContext.startActivity(i);
                        }

                        if (id == R.id.copyLink) {

                            ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Link", contentUrl);
                            clipboard.setPrimaryClip(clip);
                        }
                        return true;
                    }
                });

            }
        });
        imagePermission.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {


                String postUserId = item.getPostUserid();
                boolean isNotificationOff = item.isIsNotificationOff();
                popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.post_permission_menu, popupMenu.getMenu());

                if (userIds.equalsIgnoreCase(postUserId)) {
                    popupMenu.getMenu().findItem(R.id.blockedUser).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.reportedPost).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.publics).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.friends).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.onlyMe).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(true);
                    //  popupMenu.getMenu().findItem(R.id.turnOffNotification).setVisible(true);
                } else {
                    popupMenu.getMenu().findItem(R.id.blockedUser).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.reportedPost).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.publics).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.friends).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.onlyMe).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    // v  popupMenu.getMenu().findItem(R.id.turnOffNotification).setVisible(true);
                }


                if (App.isNotificationStatus()) {

                    if (notificationOff) {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);
                    } else {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

                    }


                } else {
                    if (isNotificationOff) {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);

                    } else {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

                    }
                }

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        postPermissions = menuItem.getTitle().toString();
                        if (id == R.id.blockedUser) {
                            if (!((Activity) mContext).isFinishing()) {
                                App.setItem(item);
                                showBlockUser(v);
                            } else {
                                dismissDialog();
                            }
                        }
                        if (id == R.id.reportedPost) {
                            App.setItem(item);
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, item.getPostUserid(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.publics) {

                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "0", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }


                        }
                        if (id == R.id.friends) {
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "2", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.onlyMe) {
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "1", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }

                        }

                        if (id == R.id.edit) {
                            Intent intent = new Intent(mContext, EditPost.class);
                            App.setPosition(position);
                            intent.putExtra(ITEM_KEY, (Parcelable) item);
                            intent.putExtra("position", position);
                            mContext.startActivity(intent);
                            ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                        }
                        if (id == R.id.delete) {
                            listener.deletePost(item, position);
                        }
                        if (id == R.id.turnOffNotification) {
                            activity = (AppCompatActivity) v.getContext();

                            switch (postPermissions) {
                                case "Turn off notifications":
                                    notificationOff = true;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOff(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                    }
                                    break;
                                case "Turn on notifications":
                                    notificationOff = false;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOn(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                    }
                                    break;

                            }
                        }
                        return true;
                    }
                });

            }
        });
        imageSharePermission.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {


                String postUserId = item.getPostUserid();
                boolean isNotificationOff = item.isIsNotificationOff();
                popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.post_permission_menu, popupMenu.getMenu());

                if (userIds.equalsIgnoreCase(postUserId)) {
                    popupMenu.getMenu().findItem(R.id.blockedUser).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.reportedPost).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.publics).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.friends).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.onlyMe).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(true);
                    //  popupMenu.getMenu().findItem(R.id.turnOffNotification).setVisible(true);
                } else {
                    popupMenu.getMenu().findItem(R.id.blockedUser).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.reportedPost).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.publics).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.friends).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.onlyMe).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    // v  popupMenu.getMenu().findItem(R.id.turnOffNotification).setVisible(true);
                }


                if (App.isNotificationStatus()) {

                    if (notificationOff) {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);
                    } else {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

                    }


                } else {
                    if (isNotificationOff) {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn on notifications").setIcon(R.drawable.ic_notifications_black_24dp);

                    } else {
                        popupMenu.getMenu().add(1, R.id.turnOffNotification, 1, "Turn off notifications").setIcon(R.drawable.ic_notifications_off_black_24dp);

                    }
                }

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        postPermissions = menuItem.getTitle().toString();
                        if (id == R.id.blockedUser) {
                            if (!((Activity) mContext).isFinishing()) {
                                App.setItem(item);
                                showBlockUser(v);
                            } else {
                                dismissDialog();
                            }
                        }
                        if (id == R.id.reportedPost) {
                            App.setItem(item);
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, item.getPostUserid(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.publics) {

                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "0", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }


                        }
                        if (id == R.id.friends) {
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "2", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.onlyMe) {
                            activity = (AppCompatActivity) v.getContext();
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "1", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(activity.getSupportFragmentManager());
                            }

                        }

                        if (id == R.id.edit) {
                            Intent intent = new Intent(mContext, EditPost.class);
                            App.setPosition(position);
                            intent.putExtra(ITEM_KEY, (Parcelable) item);
                            intent.putExtra("position", position);
                            mContext.startActivity(intent);
                            ((Activity) mContext).overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                        }
                        if (id == R.id.delete) {
                            listener.deletePost(item, position);
                        }
                        if (id == R.id.turnOffNotification) {
                            activity = (AppCompatActivity) v.getContext();

                            switch (postPermissions) {
                                case "Turn off notifications":
                                    notificationOff = true;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOff(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                    }
                                    break;
                                case "Turn on notifications":
                                    notificationOff = false;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOn(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(activity.getSupportFragmentManager());
                                    }
                                    break;

                            }
                        }
                        return true;
                    }
                });

            }
        });
        commentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                //  mContext.startActivity(new Intent(mContext, CommentPost.class));
//                FullBottomSheetDialogFragment postPermissions = new FullBottomSheetDialogFragment();
//                postPermissions.show(activity.getSupportFragmentManager(), "PostPermission");
                if (networkOk) {

                    Call<CommentItem> call = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", item.getPostId(), userIds);
                    sendAllCommentItemRequest(call);

                    //    log("Running code");
                    delayLoadComment(mProgressBar);
                } else {
                    Tools.showNetworkDialog(activity.getSupportFragmentManager());


                }

            }
        });
    }

    private void sendPostUnLikeRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String status = object.getString("status");
                            if ("true".equalsIgnoreCase(status)) {

                                postLikeNumeric = Integer.parseInt(postLike);

                                postLikeNumeric = postLikeNumeric <= 0 ? 0 : --postLikeNumeric;
                                postLike=String.valueOf(postLikeNumeric);
                                item.getPostFooter().setPostTotalLike(postLike);
                                item.getPostFooter().setLikeUserStatus(false);

                                if (0 == postLikeNumeric) {
                                    tvPostLikeCount.setVisibility(View.GONE);
                                } else {
                                    SpannableString content = new SpannableString(String.valueOf(postLikeNumeric));
                                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                    tvPostLikeCount.setVisibility(View.VISIBLE);
                                    tvPostLikeCount.setText(content);
                                }
                                imgLike.setImageResource(R.drawable.like_normal);
                            } else {
                                Call<String> mCall = webService.postLike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                                sendPostLikeRequest(mCall);
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

    private void sendPostLikeRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String status = object.getString("status");
                            if ("true".equalsIgnoreCase(status)) {
                                Call<String> mCall = webService.sendBrowserNotification(
                                        deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                                        profileId,//"26444",
                                        token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                                        item.getPostUserid(),//"26444",
                                        userIds,//"26444",
                                        item.getPostId(),
                                        "like_post"
                                );
                                sendBrowserNotificationRequest(mCall);

                                postLikeNumeric = Integer.parseInt(postLike);
                                postLikeNumeric++;
                                postLike=String.valueOf(postLikeNumeric);
                                item.getPostFooter().setPostTotalLike(postLike);
                                item.getPostFooter().setLikeUserStatus(true);

                                SpannableString content = new SpannableString(String.valueOf(postLikeNumeric));
                                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                tvPostLikeCount.setVisibility(View.VISIBLE);
                                tvPostLikeCount.setText(content);
                                imgLike.setImageResource(R.drawable.like_done);

                            } else {

                                Call<String> mCall = webService.postUnlike(deviceId, userIds, token, userIds, item.getPostUserid(), item.getPostId());
                                sendPostUnLikeRequest(mCall);

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

    private String getCountText(int count) {
        String countText = "";
        if (count > 4) {
            countText = "+" + (count - 4);
        }
        return countText;
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

    private void sendPostPermissionRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                switch (postPermissions) {
                                    case "Public":
                                        imagePostPermission.setBackgroundResource(R.drawable.ic_public_black_24dp);
                                        break;
                                    case "Friends":
                                        imagePostPermission.setBackgroundResource(R.drawable.ic_friends_12dp);
                                        break;
                                    case "Only Me":
                                        imagePostPermission.setBackgroundResource(R.drawable.ic_only_me_12dp);
                                        break;

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

    private void sendAllCommentItemRequest(Call<CommentItem> call) {

        call.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {


                if (response.body() != null) {
                    CommentItem commentItem = response.body();
                    Intent intent = new Intent(mContext, CommentPost.class);
                    intent.putExtra(COMMENT_KEY, (Parcelable) commentItem);
                    intent.putExtra(ITEM_KEY, (Parcelable) item);
                    mContext.startActivity(intent);

                }


            }

            @Override
            public void onFailure(Call<CommentItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });

    }

    private void sendShareItemRequest(Call<PostShareItem> call) {


        call.enqueue(new Callback<PostShareItem>() {

            @Override
            public void onResponse(Call<PostShareItem> call, Response<PostShareItem> response) {

                PostShareItem postShareItem = response.body();
                Log.d("Data", postShareItem.toString());
                if (postShareItem != null) {
                    //   adapter = new PostAdapter(getActivity(), postItemList);
                    //  Toast.makeText(mContext, "item selected " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PostShare.class);
                    //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                    intent.putExtra(ITEM_KEY, (Parcelable) postShareItem);
                    mContext.startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<PostShareItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

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
