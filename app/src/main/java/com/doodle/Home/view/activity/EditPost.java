package com.doodle.Home.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Home.holder.mediaHolder.ImageViewHolder;
import com.doodle.Home.holder.mediaHolder.VideoViewHolder;
import com.doodle.Home.model.PostFile;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.PostTextIndex;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Home.service.HomeService;
import com.doodle.Post.adapter.ChatAdapter;
import com.doodle.Post.adapter.ImageAdapter;
import com.doodle.Post.adapter.MediaAdapter;
import com.doodle.Post.adapter.MentionUserAdapter;
import com.doodle.Post.adapter.MimAdapter;
import com.doodle.Post.model.Category;
import com.doodle.Post.model.CategoryItem;
import com.doodle.Post.model.MentionUser;
import com.doodle.Post.model.Mim;
import com.doodle.Post.model.MultipleMediaFile;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.model.PostVideo;
import com.doodle.Post.model.Subcatg;
import com.doodle.Post.service.DataProvider;
import com.doodle.Post.service.PostService;
import com.doodle.Post.view.activity.PostNew;
import com.doodle.Post.view.fragment.Audience;
import com.doodle.Post.view.fragment.ContributorStatus;
import com.doodle.Post.view.fragment.PostPermission;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PageTransformer;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.doodle.Tool.Tools.containsIllegalCharacters;
import static com.doodle.Tool.Tools.extractMentionText;
import static com.doodle.Tool.Tools.extractMentionUser;
import static com.doodle.Tool.Tools.getMD5EncryptedString;
import static com.doodle.Tool.Tools.isNullOrEmpty;

public class EditPost extends AppCompatActivity implements View.OnClickListener,
        PostPermission.BottomSheetListener,
        Audience.BottomSheetListener,
        ContributorStatus.ContributorStatusListener {

    // data to populate the RecyclerView with

    private ScrollView contentPost;
    private RelativeLayout contentPostView;
    private LinearLayout contentPostPermission, messageContainer;
    private TextView tvPermission, tvAudience;
    private ImageView imgPermission;
    private PrefManager manager;
    private HomeService homeService;
    private PostService webService, videoServices;
    private final String TAG = "PostNew";
    private boolean networkOk;
    private CircularProgressView progressView;
    boolean isGrantGallery = false;
    boolean isGrantCamera = false;

    int position;
    private String profileId;
    private String deviceId;
    private String userIds;
    private String toUserId;
    private String token;
    private CategoryItem categoryItem;
    private TextView tvSubmitPost;

    private MultipartBody.Part fileToUpload;

    //ExpandableListView
    //initialize

    //MIM
    RecyclerView mimRecyclerView;
    List<Mim> viewColors = DataProvider.mimList;
    boolean rvMimShow;
    //CATEGORY/AUDIENCE
    Drawable mDrawable;


    //Emoji
    ChatAdapter chatAdapter;
    EmojiPopup emojiPopup;
    EmojiEditText editPostMessage;
    ViewGroup rootView;
    ImageView emojiButton;

    //POST-IMAGE
    private ImageAdapter imageAdapter;
    private Uri profileImagePathUri;
    private static final int REQUEST_TAKE_CAMERA = 101;
    private static final int REQUEST_TAKE_GALLERY_IMAGE = 102;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 103;
    private RecyclerView mediaRecyclerView;
    boolean rvMediaShow;

    private List<PostImage> postImages   = new ArrayList<>();
    private List<PostVideo> postVideos  = new ArrayList<>(); ;


    //POST LINK SCRIPT
    private EditText editText, editTextTitlePost, editTextDescriptionPost;
    private Button submitButton, randomButton;
    private Context context;
    private ViewGroup dropPreview, dropPost;
    private TextView previewAreaTitle, postAreaTitle;
    private String currentTitle, currentUrl, currentCannonicalUrl,
            currentDescription;

    private Bitmap[] currentImageSet;
    private Bitmap currentImage;
    private int currentItem = 0;
    private int countBigImages = 0;
    private boolean noThumb;
    private TextCrawler textCrawler;
    boolean isLinkScript;
    List<String> extractedUrls = new ArrayList<>();
    private LinearLayout linkScriptContainer;
    private boolean rvLinkScriptShow;


    //MentionUserSearch
    RecyclerView recyclerViewSearchMention;
    private boolean rvMentionUserShow;
    private String userQuery;
    private boolean isFirstTimeShowMention;

    //CREATE NEW POST
    private String postType = "status";
    private int status;
    private int postPermission;
    private String imageFile, imageString, videoString;
    private List<String> uploadImageName = new ArrayList<>();
    private int contentType;
    private String categoryId = "", subCategoryId = "";
    private String contentTitle;
    private String contentLinkUrl;
    private String contentHost;
    private String contentLinkTitle;
    private String contentLinkDesc;
    private String contentLinkImage;
    private List<String> imageList;
    private List<String> videoList;
    private String myUrl = "";
    private boolean isYoutubeURL;
    private int hasMim;
    private int scheduleTime = 0;
    private String friends;
    // Set<String> friendSet = new HashSet<String>();
    List<String> friendSet = new ArrayList<>();
    private String mentionSearchData;

    private View mView;
    private String categoryName, audience;
    private boolean isAddContentTitle;
    private boolean isDuplicateFile;

    private String imageFilePath, videoFilePath;
    private String fileEncoded = "";
    private String replaceContent;
    List<String> nameList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    Map<String, String> wordsToReplace = new HashMap<String, String>();
    Set<String> keys = wordsToReplace.keySet();
    private String mentionMessage;
    private String base64File;
    private String videoExtension;
    private int postId;
    private String name;

    //EDIT POST
    PostItem editPostItem;
    String editPostId;
    private String text;

    //Multiple Media File
    List<String> mediaFiles;
    ProgressDialog progressDialog;
    private String deleteMediaIds;
    private CircleImageView imgPostUser;
    private String imageUrl;
    List<String> idSet;

    //DELETE MEDIA
    public ImageViewHolder.ImageListener imageListener;
    public VideoViewHolder.VideoListen videoListen;
   public MediaAdapter mediaAdapter;
    List<String> deleteMediaFiles;
    private FloatingActionButton postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);

        editPostItem = new PostItem();
        editPostItem = getIntent().getExtras().getParcelable(TextHolder.ITEM_KEY);
        position = getIntent().getIntExtra("position", -1);

        if (editPostItem == null) {
            throw new AssertionError("Null data item received!");
        }

        manager = new PrefManager(this);
        homeService = HomeService.mRetrofit.create(HomeService.class);
        webService = PostService.mRetrofit.create(PostService.class);
        videoServices = PostService.videoRetrofit.create(PostService.class);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        mView = new View(this);
        imageList = new ArrayList<>();
        videoList = new ArrayList<>();
        mediaFiles = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.checking));
        deleteMediaFiles=new ArrayList<>();

        mediaRecyclerView = findViewById(R.id.rvPostMedia);
        mimRecyclerView = (RecyclerView) findViewById(R.id.rvMim);
        recyclerViewSearchMention = (RecyclerView) findViewById(R.id.rvSearchMention);
        editPostMessage = findViewById(R.id.editPostMessage);
        editPostMessage.setOnClickListener(this);
        idSet = new ArrayList<>();
        contentPost = findViewById(R.id.contentPost);
        linkScriptContainer = findViewById(R.id.linkScriptContainer);

        findViewById(R.id.btnAttachment).setOnClickListener(this);
        tvSubmitPost = findViewById(R.id.tvSubmitPost);
        tvSubmitPost.setOnClickListener(this);
        findViewById(R.id.contentCategory).setOnClickListener(this);
        findViewById(R.id.imageCamera).setOnClickListener(this);
        findViewById(R.id.imageGallery).setOnClickListener(this);
        findViewById(R.id.imageVideo).setOnClickListener(this);
        findViewById(R.id.imageCancelPost).setOnClickListener(this);
        tvPermission = findViewById(R.id.tvPermission);
        tvPermission.setText(manager.getPostPermission());
        tvAudience = findViewById(R.id.tvAudience);
        tvAudience.setText(manager.getPostAudience());
        imgPermission = findViewById(R.id.imgPermission);
        contentPostPermission = findViewById(R.id.contentPostPermission);
        contentPostPermission.setOnClickListener(this);
        contentPostView = findViewById(R.id.contentPostView);
        messageContainer = findViewById(R.id.messageContainer);
        messageContainer.setOnClickListener(this);
        mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos, imageListener, videoListen);
        imageListener=new ImageViewHolder.ImageListener() {
            @Override
            public void deleteImage(PostImage postImage, int position) {




                postImages.remove(postImage);
                mediaAdapter.deleteItem(position);
                mediaRecyclerView.scrollToPosition(position);

                String mediaId = postImage.getImageId();
                if (!isNullOrEmpty(mediaId)) {
                    deleteMediaFiles.add(mediaId);
                    App.setDeleteMediaIds(deleteMediaFiles);
                }

             //   makeText(EditPost.this, "image delete", LENGTH_SHORT).show();

            }
        };
        videoListen=new VideoViewHolder.VideoListen() {
            @Override
            public void deleteVideo(PostVideo postVideo, int position) {
                postVideos.remove(postVideo);
                mediaAdapter.deleteItem(position);
                mediaRecyclerView.scrollToPosition(position);
                String mediaId = postVideo.getVideoId();
                if (!isNullOrEmpty(mediaId)) {
                    deleteMediaFiles.add(mediaId);
                    App.setDeleteMediaIds(deleteMediaFiles);
                }

            //    makeText(EditPost.this, "video delete", LENGTH_SHORT).show();
            }
        };

       // mediaRecyclerView.setAdapter(mediaAdapter);

        MimAdapter.RecyclerViewClickListener listener = (view, position) -> {


            String mimColor = viewColors.get(position).getMimColor();
            hasMim = viewColors.get(position).getId();
            if (mimColor.startsWith("#")) {
                if (mimColor.contentEquals("#FFFFFF")) {
                    int mColor = Color.parseColor(mimColor);
                    messageContainer.setBackgroundColor(mColor);
                    messageContainer.setGravity(Gravity.START);
                    editPostMessage.setGravity(Gravity.START);
                    editPostMessage.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                    editPostMessage.setTextColor(Color.parseColor("#000000"));
                    ViewGroup.LayoutParams params = messageContainer.getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen._220sdp);
                    messageContainer.setLayoutParams(params);
                } else {
                    int mColor = Color.parseColor(mimColor);
                    messageContainer.setBackgroundColor(mColor);
                    if (mimColor.contentEquals("#C6FFD4")) {
                        editPostMessage.setTextColor(Color.parseColor("#000000"));
                    }
                    ViewGroup.LayoutParams params = messageContainer.getLayoutParams();
                    params.height = (int) getResources().getDimension(R.dimen._200sdp);
                    messageContainer.setLayoutParams(params);
                    messageContainer.setGravity(Gravity.CENTER);
                    editPostMessage.setGravity(Gravity.CENTER);
                    editPostMessage.setTextAppearance(this, android.R.style.TextAppearance_Large);
                    editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                }


            } else {
                String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                Picasso.with(this).load(imageUrl).into(target);
                messageContainer.setBackground(mDrawable);
                editPostMessage.setHeight(150);
                switch (mimColor) {
                    case "img_bg_birthday.png":
                        editPostMessage.setTextColor(Color.parseColor("#000000"));
                        break;
                    case "img_bg_love.png":
                        editPostMessage.setTextColor(Color.parseColor("#2D4F73"));
                        break;
                    case "img_bg_love2.png":
                        editPostMessage.setTextColor(Color.parseColor("#444748"));
                        break;
                    case "img_bg_red.png":
                        editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case "img_bg_love3.png":
                        editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }
            }


        };
        MimAdapter adapter = new MimAdapter(this, viewColors, listener);
        mimRecyclerView.setAdapter(adapter);

        profileId = manager.getProfileId();
        userIds = manager.getProfileId();
        toUserId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();


        chatAdapter = new ChatAdapter();

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

        rootView = findViewById(R.id.main_activity_root_view);
        emojiButton = findViewById(R.id.main_activity_emoji);
        final ImageView sendButton = findViewById(R.id.main_activity_send);

//        emojiButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);
//        sendButton.setColorFilter(ContextCompat.getColor(this, R.color.emoji_icons), PorterDuff.Mode.SRC_IN);

        //   emojiButton.setOnClickListener(ignore -> emojiPopup.rvMimToggle());
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emojiPopup.toggle();


            }
        });
        sendButton.setOnClickListener(ignore -> {
            //   final String text = editPostMessage.getText().toString().trim();
            final String text = editPostMessage.getText().toString().trim();

            if (text.length() > 0) {
                chatAdapter.add(text);

                editPostMessage.setText("");
            }
        });

        final RecyclerView recyclerViews = findViewById(R.id.main_activity_recycler_view);
        recyclerViews.setAdapter(chatAdapter);
        recyclerViews.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        setUpEmojiPopup();

        textCrawler = new TextCrawler();

        editTextTitlePost = null;
        editTextDescriptionPost = null;

        /** --- From ShareVia Intent */
        if (getIntent().getExtras() != null) {
            String shareVia = (String) getIntent().getExtras().get(Intent.EXTRA_TEXT);
            if (shareVia != null) {
                editText.setText(shareVia);
            }
        }
        if (getIntent().getAction() == Intent.ACTION_VIEW) {
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            String builded = scheme + "://" + host + "/";

            for (String string : params) {
                builded += string + "/";
            }

            if (data.getQuery() != null && !data.getQuery().equals("")) {
                builded = builded.substring(0, builded.length() - 1);
                builded += "?" + data.getQuery();
            }

            System.out.println(builded);

            editText.setText(builded);

        }
        /** --- */


        postButton = (FloatingActionButton) findViewById(R.id.post);

        previewAreaTitle = (TextView) findViewById(R.id.preview_area);
        postAreaTitle = (TextView) findViewById(R.id.post_area);

        /** Where the previews will be dropped */
        dropPreview = (ViewGroup) findViewById(R.id.drop_preview);

        /** Where the previews will be dropped */
        dropPost = (ViewGroup) findViewById(R.id.drop_post);


        initPostButton();


        editPostMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  makeText(PostNew.this, "beforeTextChanged " + s, LENGTH_SHORT).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //   makeText(PostNew.this, "onTextChanged " + s, LENGTH_SHORT).show();
                extractedUrls = Tools.extractUrls(s.toString());
                /// if(uploadImageName.)
                contentTitle = s.toString().trim();


                //  String s = "my very long string to test";

                for (String st : contentTitle.split(" ")) {
                    if (st.startsWith("@")) {
                        userQuery = st;
                    }else {
                        userQuery="";
                    }
                }

                if (nameList.size() > 0) {
                    rvMimShow = false;
                    rvMimToggle();
                }

                if (!isNullOrEmpty(userQuery) && userQuery.length() > 1) {
                    rvMentionUserShow = true;
                    mentionUserToggle();
                    mentionUsers();
                } else if (isFirstTimeShowMention && !isNullOrEmpty(userQuery)) {
                    rvMentionUserShow = true;
                    mentionUserToggle();
                    mentionUsers();
                } else if (isFirstTimeShowMention && isNullOrEmpty(userQuery)) {
                    rvMentionUserShow = false;
                    mentionUserToggle();

                }

           /*     if (extractedUrls.size() == 0) {
                    isLinkScript = false;
                    releasePreviewArea();
                    rvLinkScriptShow = false;
                    linkScriptToggle();

                    if (!isNullOrEmpty(contentTitle)) {
                        //  makeText(PostNew.this, "Button Enable-1!", LENGTH_SHORT).show();
                    }
                }*/
                if (extractedUrls.size() > 0) {
                    rvMimShow = false;
                    rvMimToggle();
                    rvMediaShow = false;
                    mediaRecyclerViewToggle();
                    rvLinkScriptShow = true;
                    linkScriptToggle();

                    StringBuilder builder = new StringBuilder();
                    for (String temp : extractedUrls) {
                        builder.append(temp);
                    }
                    myUrl = builder.toString();


                }
                if (!isLinkScript && extractedUrls.size() == 1) {
                    for (String url : extractedUrls) {
                        textCrawler.makePreview(callback, url);
                        isLinkScript = true;
                        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                        if (!url.isEmpty() && url.matches(pattern)) {
                            /// Valid youtube URL
                            isYoutubeURL = true;
                        } else {
                            isYoutubeURL = false;
                            // Not Valid youtube URL
                        }
                    }
                }


                if (!contentTitle.isEmpty() && myUrl != null && contentTitle.length() > myUrl.length()) {
                    isAddContentTitle = true;
                    tvSubmitPost.setVisibility(View.VISIBLE);

                } else if (myUrl.isEmpty() && !isNullOrEmpty(contentTitle) && contentTitle.length() > 0) {
                    isAddContentTitle = true;
                    tvSubmitPost.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isNullOrEmpty(mentionMessage)) {
                    //mentionMessage += s;
//                    makeText(PostNew.this, "mention-data: " + mentionMessage, LENGTH_SHORT).show();
                }

                //  List<String> extractedUrls = extractUrls("ForgotPasswords to https://stackoverflow.com/ and here is another link http://www.google.com/ \n which is a great search engine");


            }

        });


        //UPDATE POST DATA

        String postMessage = editPostItem.getPostText();
        editPostMessage.append(postMessage);
        tvAudience.setText(editPostItem.getCatName());
        tvPermission.setText(editPostItem.getPermission());
        categoryId = editPostItem.getCatId();
        subCategoryId = editPostItem.getCatId();
        editPostId = editPostItem.getPostId();
        hasMim = Integer.parseInt(editPostItem.getHasMeme());
        List<PostFile> postFiles = editPostItem.getPostFiles();
        contentLinkDesc = editPostItem.getPostLinkDesc();
        contentLinkTitle = editPostItem.getPostLinkTitle();
        contentLinkUrl = editPostItem.getPostLinkUrl();
        postPermission = Integer.parseInt(editPostItem.getPermission());
        status = Integer.parseInt(editPostItem.getPostType());
        contentType = Integer.parseInt(editPostItem.getPostType());

        //  contentHost=editPostItem.g
        //friends=editPostItem.get

        for (Mim temp : viewColors) {
            int id = temp.getId();
            if (id == hasMim) {
                String mimColor = temp.getMimColor();
                if (mimColor.startsWith("#")) {
                    if (mimColor.contentEquals("#FFFFFF")) {
                        int mColor = Color.parseColor(mimColor);
                        messageContainer.setBackgroundColor(mColor);
                        messageContainer.setGravity(Gravity.START);
                        editPostMessage.setGravity(Gravity.START);
                        editPostMessage.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                        editPostMessage.setTextColor(Color.parseColor("#000000"));
                        ViewGroup.LayoutParams params = messageContainer.getLayoutParams();
                        params.height = 300;
                        messageContainer.setLayoutParams(params);
                    } else {
                        int mColor = Color.parseColor(mimColor);
                        messageContainer.setBackgroundColor(mColor);
                        if (mimColor.contentEquals("#C6FFD4")) {
                            editPostMessage.setTextColor(Color.parseColor("#000000"));
                        }
                        ViewGroup.LayoutParams params = messageContainer.getLayoutParams();
                        params.height = 350;
                        messageContainer.setLayoutParams(params);
                        messageContainer.setGravity(Gravity.CENTER);
                        editPostMessage.setGravity(Gravity.CENTER);
                        editPostMessage.setTextAppearance(this, android.R.style.TextAppearance_Large);
                        editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                    }


                } else {
                    String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                    Picasso.with(this).load(imageUrl).into(target);
                    messageContainer.setBackground(mDrawable);
                    editPostMessage.setHeight(150);
                    switch (mimColor) {
                        case "img_bg_birthday.png":
                            editPostMessage.setTextColor(Color.parseColor("#000000"));
                            break;
                        case "img_bg_love.png":
                            editPostMessage.setTextColor(Color.parseColor("#2D4F73"));
                            break;
                        case "img_bg_love2.png":
                            editPostMessage.setTextColor(Color.parseColor("#444748"));
                            break;
                        case "img_bg_red.png":
                            editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                        case "img_bg_love3.png":
                            editPostMessage.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }
            }

        }

        StringBuilder nameBuilder = new StringBuilder();
        List<String> mentionUrl = extractUrls(editPostItem.getPostText());
        List<String> mentionIds = editPostItem.getMentionedUserIds();

        if (mentionIds.size() > 0) {

            for (PostTextIndex temp : editPostItem.getPostTextIndex()) {
                String postType = temp.getType();
                if (postType.equalsIgnoreCase("mention")) {
                    String mentionUserName = extractMentionUser(temp.getText());
                    nameList.add(mentionUserName);


                    // idList.add(id);
                    nameBuilder.append(mentionUserName);
                    nameBuilder.append(" ");
                }

            }


            for (String temp : mentionIds) {
                idList.add(temp);
                friendSet.add(temp);
            }


            for (int i = 0; i < nameList.size(); i++) {

                String mName = nameList.get(i);
                for (int j = 0; j < idList.size(); j++) {
                    String mId = idList.get(j);

                    if (i == j) {
                        wordsToReplace.put(mName, "@[" + mName + "]" + "(id:" + mId + ")");
                        //    wordsToReplace.put(mName, "mention_"+mId);//@[Mijanur Rahaman](id:32)
                        keys = wordsToReplace.keySet();
                        break;
                    }

                }
            }

            //  friendSet.add(id);
            String separator = ", ";
            int total = friendSet.size() * separator.length();
            for (String s : friendSet) {
                total += s.length();
            }

            StringBuilder sb = new StringBuilder(total);
            for (String s : friendSet) {
                sb.append(separator).append(s);
            }

            friends = sb.substring(separator.length()).replaceAll("\\s+", "");


            if (mentionUrl.size() > 0 && extractMentionText(editPostItem).trim().length() > 0) {

                String full_text = extractMentionText(editPostItem).trim();

                if (containsIllegalCharacters(full_text)) {
                    //tvPostContent.setVisibility(View.GONE);
                    editPostMessage.setVisibility(View.VISIBLE);

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
                        if (val >= 0) {
                            str.setSpan(new StyleSpan(Typeface.ITALIC), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            str.setSpan(new BackgroundColorSpan(Color.parseColor("#D8DFEA")), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }


                    editPostMessage.setText("");
                    editPostMessage.append(str);

                    mentionMessage = str.toString();
                    for (String key : keys) {
                        mentionMessage = mentionMessage.replace(key, wordsToReplace.get(key));
                        Log.d("message", mentionMessage);
                    }


                } else {
                    // tvPostEmojiContent.setVisibility(View.GONE);
                    editPostMessage.setVisibility(View.VISIBLE);
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
                        if (val >= 0) {
                            str.setSpan(new StyleSpan(Typeface.ITALIC), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            str.setSpan(new BackgroundColorSpan(Color.parseColor("#D8DFEA")), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }


                    editPostMessage.setText("");
                    editPostMessage.append(str);

                    mentionMessage = str.toString();
                    for (String key : keys) {
                        mentionMessage = mentionMessage.replace(key, wordsToReplace.get(key));
                        Log.d("message", mentionMessage);
                    }

                }


            }
        }


        if (postFiles.size() > 0) {
            for (PostFile temp : postFiles) {

                videoString = temp.getVideoName();
                String postType = temp.getPostType();
                if (!isNullOrEmpty(postType)) {
                    if ("1".equalsIgnoreCase(postType)) {
                        imageString = temp.getImageName();
                        postImages.add(new PostImage(imageString, temp.getId()));
                    } else if ("2".equalsIgnoreCase(postType)) {
                        videoString = temp.getVideoName();
                        postVideos.add(new PostVideo(videoString, temp.getId()));
                    }
                }

            }

            rvMediaShow = true;
            mediaRecyclerViewToggle();
            MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos,imageListener,videoListen);
            mediaRecyclerView.setAdapter(mediaAdapter);

        }


    }

    private void mentionUsers() {
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();

            Call<List<MentionUser>> call = webService.searchMentionUser(deviceId, profileId, token, userQuery);
            sendMentionUserRequest(call);


        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();
        }
    }

    ArrayList<String> mList = new ArrayList<>();

    private void sendMentionUserRequest(Call<List<MentionUser>> call) {
        call.enqueue(new Callback<List<MentionUser>>() {


            @Override
            public void onResponse(Call<List<MentionUser>> call, Response<List<MentionUser>> response) {


                List<MentionUser> mentionUsers = response.body();
                replaceContent = contentTitle.replace(userQuery, " ");


                MentionUserAdapter.RecyclerViewClickListener listener = (view, position) -> {


                    String name = mentionUsers.get(position).getDisplay();
                    String id = mentionUsers.get(position).getId();

                    nameList.add(name);


                    idList.add(id);
                    StringBuilder nameBuilder = new StringBuilder();
                    nameBuilder.append(name);

                    friendSet.add(id);
                    String separator = ", ";
                    int total = friendSet.size() * separator.length();
                    for (String s : friendSet) {
                        total += s.length();
                    }

                    StringBuilder sb = new StringBuilder(total);
                    for (String s : friendSet) {
                        sb.append(separator).append(s);
                    }

                    friends = sb.substring(separator.length()).replaceAll("\\s+", "");


                    if (nameList.size() > 0) {
                        //Create new list
                        String nameStr = nameBuilder.toString();
                        String[] nameArr = nameStr.split(" ");

                        //   String[] nameArr = new String[nameList.size()];
                        //   nameArr = nameList.toArray(nameArr);
                        StringBuilder mentionBuilder = new StringBuilder();
                        String mention_text = contentTitle.replaceAll(userQuery, "mention_" + id);

                        String full_text = contentTitle.replaceAll(userQuery, name);
                        //  SpannableString spannableStr = new SpannableString(full_text);
                        //main string
                        //  String full_text = "We love our country a lot.";
                        //split strings by space
                        String[] splittedWords = full_text.split(" ");
                        SpannableString str = new SpannableString(full_text);


                        //Check the matching words
                        for (int i = 0; i < nameArr.length; i++) {
                            for (int j = 0; j < splittedWords.length; j++) {
                                if (nameArr[i].equalsIgnoreCase(splittedWords[j])) {
                                    mList.add(nameArr[i]);
                                }
                            }
                        }
                        //make the words bold
                        for (int k = 0; k < mList.size(); k++) {
                            int val = full_text.indexOf(mList.get(k));
                            if (val >= 0) {
                                str.setSpan(new StyleSpan(Typeface.ITALIC), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                str.setSpan(new BackgroundColorSpan(Color.parseColor("#D8DFEA")), val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

//                        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#D8DFEA"));
//                        spannableStr.setSpan(backgroundColorSpan, full_text.length()-name.length(), full_text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        //hi azhar @[Mimi Owensby](id:3232) note @[Mijanur Rahaman](id:32) and @[Samuel Rivero](id:36)


                        //  Pattern p = Pattern.compile("cat");

                        for (int i = 0; i < nameList.size(); i++) {

                            String mName = nameList.get(i);
                            for (int j = 0; j < idList.size(); j++) {
                                String mId = idList.get(j);

                                if (i == j) {
                                    wordsToReplace.put(mName, "@[" + mName + "]" + "(id:" + mId + ")");
                                    //    wordsToReplace.put(mName, "mention_"+mId);//@[Mijanur Rahaman](id:32)
                                    keys = wordsToReplace.keySet();
                                    break;
                                }

                            }
                        }

                        mentionMessage = str.toString();
                        for (String key : keys) {
                            mentionMessage = mentionMessage.replace(key, wordsToReplace.get(key));
                            Log.d("message", mentionMessage);
                        }

                        Log.d("message", mentionMessage);

                        editPostMessage.setText("");
                        editPostMessage.append(str);


                    }


                    userQuery = "";
                    rvMentionUserShow = false;
                    mentionUserToggle();

                    rvMimShow = false;
                    rvMimToggle();

                };
                MentionUserAdapter mentionUserAdapter = new MentionUserAdapter(EditPost.this, mentionUsers, listener);
                recyclerViewSearchMention.setAdapter(mentionUserAdapter);


                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                isFirstTimeShowMention = true;

            }

            @Override
            public void onFailure(Call<List<MentionUser>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });

    }

    private void rvMimVisibility() {
        if (mimRecyclerView.getVisibility() == View.VISIBLE) {
            rvMimShow = false;
        } else {
            rvMimShow = true;
        }
    }


    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {

            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }


    private void initPostButton() {
        postButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                postAreaTitle.setVisibility(View.VISIBLE);
                tvSubmitPost.setVisibility(View.VISIBLE);
                previewAreaTitle.setVisibility(View.GONE);
                postButton.setVisibility(View.GONE);
                tvSubmitPost.setEnabled(true);

                /** Inflating the preview layout */
                View mainView = getLayoutInflater().inflate(R.layout.main_view,
                        null);

                LinearLayout linearLayout = (LinearLayout) mainView
                        .findViewById(R.id.external);

                /**
                 * Inflating the post content
                 */
                final View content = getLayoutInflater().inflate(
                        R.layout.post_content, linearLayout);

                /** Fullfilling the content layout */
                final LinearLayout infoWrap = (LinearLayout) content
                        .findViewById(R.id.info_wrap);

                final TextView contentTextView = (TextView) content
                        .findViewById(R.id.post_content);
                final ImageView imageView = (ImageView) content
                        .findViewById(R.id.image_post);
                final TextView titleTextView = (TextView) content
                        .findViewById(R.id.title);

                final TextView urlTextView = (TextView) content
                        .findViewById(R.id.url);
                final TextView descriptionTextView = (TextView) content
                        .findViewById(R.id.description);

                contentTextView.setText(TextCrawler.extendedTrim(editPostMessage
                        .getText().toString()));

                if (currentImage != null && !noThumb) {
                    imageView.setImageBitmap(currentImage);
                } else {

                }

                if (!isNullOrEmpty(currentTitle))
                    titleTextView.setText(currentTitle);
                else
                    titleTextView.setVisibility(View.GONE);

                if (!isNullOrEmpty(currentDescription))
                    descriptionTextView.setText(currentDescription);
                else
                    descriptionTextView.setVisibility(View.GONE);

                urlTextView.setText(currentCannonicalUrl);

                final String currentUrlLocal = currentUrl;

                mainView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        String url = currentUrlLocal;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                dropPost.removeAllViews();
                dropPost.addView(mainView, 0);
                dropPreview.removeAllViews();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tvPermission.setText(manager.getPostPermission());
    }

    @Override
    protected void onPause() {
        super.onPause();
        tvPermission.setText(manager.getPostPermission());
        Category mCategory = App.getmCategory();
        Subcatg mSubcatg = App.getmSubcatg();

        if (mCategory != null) {
            App.setCategoryId(mCategory.getCategoryId());
            categoryName = mCategory.getCategoryName();
            audience = "";
            manager.setPostAudience(categoryName);
            tvAudience.setText(categoryName);
        }
        if (mSubcatg != null) {
            audience = mSubcatg.getSubCategoryName();
            manager.setPostAudience(audience);
            tvAudience.setText(audience);
        }

//        if (mSubcatg != null && mCategory != null) {
//            //categoryId = Integer.parseInt(mCategory.getCategoryId());
//            // subCategoryId = Integer.parseInt(mSubcatg.getSubCategoryId());
//            App.setCategoryId(mCategory.getCategoryId());
//            categoryName = mCategory.getCategoryName();
//            audience = mSubcatg.getSubCategoryName();
//            manager.setPostAudience(audience);
//            tvAudience.setText(audience);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPermission.setText(manager.getPostPermission());
        Category mCategory = App.getmCategory();
        Subcatg mSubcatg = App.getmSubcatg();

        if (mCategory != null) {
            App.setCategoryId(mCategory.getCategoryId());
            categoryName = mCategory.getCategoryName();
            audience = "";
            manager.setPostAudience(categoryName);
            tvAudience.setText(categoryName);
        }
        if (mSubcatg != null) {
            audience = mSubcatg.getSubCategoryName();
            manager.setPostAudience(audience);
            tvAudience.setText(audience);
        }

//        if (mSubcatg != null && mCategory != null) {
//            //  categoryId = Integer.parseInt(mCategory.getCategoryId());
//            App.setCategoryId(mCategory.getCategoryId());
//            //  subCategoryId = Integer.parseInt(mSubcatg.getSubCategoryId());
//            categoryName = mCategory.getCategoryName();
//            audience = mSubcatg.getSubCategoryName();
//            manager.setPostAudience(audience);
//            tvAudience.setText(audience);
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.contentPostPermission:
                String message = tvPermission.getText().toString();
                PostPermission postPermissions = PostPermission.newInstance(message);
                postPermissions.show(getSupportFragmentManager(), "PostPermission");
                break;
            case R.id.btnAttachment:
                break;
            case R.id.imageCancelPost:
                finish();
                break;
            case R.id.messageContainer:
//                editPostMessage.setEnabled(true);
//                if (postVideos.isEmpty() && postImages.isEmpty()) {
//                    rvMimShow = true;
//                    rvMimToggle();
//                } else {
//                    rvMimShow = false;
//                    rvMimToggle();
//                }
//                if (postVideos.isEmpty()) {
//                    rvMimShow = true;
//                    rvMimToggle();
//                } else {
//                    rvMimShow = false;
//                    rvMimToggle();
//                }
//                if (postImages.isEmpty()) {
//                    rvMimShow = true;
//                    rvMimToggle();
//                } else {
//                    rvMimShow = false;
//                    rvMimToggle();
//                }
                break;
            case R.id.editPostMessage:
                if (postVideos.isEmpty() && postImages.isEmpty()) {
                    rvMimShow = true;
                    rvMimToggle();
                } else {
                    rvMimShow = false;
                    rvMimToggle();
                }
                if (postVideos.isEmpty()) {
                    rvMimShow = true;
                    rvMimToggle();
                } else {
                    rvMimShow = false;
                    rvMimToggle();
                }
                if (postImages.isEmpty()) {
                    rvMimShow = true;
                    rvMimToggle();
                } else {
                    rvMimShow = false;
                    rvMimToggle();
                }
                break;
            case R.id.tvSubmitPost:

                checkContentType();
                if (contentTitle.isEmpty()) {
                    Tools.showCustomToast(EditPost.this, mView, "Please add a post description", Gravity.TOP);
                } else if (categoryId.isEmpty() && subCategoryId.isEmpty()) {
                    Tools.showCustomToast(EditPost.this, mView, "Please select your post’s audience.", Gravity.TOP);
                } else if (!isAddContentTitle) {
                    Tools.showCustomToast(EditPost.this, mView, "You must add the post description!", Gravity.TOP);
                } else if (contentTitle.length() < 8) {
                    Tools.showCustomToast(EditPost.this, mView, "Cat’s got your tongue? Please write at least 8 characters in your post description.", Gravity.TOP);
                } else {
                    //  friendSet.add(id);
                    String separator = ", ";

                    idSet = App.getDeleteMediaIds();
                    if (idSet != null && idSet.size() > 0) {
                        int total = idSet.size() * separator.length();
                        for (String s : idSet) {
                            total += s.length();
                        }

                        StringBuilder sb = new StringBuilder(total);
                        for (String s : idSet) {
                            sb.append(separator).append(s);
                        }

                        deleteMediaIds = sb.substring(separator.length()).replaceAll("\\s+", "");
                    }
                    createNewPost();
                }

                // Tools.showCustomToast(LikerSearch.this, mView, " Write Minimum Three Characters !", Gravity.TOP);


//                String linkText = editPostMessage.getText().toString();
//                textCrawler.makePreview(callback, linkText);
//                if (linkText.startsWith("http")) {
//                }
                break;
            case R.id.contentCategory:

                /*Intent intent = new Intent(EditPost.this, PostCategory.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing);*/
                break;
            case R.id.imageCamera:
                rvMimShow = false;
                rvMimToggle();
                if (isGrantCamera) {
                    sendImageFromCamera();
                } else {
                    checkCameraPermission();
                }

                break;
            case R.id.imageGallery:

                rvMimShow = false;
                rvMimToggle();
                if (isGrantGallery) {

                    sendImageFromGallery();
                } else {
                    checkGalleryPermission();
                }
                //
                break;
            case R.id.imageVideo:
                rvMimShow = false;
                rvMimToggle();
                checkVideoPermission();

                break;
        }
    }

    private void createNewPost() {
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.postEdited(
                    deviceId,//"8b64708fa409da20341b1a555d1ddee526444",
                    profileId,//"26444",
                    token,// "5d199fc8529c2$2y$10$C9mvDyOEhJ2Nc/e4Ji4gVOivCvaO4OBobPW2ky4oftvVniCZ8hKzuJhxEGIHYSCprmWSJ1rd4hGHDEqUNRAwAR4fxMWwEyV6VSZEU",
                    Integer.parseInt(userIds),//"26444",
                    toUserId,//"26444",
                    postType,//"status",
                    status,//0,
                    imageFile,
                    fileEncoded,//"",
                    postPermission,//0,
                    categoryId,//3,
                    subCategoryId,// 54,
                    contentType,//1,
                    contentTitle,//contentTitle,//"Here is studio.. ",
                    contentLinkUrl,//"",
                    contentHost,//"",
                    contentLinkTitle,//"",
                    contentLinkDesc,//"",
                    contentLinkImage,//"",
                    "abcdxyz",
                    profileId,//"june8045",
                    friends,//"",
                    scheduleTime,//0,
                    hasMim,
                    0,
                    editPostId,//0
                    mediaFiles.toString(),
                    deleteMediaIds
            );
            sendNewPostRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    private void checkContentType() {
        if (contentTitle != null)
          /*  if (contentTitle.length() > 0 && extractedUrls.size() < 0 && isNullOrEmpty(imageFile) && postVideos.size() < 0 && postImages.size() < 0) {
                contentType = 1;
                status = 1;
            } else*/
            if (contentTitle.length() > 0 && postImages.size() > 0) {
                contentType = 2;
                status = 2;
            } else if (contentTitle.length() > 0 && extractedUrls.size() > 0) {
                contentType = 3;
                status = 3;
            } else if (contentTitle.length() > 0 && isYoutubeURL) {
                contentType = 4;
                status = 4;
            } else if (contentTitle.length() > 0 && postVideos.size() > 0) {
                contentType = 2;
                status = 2;
            } else {
                contentType = 1;
                status = 1;

                if (!isNullOrEmpty(mentionMessage)) {
                    for (String temp : nameList) {
                        name = temp;
                    }
                    if (contentTitle.contains(name)) {
                        String text = contentTitle;
                        String lastPlainText = text.substring((text.indexOf(name) + name.length()));
                        mentionMessage += lastPlainText;
                        contentTitle = mentionMessage;
                    }


                }
            }
    }

    private void rvMimToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(mimRecyclerView);

        TransitionManager.beginDelayedTransition(rootView, transition);
        mimRecyclerView.setVisibility(rvMimShow ? View.VISIBLE : View.GONE);
    }


    private void mediaRecyclerViewToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(mediaRecyclerView);

        TransitionManager.beginDelayedTransition(rootView, transition);
        mediaRecyclerView.setVisibility(rvMediaShow ? View.VISIBLE : View.GONE);
    }

    private void linkScriptToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(linkScriptContainer);

        TransitionManager.beginDelayedTransition(rootView, transition);
        linkScriptContainer.setVisibility(rvLinkScriptShow ? View.VISIBLE : View.GONE);
    }

    private void mentionUserToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(recyclerViewSearchMention);

        TransitionManager.beginDelayedTransition(rootView, transition);
        recyclerViewSearchMention.setVisibility(rvMentionUserShow ? View.VISIBLE : View.GONE);
    }

    private void sendNewPostRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        try {
                            JSONObject object = new JSONObject(response.body());
                            JSONObject successObject = object.getJSONObject("success");
                            if(successObject.length()>0){
                                boolean status = successObject.getBoolean("status");
                                //  boolean topContributorStatus = successObject.getBoolean("top_contributor_status");
                                //postId = successObject.getInt("post_id");
                                if (status) {

                                    sendPostItemRequest(editPostId, position);

                                } else {
                                    if (contentType == 5) {

                                        Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                                        sendVideoRequest(mediaCall);

                                    } else {
                                        startActivity(new Intent(EditPost.this, Home.class));
                                        finish();
                                    }

                                }
                            }



                            JSONObject errorObject = object.getJSONObject("errors");
                            if (errorObject.length() > 0) {
                                String post_duplicate = errorObject.getString("post_duplicate");
                                Tools.toast(EditPost.this, post_duplicate, R.drawable.ic_warning_black_24dp);
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


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

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
    public void onDestroy() {  // could be in onPause or onStop
        Picasso.with(this).cancelRequest(target);
        App.setmCategory(null);
        App.setmSubcatg(null);
        manager.setPostAudience("");
        super.onDestroy();
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
                .setOnEmojiClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
                .setOnEmojiPopupShownListener(() -> emojiButton.setImageResource(R.drawable.ic_keyboard))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
                .setOnEmojiPopupDismissListener(() -> emojiButton.setImageResource(R.drawable.emoji_twitter_category_smileysandpeople))
                .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .setPageTransformer(new PageTransformer())
                .build(editPostMessage);
    }

    Uri imageUri;

    public void sendImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getImageUri();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_CAMERA);
        }

    }

    public void sendImageFromGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_TAKE_GALLERY_IMAGE);

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent1, "Select images"), REQUEST_TAKE_GALLERY_IMAGE);
        } else {
            Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent2.addCategory(Intent.CATEGORY_OPENABLE);
            intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent2.setType("image/*");
            startActivityForResult(intent2, REQUEST_TAKE_GALLERY_IMAGE);
        }

    }


    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_GALLERY_IMAGE);
            isGrantGallery = false;

        } else {
            sendImageFromGallery();
            makeText(this, R.string.grant, LENGTH_SHORT).show();
            isGrantGallery = true;
        }

    }

    private void checkVideoPermission() {
        /*RUN TIME PERMISSIONS*/

        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(EditPost.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(EditPost.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(EditPost.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_TAKE_GALLERY_VIDEO);
            }
        } else {
            sendVideoFroGallery();
            makeText(this, R.string.grant, LENGTH_SHORT).show();
        }
    }

    private void sendVideoFroGallery() {

//        Intent intent = new Intent();
//        intent.setType("video/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select PostVideo"), REQUEST_TAKE_GALLERY_VIDEO);

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent1 = new Intent();
            intent1.setType("video/mp4");
            intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent1, "Select videos"), REQUEST_TAKE_GALLERY_VIDEO);
        } else {
            Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent2.addCategory(Intent.CATEGORY_OPENABLE);
            intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent2.setType("video/mp4");
            startActivityForResult(intent2, REQUEST_TAKE_GALLERY_VIDEO);
        }


    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_CAMERA);
            isGrantCamera = false;

        } else {

            sendImageFromCamera();
            makeText(this, R.string.grant, LENGTH_SHORT).show();
            isGrantCamera = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_GALLERY_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeText(this, R.string.gallery_granted, LENGTH_SHORT).show();
                    sendImageFromGallery();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeText(this, R.string.gallery_granted, LENGTH_SHORT).show();
                    sendImageFromCamera();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;
            case REQUEST_TAKE_GALLERY_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeText(this, R.string.gallery_granted, LENGTH_SHORT).show();
                    sendVideoFroGallery();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_GALLERY_IMAGE) {

            if (resultCode == RESULT_OK) {

                try {
                    getSelectedImagesPath(requestCode, data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(this, "Cancel Gallery", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_TAKE_CAMERA) {

            if (resultCode == RESULT_OK) {

                String imagePath = imageUri.getPath();
                File file = new File(imagePath);

                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;

                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                Call<String> mediaCall = webService.addPhoto(deviceId, profileId, token, fileToUpload);
                addPhotoRequest(mediaCall);

                progressDialog.show();
                postImages.add(new PostImage("file://" + imagePath,fileEncoded));
                if (postImages.size() > 0)
                    rvMediaShow = true;
                mediaRecyclerViewToggle();
                MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos,imageListener,videoListen);
                mediaRecyclerView.setAdapter(mediaAdapter);
                progressDialog.dismiss();


            } else {
                Toast.makeText(this, "Cancel Camera Capture", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {

                try {
                    getSelectedVideosPath(requestCode, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }


    }

    private void addPhotoRequest(Call<String> call) {


        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean mediaStatus = object.getBoolean("status");
                            imageFile = object.getString("filename");
                            uploadImageName.add(imageFile);
                            MultipleMediaFile mediaFile = new MultipleMediaFile();
                            mediaFile.setBase64Md5(fileEncoded);
                            mediaFile.setFileType("image");
                            mediaFile.setName(imageFile);
                            Gson gson = new Gson();
                            String gsonString = gson.toJson(mediaFile);
                            mediaFiles.add(gsonString);
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    private void sendVideoRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());

                            startActivity(new Intent(EditPost.this, Home.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    private void sendImageRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            imageFile = object.getString("filename");
                            uploadImageName.add(imageFile);
                            if (!isNullOrEmpty(imageFilePath)) {
                                String imagePath = "file://" + imageFilePath;
                                postImages.add(new PostImage(imagePath,fileEncoded));
                                if (postImages.size() > 0)
                                    rvMediaShow = true;
                                mediaRecyclerViewToggle();
                                MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos,imageListener,videoListen);
                                mediaRecyclerView.setAdapter(mediaAdapter);
                                progressView.setVisibility(View.GONE);
                                progressView.stopAnimation();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    private void uploadFile(Bitmap imageBitmap, int i) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        imageUri = getImageUri();
    }

    private Uri getImageUri() {
        Uri m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception p_e) {
        }
        return m_imgUri;
    }

    public String getPath(Uri uri) {
        int column_index;
        String imagePath;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }

    private List<String> getSelectedVideos(int requestCode, Intent data) {

        List<String> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getPath(this, videoURI);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            String filePath = getPath(this, videoURI);
            result.add(filePath);
        }

        return result;
    }

    private List<PostVideo> getSelectedVideosPath(int requestCode, Intent data) throws IOException {

        List<PostVideo> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getPath(this, videoURI);
                String videoPath = "file://" + filePath;
                //base64File = getBase64(filePath);

                videoExtension = videoPath.substring(videoPath.lastIndexOf(".") + 1);

                Log.d("Extension: ", videoExtension);
                String strMD5 = getMD5EncryptedString(videoPath);
                fileEncoded = strMD5;
                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
                sendIsDuplicateVideoRequest(call);

                //   result.add(new PostVideo(videoPath));

                File file = new File(filePath);
                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
//                Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, true);
//                sendImageRequest(mediaCall);
            }
        } else {
            Uri videoURI = data.getData();
            videoFilePath = getPath(this, videoURI);
            String videoPath = "file://" + videoFilePath;

            //  base64File = getBase64(videoFilePath);
            videoExtension = videoPath.substring(videoPath.lastIndexOf(".") + 1);

            Log.d("Extension: ", videoExtension);

            String strMD5 = getMD5EncryptedString(videoPath);
            fileEncoded = strMD5;
            Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "2", strMD5);
            sendIsDuplicateVideoRequest(call);


            File file = new File(videoFilePath);
            //Parsing any Media type file
            RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
            fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);

        }

        return result;
    }

    private void sendIsDuplicateVideoRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                String message = "You have already posted it .";
                                Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);
                            } else {

                                if (!isNullOrEmpty(videoFilePath)) {
                                    File file = new File(videoFilePath);
                                    //Parsing any Media type file
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
                                    Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                                    sendUploadVideoRequest(mediaCall);

                                    progressDialog.show();

                                    String videoPath = "file://" + videoFilePath;
                                    PostVideo postVideo=new PostVideo();
                                    postVideo.setVideoPath(videoPath);
                                    postVideo.setMdFive(fileEncoded);
                                    postVideos.add(postVideo);
                                    if (postVideos.size() > 0)
                                        rvMediaShow = true;
                                    mediaRecyclerViewToggle();
                                    MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos,imageListener,videoListen);
                                    mediaRecyclerView.setAdapter(mediaAdapter);


                                }


                              //  String message = "Add gallery successfully!";
                             //   Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void sendUploadVideoRequest(Call<String> mediaCall) {

        mediaCall.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String video_name = object.getString("video_name");
                            String duration = object.getString("duration");
                            String image_name = object.getString("image_name");
                            String large_image_name = object.getString("large_image_name");

                            MultipleMediaFile mediaFile = new MultipleMediaFile();
                            mediaFile.setBase64Md5(fileEncoded);
                            mediaFile.setDuration(duration);
                            mediaFile.setFileType("video");
                            mediaFile.setImageName(image_name);
                            mediaFile.setLargeImageName(large_image_name);
                            mediaFile.setName(video_name);
                            Gson gson = new Gson();
                            String gsonString = gson.toJson(mediaFile);
                            mediaFiles.add(gsonString);

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

    private List<PostImage> getSelectedImagesPath(int requestCode, Intent data) throws FileNotFoundException {

        List<PostImage> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                imageFilePath = getPath(this, videoURI);
                String imagePath = "file://" + imageFilePath;
                //     String strBase64 = getBase64(imagePath);
                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;
                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                sendIsDuplicateImageRequest(call);

            }
        } else {
            Uri videoURI = data.getData();
            imageFilePath = getPath(this, videoURI);
            String imagePath = "file://" + imageFilePath;
            String strMD5 = getMD5EncryptedString(imagePath);
            fileEncoded = strMD5;
            //   String strBase64 = getBase64(imageFilePath);
            Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
            sendIsDuplicateImageRequest(call);


        }

        return result;
    }

    private void sendIsDuplicateImageRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                String message = "You have already posted it .";
                                Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);
                            } else {
                                if (!isNullOrEmpty(imageFilePath)) {
                                    File file = new File(imageFilePath);
                                    //Parsing any Media type file
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                                    Call<String> mediaCall = webService.addPhoto(deviceId, profileId, token, fileToUpload);
                                    addPhotoRequest(mediaCall);

                                    progressDialog.show();
                                    String imagePath = "file://" + imageFilePath;
                                    postImages.add(new PostImage(imagePath,fileEncoded));
                                    if (postImages.size() > 0)
                                        rvMediaShow = true;
                                    mediaRecyclerViewToggle();
                                    MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos,imageListener,videoListen);
                                    mediaRecyclerView.setAdapter(mediaAdapter);
                                    progressView.setVisibility(View.GONE);
                                    progressView.stopAnimation();

                                }


                            //    String message = "Add gallery successfully!";
                             //   Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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


    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }


            output64.close();


            encodedFile = output.toString();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    private void sendIsDuplicateRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                String message = "You have already posted it .";
                                Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);
                            } else {
                                if (!isNullOrEmpty(imageFilePath)) {
                                    File file = new File(imageFilePath);
                                    //Parsing any Media type file
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                                    Call<String> mediaCall = webService.addPhoto(deviceId, profileId, token, fileToUpload);
                                    sendImageRequest(mediaCall);
                                }

                                if (!isNullOrEmpty(videoFilePath)) {
                               /*     File file = new File(videoFilePath);
                                    //Parsing any Media type file
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("video/*"), file);
                                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
                                    Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, true);
                                    sendImageRequest(mediaCall);*/

                                    String videoPath = "file://" + videoFilePath;
                                    imageFile = "default.mp4";
                                    PostVideo postVideo=new PostVideo();
                                    postVideo.setVideoPath(videoPath);
                                    postVideo.setMdFive(fileEncoded);
                                    postVideos.add(postVideo);
                                    if (postVideos.size() > 0)
                                        rvMediaShow = true;
                                    mediaRecyclerViewToggle();
                                    MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), postImages, postVideos,imageListener,videoListen);
                                    mediaRecyclerView.setAdapter(mediaAdapter);

                                }

                           //     String message = "Add gallery successfully!";
                          //      Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private LinkPreviewCallback callback = new LinkPreviewCallback() {
        /**
         * This view is used to be updated or added in the layout after getting
         * the result
         */
        private View mainView;
        private LinearLayout linearLayout;
        private View loading;
        private ImageView imageView;

        @Override
        public void onPre() {
            hideSoftKeyboard();

            currentImageSet = null;
            currentItem = 0;

         //   tvSubmitPost.setVisibility(View.GONE);
            previewAreaTitle.setVisibility(View.VISIBLE);

            currentImage = null;
            noThumb = false;
            currentTitle = currentDescription = currentUrl = currentCannonicalUrl = "";

          //  tvSubmitPost.setEnabled(false);

            /** Inflating the preview layout */
            mainView = getLayoutInflater().inflate(R.layout.main_view, null);

            linearLayout = (LinearLayout) mainView.findViewById(R.id.external);

            /**
             * Inflating a loading layout into Main View LinearLayout
             */
            loading = getLayoutInflater().inflate(R.layout.loading,
                    linearLayout);

            dropPreview.addView(mainView);
        }

        @Override
        public void onPos(final SourceContent sourceContent, boolean isNull) {

            /** Removing the loading layout */
            linearLayout.removeAllViews();

            if (isNull || sourceContent.getFinalUrl().equals("")) {
                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                View failed = getLayoutInflater().inflate(R.layout.failed,
                        linearLayout);

                TextView titleTextView = (TextView) failed
                        .findViewById(R.id.text);
                titleTextView.setText(getString(R.string.failed_preview) + "\n"
                        + sourceContent.getFinalUrl());

                failed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        releasePreviewArea();
                        editPostMessage.setText("");
                        editPostMessage.getText().clear();
                    }
                });

            } else {
              //  postButton.setVisibility(View.VISIBLE);
              //  tvSubmitPost.setVisibility(View.VISIBLE);
                currentImageSet = new Bitmap[sourceContent.getImages().size()];

                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                final View content = getLayoutInflater().inflate(
                        R.layout.preview_content, linearLayout);

                /** Fullfilling the content layout */
                final LinearLayout infoWrap = (LinearLayout) content
                        .findViewById(R.id.info_wrap);
                final LinearLayout titleWrap = (LinearLayout) infoWrap
                        .findViewById(R.id.title_wrap);
                final LinearLayout thumbnailOptions = (LinearLayout) content
                        .findViewById(R.id.thumbnail_options);


                final ImageView imageSet = (ImageView) content
                        .findViewById(R.id.image_post_set);

                // final TextView close = (TextView) titleWrap.findViewById(R.id.close);
                final FloatingActionButton close = (FloatingActionButton) findViewById(R.id.close);
                final TextView titleTextView = (TextView) titleWrap
                        .findViewById(R.id.title);
                final EditText titleEditText = (EditText) titleWrap
                        .findViewById(R.id.input_title);
                final TextView urlTextView = (TextView) content
                        .findViewById(R.id.url);
                final TextView descriptionTextView = (TextView) content
                        .findViewById(R.id.description);
                final EditText descriptionEditText = (EditText) content
                        .findViewById(R.id.input_description);
                final TextView countTextView = (TextView) thumbnailOptions
                        .findViewById(R.id.count);

                final Button previousButton = (Button) thumbnailOptions
                        .findViewById(R.id.post_previous);
                final Button forwardButton = (Button) thumbnailOptions
                        .findViewById(R.id.post_forward);

                editTextTitlePost = titleEditText;
                editTextDescriptionPost = descriptionEditText;

                titleTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        titleTextView.setVisibility(View.GONE);

                        titleEditText.setText(TextCrawler
                                .extendedTrim(titleTextView.getText()
                                        .toString()));
                        titleEditText.setVisibility(View.VISIBLE);
                    }
                });
                titleEditText
                        .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                            @Override
                            public boolean onEditorAction(TextView arg0,
                                                          int arg1, KeyEvent arg2) {

                                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    titleEditText.setVisibility(View.GONE);

                                    currentTitle = TextCrawler
                                            .extendedTrim(titleEditText
                                                    .getText().toString());

                                    titleTextView.setText(currentTitle);
                                    titleTextView.setVisibility(View.VISIBLE);

                                    hideSoftKeyboard();
                                }

                                return false;
                            }
                        });
                descriptionTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        descriptionTextView.setVisibility(View.GONE);

                        descriptionEditText.setText(TextCrawler
                                .extendedTrim(descriptionTextView.getText()
                                        .toString()));
                        descriptionEditText.setVisibility(View.VISIBLE);
                    }
                });
                descriptionEditText
                        .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                            @Override
                            public boolean onEditorAction(TextView arg0,
                                                          int arg1, KeyEvent arg2) {

                                if (arg2.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    descriptionEditText
                                            .setVisibility(View.GONE);

                                    currentDescription = TextCrawler
                                            .extendedTrim(descriptionEditText
                                                    .getText().toString());

                                    descriptionTextView
                                            .setText(currentDescription);
                                    descriptionTextView
                                            .setVisibility(View.VISIBLE);

                                    hideSoftKeyboard();
                                }

                                return false;
                            }
                        });

                close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        releasePreviewArea();
                    }
                });


                previousButton.setEnabled(false);
                previousButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (currentItem > 0)
                            changeImage(previousButton, forwardButton,
                                    currentItem - 1, sourceContent,
                                    countTextView, imageSet, sourceContent
                                            .getImages().get(currentItem - 1),
                                    currentItem);
                    }
                });
                forwardButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (currentItem < sourceContent.getImages().size() - 1)
                            changeImage(previousButton, forwardButton,
                                    currentItem + 1, sourceContent,
                                    countTextView, imageSet, sourceContent
                                            .getImages().get(currentItem + 1),
                                    currentItem);
                    }
                });

                if (sourceContent.getImages().size() > 0) {

                    if (sourceContent.getImages().size() > 1) {
                        countTextView.setText("1 " + getString(R.string.of)
                                + " " + sourceContent.getImages().size());

                        thumbnailOptions.setVisibility(View.VISIBLE);
                    }


                    UrlImageViewHelper.setUrlDrawable(imageSet, sourceContent
                            .getImages().get(0), new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[0] = loadedBitmap;
                            }
                        }
                    });

                } else {

                }

                if (sourceContent.getTitle().equals(""))
                    sourceContent.setTitle(getString(R.string.enter_title));
                if (sourceContent.getDescription().equals(""))
                    sourceContent
                            .setDescription(getString(R.string.enter_description));

                titleTextView.setText(sourceContent.getTitle());
                urlTextView.setText(sourceContent.getCannonicalUrl());
                descriptionTextView.setText(sourceContent.getDescription());

              //  postButton.setVisibility(View.VISIBLE);
            }

            currentTitle = sourceContent.getTitle();
            contentLinkTitle = currentTitle;
            currentDescription = sourceContent.getDescription();
            contentLinkDesc = currentDescription;
            currentUrl = sourceContent.getUrl();
            contentLinkUrl = currentUrl;
            currentCannonicalUrl = sourceContent.getCannonicalUrl();
            contentHost = currentCannonicalUrl;
            List<String> imageUrl = sourceContent.getImages();
            for (String temp : imageUrl
            ) {
                contentLinkImage = temp;
            }
        }
    };

    /**
     * Hide keyboard
     */
    private void hideSoftKeyboard() {
        hideSoftKeyboard(editPostMessage);

        if (editTextTitlePost != null)
            hideSoftKeyboard(editTextTitlePost);
        if (editTextDescriptionPost != null)
            hideSoftKeyboard(editTextDescriptionPost);
    }

    private void hideSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void releasePreviewArea() {
        tvSubmitPost.setEnabled(true);
        tvSubmitPost.setVisibility(View.VISIBLE);
        postButton.setVisibility(View.GONE);
        previewAreaTitle.setVisibility(View.GONE);
        dropPreview.removeAllViews();
    }


    /**
     * Change the current image in image set
     */
    private void changeImage(Button previousButton, Button forwardButton,
                             final int index, SourceContent sourceContent,
                             TextView countTextView, ImageView imageSet, String url,
                             final int current) {

        if (currentImageSet[index] != null) {
            currentImage = currentImageSet[index];
            imageSet.setImageBitmap(currentImage);
        } else {
            UrlImageViewHelper.setUrlDrawable(imageSet, url,
                    new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                currentImageSet[index] = loadedBitmap;
                            }
                        }
                    });

        }

        currentItem = index;

        if (index == 0)
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);

        if (index == sourceContent.getImages().size() - 1)
            forwardButton.setEnabled(false);
        else
            forwardButton.setEnabled(true);

        countTextView.setText((index + 1) + " " + getString(R.string.of) + " "
                + sourceContent.getImages().size());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void onPositiveResult(DialogFragment dlg) {
        makeText(EditPost.this, "positive button", LENGTH_SHORT).show();
        //addedPostContributor
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.addedPostContributor(deviceId, profileId, token, categoryId, subCategoryId, 5, userIds);
            addedPostContributorRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    private void sendPostItemRequest(String postId, int position) {
        Call<PostItem> call = homeService.getSinglePostDetails(deviceId, profileId, token, profileId, postId);
        call.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                PostItem postItem = response.body();
                if (postItem != null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("post_item", (Parcelable) postItem);
                    returnIntent.putExtra("position", position);
                    returnIntent.putExtra("isFooterChange", false);
                    returnIntent.setAction(AppConstants.POST_CHANGE_BROADCAST);
                    sendBroadcast(returnIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
            }
        });
    }

    private void addedPostContributorRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Response", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            String status = object.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                if (contentType == 5) {
                                    Call<String> mediaCall = videoServices.uploadVideo(deviceId, profileId, token, fileToUpload, postId, true);
                                    sendVideoRequest(mediaCall);
                                } else {
                                    Intent intent = new Intent(EditPost.this, Home.class);
                                    intent.putExtra("STATUS", status);
                                    startActivity(intent);
                                    String message = "You are now a contributor to the Hobby & Leisure - Airplanes category and your post has been added to your profile.";
                                    Tools.showCustomToast(EditPost.this, mView, message, Gravity.CENTER);
                                }

                            } else {
                                Tools.showCustomToast(EditPost.this, mView, status, Gravity.CENTER);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @Override
    public void onNegativeResult(DialogFragment dlg) {
        makeText(EditPost.this, "Negative Button", LENGTH_SHORT).show();

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<String> call = webService.addedPostContributor(deviceId, profileId, token, categoryId, subCategoryId, 6, userIds);
            addedPostContributorRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    //Your post has been successfully added to your profile. View Post
    @Override
    public void onNeutralResult(DialogFragment dlg) {

    }
}
