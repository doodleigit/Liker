package com.doodle.Comment.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Comment.adapter.CommentAdapter;
import com.doodle.Comment.model.AnimationItem;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reason;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Comment.holder.CommentImageHolder;
import com.doodle.Comment.holder.CommentLinkScriptHolder;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.holder.CommentTextHolder;
import com.doodle.Comment.holder.CommentYoutubeHolder;
import com.doodle.Comment.view.fragment.BlockUserDialog;
import com.doodle.Comment.view.fragment.FollowSheet;
import com.doodle.Comment.view.fragment.ReportLikerMessageSheet;
import com.doodle.Comment.view.fragment.ReportPersonMessageSheet;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Comment.view.fragment.ReportSendCategorySheet;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Home.model.PostItem;
import com.doodle.Post.adapter.MentionUserAdapter;
import com.doodle.Post.model.MentionUser;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.service.PostService;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PageTransformer;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.google.gson.Gson;
import com.leocardz.link.preview.library.TextCrawler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import static com.doodle.Home.holder.TextHolder.COMMENT_KEY;
import static com.doodle.Home.holder.TextHolder.ITEM_KEY;

import static com.doodle.Home.holder.TextHolder.POST_ITEM_POSITION;
import static com.doodle.Post.view.activity.PostNew.isExternalStorageDocument;
import static com.doodle.Tool.MediaUtil.getDataColumn;
import static com.doodle.Tool.MediaUtil.isDownloadsDocument;
import static com.doodle.Tool.MediaUtil.isGooglePhotosUri;
import static com.doodle.Tool.MediaUtil.isMediaDocument;
import static com.doodle.Tool.Tools.getMD5EncryptedString;
import static com.doodle.Tool.Tools.isNullOrEmpty;

public class CommentPost extends AppCompatActivity implements View.OnClickListener,
        CommentTextHolder.CommentListener,
        CommentLinkScriptHolder.CommentListener,
        CommentYoutubeHolder.CommentListener,
        CommentImageHolder.CommentListener,
        ReportReasonSheet.BottomSheetListener,
        ReportSendCategorySheet.BottomSheetListener,
        ReportPersonMessageSheet.BottomSheetListener,
        ReportLikerMessageSheet.BottomSheetListener,
        FollowSheet.BottomSheetListener,
        BlockUserDialog.BlockListener {

    private static final String TAG = "CommentPost";
    //    private List<Comment> commentList;
    private List<Comment_> comment_list;
    private RecyclerView recyclerView;
    private TextView userName;
    private Drawable mDrawable;
    private EditText etComment;

    public CommentService commentService;
    public PostService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
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
    int offset = 10;
    private LinearLayoutManager layoutManager;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private boolean isScrolling;
    boolean networkOk;
    ProgressBar progressView;
    PostItem postItem;
    CommentAdapter adapter;
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

    private TextView tvLikes, tvStars;
    //Edit Comment
    Comment_ comment_Item, commentChild;
    Reply reply;
    ImageView imageEditComment, imageSendComment;
    private int position;
    private ReportReason reportReason;

    private List<Reason> reasonList;
    private boolean isFriend;
    private String blockUserId;

    //ANIMATION
    private AnimationItem[] mAnimationItems;
    private AnimationItem mSelectedItem;
    private final Handler mHandler = new Handler();
    private String queryText;


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
//        commentList = new ArrayList<Comment>();
        comment_list = new ArrayList<Comment_>();
        comment_Item = new Comment_();
        commentChild = new Comment_();
        reportReason = new ReportReason();
        reasonList = new ArrayList<>();
        reply = new Reply();
        recyclerView = findViewById(R.id.recyclerView);
        rvSearchMention = findViewById(R.id.rvSearchMention);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        CommentItem commentItem = getIntent().getExtras().getParcelable(COMMENT_KEY);
        postItem = getIntent().getExtras().getParcelable(ITEM_KEY);
        position = getIntent().getExtras().getInt(POST_ITEM_POSITION);
        //commentChild = getIntent().getExtras().getParcelable(COMMENT_CHILD_KEY);
        if (commentItem == null) {
            throw new AssertionError("Null data item received!");
        }
        if (postItem == null) {
            throw new AssertionError("Null data item received!");
        }
//        if (commentChild == null) {
//            throw new AssertionError("Null data item received!");
//        }
//        commentList = commentItem.getComments();
        for (Comment temp : commentItem.getComments()) {
            comment_list = temp.getComments();
        }

        IntentFilter replyBroadcastIntent = new IntentFilter();
        replyBroadcastIntent.addAction(AppConstants.REPLY_CHANGE_BROADCAST);
        Objects.requireNonNull(this).registerReceiver(replyBroadcast, replyBroadcastIntent);

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
        App.setRvCommentHeader(false);
        adapter = new CommentAdapter(this, comment_list, postItem, this, this, this, this);
        recyclerView.setAdapter(adapter);
        postId = postItem.getSharedPostId();
        tvLikes = findViewById(R.id.tvLikes);
        tvStars = findViewById(R.id.tvStars);

        userName.setText(String.format("%s %s", userInfo.getFirstName(), userInfo.getLastName()));
        int totalStars = Integer.parseInt(userInfo.getGoldStars()) + Integer.parseInt(userInfo.getSliverStars());
        tvStars.setText(String.valueOf(totalStars) + " Stars");
        tvLikes.setText(userInfo.getTotalLikes() + " Likes");
        setUpEmojiPopup();
        imageSendComment.setVisibility(View.GONE);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //   makeText(PostNew.this, "onTextChanged " + s, LENGTH_SHORT).show();
                extractedUrls = Tools.extractUrls(s.toString());
                /// if(uploadImageName.)
                commentText = s.toString().trim();


                if (commentText.length() > 0) {
                    imageSendComment.setVisibility(View.VISIBLE);
                } else {
                    imageSendComment.setVisibility(View.GONE);
                    imageEditComment.setVisibility(View.GONE);
                }

                //  String s = "my very long string to test";

                for (String st : commentText.split(" ")) {
                    if (st.startsWith("@")) {
                        userQuery = st;
                    } else {
                        userQuery = "";
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
                } else if (isFirstTimeShowMention && isNullOrEmpty(userQuery)) {
                    rvMentionUserShow = false;
                    mentionUserToggle();
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


        mAnimationItems = getAnimationItems();
        mSelectedItem = mAnimationItems[1];
        runLayoutAnimation(recyclerView, mSelectedItem);

    }


    private AnimationItem[] getAnimationItems() {
        return new AnimationItem[]{
                new AnimationItem("Fall down", R.anim.layout_animation_fall_down),
                new AnimationItem("Slide from right", R.anim.layout_animation_from_right),
                new AnimationItem("Slide from bottom", R.anim.layout_animation_from_bottom)
        };
    }


    private void runLayoutAnimation(final RecyclerView recyclerView, final AnimationItem item) {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, item.getResourceId());

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }


    private void mentionUsers() {
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);

            Call<List<MentionUser>> call = webService.searchMentionUser(deviceId, profileId, token, userQuery);
            sendMentionUserRequest(call);


        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
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
                if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {

                    Call<CommentItem> call = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", postItem.getPostId(), userIds);
                    sendAllCommentItemRequest(call);
                } else {
                    Tools.showNetworkDialog(getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                }
            }
        }, 200);

    }

    private void sendAllCommentItemRequest(Call<CommentItem> call) {
        call.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {

                CommentItem commentItem = response.body();
                if (commentItem != null && commentItem.getComments() != null) {
                    List<Comment> comment = commentItem.getComments();
//                commentList = commentItem.getComments();
                    for (Comment temp : comment) {
//                    Collections.reverse(temp.getComments());
                        comment_list.addAll(temp.getComments());
                    }
                    offset += 10;
//                    Log.d("commentItem", commentItem.toString());
//                    if (commentList != null) {
//                        adapter.addPagingData(comment_list);
//                    }
                    adapter.notifyDataSetChanged();
                }
                progressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommentItem> mCall, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressView.setVisibility(View.GONE);
            }
        });

    }

    private void sendBlockCommentItemRequest(Call<CommentItem> call) {

        call.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {


                if (response.body() != null) {
                    CommentItem commentItem = response.body();
                    PostItem item = new PostItem();
                    Intent intent = new Intent(CommentPost.this, CommentPost.class);
                    intent.putExtra(COMMENT_KEY, (Parcelable) commentItem);
                    intent.putExtra(ITEM_KEY, (Parcelable) item);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onFailure(Call<CommentItem> call, Throwable t) {
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

                if (!isNullOrEmpty(comment_Item.getCommentText())) {
                    if (commentText.equalsIgnoreCase(comment_Item.getCommentText())) {
                        etComment.getText().clear(); //edit comment send button
                    } else {
                        Call<Comment_> mCall = commentService.editPostComment(deviceId, profileId, token, "1", "0", String.valueOf(comment_Item.getId()), fileToUpload, commentText, commentType, hasMention, true, linkUrl, mention, postId, userIds);
                        sendCommentEditItemRequest(mCall);
                        etComment.getText().clear(); //edit comment send button
                    }
                    imageEditComment.setVisibility(View.GONE);
                    imageSendComment.setVisibility(View.VISIBLE);
                }

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


                Comment_ commentItems = response.body();
                int editCommentId = Integer.parseInt(commentItems.getId());
                if (editCommentId > 0) {
                    adapter.updateData(commentItems, position);
                    progressDialog.dismiss();
                    etComment.setText("");
//                    offset++;
                    recyclerView.smoothScrollToPosition(position);
                }

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

//            Call<String> mediaCall = webService.addPhoto(deviceId, profileId, token, fileToUpload);
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
                int newCommentId = Integer.parseInt(commentItems.getId());

                if (newCommentId > 0) {
                    int totalComment = Integer.parseInt(postItem.getTotalComment()) + 1;
                    postItem.setTotalComment(String.valueOf(totalComment));
                    App.getAppContext().sendBroadcast((new Intent().putExtra("post_item", (Serializable) postItem).putExtra("position", position).putExtra("isFooterChange", true).setAction(AppConstants.POST_CHANGE_BROADCAST)));
/*
                    Comment_ commentItem = new Comment_();
                    commentItem.setId(String.valueOf(commentItems.getInsertId()));
                    commentItem.setCommentImage(commentItems.getCommentImage());
                    commentItem.setUserPhoto(userInfo.getPhoto());
                    commentItem.setCommentType(String.valueOf(commentType));
                    commentItem.setCommentText(commentItems.getCommentText());
                    commentItem.setHasMention(String.valueOf(hasMention));
                    commentItem.setPostId(postId);
                    commentItem.setTotalLike("0");
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
                    Log.d("comment: ", commentItem.toString());*/


                    adapter.refreshData(commentItems);
                    progressDialog.dismiss();
                    etComment.setText("");
                    offset++;
                    recyclerView.smoothScrollToPosition(0);
                    // App.setCommentCount(1);
                }

//                adapter = new CommentAdapter(CommentPost.this, comment_list, postItem);
//                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Comment_> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }


    @Override
    public void onTitleClicked(Comment_ commentItem, int position, Reply reply) {
        this.comment_Item = commentItem;
        this.reply = reply;
        this.position = position;
        imageEditComment.setVisibility(View.VISIBLE);
        imageSendComment.setVisibility(View.GONE);
//        etComment.setText(commentItem.getCommentText());
        etComment.append(commentItem.getCommentText());
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


    @Override
    public void commentDelete(Comment_ commentItem, int position, Reply reply) {
        this.comment_Item = commentItem;
        this.reply = reply;
        this.position = position;

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {

            String commentId = commentItem.getId();
            String postId = commentItem.getPostId();
            Call<String> call = commentService.deletePostComment(deviceId, profileId, token, commentId, postId, userIds);
            sendDeleteCommentRequest(call);
            // delayLoadComment(mProgressBar);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());

        }
    }

    private void sendDeleteCommentRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");

                            if (status) {

                                int totalComment = Integer.parseInt(postItem.getTotalComment()) - 1;
                                postItem.setTotalComment(String.valueOf(totalComment));
                                App.getAppContext().sendBroadcast((new Intent().putExtra("post_item", (Serializable) postItem).putExtra("position", position).putExtra("isFooterChange", true).setAction(AppConstants.POST_CHANGE_BROADCAST)));

                                comment_list.remove(position);
//                                adapter.deleteItem(position);
                                adapter.notifyDataSetChanged();
                                --offset;
//                                recyclerView.scrollToPosition(position);
                                // adapter.notifyDataSetChanged();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    String reportId;

    @Override
    public void onButtonClicked(int image, String text) {
        reportId = text;
        commentChild = App.getCommentItem();
        boolean isFollow = App.isIsFollow();
        ReportSendCategorySheet reportSendCategorySheet = ReportSendCategorySheet.newInstance(reportId, commentChild, isFollow);
        reportSendCategorySheet.show(getSupportFragmentManager(), "ReportSendCategorySheet");
    }

    @Override
    public void onFollowClicked(int image, String text) {
        String message = text;
        FollowSheet followSheet = FollowSheet.newInstance(reportId, commentChild);
        followSheet.show(getSupportFragmentManager(), "FollowSheet");
    }

    @Override
    public void onReportLikerClicked(int image, String text) {
        String message = text;
        commentChild = App.getCommentItem();
        ReportLikerMessageSheet reportLikerMessageSheet = ReportLikerMessageSheet.newInstance(reportId, commentChild);
        reportLikerMessageSheet.show(getSupportFragmentManager(), "ReportLikerMessageSheet");
    }

    @Override
    public void onPersonLikerClicked(int image, String text) {

        commentChild = App.getCommentItem();
        Reply reply = new Reply();
        ReportPersonMessageSheet reportPersonMessageSheet = ReportPersonMessageSheet.newInstance(reportId, commentChild, reply);
        reportPersonMessageSheet.show(getSupportFragmentManager(), "ReportPersonMessageSheet");
    }

    @Override
    public void onReportPersonMessageClicked(int image, String text) {

    }

    @Override
    public void onReportLikerMessageClicked(int image, String text) {

    }

    @Override
    public void onUnfollowClicked(int image, String text) {

    }


    @Override
    public void onBlockResult(DialogFragment dlg) {
        commentChild = App.getCommentItem();
        blockUserId = commentChild.getUserId();
        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            Call<String> call = commentService.blockedUser(deviceId, profileId, token, blockUserId, userIds);
            sendBlockUserRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
        }
    }

    @Override
    public void onCancelResult(DialogFragment dlg) {

    }

    private void sendBlockUserRequest(Call<String> call) {

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");

                            if (status) {

                                App.setIsBockComment(true);
                                Call<CommentItem> blockCall = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", postItem.getPostId(), userIds);
                                sendBlockCommentItemRequest(blockCall);

                                //    log("Running code");
                                //  delayLoadComment(mProgressBar);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("message", t.getMessage());
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.isIsBockReply()) {
            App.setIsBockReply(false);
            Call<CommentItem> blockCall = commentService.getAllPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", postItem.getPostId(), userIds);
            sendBlockCommentItemRequest(blockCall);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    BroadcastReceiver replyBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Comment_ commentItem = (Comment_) intent.getSerializableExtra("comment_item");
            int position = intent.getIntExtra("comment_position", -1);
            //  String type=intent.getStringExtra("type");

            if (position != -1) {
                //   Log.d("Cbndklfj ",commentItem.toString());

                comment_list.remove(position);
                comment_list.add(position, commentItem);
                adapter.notifyItemChanged(position);

              /*  if (postItemList.size() >= position + 1) {
                    if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {


                        if("permission".equalsIgnoreCase(type)){
                            postItemList.remove(position);
                            postItemList.add(position, postItem);
                            adapter.notifyItemChanged(position);
                        }else {
                            postItemList.remove(position);
                            // adapter.notifyItemChanged(position);
                            adapter.notifyDataSetChanged();
                        }


                    }
                }*/
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.dismissDialog();
        mHandler.removeCallbacksAndMessages(null);
        Objects.requireNonNull(this).unregisterReceiver(replyBroadcast);
    }

}
