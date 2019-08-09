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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Post.service.PostService;
import com.doodle.Profile.adapter.ViewPagerAdapter;
import com.doodle.Profile.model.UserAllInfo;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.Search.LikerSearch;
import com.doodle.utils.AppConstants;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
//    private ViewPager viewPager;
    private Toolbar toolbar;
    LinearLayout searchLayout;
    private RelativeLayout coverImageLayout, profileImageLayout;
    private ImageView ivCoverImage, ivProfileImage, ivChangeCoverImage, ivChangeProfileImage;
    private TextView tvUserName, tvTotalInfoCount;

    private ProfileService profileService;
    private ProgressDialog progressDialog;

    private PrefManager manager;
    private UserAllInfo userAllInfo;
    private Uri imageUri;
    private String profileUserId;
    private final int REQUEST_TAKE_CAMERA = 101;
    private final int REQUEST_TAKE_GALLERY_IMAGE = 102;
    private int uploadContentType = 0;
    private String deviceId, profileId, token, userName, fullName, profileImage, coverImage, allCountInfo;

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
        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userName = manager.getUserName();

        toolbar = findViewById(R.id.toolbar);
        searchLayout = findViewById(R.id.search_layout);
        coverImageLayout = findViewById(R.id.cover_image_layout);
        profileImageLayout = findViewById(R.id.profile_image_layout);
        ivCoverImage = findViewById(R.id.cover_image);
        ivProfileImage = findViewById(R.id.profile_image);
        ivChangeCoverImage = findViewById(R.id.change_cover_image);
        ivChangeProfileImage = findViewById(R.id.change_profile_image);
        tvUserName = findViewById(R.id.user_name);
        tvTotalInfoCount = findViewById(R.id.total_info_count);
        tabLayout = findViewById(R.id.tabs);
//        viewPager = findViewById(R.id.viewpager);

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, LikerSearch.class));
            }
        });

        coverImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadContentType = 1;
                selectImageSource(ivChangeCoverImage);
            }
        });

        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadContentType = 0;
                selectImageSource(ivChangeProfileImage);
//                Crop.of(inputUri, outputUri).asSquare().start(this);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        Call<UserAllInfo> call = profileService.getUserInfo(deviceId, profileId, token, profileId, userName, true);
        getUserInfo(call);
    }

    private void setData() {
        fullName = userAllInfo.getFirstName() + " " + userAllInfo.getLastName();
        profileImage = AppConstants.USER_UPLOADED_IMAGES + userAllInfo.getPhoto();
        coverImage = AppConstants.USER_UPLOADED_IMAGES + userAllInfo.getCoverImage();
        allCountInfo = userAllInfo.getTotalLikes() + " Likes " + userAllInfo.getTotalFollowers() + " Followers " + userAllInfo.getGoldStars() + " Stars";
        tvUserName.setText(fullName);
        tvTotalInfoCount.setText(allCountInfo);
        loadProfileImage();
        loadCoverImage();
    }

    private void loadProfileImage() {
        Glide.with(App.getAppContext())
                .load(profileImage)
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
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.container, fragment).commit();
    }

    private void selectImageSource(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.image_source_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.select_profile_picture:
                        checkGalleryPermission();
                        return true;
                    case R.id.capture_profile_picture:
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
        String path = Utils.getPath(this, imageUri);
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        Call<String> mediaCall;
        if (uploadContentType == 0) {
            mediaCall = profileService.uploadProfileImage(deviceId, profileId, token, fileToUpload);
        } else {
            mediaCall = profileService.uploadCoverImage(deviceId, profileId, token, fileToUpload);
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
        if (requestCode == Crop.REQUEST_CROP) {
            uploadImage();
        }
        if (requestCode == REQUEST_TAKE_GALLERY_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    getSelectedImagesPath(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Cancel Gallery", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_TAKE_CAMERA) {
            if (resultCode == RESULT_OK) {
                cropImage(imageUri);
            } else {
                Toast.makeText(this, "Cancel Camera Capture", Toast.LENGTH_SHORT).show();
            }
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
            imageUri = uri;
            cropImage(uri);
        } else {
            Uri uri = data.getData();
            imageUri = uri;
            cropImage(uri);
        }
    }

    private void cropImage(Uri uri) {
        if (uploadContentType == 0) {
            Crop.of(uri, imageUri).asSquare().start(this);
        } else {
            Crop.of(uri, imageUri).withAspect(5, 1).start(this);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new PostFragment(), "Post");
        adapter.addFrag(new AboutFragment(), "About");
        adapter.addFrag(new FriendsFragment(), "Friends");
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
        tabLayout.addTab(tabLayout.newTab().setText("Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Stars"));
        initialFragment(new PostFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    initialFragment(new PostFragment());
                } else if (tab.getPosition() == 1) {
                    initialFragment(new AboutFragment());
                } else if (tab.getPosition() == 2) {
                    initialFragment(new FriendsFragment());
                } else if (tab.getPosition() == 3) {
                    initialFragment(new PhotosFragment());
                } else if (tab.getPosition() == 4) {
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

    private void getUserInfo(Call<UserAllInfo> call) {
        call.enqueue(new Callback<UserAllInfo>() {
            @Override
            public void onResponse(Call<UserAllInfo> call, Response<UserAllInfo> response) {
                userAllInfo = response.body();
                setData();
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<UserAllInfo> call, Throwable t) {
                progressDialog.hide();
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
                    String message = object.getString("message");
                    if (status) {
                        String image = object.getString("image");
                        if (uploadContentType == 0) {
                            profileImage = image;
                            loadProfileImage();
                        } else {
                            coverImage = image;
                            loadCoverImage();
                        }
                        Toast.makeText(getApplicationContext(), message, LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong.", LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong.", LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
    }



}
