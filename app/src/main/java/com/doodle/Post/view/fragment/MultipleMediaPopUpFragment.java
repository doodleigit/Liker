package com.doodle.Post.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.activity.CommentPost;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Home.adapter.GalleryAdapter;
import com.doodle.Home.holder.ImageHolder;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.view.activity.EditPost;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Home.view.fragment.LikerUserListFragment;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.COMMENT_KEY;
import static com.doodle.Tool.AppConstants.FACEBOOK_SHARE;
import static com.doodle.Tool.AppConstants.ITEM_KEY;
import static com.doodle.Tool.Tools.containsIllegalCharacters;
import static com.doodle.Tool.Tools.delayLoadComment;
import static com.doodle.Tool.Tools.dismissDialog;
import static com.doodle.Tool.Tools.getDomainName;
import static com.doodle.Tool.Tools.getSpannableStringBuilder;
import static com.doodle.Tool.Tools.isNullOrEmpty;
import static com.doodle.Tool.Tools.sendNotificationRequest;
import static com.doodle.Tool.Tools.showBlockUser;
import static java.lang.Integer.parseInt;

public class MultipleMediaPopUpFragment extends Fragment {

    View view;
    private RelativeLayout commentHold;
    private LinearLayout linkScriptContainer, postBodyLayer;
    private EmojiTextView tvCommentMessage;
    private TextView tvHeaderInfo, tvPostTime, tvPostUserName, tvPostLinkTitle, tvPostLinkDescription, tvPostLinkHost, tvImgShareCount, tvPostLikeCount, tvCommentCount, tvCommentUserName, tvCommentTime, tvCommentLike, tvCommentReply, tvCountCommentLike;
    private CircleImageView imagePostUser, imageCommentUser;
    private ReadMoreTextView tvPostContent;
    private EmojiTextView tvPostEmojiContent;
    private ImageView imgLinkScript, imagePostPermission, imagePostShare, imagePermission, imgLike, imageCommentLikeThumb, imagePostComment, star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    private ProgressBar mProgressBar;
    private RecyclerView singleImgRecyclerView;

    private Drawable mDrawable;

    private PostItem item;
    private GalleryAdapter galleryAdapter;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private PopupMenu popup, popupMenu;
    private HomeService webService;
    private CommentService commentService;
    private PrefManager manager;

    private String deviceId, profileId, token, userIds;
    private int limit = 10, position = -1;
    private int offset = 0;
    private boolean networkOk, hasFooter, isCommentAction;
    private String postPermissions;
    private boolean notificationOff;

    //Delete post
    public ImageHolder.PostItemListener listener;
    private String blockUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.multiple_media_popup_fragment_layout, container, false);

        initialComponent();
        setData();

        return view;
    }

    private void initialComponent() {
        item = getArguments().getParcelable(ITEM_KEY);
        hasFooter = getArguments().getBoolean("has_footer");
        isCommentAction = getArguments().getBoolean("is_comment_action");
        position = getArguments().getInt("position");

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);

        linkScriptContainer = view.findViewById(R.id.linkScriptContainer);
        postBodyLayer = view.findViewById(R.id.postBodyLayer);
        tvPostLinkTitle = view.findViewById(R.id.tvPostLinkTitle);
        tvPostLinkDescription = view.findViewById(R.id.tvPostLinkDescription);
        tvPostLinkHost = view.findViewById(R.id.tvPostLinkHost);
        imgLinkScript = view.findViewById(R.id.imgLinkScript);
        imagePostShare = view.findViewById(R.id.imagePostShare);
        imagePermission = view.findViewById(R.id.imagePermission);
        tvPostUserName = view.findViewById(R.id.tvPostUserName);
        imagePostUser = view.findViewById(R.id.imagePostUser);
        tvHeaderInfo = view.findViewById(R.id.tvHeaderInfo);
        tvImgShareCount = view.findViewById(R.id.tvImgShareCount);
        tvPostTime = view.findViewById(R.id.tvPostTime);
        tvPostLikeCount = view.findViewById(R.id.tvPostLikeCount);
        //tvPostContent = (ReadMoreTextView) view.findViewById(R.id.tvPostContent);
        tvPostContent = view.findViewById(R.id.tvPostContent);
        tvPostEmojiContent = view.findViewById(R.id.tvPostEmojiContent);
        tvCommentCount = view.findViewById(R.id.tvCommentCount);

        star1 = view.findViewById(R.id.star1);
        star2 = view.findViewById(R.id.star2);
        star3 = view.findViewById(R.id.star3);
        star4 = view.findViewById(R.id.star4);
        star5 = view.findViewById(R.id.star5);
        star6 = view.findViewById(R.id.star6);
        star7 = view.findViewById(R.id.star7);
        star8 = view.findViewById(R.id.star8);
        star9 = view.findViewById(R.id.star9);
        star10 = view.findViewById(R.id.star10);
        star11 = view.findViewById(R.id.star11);
        star12 = view.findViewById(R.id.star12);
        star13 = view.findViewById(R.id.star13);
        star14 = view.findViewById(R.id.star14);
        star15 = view.findViewById(R.id.star15);
        star16 = view.findViewById(R.id.star16);

        imagePostPermission = view.findViewById(R.id.imagePostPermission);

        //Comment
        tvCommentMessage = view.findViewById(R.id.tvCommentMessage);
        commentHold = view.findViewById(R.id.commentHold);
        imgLike = view.findViewById(R.id.imgLike);
        imageCommentLikeThumb = view.findViewById(R.id.imageCommentLikeThumb);
        imageCommentUser = view.findViewById(R.id.imageCommentUser);

        tvCommentUserName = view.findViewById(R.id.tvCommentUserName);
        tvCommentTime = view.findViewById(R.id.tvCommentTime);
        tvCommentLike = view.findViewById(R.id.tvCommentLike);
        tvCommentReply = view.findViewById(R.id.tvCommentReply);
        tvCountCommentLike = view.findViewById(R.id.tvCountCommentLike);
        imageCommentLikeThumb.setVisibility(View.GONE);
        tvCountCommentLike.setVisibility(View.GONE);

        //All comment post
        commentService = CommentService.mRetrofit.create(CommentService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());
        mProgressBar = view.findViewById(R.id.ProgressBar);
        imagePostComment = view.findViewById(R.id.imagePostComment);
        singleImgRecyclerView = view.findViewById(R.id.recyclerView);
        singleImgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GalleryAdapter.RecyclerViewClickListener listener = new GalleryAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        };

        galleryAdapter = new GalleryAdapter(getActivity(), item.getPostFiles(), listener);
//        singleImgRecyclerView.setMediaObjects(item.getPostFiles());
        singleImgRecyclerView.setAdapter(galleryAdapter);
    }

    private void setData() {
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

        if (!isNullOrEmpty(item.getTotalComment()) && !"0".equalsIgnoreCase(item.getTotalComment())) {
            tvCommentCount.setText(item.getTotalComment());
        }

        if (item.getPostFooter().isLikeUserStatus()) {
            imgLike.setImageResource(R.drawable.like_done);
        } else {
            imgLike.setImageResource(R.drawable.like_normal);
        }

        if (isCommentAction) {
            openCommentSection();
        }

        setDataPostWise();

        tvPostLikeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
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

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userIds.equalsIgnoreCase(item.getPostUserid())) {
                    Tools.toast(getContext(), "On Liker, you can't like your own posts. That would be cheating ", R.drawable.ic_info_outline_blue_24dp);
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

        tvPostUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("user_id", item.getPostUserid()).putExtra("user_name", item.getPostUsername()));
            }
        });

        imagePostUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class).putExtra("user_id", item.getPostUserid()).putExtra("user_name", item.getPostUsername()));
            }
        });

        imagePostShare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(getActivity(), v);
                popup.getMenuInflater().inflate(R.menu.share_menu, popup.getMenu());

//                popup.show();
                MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) popup.getMenu(), v);
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

                                    Toast.makeText(getContext(), "Share successFull", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(getContext(), "Share cancel", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(FacebookException error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            getContext().startActivity(i);
                        }
                        if (id == R.id.copyLink) {

                            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
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
                popupMenu = new PopupMenu(getActivity(), v);
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
                MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) popupMenu.getMenu(), v);
                menuHelper.setForceShowIcon(true);
                menuHelper.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        postPermissions = menuItem.getTitle().toString();
                        if (id == R.id.blockedUser) {
                            if (!((Activity) getActivity()).isFinishing()) {
                                App.setItem(item);
                                showBlockUser(v);
                            } else {
                                dismissDialog();
                            }
                        }
                        if (id == R.id.reportedPost) {
                            App.setItem(item);
                            if (networkOk) {
                                Call<ReportReason> call = commentService.getReportReason(deviceId, profileId, token, item.getPostUserid(), "2", userIds);
                                sendReportReason(call);
                            } else {
                                Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.publics) {
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "0", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.friends) {
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "2", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                            }
                        }
                        if (id == R.id.onlyMe) {
                            if (networkOk) {
                                Call<String> call = webService.postPermission(deviceId, profileId, token, "1", item.getPostId());
                                sendPostPermissionRequest(call);
                            } else {
                                Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                            }

                        }

                        if (id == R.id.edit) {
                            Intent intent = new Intent(getContext(), EditPost.class);
                            intent.putExtra(ITEM_KEY, (Parcelable) item);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                        }
                        if (id == R.id.delete) {
//                            listener.deletePost(item, position); work
                        }
                        if (id == R.id.turnOffNotification) {
                            switch (postPermissions) {
                                case "Turn off notifications":
                                    notificationOff = true;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOff(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                                    }
                                    break;
                                case "Turn on notifications":
                                    notificationOff = false;
                                    if (networkOk) {
                                        Call<String> call = webService.postNotificationTurnOn(deviceId, profileId, token, userIds, item.getPostId());
                                        sendNotificationRequest(call);
                                    } else {
                                        Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                                    }
                                    break;
                            }
                        }
                        return true;
                    }
                });

            }
        });

        imagePostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentSection();
            }
        });
    }

    private void openCommentSection() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        if (networkOk) {
            Call<CommentItem> call = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", item.getPostId(), userIds);
            sendAllCommentItemRequest(call);
            delayLoadComment(mProgressBar);
        } else {
            Tools.showNetworkDialog(activity.getSupportFragmentManager());
        }
    }

    private void setDataPostWise() {
        if (item.getPostType().equals("3") || item.getPostType().equals("4")) {
            linkScriptContainer.setVisibility(View.VISIBLE);
            if (item.getPostType().equalsIgnoreCase("3")) {
                String linkImage = AppConstants.Link_IMAGE_PATH + item.getPostImage();
                Glide.with(App.getAppContext())
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgLinkScript);
            } else if (item.getPostType().equalsIgnoreCase("4")) {
                String linkImage = AppConstants.YOUTUBE_IMAGE_PATH + item.getPostImage();
                Glide.with(App.getAppContext())
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgLinkScript);
            }
            tvPostLinkTitle.setText(item.getPostLinkTitle());
            tvPostLinkDescription.setText(item.getPostLinkDesc());
            try {
                String domainName = getDomainName(item.getPostLinkUrl());
                tvPostLinkHost.setText(domainName);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            linkScriptContainer.setVisibility(View.GONE);
            if (item.getPostType().equals("1")) {
                String hasMim = item.getHasMeme();
                int mimId = Integer.parseInt(hasMim);
                if (mimId > 0)
                    setMimPost();
            }
        }
    }

    private void setMimPost() {
        List<Mim> viewColors = DataProvider.mimList;
        int mimId = Integer.parseInt(item.getHasMeme());
        for (Mim temp : viewColors) {
            int getId = temp.getId() == 1 ? 0 : temp.getId();
            if (mimId == getId && mimId > 0) {
                String mimColor = temp.getMimColor();
                if (mimColor.startsWith("#")) {
                    postBodyLayer.setBackgroundColor(Color.parseColor(mimColor));
                    ViewGroup.LayoutParams params = postBodyLayer.getLayoutParams();
                    if (App.isIsMimPopup()) {
                        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
                        int myMimHeight = (displayMetrics.heightPixels) * 75 / 100;
                        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                        // params.height= (int) dpHeight;
                        params.height = myMimHeight;
                    } else {
                        params.height = 350;
                    }
                    postBodyLayer.setLayoutParams(params);
                    postBodyLayer.setGravity(Gravity.CENTER);
                    tvPostContent.setGravity(Gravity.CENTER);
                    tvPostEmojiContent.setGravity(Gravity.CENTER);
                    tvPostContent.setTextAppearance(App.getAppContext(), android.R.style.TextAppearance_Large);
                    tvPostEmojiContent.setTextAppearance(App.getAppContext(), android.R.style.TextAppearance_Large);
                    tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                    tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                } else {

                    String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                    Picasso.with(App.getInstance()).load(imageUrl).into(target);
                    postBodyLayer.setGravity(Gravity.CENTER);
                    tvPostContent.setGravity(Gravity.CENTER);
                    tvPostEmojiContent.setGravity(Gravity.CENTER);
                    postBodyLayer.setBackground(mDrawable);
                    tvPostContent.setHeight(150);
                    tvPostEmojiContent.setHeight(150);
                    switch (mimColor) {
                        case "img_bg_birthday.png":
                            tvPostContent.setTextColor(Color.parseColor("#000000"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#000000"));
                            break;
                        case "img_bg_love.png":
                            tvPostContent.setTextColor(Color.parseColor("#2D4F73"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#2D4F73"));
                            break;
                        case "img_bg_love2.png":
                            tvPostContent.setTextColor(Color.parseColor("#444748"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#444748"));
                            break;
                        case "img_bg_red.png":
                        case "img_bg_love3.png":
                            tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }
            }
        }
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

                                int postLikeNumeric = Integer.valueOf(item.getPostFooter().getPostTotalLike());
                                postLikeNumeric = postLikeNumeric <= 0 ? 0 : --postLikeNumeric;
                                item.getPostFooter().setPostTotalLike(String.valueOf(postLikeNumeric));
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

                                if (hasFooter)
                                    getActivity().sendBroadcast((new Intent().putExtra("post_item", (Serializable) item).putExtra("position", position).putExtra("isFooterChange", true).setAction(AppConstants.POST_CHANGE_BROADCAST)));

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

                                int postLikeNumeric = Integer.valueOf(item.getPostFooter().getPostTotalLike());
                                postLikeNumeric++;
                                item.getPostFooter().setPostTotalLike(String.valueOf(postLikeNumeric));
                                item.getPostFooter().setLikeUserStatus(true);

                                SpannableString content = new SpannableString(String.valueOf(postLikeNumeric));
                                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                tvPostLikeCount.setVisibility(View.VISIBLE);
                                tvPostLikeCount.setText(content);
                                imgLike.setImageResource(R.drawable.like_done);

                                if (hasFooter)
                                    getActivity().sendBroadcast((new Intent().putExtra("post_item", (Serializable) item).putExtra("position", position).putExtra("isFooterChange", true).setAction(AppConstants.POST_CHANGE_BROADCAST)));

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


    private void sendAllCommentItemRequest(Call<CommentItem> call) {

        call.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {
                if (response.body() != null) {
                    CommentItem commentItem = response.body();
                    Intent intent = new Intent(getContext(), CommentPost.class);
                    intent.putExtra(COMMENT_KEY, (Parcelable) commentItem);
                    intent.putExtra(ITEM_KEY, (Parcelable) item);
                    startActivity(intent);
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
                    Intent intent = new Intent(getContext(), PostShare.class);
                    //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                    intent.putExtra(ITEM_KEY, (Parcelable) postShareItem);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<PostShareItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

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
                    reportReasonSheet.show(getChildFragmentManager(), "ReportReasonSheet");

                }
            }

            @Override
            public void onFailure(Call<ReportReason> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
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


}
