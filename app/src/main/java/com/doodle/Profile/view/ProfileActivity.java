package com.doodle.Profile.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.fragment.BlockUserDialog;
import com.doodle.Comment.view.fragment.FollowSheet;
import com.doodle.Comment.view.fragment.ReportLikerMessageSheet;
import com.doodle.Comment.view.fragment.ReportPersonMessageSheet;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Comment.view.fragment.ReportSendCategorySheet;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.view.fragment.PostPermissionSheet;
import com.doodle.Profile.adapter.ViewPagerAdapter;
import com.doodle.Profile.model.Privacy;
import com.doodle.Profile.model.UserAllInfo;
import com.doodle.Profile.service.ProfileDataFetchCompleteListener;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.Search.LikerSearch;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.doodle.Tool.Tools.isEmpty;

public class ProfileActivity extends AppCompatActivity implements ReportReasonSheet.BottomSheetListener,
        ReportSendCategorySheet.BottomSheetListener,
        ReportPersonMessageSheet.BottomSheetListener,
        ReportLikerMessageSheet.BottomSheetListener,
        FollowSheet.BottomSheetListener,
        BlockUserDialog.BlockListener,
        PostPermissionSheet.BottomSheetListener {

    private TabLayout tabLayout;
    //    private ViewPager viewPager;
    private Toolbar toolbar;
    private ScrollView scrollView;
    private LinearLayout searchLayout, followLayout;
    private RelativeLayout coverImageLayout, profileImageLayout;
    private ImageView ivCoverImage, ivProfileImage, ivChangeCoverImage, ivChangeProfileImage;
    private TextView tvUserName, tvTotalInfoCount, tvFollow;

    private ProfileService profileService;
    private CommentService commentService;
    private ProgressDialog progressDialog;
    public ProfileDataFetchCompleteListener profileDataFetchCompleteListener;

    private PrefManager manager;
    private UserAllInfo userAllInfo;
    private Uri imageUri;
    private String profileUserId;
    private final int REQUEST_TAKE_CAMERA = 101;
    private final int REQUEST_TAKE_GALLERY_IMAGE = 102;
    private int uploadContentType = 0;
    private String deviceId, userId, token, profileUserName, fullName, userImage, coverImage, allCountInfo;
    private boolean isOwnProfile, isFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialComponent();
        setupTabIcons();
        getData();
    }

    private void initialComponent() {
        profileUserId = getIntent().getStringExtra("user_id");
        profileUserName = getIntent().getStringExtra("user_name");
        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();

        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.scrollView);
        searchLayout = findViewById(R.id.search_layout);
        followLayout = findViewById(R.id.follow_layout);
        coverImageLayout = findViewById(R.id.cover_image_layout);
        profileImageLayout = findViewById(R.id.profile_image_layout);
        ivCoverImage = findViewById(R.id.cover_image);
        ivProfileImage = findViewById(R.id.profile_image);
        ivChangeCoverImage = findViewById(R.id.change_cover_image);
        ivChangeProfileImage = findViewById(R.id.change_profile_image);
        tvUserName = findViewById(R.id.user_name);
        tvTotalInfoCount = findViewById(R.id.total_info_count);
        tvFollow = findViewById(R.id.follow);
        tabLayout = findViewById(R.id.tabs);
//        viewPager = findViewById(R.id.viewpager);

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        commentService = CommentService.mRetrofit.create(CommentService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        ownProfileCheck();

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, LikerSearch.class));
            }
        });

        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOwnProfile) {
                    uploadContentType = 0;
                    selectImageSource(ivChangeProfileImage);
                }
            }
        });

        coverImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOwnProfile) {
                    uploadContentType = 1;
                    selectImageSource(ivChangeCoverImage);
                }
            }
        });

        followLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFollow) {
                    setUnFollow(profileUserId);
                } else {
                    setFollow(profileUserId);
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff == 0) {
                    sendBroadcast((new Intent()).setAction(AppConstants.PROFILE_PAGE_PAGINATION_BROADCAST));
                }
            }
        });
    }

    private void ownProfileCheck() {
        if (userId.equals(profileUserId)) {
            isOwnProfile = true;
            ivChangeProfileImage.setVisibility(View.VISIBLE);
            ivChangeCoverImage.setVisibility(View.VISIBLE);
            followLayout.setVisibility(View.INVISIBLE);
        } else {
            isOwnProfile = false;
            ivChangeProfileImage.setVisibility(View.INVISIBLE);
            ivChangeCoverImage.setVisibility(View.INVISIBLE);
            followLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getData() {

        Call<UserAllInfo> call = profileService.getUserInfo(deviceId, userId, token, userId, profileUserName, true);
        getUserInfo(call);
    }

    private void setData() {
        fullName = userAllInfo.getFirstName() + " " + userAllInfo.getLastName();
        userImage = AppConstants.USER_UPLOADED_IMAGES + userAllInfo.getPhoto();
        coverImage = AppConstants.USER_UPLOADED_IMAGES + userAllInfo.getCoverImage();
        allCountInfo = userAllInfo.getTotalLikes() + " Likes " + userAllInfo.getTotalFollowers() + " Followers " + userAllInfo.getTotalFollowings() + " Following";

        tvUserName.setText(fullName);
        tvTotalInfoCount.setText(allCountInfo);
        loadProfileImage();
        loadCoverImage();
    }

    private void loadProfileImage() {
        Glide.with(App.getAppContext())
                .load(userImage)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(ivProfileImage);
    }

    private void loadCoverImage() {
        Glide.with(App.getAppContext())
                .load(coverImage)
                .centerCrop()
                .dontAnimate()
                .into(ivCoverImage);
    }

    private void initialFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", profileUserId);
        bundle.putString("user_name", profileUserName);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment).commit();
    }

    private void selectImageSource(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //Inflating the Popup using xml file
        if (uploadContentType == 0) {
            popup.getMenuInflater().inflate(R.menu.image_source_menu, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.cover_image_source_menu, popup.getMenu());
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.select_picture:
                        checkGalleryPermission();
                        return true;
                    case R.id.capture_picture:
                        checkCameraPermission();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.show();//showing popup menu
    }

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_GALLERY_IMAGE);
        } else {
            sendImageFromGallery();
        }
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_CAMERA);
        } else {
            sendImageFromCamera();
        }
    }

    public void sendImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getImageUri();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_CAMERA);
        }
    }

    public void sendImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_IMAGE);
    }

    private void uploadImage() {
        String path = Tools.getPath(this, imageUri);
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        Call<String> mediaCall;
        if (uploadContentType == 0) {
            mediaCall = profileService.uploadProfileImage(deviceId, userId, token, fileToUpload);
        } else {
            mediaCall = profileService.uploadCoverImage(deviceId, userId, token, fileToUpload);
        }
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        sendImageRequest(mediaCall);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_GALLERY_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendImageFromGallery();
                }
                break;
            case REQUEST_TAKE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendImageFromCamera();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (result != null) {
                    imageUri = result.getUri();
                    uploadImage();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(), LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_TAKE_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    getSelectedImagesPath(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
//            else {
//                Toast.makeText(this, "Cancel Gallery", Toast.LENGTH_SHORT).show();
//            }
        }
        if (requestCode == REQUEST_TAKE_CAMERA) {
            if (resultCode == RESULT_OK) {
                cropImage(imageUri);
            }
//            else {
//                Toast.makeText(this, "Cancel Camera Capture", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private Uri getImageUri() {
        Uri m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception ignored) {
        }
        return m_imgUri;
    }

    private void getSelectedImagesPath(Intent data) throws FileNotFoundException {
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            Uri uri = null;
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                uri = item.getUri();
            }
            cropImage(uri);
        } else {
            Uri uri = data.getData();
            cropImage(uri);
        }
    }

    private void cropImage(Uri uri) {
        if (uploadContentType == 0) {
            CropImage.activity(uri)
                    .setRequestedSize(300, 300)
                    .setAspectRatio(1, 1)
                    .setMinCropResultSize(300, 300)
                    .start(this);
        } else {
            CropImage.activity(uri)
                    .setRequestedSize(1150, 235)
                    .setAspectRatio(4, 1)
                    .setMinCropResultSize(1150, 235)
                    .start(this);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PostFragment(), "Post");
        adapter.addFrag(new AboutFragment(), "About");
        adapter.addFrag(new FollowersFragment(), "Friends");
        adapter.addFrag(new PhotosFragment(), "Photos");
        adapter.addFrag(new StarFragment(), "Star");
        viewPager.setAdapter(adapter);
    }

//    private void setupViewPager() {
//        viewPager = findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();
//    }

    private void setupTabIcons() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.posts)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.about)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.followers)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.following)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.photos)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.stars)));
        initialFragment(new PostFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    initialFragment(new PostFragment());
                } else if (tab.getPosition() == 1) {
                    initialFragment(new AboutFragment());
                } else if (tab.getPosition() == 2) {
                    initialFragment(new FollowersFragment());
                } else if (tab.getPosition() == 3) {
                    initialFragment(new FollowingFragment());
                } else if (tab.getPosition() == 4) {
                    initialFragment(new PhotosFragment());
                } else if (tab.getPosition() == 5) {
                    initialFragment(new StarFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void isFriendStatus() {
        Call<String> call = profileService.isFriendStatus(deviceId, userId, token, userId, profileUserName);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean follow = obj.getBoolean("follow");
                    if (follow) {
                        isFollow = true;
                        tvFollow.setText(getString(R.string.following));
                    } else {
                        isFollow = false;
                        tvFollow.setText(getString(R.string.follow));
                    }
                    if (profileDataFetchCompleteListener != null) {
                        profileDataFetchCompleteListener.onComplete(userAllInfo.getPrivacy().getWallPermission(), isFollow);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getUserInfo(Call<UserAllInfo> call) {
        call.enqueue(new Callback<UserAllInfo>() {
            @Override
            public void onResponse(Call<UserAllInfo> call, Response<UserAllInfo> response) {
                userAllInfo = response.body();
                isFriendStatus();
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserAllInfo> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setFollow(String followUserId) {
        progressDialog.setMessage(getString(R.string.updating));
        progressDialog.show();
        Call<String> call = profileService.setFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        isFollow = true;
                        tvFollow.setText(getString(R.string.following));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setUnFollow(String followUserId) {
        progressDialog.setMessage(getString(R.string.updating));
        progressDialog.show();
        Call<String> call = profileService.setUnFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        isFollow = false;
                        tvFollow.setText(getString(R.string.follow));
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void sendImageRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    boolean status = object.getBoolean("status");
                    if (status) {
                        String image = object.getString("file_name");
                        String message;
                        if (uploadContentType == 0) {
                            userImage = AppConstants.USER_UPLOADED_IMAGES + image;
                            loadProfileImage();
                            message = getString(R.string.profile_photo_has_been_updated);
                        } else {
                            coverImage = AppConstants.USER_UPLOADED_IMAGES + image;
                            loadCoverImage();
                            message = getString(R.string.cover_photo_has_been_updated);
                        }
                        Toast.makeText(getApplicationContext(), message, LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
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
                                initialFragment(new PostFragment());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    public void postPermissionEnable(int image, String reasonId) {
        Toast.makeText(this, "post permission enable..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlockResult(DialogFragment dlg) {
        String blockUserId = "";
        PostItem item = new PostItem();
        item = App.getItem();
        if (!isEmpty(item)) {
            blockUserId = item.getPostUserid();
        }


        Call<String> call = commentService.blockedUser(deviceId, userId, token, blockUserId, userId);
        sendBlockUserRequest(call);
    }

    @Override
    public void onCancelResult(DialogFragment dlg) {

    }

    @Override
    public void onUnfollowClicked(int image, String text) {

    }

    @Override
    public void onReportLikerMessageClicked(int image, String text) {

    }

    @Override
    public void onReportPersonMessageClicked(int image, String text) {

    }

    private String reportId;

    @Override
    public void onButtonClicked(int image, String reasonId) {
        reportId = reasonId;
        Comment_ commentChild = new Comment_();
        commentChild = App.getCommentItem();
        boolean isFollow = App.isIsFollow();
        ReportSendCategorySheet reportSendCategorySheet = ReportSendCategorySheet.newInstance(reportId, commentChild, isFollow);
        reportSendCategorySheet.show(getSupportFragmentManager(), "ReportSendCategorySheet");
    }

    @Override
    public void onFollowClicked(int image, String text) {
        String message = text;

        Comment_ commentChild = new Comment_();
        commentChild = App.getCommentItem();

        FollowSheet followSheet = FollowSheet.newInstance(reportId, commentChild);
        followSheet.show(getSupportFragmentManager(), "FollowSheet");
    }

    @Override
    public void onReportLikerClicked(int image, String text) {
        String message = text;
        Comment_ commentChild = new Comment_();
        commentChild = App.getCommentItem();
        ReportLikerMessageSheet reportLikerMessageSheet = ReportLikerMessageSheet.newInstance(reportId, commentChild);
        reportLikerMessageSheet.show(getSupportFragmentManager(), "ReportLikerMessageSheet");
    }

    @Override
    public void onPersonLikerClicked(int image, String text) {
        String message = text;
        Comment_ commentChild = new Comment_();
        commentChild = null;
        Reply reply = new Reply();
        reply=null;
        ReportPersonMessageSheet reportPersonMessageSheet = ReportPersonMessageSheet.newInstance(reportId, commentChild, reply);
        reportPersonMessageSheet.show(getSupportFragmentManager(), "ReportPersonMessageSheet");
    }
}
