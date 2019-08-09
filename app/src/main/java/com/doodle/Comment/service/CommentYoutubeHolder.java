package com.doodle.Comment.service;

import android.annotation.SuppressLint;
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
import android.text.util.Linkify;
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
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.LinkData;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.view.activity.ReplyPost;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.utils.AppConstants.LINK_IMAGES;
import static com.doodle.utils.AppConstants.PROFILE_IMAGE;
import static com.doodle.utils.Utils.delayLoadComment;
import static com.doodle.utils.Utils.getDomainName;
import static com.doodle.utils.Utils.isEmpty;
import static com.doodle.utils.Utils.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class CommentYoutubeHolder extends RecyclerView.ViewHolder {


    public CircleImageView imagePostUser;
    public ReadMoreTextView tvPostContent;
    public EmojiTextView tvPostEmojiContent;
    public ImageView star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    public LinearLayout postBodyLayer,youtubeHold;
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

    public TextView tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike,tvLinkHost,tvDescription;
    private String userPostId;
    private PopupMenu popupCommentMenu;


    //SHOW ALL COMMENTS
    private CommentService commentService;
    int limit = 10;
    int offset = 0;
    boolean networkOk;


    //ReplyAll
    private ProgressBar mProgressBar;
    public static final String REPLY_ITEM_KEY = "reply_item_key";
    public static final String REPLY_KEY = "reply_key";
    public static final String COMMENT_ITEM_KEY = "comment_item_key";
    public static final String POST_ITEM_KEY = "post_item_key";
    public PostItem postItem;
    public Reply itemReply;
    List<Reply> replyItem;

    //EDIT COMMENT
    CommentListener commentListener;

    public interface CommentListener {

        void onTitleClicked(Comment_ commentItem, int position);
    }
    int position;

    public CommentYoutubeHolder(View itemView, Context context, final CommentListener listener) {
        super(itemView);

        mContext = context;
        commentListener = listener;
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        itemReply=new Reply();
        replyItem=new ArrayList<>();

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
        tvLinkHost = itemView.findViewById(R.id.tvLinkHost);
        tvDescription = itemView.findViewById(R.id.tvDescription);
        youtubeHold = itemView.findViewById(R.id.youtubeHold);

        imagePostCommenting = itemView.findViewById(R.id.image_post);
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


        //ReplyAll
         mProgressBar=itemView.findViewById(R.id.ProgressBar);
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

    public void setItem(Comment_ commentItem, PostItem postItem,int position) {

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
        tvCommentTime.setText(Utils.chatDateCompare(mContext, Long.valueOf(commentTime)));

        LinkData linkItem = commentItem.getLinkData();
        if(!isEmpty(linkItem)){
            String imageName=linkItem.getImageName();
            String linkFullUrl=linkItem.getLinkFullUrl();
            String linkTitle=linkItem.getLinkTitle();
            tvCommentMessage.setText(linkFullUrl);
            Linkify.addLinks(tvCommentMessage, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Utils.stripUnderlines(tvCommentMessage);
            try {
                if(!isNullOrEmpty(linkFullUrl)){
                    String domainName = getDomainName(linkFullUrl);
                    tvLinkHost.setText(domainName);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            tvDescription.setText(linkTitle);
         //   String linkImage = AppConstants.YOUTUBE_IMAGE_PATH + imageName;
            String linkImage = LINK_IMAGES + imageName;
            Glide.with(App.getAppContext())
                    .load(linkImage)
                    .centerCrop()
                    .dontAnimate()
                    .into(imagePostCommenting);


            youtubeHold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent browserIntents = new Intent(Intent.ACTION_VIEW, Uri.parse(linkFullUrl));
                    browserIntents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getAppContext().startActivity(browserIntents);
                }
            });
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
                String commentUserId = commentItem.getUserId();
                if(userIds.equalsIgnoreCase(commentUserId)){
                    popupCommentMenu.getMenu().findItem(R.id.reportComment).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.blockUser).setVisible(false);
                    popupCommentMenu.getMenu().findItem(R.id.editComment).setVisible(true);
                    popupCommentMenu.getMenu().findItem(R.id.deleteComment).setVisible(true);
                }else {
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

                            Toast.makeText(App.getAppContext(), "reportComment : ", Toast.LENGTH_SHORT).show();
                        }

                        if (id == R.id.blockUser) {
                            Toast.makeText(App.getAppContext(), "blockUser : ", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.editComment) {
                            commentListener.onTitleClicked(commentItem, position);

                        }

                        if (id == R.id.deleteComment) {
                            Toast.makeText(App.getAppContext(), "deleteComment : ", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });

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
                        Utils.showNetworkDialog(activity.getSupportFragmentManager());

                    }
                } else {
                    Intent intent = new Intent(mContext, ReplyPost.class);
                    intent.putExtra(COMMENT_ITEM_KEY, (Parcelable) commentItem);
                    intent.putExtra(POST_ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra(REPLY_ITEM_KEY,   (Parcelable) itemReply);
                    intent.putParcelableArrayListExtra(REPLY_KEY, (ArrayList<? extends Parcelable>) replyItem);

                    mContext.startActivity(intent);
                }
            }
        });

    }

    private void sendAllCommentReplyListRequest(Call<List<Reply>> call) {

        call.enqueue(new Callback<List<Reply>>() {

            @Override
            public void onResponse(Call<List<Reply>> mCall, Response<List<Reply>> response) {


                if(response.body()!=null){
                    replyItem = response.body();
                    itemReply=replyItem.get(position);
                    Log.d("replyItem ",replyItem.toString());

                    Intent intent = new Intent(mContext, ReplyPost.class);
                    intent.putExtra(COMMENT_ITEM_KEY, (Parcelable) commentItem);
                    intent.putExtra(POST_ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra(REPLY_ITEM_KEY,   (Parcelable) itemReply);
                    intent.putParcelableArrayListExtra(REPLY_KEY, (ArrayList<? extends Parcelable>) replyItem);                    mContext.startActivity(intent);

                }else {
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


}
