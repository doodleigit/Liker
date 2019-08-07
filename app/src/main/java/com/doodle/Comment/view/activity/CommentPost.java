package com.doodle.Comment.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Comment.adapter.AllCommentAdapter;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.service.CommentTextHolder;
import com.doodle.Home.model.PostItem;
import com.doodle.Post.adapter.MentionUserAdapter;
import com.doodle.Post.model.MentionUser;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.service.PostService;
import com.doodle.R;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PageTransformer;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.google.gson.Gson;
import com.leocardz.link.preview.library.TextCrawler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.doodle.Comment.service.CommentTextHolder.COMMENT_ITEM_KEY;
import static com.doodle.Comment.service.CommentTextHolder.POST_ITEM_KEY;
import static com.doodle.Comment.service.CommentTextHolder.REPLY_ITEM_KEY;
import static com.doodle.Home.service.TextHolder.COMMENT_KEY;
import static com.doodle.Home.service.TextHolder.ITEM_KEY;
import static com.doodle.Post.view.activity.PostNew.isExternalStorageDocument;
import static com.doodle.utils.MediaUtil.getDataColumn;
import static com.doodle.utils.MediaUtil.isDownloadsDocument;
import static com.doodle.utils.MediaUtil.isGooglePhotosUri;
import static com.doodle.utils.MediaUtil.isMediaDocument;
import static com.doodle.utils.Utils.getMD5EncryptedString;
import static com.doodle.utils.Utils.isNullOrEmpty;

public class CommentPost extends AppCompatActivity implements View.OnClickListener, CommentTextHolder.ExcellentAdventureListener {

    private static final String TAG = "CommentPost";
    private List<Comment> commentList;
    private List<Comment_> comment_list;
    private RecyclerView recyclerView;
    private TextView userName;
    private Drawable mDrawable;
    private EditText etComment;

    public CommentService commentService;
    public PostService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;
    private String postId;
    private boolean isAddContentTitle;
    //Emoji
    private ImageView imageEmoji;
    private EmojiPopup emojiPopup;
    private ViewGroup rootView;

    //Gallery
    private ImageView imageGallery;
    private boolean isGrantGallery;
    private static final int REQUEST_TAKE_GALLERY_IMAGE = 102;

    //LinkScript
    List<String> extractedUrls = new ArrayList<>();
    private boolean isLinkScript;
    private TextCrawler textCrawler;
    private String myUrl;
    private boolean isYoutubeURL;

    //create Comment
    private String commentImage, commentText, linkUrl;
    private int commentType, hasMention;
    private String imageFilePath;
    private String imageFile;
    private String mention;

    int limit = 10;
    int offset = 0;
    private LinearLayoutManager layoutManager;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private boolean isScrolling;
    boolean networkOk;
    ProgressBar progressView;
    PostItem postItem;
    AllCommentAdapter adapter;
    private String fileEncoded;
    MultipartBody.Part fileToUpload;
    private ProgressDialog progressDialog;

    //MENTION
    RecyclerView rvSearchMention;
    private boolean rvMentionUserShow;
    private String userQuery;
    private boolean isFirstTimeShowMention;
    private String replaceContent;
    List<String> nameList = new ArrayList<>();
    List<String> idList = new ArrayList<>();
    List<String> friendSet = new ArrayList<>();
    private String friends;
    ArrayList<String> mList = new ArrayList<>();
    Map<String, String> wordsToReplace = new HashMap<String, String>();
    Set<String> keys = wordsToReplace.keySet();
    private String mentionMessage;
    private String name;
    private UserInfo userInfo;
    private String changeData;

    //Edit Comment
    Comment_ comment_Item;
    ImageView imageEditComment, imageSendComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        toolbar.setNavigationIcon(R.drawable.ic_people_black_24dp);
//        toolbar.setNavigationIcon(mDrawable);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rootView = findViewById(R.id.main_activity_root_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        textCrawler = new TextCrawler();
        imageSendComment = findViewById(R.id.imageSendComment);
        imageEditComment = findViewById(R.id.imageEditComment);
        imageSendComment.setOnClickListener(this);
        imageEditComment.setOnClickListener(this);
        findViewById(R.id.imageGallery).setOnClickListener(this);
        imageEmoji = findViewById(R.id.imageEmoji);
        imageEmoji.setOnClickListener(this);
        userName = findViewById(R.id.user_name);
        etComment = findViewById(R.id.etComment);
        commentList = new ArrayList<Comment>();
        comment_list = new ArrayList<Comment_>();
        comment_Item = new Comment_();
        recyclerView = findViewById(R.id.recyclerView);
        rvSearchMention = findViewById(R.id.rvSearchMention);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        CommentItem commentItem = getIntent().getExtras().getParcelable(COMMENT_KEY);
        postItem = getIntent().getExtras().getParcelable(ITEM_KEY);
        if (commentItem == null) {
            throw new AssertionError("Null data item received!");
        }
        if (postItem == null) {
            throw new AssertionError("Null data item received!");
        }
        commentList = commentItem.getComments();
        for (Comment temp : commentList) {
            comment_list = temp.getComments();
        }


  /*      Bundle bundle = getIntent().getExtras();
        Log.d("bundle", bundle.toString());
        if (bundle != null) {
            Comment_ commentItem_ = getIntent().getExtras().getParcelable(COMMENT_ITEM_KEY);
            postItem = getIntent().getExtras().getParcelable(POST_ITEM_KEY);
            Log.d("commentItem: ", commentItem_.toString());


            if (commentItem_ == null) {

                throw new AssertionError("Null data item received!");
            }
            if (postItem == null) {
                throw new AssertionError("Null data item received!");
            }
        }
*/

        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        progressView = findViewById(R.id.progress_bar);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        Gson gson = new Gson();
        String json = manager.getUserInfo();
        userInfo = gson.fromJson(json, UserInfo.class);
//        Log.d("userInfo",userInfo.toString());
        commentService = CommentService.mRetrofit.create(CommentService.class);
        webService = PostService.mRetrofit.create(PostService.class);
        //  Picasso.with(App.getInstance()).load(imageUrl).into(target);
        adapter = new AllCommentAdapter(this, comment_list, postItem, this);
        recyclerView.setAdapter(adapter);
        postId = postItem.getSharedPostId();
        userName.setText(String.format("%s %s", userInfo.getFirstName(), userInfo.getLastName()));
        setUpEmojiPopup();

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //   makeText(PostNew.this, "onTextChanged " + s, LENGTH_SHORT).show();
                extractedUrls = Utils.extractUrls(s.toString());
                /// if(uploadImageName.)
                commentText = s.toString().trim();


                //  String s = "my very long string to test";

                for (String st : commentText.split(" ")) {
                    if (st.startsWith("@")) {
                        userQuery = st;
                    }
                }


                if (!isNullOrEmpty(userQuery) && userQuery.length() > 1) {
                    rvMentionUserShow = true;
                    mentionUserToggle();
                    mentionUsers();
                } else if (isFirstTimeShowMention && !isNullOrEmpty(userQuery)) {
                    rvMentionUserShow = true;
                    mentionUserToggle();
                    mentionUsers();
                }

                if (extractedUrls.size() == 0) {
                    isLinkScript = false;
//                    releasePreviewArea();
//                    rvLinkScriptShow = false;
//                    linkScriptToggle();
                    commentType = 1;


                }
                if (extractedUrls.size() > 0) {

                    StringBuilder builder = new StringBuilder();
                    for (String temp : extractedUrls) {
                        builder.append(temp);
                    }
                    myUrl = builder.toString();
                    linkUrl = myUrl;


                }
                if (!isLinkScript && extractedUrls.size() == 1) {
                    for (String url : extractedUrls) {
//                        textCrawler.makePreview(callback, url);
                        isLinkScript = true;
                        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                        if (!url.isEmpty() && url.matches(pattern)) {
                            /// Valid youtube URL
                            isYoutubeURL = true;
                            commentType = 4;
                        } else {
                            isYoutubeURL = false;
                            commentType = 4;
                            // Not Valid youtube URL
                        }
                    }
                }


                if (!isNullOrEmpty(commentText) && !isNullOrEmpty(myUrl) && commentText.length() > myUrl.length()) {
                    isAddContentTitle = true;


                } else if (isNullOrEmpty(myUrl) && !isNullOrEmpty(commentText) && commentText.length() > 0) {
                    isAddContentTitle = true;

                }


                if(commentText.equalsIgnoreCase(comment_Item.getCommentText())){
                    imageEditComment.setVisibility(View.GONE);
                    imageSendComment.setVisibility(View.VISIBLE);
                }else {
                    imageEditComment.setVisibility(View.VISIBLE);
                    imageSendComment.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }

        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                totalItems = layoutManager.getItemCount();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    PerformPagination();
                }


            }
        });


    }

    private void mentionUsers() {
        if (networkOk) {
            progressView.setVisibility(View.VISIBLE);

            Call<List<MentionUser>> call = webService.searchMentionUser(deviceId, profileId, token, userQuery);
            sendMentionUserRequest(call);


        } else {
            Utils.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);


        }
    }

    private void sendMentionUserRequest(Call<List<MentionUser>> call) {
        call.enqueue(new Callback<List<MentionUser>>() {


            @Override
            public void onResponse(Call<List<MentionUser>> call, Response<List<MentionUser>> response) {


                List<MentionUser> mentionUsers = response.body();
                replaceContent = commentText.replace(userQuery, " ");


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

                    mention = friends;
                    if (nameList.size() > 0) {
                        //Create new list
                        String nameStr = nameBuilder.toString();
                        String[] nameArr = nameStr.split(" ");


                        StringBuilder mentionBuilder = new StringBuilder();
                        String mention_text = commentText.replaceAll(userQuery, "mention_" + id);

                        String full_text = commentText.replaceAll(userQuery, name);

                        //split strings by space
                        String[] splittedWords = full_text.split(" ");
                        SpannableString str = new SpannableString(full_text);
                        Log.d(TAG, "onResponse: " + splittedWords);

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

                        etComment.setText("");
                        etComment.append(str);


                    }


                    userQuery = "";
                    rvMentionUserShow = false;
                    mentionUserToggle();

                    //  rvMimShow = false;
                    //  rvMimToggle();

                };
                MentionUserAdapter mentionUserAdapter = new MentionUserAdapter(CommentPost.this, mentionUsers, listener);
                rvSearchMention.setAdapter(mentionUserAdapter);


                progressView.setVisibility(View.GONE);
                isFirstTimeShowMention = true;

            }

            @Override
            public void onFailure(Call<List<MentionUser>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
            }
        });

    }

    private void mentionUserToggle() {
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(rvSearchMention);

        TransitionManager.beginDelayedTransition(rootView, transition);
        rvSearchMention.setVisibility(rvMentionUserShow ? View.VISIBLE : View.GONE);
    }

    private void PerformPagination() {
        progressView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (networkOk) {

                    Call<CommentItem> call = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", postItem.getPostId(), userIds);
                    sendAllCommentItemRequest(call);
                } else {
                    Utils.showNetworkDialog(getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                }
            }
        }, 2000);

    }

    private void sendAllCommentItemRequest(Call<CommentItem> call) {
        call.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {

                CommentItem commentItem = response.body();
                commentList = commentItem.getComments();
                for (Comment temp : commentList) {
                    Collections.reverse(temp.getComments());
                    comment_list = temp.getComments();
                }
                Log.d("commentItem", commentItem.toString());
                if (commentList != null) {
                    adapter.addPagingData(comment_list);
                    offset += 10;
                    progressView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<CommentItem> mCall, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressView.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imageSendComment:

                if (!isNullOrEmpty(mentionMessage)) {
                    for (String temp : nameList) {
                        name = temp;
                    }
                    if (commentText.contains(name)) {
                        String text = commentText;
                        String lastPlainText = text.substring((text.indexOf(name) + name.length()));
                        mentionMessage += lastPlainText;
                        commentText = mentionMessage;
                    }
                    hasMention = 1;
                    commentType = 1;
                }


                Call<Comment_> call = commentService.addedComment(deviceId, profileId, token, fileToUpload, commentText, commentType, hasMention, linkUrl, mention, postId, userIds);
                sendCommentItemRequest(call);

                break;
            case R.id.imageEditComment:
                if (!isNullOrEmpty(mentionMessage)) {
                    for (String temp : nameList) {
                        name = temp;
                    }
                    if (commentText.contains(name)) {
                        String text = commentText;
                        String lastPlainText = text.substring((text.indexOf(name) + name.length()));
                        mentionMessage += lastPlainText;
                        commentText = mentionMessage;
                    }
                    hasMention = 1;
                    commentType = 1;
                }
                Call<Comment_> mCall = commentService.editPostComment(deviceId, profileId, token, "1", "0", String.valueOf(comment_Item.getId()), fileToUpload, commentText, commentType, hasMention, true, linkUrl, mention, postId, userIds);
                sendCommentEditItemRequest(mCall);
                break;

            case R.id.imageEmoji:
                emojiPopup.toggle();
                break;

            case R.id.imageGallery:
                if (isGrantGallery) {

                    sendImageFromGallery();
                } else {
                    checkGalleryPermission();
                }
                break;
        }
    }

    private void sendCommentEditItemRequest(Call<Comment_> call) {

        call.enqueue(new Callback<Comment_>() {

            @Override
            public void onResponse(Call<Comment_> call, Response<Comment_> response) {

                if (response.isSuccessful()) {
                    Log.d("response", response.body().toString());
                }

                Comment_ commentItems = response.body();
                int insertId = commentItems.getInsertId();
                Log.d("Data", commentItems.toString());
                if (insertId > 0) {

                    Comment_ commentItem = new Comment_();
                    commentItem.setCommentImage(commentItems.getCommentImage());
                    commentItem.setUserPhoto(userInfo.getPhoto());
                    commentItem.setCommentType(String.valueOf(commentType));
                    commentItem.setCommentText(commentItems.getCommentText());
                    commentItem.setHasMention(String.valueOf(hasMention));
                    //  commentItem.getLinkData().setLinkFullUrl(linkUrl);

                    //  commentItem.getLinkData().setLinkFullUrl(linkUrl);
                    commentItem.setCommentTextIndex(commentItems.getCommentTextIndex());
                    commentItem.setLinkData(commentItems.getLinkData());
                    commentItem.setUserId(profileId);
                    commentItem.setUserFirstName(userInfo.getFirstName());
                    commentItem.setUserLastName(userInfo.getLastName());
                    commentItem.setUserGoldStars(userInfo.getGoldStars());
                    commentItem.setUserSliverStars(userInfo.getSliverStars());
                    long seconds = System.currentTimeMillis() / 1000;
                    commentItem.setDateTime(String.valueOf(seconds));
                    Log.d("comment: ", commentItem.toString());
                    adapter.refreshData(commentItem);
                    progressDialog.dismiss();
                    etComment.setText("");
                    offset++;
                    recyclerView.smoothScrollToPosition(0);
                    // App.setCommentCount(1);
                }

//                adapter = new AllCommentAdapter(CommentPost.this, comment_list, postItem);
//                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Comment_> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });

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
                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;
                //commentImage=fileEncoded;
                commentType = 2;
              /*  String imagePath = "file://" + imageFilePath;
                //     String strBase64 = getBase64(imagePath);
                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;
                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                sendIsDuplicateRequest(call);*/
                File file = new File(imageFilePath);
                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                fileToUpload = MultipartBody.Part.createFormData("comment_image", file.getName(), requestFile);


                progressDialog.show();
                Call<Comment_> call = commentService.addedComment(deviceId, profileId, token, fileToUpload, commentText, commentType, hasMention, linkUrl, mention, postId, userIds);
                sendCommentItemRequest(call);

            }
        } else {
            Uri videoURI = data.getData();
            imageFilePath = getPath(this, videoURI);
            String imagePath = "file://" + imageFilePath;
            String strMD5 = getMD5EncryptedString(imagePath);
            fileEncoded = strMD5;
            //  commentImage=fileEncoded;
            commentType = 2;
      /*      String imagePath = "file://" + imageFilePath;
            String strMD5 = getMD5EncryptedString(imagePath);
            fileEncoded = strMD5;
            //   String strBase64 = getBase64(imageFilePath);
            Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
            sendIsDuplicateRequest(call);*/
            File file = new File(imageFilePath);
            //Parsing any Media type file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
            fileToUpload = MultipartBody.Part.createFormData("comment_image", file.getName(), requestFile);
            progressDialog.show();
            Call<Comment_> call = commentService.addedComment(deviceId, profileId, token, fileToUpload, commentText, commentType, hasMention, linkUrl, mention, postId, userIds);
            sendCommentItemRequest(call);

//            Call<String> mediaCall = webService.postImage(deviceId, profileId, token, fileToUpload);
//            sendImageRequest(mediaCall);

        }

        return result;
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
                .build(etComment);
    }

    private void sendCommentItemRequest(Call<Comment_> call) {

        call.enqueue(new Callback<Comment_>() {

            @Override
            public void onResponse(Call<Comment_> call, Response<Comment_> response) {

                Comment_ commentItems = response.body();
                int insertId = commentItems.getInsertId();
                Log.d("Data", commentItems.toString());
                if (insertId > 0) {

                    Comment_ commentItem = new Comment_();
                    commentItem.setCommentImage(commentItems.getCommentImage());
                    commentItem.setUserPhoto(userInfo.getPhoto());
                    commentItem.setCommentType(String.valueOf(commentType));
                    commentItem.setCommentText(commentItems.getCommentText());
                    commentItem.setHasMention(String.valueOf(hasMention));
                    //  commentItem.getLinkData().setLinkFullUrl(linkUrl);

                    //  commentItem.getLinkData().setLinkFullUrl(linkUrl);
                    commentItem.setCommentTextIndex(commentItems.getCommentTextIndex());
                    commentItem.setLinkData(commentItems.getLinkData());
                    commentItem.setUserId(profileId);
                    commentItem.setUserFirstName(userInfo.getFirstName());
                    commentItem.setUserLastName(userInfo.getLastName());
                    commentItem.setUserGoldStars(userInfo.getGoldStars());
                    commentItem.setUserSliverStars(userInfo.getSliverStars());
                    long seconds = System.currentTimeMillis() / 1000;
                    commentItem.setDateTime(String.valueOf(seconds));
                    Log.d("comment: ", commentItem.toString());
                    adapter.refreshData(commentItem);
                    progressDialog.dismiss();
                    etComment.setText("");
                    offset++;
                    recyclerView.smoothScrollToPosition(0);
                    // App.setCommentCount(1);
                }

//                adapter = new AllCommentAdapter(CommentPost.this, comment_list, postItem);
//                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Comment_> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }


    @Override
    public void onTitleClicked(Comment_ commentItem) {
        this.comment_Item = commentItem;
        etComment.setText(commentItem.getCommentText());
        etComment.requestFocus();
        etComment.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                      InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                      keyboard.showSoftInput(etComment, 0);
                                  }
                              }
                , 200);




    }

}
