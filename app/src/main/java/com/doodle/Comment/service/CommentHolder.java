package com.doodle.Comment.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Comment.CommentPost;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.PostTextIndex;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.view.activity.Home;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.Operation;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.utils.AppConstants.FACEBOOK_SHARE;
import static com.doodle.utils.AppConstants.PROFILE_IMAGE;
import static com.doodle.utils.Utils.containsIllegalCharacters;
import static com.doodle.utils.Utils.extractMentionText;
import static com.doodle.utils.Utils.extractUrls;
import static com.doodle.utils.Utils.getSpannableStringBuilder;
import static com.doodle.utils.Utils.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class CommentHolder extends RecyclerView.ViewHolder {


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
    public ImageView imagePostCommenting, imageCommentLikeThumb, imageCommentSettings;

    public TextView tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike;
    private String userPostId;
    private PopupMenu popupCommentMenu;


    //SHOW ALL COMMENTS
    private CommentService commentService;
    int limit = 10;
    int offset = 1;
    boolean networkOk;
    CircularProgressView progressView;


    public CommentHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;
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
        progressView = (CircularProgressView) itemView.findViewById(R.id.progress_view);

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

    public void setItem(Comment_ commentItem) {

        this.commentItem = commentItem;
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

             /*   if(userPostId.equalsIgnoreCase(commentPostId)){
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(true);
                }else {
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(false);
                }
*/

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupCommentMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupCommentMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.reportComment) {

                            Toast.makeText(App.getAppContext(), "reportComment : ", Toast.LENGTH_SHORT).show();
                        }

                        if (id == R.id.blockUser) {
                            Toast.makeText(App.getAppContext(), "blockUser : ", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.editComment) {
                            Toast.makeText(App.getAppContext(), "editComment : ", Toast.LENGTH_SHORT).show();

                        }

                        if (id == R.id.deleteComment) {
                            Toast.makeText(App.getAppContext(), "deleteComment : ", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });

            }
        });

    }


}
