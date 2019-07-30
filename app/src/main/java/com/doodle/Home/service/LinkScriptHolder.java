package com.doodle.Home.service;

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
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
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
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.Comment_;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.Operation;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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

import static com.doodle.utils.AppConstants.FACEBOOK_SHARE;
import static com.doodle.utils.AppConstants.PROFILE_IMAGE;
import static com.doodle.utils.Utils.containsIllegalCharacters;
import static com.doodle.utils.Utils.getDomainName;
import static com.doodle.utils.Utils.getSpannableStringBuilder;
import static com.doodle.utils.Utils.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class LinkScriptHolder extends RecyclerView.ViewHolder {

    public TextView tvHeaderInfo, tvPostTime, tvPostUserName, tvImgShareCount, tvPostLikeCount, tvLinkScriptText,tvCommentCount;
    public CircleImageView imagePostUser;
    public ReadMoreTextView tvPostContent;
    public EmojiTextView tvPostEmojiContent;
    public ImageView star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    public ImageView imgLinkScript;
    public LinearLayout postBodyLayer;
    PostItem item;
   public TextView tvPostLinkTitle,tvPostLinkDescription,tvPostLinkHost;


    public ImageView imagePostShare,imagePermission;
    private PopupMenu popup,popupMenu;
    public HomeService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;
    public static final String ITEM_KEY = "item_key";
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String linkImage;

    //Comment
    Comment commentItem;
    private List<Comment_> comments = new ArrayList<Comment_>();
    private String commentPostId;
    RelativeLayout commentHold;
    private String commentText, commentUserName, commentUserImage, commentImage, commentTime;
    public EmojiTextView tvCommentMessage;
    public ImageView imagePostCommenting, imageCommentLikeThumb, imageCommentSettings;
    public CircleImageView imageCommentUser;
    public TextView tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike;
    private String userPostId;
    private PopupMenu popupCommentMenu;
    public LinkScriptHolder(View itemView,Context context) {
        super(itemView);

        mContext=context;
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
        imgLinkScript = itemView.findViewById(R.id.imgLinkScript);
        tvPostLinkTitle = itemView.findViewById(R.id.tvPostLinkTitle);
        tvPostLinkDescription = itemView.findViewById(R.id.tvPostLinkDescription);
        tvPostLinkHost = itemView.findViewById(R.id.tvPostLinkHost);


        //Comment
        tvCommentMessage = itemView.findViewById(R.id.tvCommentMessage);
        commentHold = (RelativeLayout) itemView.findViewById(R.id.commentHold);
        imagePostCommenting = itemView.findViewById(R.id.imagePostCommenting);
        imageCommentLikeThumb = itemView.findViewById(R.id.imageCommentLikeThumb);
        imageCommentSettings = itemView.findViewById(R.id.imageCommentSettings);
        imageCommentUser = itemView.findViewById(R.id.imageCommentUser);
        tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
        tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
        tvCommentLike = itemView.findViewById(R.id.tvCommentLike);
        tvCommentReply = itemView.findViewById(R.id.tvCommentReply);
        tvCountCommentLike = itemView.findViewById(R.id.tvCountCommentLike);
        imageCommentLikeThumb.setVisibility(View.GONE);
        tvCountCommentLike.setVisibility(View.GONE);

    }


    public void setItem(PostItem item, Comment commentItem) {
        this.item = item;

        this.commentItem = commentItem;
        userPostId = item.getPostId();
        commentPostId = commentItem.getPostId();
        comments = commentItem.getComments();

        if (!comments.isEmpty()) {
            commentHold.setVisibility(View.VISIBLE);
            for (Comment_ temp : comments) {
                commentText = temp.getCommentText();
                commentUserName = temp.getUserFirstName() + " " + temp.getUserLastName();
                commentUserImage = temp.getUserPhoto();
                commentImage = temp.getCommentImage();
                commentTime = temp.getDateTime();
            }

            if (isNullOrEmpty(commentImage)) {
                imagePostCommenting.setVisibility(View.GONE);
            } else {
                imagePostCommenting.setVisibility(View.VISIBLE);
            }

            tvCommentUserName.setText(commentUserName);
            tvCommentMessage.setText(commentText);
            tvCommentTime.setText(Utils.chatDateCompare(mContext, Long.valueOf(commentTime)));

            String commentUserImageUrl = PROFILE_IMAGE + commentUserImage;
            Glide.with(App.getAppContext())
                    .load(commentUserImageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .into(imageCommentUser);

        } else {
            commentHold.setVisibility(View.GONE);
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


        String text = item.getPostText();
        String contentUrl = FACEBOOK_SHARE + item.getSharedPostId();
        if (containsIllegalCharacters(text)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(item.getPostText());
            Linkify.addLinks(tvPostEmojiContent, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Utils.stripUnderlines(tvPostEmojiContent);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            tvPostContent.setText(item.getPostText());
            Linkify.addLinks(tvPostContent, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Utils.stripUnderlines(tvPostContent);

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
//

        SpannableStringBuilder builder = getSpannableStringBuilder(likes, followers, totalStars, categoryName);


        tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        long myMillis = Long.parseLong(item.getDateTime()) * 1000;
        String postDate = Operation.getFormattedDateFromTimestamp(myMillis);
        tvPostTime.setText(postDate);
        tvHeaderInfo.setText(builder);
        if(!isNullOrEmpty(item.getTotalComment())&& !"0".equalsIgnoreCase(item.getTotalComment())){
            tvCommentCount.setText(item.getTotalComment());
        }
        PostFooter postFooter = item.getPostFooter();
        String postLike = postFooter.getPostTotalLike();
        int postTotalShare = postFooter.getPostTotalShare();
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


        if ("default-profile-picture.png".equalsIgnoreCase(item.getPostImage())) {
            imgLinkScript.setVisibility(View.GONE);
        } else {
            imgLinkScript.setVisibility(View.VISIBLE);
            if (item.getPostType().equalsIgnoreCase("3")) {
                 linkImage= AppConstants.Link_IMAGE_PATH + item.getPostImage();
                Glide.with(App.getAppContext())
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgLinkScript);
            } else if (item.getPostType().equalsIgnoreCase("4")) {
                linkImage = AppConstants.YOUTUBE_IMAGE_PATH + item.getPostImage();
                Glide.with(App.getAppContext())
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgLinkScript);
            }

        }


        tvPostLinkTitle.setText(item.getPostLinkTitle());
        tvPostLinkDescription.setText(item.getPostLinkDesc());
        try {
            String domainName = getDomainName(item.getPostLinkUrl());
            tvPostLinkHost.setText(domainName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        imgLinkScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                if (!item.getPostLinkUrl().isEmpty() && item.getPostLinkUrl().matches(pattern)) {
                    /// Valid youtube URL
                    Intent browserIntents = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPostLinkUrl()));
                    browserIntents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getAppContext().startActivity(browserIntents);
                } else {
                    // Not Valid youtube URL
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPostLinkUrl()));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getAppContext().startActivity(browserIntent);
                }

            }
        });


        imagePostShare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.share_menu, popup.getMenu());

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popup.getMenu(),v);
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

                popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.post_permission_menu, popupMenu.getMenu());

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(mContext, (MenuBuilder) popupMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.publics) {
                            Toast.makeText(App.getAppContext(), "publics : ", Toast.LENGTH_SHORT).show();
                        }

                        if (id == R.id.friends) {
                            Toast.makeText(App.getAppContext(), "friends : ", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.onlyMe) {
                            Toast.makeText(App.getAppContext(), "onlyMe : ", Toast.LENGTH_SHORT).show();

                        }

                        if (id == R.id.edit) {
                            Toast.makeText(App.getAppContext(), "edit : ", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.delete) {
                            Toast.makeText(App.getAppContext(), "delete : ", Toast.LENGTH_SHORT).show();
                        }
                        if (id == R.id.turnOffNotification) {
                            Toast.makeText(App.getAppContext(), "turnOffNotification : ", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

            }
        });
        imageCommentSettings.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {

                popupCommentMenu = new PopupMenu(mContext, v);
                popupCommentMenu.getMenuInflater().inflate(R.menu.post_comment_menu, popupCommentMenu.getMenu());

                if(userPostId.equalsIgnoreCase(commentPostId)){
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

    private void sendShareItemRequest(Call<PostShareItem> call) {


        call.enqueue(new Callback<PostShareItem>() {

            @Override
            public void onResponse(Call<PostShareItem> call, Response<PostShareItem> response) {

                PostShareItem postShareItem = response.body();
                Log.d("Data", postShareItem.toString());
                if (postShareItem != null) {
                    //   adapter = new BreakingPostAdapter(getActivity(), postItemList);
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


}
