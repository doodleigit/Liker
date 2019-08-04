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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.Post.service.PostService;
import com.doodle.Profile.adapter.ViewPagerAdapter;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.soundcloud.android.crop.Crop;

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

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private RelativeLayout coverImageLayout, profileImageLayout;
    private ImageView ivCoverImage, ivProfileImage, ivChangeCoverImage, ivChangeProfileImage;

    private ProfileService profileService;
    private ProgressDialog progressDialog;

    private PrefManager manager;
    private Uri imageUri;
    private String profileUserId;
    private final int REQUEST_TAKE_CAMERA = 101;
    private final int REQUEST_TAKE_GALLERY_IMAGE = 102;
    private int uploadContentType = 0;
    private String deviceId, profileId, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialComponent();

        setupViewPager();

    }

    private void initialComponent() {
        profileUserId = getIntent().getStringExtra("user_id");
        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();

        toolbar = findViewById(R.id.toolbar);
        coverImageLayout = findViewById(R.id.cover_image_layout);
        profileImageLayout = findViewById(R.id.profile_image_layout);
        ivCoverImage = findViewById(R.id.cover_image);
        ivProfileImage = findViewById(R.id.profile_image);
        ivChangeCoverImage = findViewById(R.id.change_cover_image);
        ivChangeProfileImage = findViewById(R.id.change_profile_image);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.uploading));

        coverImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageSource(ivChangeCoverImage);
            }
        });

        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void initialFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", profileUserId);
        ProfileFragment profileFragment = new ProfileFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        profileFragment.setArguments(bundle);
        transaction.replace(R.id.container, profileFragment).commit();
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
        Call<String> mediaCall = profileService.updateProfileImage(deviceId, profileId, token, fileToUpload);
//        sendImageRequest(mediaCall);
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
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
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
                Crop.of(imageUri, imageUri).asSquare().start(this);
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
            Crop.of(uri, imageUri).asSquare().start(this);
        } else {
            Uri uri = data.getData();
            Crop.of(uri, imageUri).asSquare().start(this);
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

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Posts");
        tabLayout.getTabAt(1).setText("About");
        tabLayout.getTabAt(2).setText("Friends");
        tabLayout.getTabAt(3).setText("Photos");
        tabLayout.getTabAt(4).setText("Stars");
    }

}
