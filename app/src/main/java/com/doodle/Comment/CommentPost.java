package com.doodle.Comment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Comment.adapter.AllCommentAdapter;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.service.CommentService;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.TextHolder;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Post.adapter.MediaAdapter;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.service.PostService;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.PageTransformer;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.leocardz.link.preview.library.TextCrawler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.doodle.Home.service.TextHolder.COMMENT_ITEM_KEY;
import static com.doodle.Home.service.TextHolder.ITEM_KEY;
import static com.doodle.Post.view.activity.PostNew.isExternalStorageDocument;
import static com.doodle.utils.MediaUtil.getDataColumn;
import static com.doodle.utils.MediaUtil.isDownloadsDocument;
import static com.doodle.utils.MediaUtil.isGooglePhotosUri;
import static com.doodle.utils.MediaUtil.isMediaDocument;
import static com.doodle.utils.Utils.getMD5EncryptedString;
import static com.doodle.utils.Utils.isNullOrEmpty;

public class CommentPost extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CommentPost";
    private List<Comment> commentList;
    private List<Comment_> comment_list;
    private RecyclerView recyclerView;
    private TextView userName;
    private Drawable mDrawable;
    private ImageView imageSendComment;
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
    private String userQuery;
    private boolean isLinkScript;
    private TextCrawler textCrawler;
    private String myUrl ;
    private boolean isYoutubeURL;

    //create Comment
    private String commentImage, commentText, linkUrl;
    private int commentType, hasMention, mention;
    private String imageFilePath;
    private String imageFile;

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
        textCrawler = new TextCrawler();
        findViewById(R.id.imageSendComment).setOnClickListener(this);
        findViewById(R.id.imageGallery).setOnClickListener(this);
        imageEmoji = findViewById(R.id.imageEmoji);
        imageEmoji.setOnClickListener(this);
        userName = findViewById(R.id.user_name);
        etComment = findViewById(R.id.etComment);
        commentList = new ArrayList<Comment>();
        comment_list = new ArrayList<Comment_>();
        recyclerView = findViewById(R.id.recyclerView);
        CommentItem commentItem = getIntent().getExtras().getParcelable(COMMENT_ITEM_KEY);
        PostItem postItem = getIntent().getExtras().getParcelable(ITEM_KEY);
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
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        commentService = CommentService.mRetrofit.create(CommentService.class);
        webService = PostService.mRetrofit.create(PostService.class);
        //  Picasso.with(App.getInstance()).load(imageUrl).into(target);
        AllCommentAdapter adapter = new AllCommentAdapter(this, comment_list);
        recyclerView.setAdapter(adapter);
        postId = postItem.getSharedPostId();
        userName.setText(String.format("%s %s", postItem.getUserFirstName(), postItem.getUserLastName()));
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

//                for (String st : commentText.split(" ")) {
//                    if (st.startsWith("@")) {
//                        userQuery = st;
//                    }
//                }
//
//
//
//                if (!isNullOrEmpty(userQuery) && userQuery.length() > 1) {
//                    rvMentionUserShow = true;
//                    mentionUserToggle();
//                    mentionUsers();
//                } else if (isFirstTimeShowMention && !isNullOrEmpty(userQuery)) {
//                    rvMentionUserShow = true;
//                    mentionUserToggle();
//                    mentionUsers();
//                }

                if (extractedUrls.size() == 0) {
                    isLinkScript = false;
//                    releasePreviewArea();
//                    rvLinkScriptShow = false;
//                    linkScriptToggle();
                    commentType=1;


                }
                if (extractedUrls.size() > 0) {

                    StringBuilder builder = new StringBuilder();
                    for (String temp : extractedUrls) {
                        builder.append(temp);
                    }
                    myUrl = builder.toString();
                    linkUrl=myUrl;


                }
                if (!isLinkScript && extractedUrls.size() == 1) {
                    for (String url : extractedUrls) {
//                        textCrawler.makePreview(callback, url);
                        isLinkScript = true;
                        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                        if (!url.isEmpty() && url.matches(pattern)) {
                            /// Valid youtube URL
                            isYoutubeURL = true;
                            commentType=4;
                        } else {
                            isYoutubeURL = false;
                            commentType=3;
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

                Call<Comment_> call = commentService.addedComment(deviceId, profileId, token, commentImage, commentText, commentType, hasMention, linkUrl, mention, postId, userIds);
                sendShareItemRequest(call);
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
              /*  String imagePath = "file://" + imageFilePath;
                //     String strBase64 = getBase64(imagePath);
                String strMD5 = getMD5EncryptedString(imagePath);
                fileEncoded = strMD5;
                Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
                sendIsDuplicateRequest(call);*/
                File file = new File(imageFilePath);
                //Parsing any Media type file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
                Call<String> mediaCall = webService.postImage(deviceId, profileId, token, fileToUpload);
                sendImageRequest(mediaCall);

            }
        } else {
            Uri videoURI = data.getData();
            imageFilePath = getPath(this, videoURI);
      /*      String imagePath = "file://" + imageFilePath;
            String strMD5 = getMD5EncryptedString(imagePath);
            fileEncoded = strMD5;
            //   String strBase64 = getBase64(imageFilePath);
            Call<String> call = webService.isDuplicateFile(deviceId, profileId, token, userIds, "1", strMD5);
            sendIsDuplicateRequest(call);*/
            File file = new File(imageFilePath);
            //Parsing any Media type file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
            Call<String> mediaCall = webService.postImage(deviceId, profileId, token, fileToUpload);
            sendImageRequest(mediaCall);

        }

        return result;
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
                            Log.d("Image:", imageFile);


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
                //    progressView.setVisibility(View.GONE);
                //  progressView.stopAnimation();
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

    private void sendShareItemRequest(Call<Comment_> call) {

        call.enqueue(new Callback<Comment_>() {

            @Override
            public void onResponse(Call<Comment_> call, Response<Comment_> response) {

                Comment_ postShareItem = response.body();
                Log.d("Data", postShareItem.toString());
                if (postShareItem != null) {

                }

            }

            @Override
            public void onFailure(Call<Comment_> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }
}
