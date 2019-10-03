package com.doodle.Home.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.danikula.videocache.HttpProxyCacheServer;
import com.doodle.App;
import com.doodle.Home.adapter.PostAdapter;
import com.doodle.Home.holder.ImageHolder;
import com.doodle.Home.holder.LinkScriptHolder;
import com.doodle.Home.holder.LinkScriptYoutubeHolder;
import com.doodle.Home.holder.TextMimHolder;
import com.doodle.Home.holder.VideoHolder;
import com.doodle.Home.model.Headers;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.model.postshare.PostShares;
import com.doodle.Home.model.postshare.PostTextIndex;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Post.adapter.ChatAdapter;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.Post.view.fragment.PostPermission;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.Operation;
import com.doodle.Tool.PageTransformer;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.SingleLineTextView;
import com.doodle.Tool.Tools;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.App.getProxy;
import static com.doodle.Tool.Tools.containsIllegalCharacters;
import static com.doodle.Tool.Tools.extractMentionText;
import static com.doodle.Tool.Tools.extractMentionUser;
import static com.doodle.Tool.Tools.extractUrls;
import static com.doodle.Tool.Tools.getDomainName;
import static com.doodle.Tool.Tools.getSpannableStringBuilder;
import static com.doodle.Tool.Tools.isNullOrEmpty;
import static java.lang.Integer.parseInt;

public class PostShare extends AppCompatActivity implements
        View.OnClickListener,
        PostPermission.BottomSheetListener,
        TextHolder.PostItemListener,
        TextMimHolder.PostItemListener,
        VideoHolder.PostItemListener,
        LinkScriptYoutubeHolder.PostItemListener,
        LinkScriptHolder.PostItemListener,
        ImageHolder.PostItemListener
{

    private final String TAG = "PostShares";
    private TextView tvAudience;
    private CardView onlyImage, onlyVideo, onlyLinkScript, onlyLinkScriptYoutube;
    private LinearLayout onlyText, shareContent, onlyMim;
    ArrayList<String> mList;
    //postContent
    private ReadMoreTextView tvPostContent, tvPostContentMim, tvPostContentImage, tvPostContentLink, tvPostContentLinkYoutube, tvPostContentVideo;
    private String full_text;
    public EmojiTextView tvPostEmojiContent, tvPostEmojiContentMim, tvPostEmojiContentImage, tvPostEmojiContentLink, tvPostEmojiContentLinkYoutube, tvPostEmojiContentVideo;
    List<Mim> viewColors = DataProvider.mimList;
    private Drawable mDrawable;

    private ImageView imageMedia, imgLinkScript, imgLinkScriptYoutube;
    private TextView tvPostLinkTitle, tvPostLinkTitleYoutube, tvPostLinkDescription, tvPostLinkDescriptionYoutube, tvPostLinkHost, tvPostLinkHostYoutube;
    private JZVideoPlayerStandard jzVideoPlayerStandard;

    private TextView tvPermission;
    private ImageView imgPermission, imageEmoji;
    private int postPermission;
    private EmojiPopup emojiPopup;
    private ViewGroup rootView;
    EmojiEditText editPostMessage;
    private TextView tvSubmitPost;

    //Emoji
    ChatAdapter chatAdapter;

    //Network
    private boolean networkOk;
    private CircularProgressView progressView;
    public HomeService webService;
    private String profileId;
    private String deviceId;
    private String userIds;
    private String toUserId;
    private String token;
    private PrefManager manager;
    private String postId;
    private String postContent;
    private String categoryId;
    private String subCategoryId;
    private String friends;
    private String postType;
    private String userName;
    private String fromUserId;

    //socket.io
    private Socket socket;
    private Headers headers;
    private PostShares postShares;
    private String imageUrl;
    private CircleImageView imgPostUser;

    //Add Header
    public CircleImageView imagePostUser;
    public ImageView imagePostPermission, imagePermission, star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    private SingleLineTextView tvPostUserName, tvPostTime;
    private TextView tvHeaderInfo;
    private RecyclerView sharedRecyclerview;
    PostAdapter adapter;
    public List<PostItem> postItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_share);

        PostItem item = getIntent().getExtras().getParcelable(TextHolder.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }
        socket = SocketIOManager.wSocket;
        headers = new Headers();
        sharedRecyclerview = findViewById(R.id.sharedRecyclerview);
        postShares = new PostShares();
        networkOk = NetworkHelper.hasNetworkAccess(this);
        chatAdapter = new ChatAdapter();
        manager = new PrefManager(this);
        webService = HomeService.mRetrofit.create(HomeService.class);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        rootView = findViewById(R.id.main_activity_root_view);
        tvPermission = findViewById(R.id.tvPermission);
        imgPermission = findViewById(R.id.imgPermission);
        editPostMessage = findViewById(R.id.editPostMessage);
        tvSubmitPost = findViewById(R.id.tvSubmitPost);
        tvSubmitPost.setOnClickListener(this);
        postItemList = new ArrayList<>();

        imgPostUser = findViewById(R.id.imgPostUser);
        imageUrl = manager.getProfileImage();
        if (!isNullOrEmpty(imageUrl)) {
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile)
                    .into(imgPostUser);
            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile)
                    .into(imgPostUser);
        }

        App.setSharePostfooter(true);
        postItemList.add(item);
        adapter = new PostAdapter(this, postItemList, this, this, this, this, this, this, true);
        sharedRecyclerview.setAdapter(adapter);

        //HEADER
        profileId = manager.getProfileId();
        userIds = manager.getProfileId();
        fromUserId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();

        //Form Data
        postId = item.getSharedPostId();
        categoryId = item.getCatId();
        subCategoryId = "";
        friends = "";
        postType = item.getPostType();
        userName = item.getPostUsername();
        toUserId = item.getPostUserid();

        onlyImage = findViewById(R.id.onlyImage);
        onlyVideo = findViewById(R.id.onlyVideo);
        onlyLinkScript = findViewById(R.id.onlyLinkScript);
        onlyLinkScriptYoutube = findViewById(R.id.onlyLinkScriptYoutube);
        onlyMim = findViewById(R.id.onlyMim);
        onlyText = findViewById(R.id.onlyText);

        imageMedia = findViewById(R.id.imageMedia);
        imgLinkScript = findViewById(R.id.imgLinkScript);
        imgLinkScriptYoutube = findViewById(R.id.imgLinkScriptYoutube);
        tvPostLinkTitle = findViewById(R.id.tvPostLinkTitle);
        tvPostLinkTitleYoutube = findViewById(R.id.tvPostLinkTitleYoutube);
        tvPostLinkDescription = findViewById(R.id.tvPostLinkDescription);
        tvPostLinkDescriptionYoutube = findViewById(R.id.tvPostLinkDescriptionYoutube);
        tvPostLinkHost = findViewById(R.id.tvPostLinkHost);
        tvPostLinkHostYoutube = findViewById(R.id.tvPostLinkHostYoutube);
        jzVideoPlayerStandard = findViewById(R.id.videoplayer);

        shareContent = findViewById(R.id.shareContent);

        tvPostContent = (ReadMoreTextView) findViewById(R.id.tvPostContent);
        tvPostContentMim = (ReadMoreTextView) findViewById(R.id.tvPostContentMim);
        tvPostContentImage = (ReadMoreTextView) findViewById(R.id.tvPostContentImage);
        tvPostContentLink = (ReadMoreTextView) findViewById(R.id.tvPostContentLink);
        tvPostContentLinkYoutube = (ReadMoreTextView) findViewById(R.id.tvPostContentLinkYoutube);
        tvPostContentVideo = (ReadMoreTextView) findViewById(R.id.tvPostContentVideo);

        tvPostEmojiContent = findViewById(R.id.tvPostEmojiContent);
        tvPostEmojiContentMim = findViewById(R.id.tvPostEmojiContentMim);
        tvPostEmojiContentImage = findViewById(R.id.tvPostEmojiContentImage);
        tvPostEmojiContentLink = findViewById(R.id.tvPostEmojiContentLink);
        tvPostEmojiContentLinkYoutube = findViewById(R.id.tvPostEmojiContentLinkYoutube);
        tvPostEmojiContentVideo = findViewById(R.id.tvPostEmojiContentVideo);

        imageEmoji = findViewById(R.id.main_activity_emoji);
        imageEmoji.setOnClickListener(this);
        findViewById(R.id.contentPostPermission).setOnClickListener(this);
        findViewById(R.id.imageCancelPost).setOnClickListener(this);
        setUpEmojiPopup();

        mList = new ArrayList<>();
        tvAudience = findViewById(R.id.tvAudience);
        tvAudience.setText(item.getCatName());
        String postType = item.getPostType();
        int viewType = Integer.parseInt(postType);

        String hasMim = item.getHasMeme();
        int mimId = Integer.parseInt(hasMim);


        //HEADER ID

//
//        switch (viewType) {
//            case 1:
//                if (mimId > 0) {
//                    onlyMim.setVisibility(View.VISIBLE);
//                    setMim(item);
//                    break;
//                } else {
//                    onlyText.setVisibility(View.VISIBLE);
//                    setText(item);
//                    break;
//                }
//
//            case 2:
//                onlyImage.setVisibility(View.VISIBLE);
//                setImage(item);
//                break;
//            case 3:
//                onlyLinkScript.setVisibility(View.VISIBLE);
//                setLinkScript(item);
//                break;
//            case 4:
//                onlyLinkScriptYoutube.setVisibility(View.VISIBLE);
//                setLinkScriptYoutube(item);
//                break;
//            case 5:
//                onlyVideo.setVisibility(View.VISIBLE);
//                setVideo(item);
//                break;
//
//        }


        //HEADER SET
       // initheader();
        //setHeader(item);


    }

    private void setHeader(PostShareItem item) {
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

        String userImageUrl = AppConstants.PROFILE_IMAGE + item.getUesrProfileImg();
        Glide.with(App.getAppContext())
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imagePostUser);
        SpannableStringBuilder builder = getSpannableStringBuilder(getApplicationContext(), "", likes, followers, totalStars, categoryName);


        tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        long myMillis = Long.parseLong(item.getDateTime()) * 1000;
        String postDate = Operation.getFormattedDateFromTimestamp(myMillis);

        tvPostTime.setText(postDate);
        tvHeaderInfo.setText(builder);
    }

    private void initheader() {
        tvHeaderInfo = findViewById(R.id.tvHeaderInfo);
        tvPostUserName = findViewById(R.id.tvPostUserName);
        tvPostTime = findViewById(R.id.tvPostTime);
        imagePostPermission = findViewById(R.id.imagePostPermission);
        imagePostUser = findViewById(R.id.imagePostUser);
        imagePermission = findViewById(R.id.imagePermission);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        star6 = findViewById(R.id.star6);
        star7 = findViewById(R.id.star7);
        star8 = findViewById(R.id.star8);
        star9 = findViewById(R.id.star9);
        star10 = findViewById(R.id.star10);
        star11 = findViewById(R.id.star11);
        star12 = findViewById(R.id.star12);
        star13 = findViewById(R.id.star13);
        star14 = findViewById(R.id.star14);
        star15 = findViewById(R.id.star15);
        star16 = findViewById(R.id.star16);
        imagePermission.setVisibility(View.GONE);

    }

    private void setVideo(PostShareItem item) {

        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContentVideo.setVisibility(View.GONE);
            tvPostEmojiContentVideo.setVisibility(View.VISIBLE);
            tvPostEmojiContentVideo.setText(item.getPostText());
            Linkify.addLinks(tvPostEmojiContentVideo, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostEmojiContentVideo);

        } else {
            tvPostEmojiContentVideo.setVisibility(View.GONE);
            tvPostContentVideo.setVisibility(View.VISIBLE);
            tvPostContentVideo.setText(item.getPostText());
            Linkify.addLinks(tvPostContentVideo, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostContentVideo);

        }

        String videoPath = AppConstants.POST_VIDEOS + item.getVideoName();

        HttpProxyCacheServer proxy = getProxy(App.getAppContext());
        jzVideoPlayerStandard.setUp(proxy.getProxyUrl(videoPath)
                , JZVideoPlayerStandard.SCREEN_LAYOUT_LIST, "");


        Glide.with(App.getAppContext()).load(videoPath).apply(new RequestOptions().override(50, 50)).into(jzVideoPlayerStandard.thumbImageView);
    }

    private void setLinkScriptYoutube(PostShareItem item) {


        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContentLinkYoutube.setVisibility(View.GONE);
            tvPostEmojiContentLinkYoutube.setVisibility(View.VISIBLE);
            tvPostEmojiContentLinkYoutube.setText(item.getPostText());
            Linkify.addLinks(tvPostEmojiContentLinkYoutube, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostEmojiContentLinkYoutube);

        } else {
            tvPostEmojiContentLinkYoutube.setVisibility(View.GONE);
            tvPostContentLinkYoutube.setVisibility(View.VISIBLE);
            tvPostContentLinkYoutube.setText(item.getPostText());
            Linkify.addLinks(tvPostContentLinkYoutube, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostContentLinkYoutube);

        }


        if ("default-profile-picture.png".equalsIgnoreCase(item.getPostImage())) {
            imgLinkScriptYoutube.setVisibility(View.GONE);
        } else {
            imgLinkScriptYoutube.setVisibility(View.VISIBLE);
            if (item.getPostType().equalsIgnoreCase("3")) {
                String linkImage = AppConstants.Link_IMAGE_PATH + item.getPostImage();
                Glide.with(App.getAppContext())
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgLinkScriptYoutube);
            } else if (item.getPostType().equalsIgnoreCase("4")) {
                String linkImage = AppConstants.YOUTUBE_IMAGE_PATH + item.getPostImage();
                Glide.with(App.getAppContext())
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
                        .into(imgLinkScriptYoutube);
            }

        }


        tvPostLinkTitleYoutube.setText(item.getPostLinkTitle());
        tvPostLinkDescriptionYoutube.setText(item.getPostLinkDesc());
        try {
            String domainName = getDomainName(item.getPostLinkUrl());
            tvPostLinkHostYoutube.setText(domainName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void setLinkScript(PostShareItem item) {

        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContentLink.setVisibility(View.GONE);
            tvPostEmojiContentLink.setVisibility(View.VISIBLE);
            tvPostEmojiContentLink.setText(item.getPostText());
            Linkify.addLinks(tvPostEmojiContentLink, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostEmojiContentLink);

        } else {
            tvPostEmojiContentLink.setVisibility(View.GONE);
            tvPostContentLink.setVisibility(View.VISIBLE);
            tvPostContentLink.setText(item.getPostText());
            Linkify.addLinks(tvPostContentLink, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostContentLink);

        }

        if ("default-profile-picture.png".equalsIgnoreCase(item.getPostImage())) {
            imgLinkScript.setVisibility(View.GONE);
        } else {
            imgLinkScript.setVisibility(View.VISIBLE);
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

        }

        tvPostLinkTitle.setText(item.getPostLinkTitle());
        tvPostLinkDescription.setText(item.getPostLinkDesc());
        try {
            String domainName = getDomainName(item.getPostLinkUrl());
            tvPostLinkHost.setText(domainName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void setImage(PostShareItem item) {

        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContentImage.setVisibility(View.GONE);
            tvPostEmojiContentImage.setVisibility(View.VISIBLE);
            tvPostEmojiContentImage.setText(item.getPostText());
            Linkify.addLinks(tvPostEmojiContentImage, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostEmojiContentImage);

        } else {
            tvPostEmojiContentImage.setVisibility(View.GONE);
            tvPostContentImage.setVisibility(View.VISIBLE);
            tvPostContentImage.setText(item.getPostText());
            Linkify.addLinks(tvPostContentImage, Linkify.ALL);
            //set user name in blue color and remove underline from the textview
            Tools.stripUnderlines(tvPostContentImage);

        }

        String postImages = AppConstants.POST_IMAGES + item.getPostImage();

        Glide.with(App.getAppContext())
                .load(postImages)
                .centerCrop()
                .dontAnimate()
                .into(imageMedia);
    }

    private void setMim(PostShareItem item) {


        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContentMim.setVisibility(View.GONE);
            tvPostEmojiContentMim.setVisibility(View.VISIBLE);
            tvPostEmojiContentMim.setText(text);

        } else {
            tvPostEmojiContentMim.setVisibility(View.GONE);
            tvPostContentMim.setVisibility(View.VISIBLE);
            tvPostContentMim.setText(text);
        }

        int mimId = Integer.parseInt(item.getHasMeme());
        for (Mim temp : viewColors) {
            int getId = temp.getId() == 1 ? 0 : temp.getId();
            if (mimId == getId && mimId > 0) {
                String mimColor = temp.getMimColor();
                if (mimColor.startsWith("#")) {
                    onlyMim.setBackgroundColor(Color.parseColor(mimColor));
                    tvPostContentMim.setTextColor(Color.parseColor("#FFFFFF"));
                    tvPostEmojiContentMim.setTextColor(Color.parseColor("#FFFFFF"));
                } else {

                    String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                    Picasso.with(App.getInstance()).load(imageUrl).into(target);
                    onlyMim.setBackground(mDrawable);

                    switch (mimColor) {
                        case "img_bg_birthday.png":
                            tvPostContentMim.setTextColor(Color.parseColor("#000000"));
                            tvPostEmojiContentMim.setTextColor(Color.parseColor("#000000"));
                            break;
                        case "img_bg_love.png":
                            tvPostContentMim.setTextColor(Color.parseColor("#2D4F73"));
                            tvPostEmojiContentMim.setTextColor(Color.parseColor("#2D4F73"));
                            break;
                        case "img_bg_love2.png":
                            tvPostContentMim.setTextColor(Color.parseColor("#444748"));
                            tvPostEmojiContentMim.setTextColor(Color.parseColor("#444748"));
                            break;
                        case "img_bg_red.png":
                            tvPostContentMim.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContentMim.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                        case "img_bg_love3.png":
                            tvPostContentMim.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContentMim.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }
            }
        }


    }

    private void setText(PostShareItem item) {


        String text = item.getPostText();
        StringBuilder nameBuilder = new StringBuilder();
        List<String> mentionUrl = extractUrls(item.getPostText());


        for (PostTextIndex temp : item.getPostTextIndex()) {
            String postType = temp.getType();
            if (postType.equalsIgnoreCase("mention")) {
                String mentionUserName = extractMentionUser(temp.getText());
                nameBuilder.append(mentionUserName);
                nameBuilder.append(" ");
            }

        }
        if (containsIllegalCharacters(text)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(text);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            tvPostContent.setText(text);
        }

        if (mentionUrl.size() > 0 && extractMentionText(item).trim().length() > 0) {

            full_text = extractMentionText(item).trim();

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
                tvPostContent.setText(str);


            }
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("TAG", "onBitmapLoaded: " + bitmap);
            mDrawable = new BitmapDrawable(getResources(), bitmap);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.contentPostPermission:
                String message = tvPermission.getText().toString();
                PostPermission postPermissions = PostPermission.newInstance(message);
                postPermissions.show(getSupportFragmentManager(), "PostPermission");
                break;
            case R.id.main_activity_emoji:
                emojiPopup.toggle();
                break;
            case R.id.imageCancelPost:
                App.setSharePostfooter(false);
                finish();
                break;
            case R.id.tvSubmitPost:
                final String text = editPostMessage.getText().toString().trim();
                postContent = text;

                if (text.length() > 0) {
                    chatAdapter.add(text);

                    editPostMessage.setText("");
                }

                final RecyclerView recyclerViews = findViewById(R.id.main_activity_recycler_view);
                recyclerViews.setAdapter(chatAdapter);
                recyclerViews.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                shareAsPost();
                break;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.setSharePostfooter(false);
    }

    private void shareAsPost() {

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.addSharedPost(
                    deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                    profileId,//"26444",
                    token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                    userIds,//"26444",
                    postId,//"26444",
                    postContent,//"status",
                    postPermission,//0,
                    categoryId,
                    subCategoryId,//"",
                    postType,//0,
                    userName,//3,
                    friends
            );
            sendSharedPostRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    private void sendSharedPostRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        try {
                            JSONObject object = new JSONObject(response.body());
                            JSONObject successObject = object.getJSONObject("success");
                            boolean status = successObject.getBoolean("status");
                            if (status) {
                                Toast.makeText(PostShare.this, "successfully shared!", Toast.LENGTH_SHORT).show();

                                App.setIsPostShare(true);

                                Call<String> mCall = webService.sendBrowserNotification(
                                        deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                                        profileId,//"26444",
                                        token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                                        toUserId,//"26444",
                                        fromUserId,//"26444",
                                        postId,
                                        "share_post"
                                );
                                sendBrowserNotificationRequest(mCall);
                                //sendBrowserNotification
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });

    }

    private void sendBrowserNotificationRequest(Call<String> mCall) {
        mCall.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
               /* if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());


                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }*/


                String categoryId = App.getCategoryId();
                headers.setDeviceId(deviceId);
                headers.setIsApps(true);
                headers.setSecurityToken(token);
                postShares.setPostId(postId);
                postShares.setToUserId(toUserId);
                postShares.setUserId(fromUserId);

                postShares.setHeaders(headers);
                Gson gson = new Gson();
                String json = gson.toJson(postShares);

                socket.emit("post_share", json, new Ack() {
                    @Override
                    public void call(Object... args) {

                    }
                });


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });

    }


    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
                .setOnEmojiClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
                .setOnEmojiPopupShownListener(() -> imageEmoji.setImageResource(R.drawable.ic_keyboard))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
                .setOnEmojiPopupDismissListener(() -> imageEmoji.setImageResource(R.drawable.emoji_twitter_category_smileysandpeople))
                .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .setPageTransformer(new PageTransformer())
                .build(editPostMessage);
    }


    @Override
    public void onButtonClicked(int image, String text) {

        tvPermission.setText(text);
        imgPermission.setImageResource(image);
        switch (text) {
            case "Public":
                postPermission = 0;
                break;
            case "Only me":
                postPermission = 1;
                break;
            case "Friends":
                postPermission = 2;
                break;


        }

    }

    @Override
    public void deletePost(PostItem postItem, int position) {

    }
}
