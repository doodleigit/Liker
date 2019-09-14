package com.doodle.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.doodle.Home.view.CheckedActivity;
import com.doodle.Post.view.activity.PostNew;
import com.doodle.Search.LikerSearch;
import com.doodle.Authentication.view.activity.LoginAgain;
import com.doodle.Home.adapter.MyCategory;
import com.doodle.Home.model.DataItem;
import com.doodle.Home.model.SubCategoryItem;
import com.doodle.R;
import com.doodle.Tool.ConstantManager;
import com.doodle.Tool.PrefManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Liker extends AppCompatActivity implements

        View.OnClickListener,
        TextureView.SurfaceTextureListener {
    private Toolbar toolbar;
    private Spinner spinnerCategoryType, spinnerCategories;
    private String[] categoryType = {"My Categories", "All Categories", "Single Category"};
    private String[] categories = {"Game", "Food", "Drink"};


    private Button btn;
    private ExpandableListView lvCategory;

    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategory adapter;
    AutoCompleteTextView autocomplete;
    private EditText et_search;
    LinearLayout mChipView;
    private TextView tvLink, tvTrendingPost, tvBreakingPost, tvFriendsPost;
    private ImageView imgLink, imgTrendInfo, imgBreakingInfo, imgFriendsInfo;
    private String urlLink = "https://stackoverflow.com/";
    private String urlYoutube = "https://www.youtube.com/";
    private TextureView textureView;
    // Log tag.
    private static final String TAG = Liker.class.getName();

    // Asset video file name.
    private static final String FILE_NAME = "big_buck_bunny.mp4";

    // MediaPlayer instance to control playback of video file.
    private MediaPlayer mMediaPlayer;
    private static final String FILE_URL = "http://www.w3schools.com/html/mov_bbb.mp4";
    private PrefManager manager;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liker);

        toolbar = (Toolbar) findViewById(R.id.main_activiy_bar);
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_drop_down);
        toolbar.setOverflowIcon(drawable);
        manager = new PrefManager(this);
        profileImage = findViewById(R.id.profile_image);
        spinnerCategoryType = (Spinner) findViewById(R.id.spinnerCategoryType);
        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);

        ArrayAdapter<String> adapterCategoryType = new ArrayAdapter<>(this, R.layout.spinner_item, categoryType);
        spinnerCategoryType.setAdapter(adapterCategoryType);

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        spinnerCategories.setAdapter(adapterCategories);
        et_search = findViewById(R.id.et_search);
        mChipView = (LinearLayout) findViewById(R.id.cvTag);

        textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        findViewById(R.id.imgLink).setOnClickListener(this);
        findViewById(R.id.imgYoutube).setOnClickListener(this);

        findViewById(R.id.holdTrending).setOnClickListener(this);
        findViewById(R.id.holdBreaking).setOnClickListener(this);
        findViewById(R.id.holdFriends).setOnClickListener(this);

        //AdvanceSearches

        findViewById(R.id.imgAdvanceSearch).setOnClickListener(this);
        findViewById(R.id.searchHolder).setOnClickListener(this);
        findViewById(R.id.tvHome).setOnClickListener(this);


        findViewById(R.id.contentNewPost).setOnClickListener(this);

        tvTrendingPost = findViewById(R.id.tvTrendingPost);
        tvBreakingPost = findViewById(R.id.tvBreakingPost);
        tvFriendsPost = findViewById(R.id.tvFriendsPost);
        imgTrendInfo = findViewById(R.id.imgTrendInfo);
        imgBreakingInfo = findViewById(R.id.imgBreakingInfo);
        imgFriendsInfo = findViewById(R.id.imgFriendsInfo);
        tvLink = findViewById(R.id.tvLink);
        imgLink = findViewById(R.id.imgLink);
        tvLink.setText(urlLink);

        setupReferences();
        //     setupVideoView();

        spinnerCategoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                String s1 = String.valueOf(spinnerCategoryType.getSelectedItem());

                if (s1.contentEquals("My Categories")) {
                    et_search.setEnabled(true);
                    et_search.setVisibility(View.VISIBLE);
                    spinnerCategories.setVisibility(View.GONE);
                    lvCategory.setVisibility(View.VISIBLE);

                } else if (s1.contentEquals("All Categories")) {
                    et_search.setVisibility(View.VISIBLE);
                    et_search.setEnabled(true);
                    spinnerCategories.setVisibility(View.GONE);
                    lvCategory.setVisibility(View.VISIBLE);

                } else {
                    spinnerCategories.setVisibility(View.VISIBLE);
                    et_search.setVisibility(View.GONE);
                    lvCategory.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


//        btn = findViewById(R.id.btn);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,CheckedActivity.class);
//                startActivity(intent);
//            }
//        });


        String image_url = manager.getProfileImage();

        if (image_url != null && image_url.length() > 0) {
            Picasso.with(Liker.this)
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(profileImage);

        }

    }


    private void setupReferences() {

        lvCategory = findViewById(R.id.lvCategory);
        arCategory = new ArrayList<>();
        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Adventure");

        arSubCategory = new ArrayList<>();
        for (int i = 1; i < 6; i++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Adventure: " + i);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Art");
        arSubCategory = new ArrayList<>();
        for (int j = 1; j < 6; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Art: " + j);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Cooking");
        arSubCategory = new ArrayList<>();
        for (int k = 1; k < 6; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Cooking: " + k);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);


        for (DataItem data : arCategory) {
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID, data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());

            int countIsChecked = 0;
            for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID, subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID, subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());

                if (subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if (countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        adapter = new MyCategory(this, parentItems, childItems, false);
        lvCategory.setAdapter(adapter);
        //Adding 2 TextViews
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 80);
        lp.setMargins(10, 0, 10, 0);
        for (int i = 1; i <= 2; i++) {
            TextView textView = new TextView(this);

            textView.setText("TextView " + String.valueOf(i));
            textView.setBackground(getResources().getDrawable(R.drawable.drwble_chip));
            textView.setPadding(10, 10, 10, 10);
            textView.setCompoundDrawablePadding(20);

            textView.setTextColor(Color.parseColor("#8596A3"));
            // textView.setBackgroundColor(Color.parseColor("#1387D0"));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_chip, 0);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER);

            mChipView.addView(textView);
        }

        //  chipView.add(lp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.holdTrending:
                tvTrendingPost.setTextColor(Color.parseColor("#1388D1"));
                imgTrendInfo.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));

                tvBreakingPost.setTextColor(Color.parseColor("#8596A3"));
                imgBreakingInfo.setColorFilter(ContextCompat.getColor(this, R.color.colordefault));

                tvFriendsPost.setTextColor(Color.parseColor("#8596A3"));
                imgFriendsInfo.setColorFilter(ContextCompat.getColor(this, R.color.colordefault));

                startActivity(new Intent(this, CheckedActivity.class));

                break;
            case R.id.holdBreaking:
                tvBreakingPost.setTextColor(Color.parseColor("#1388D1"));
                imgBreakingInfo.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));

                tvFriendsPost.setTextColor(Color.parseColor("#8596A3"));
                imgFriendsInfo.setColorFilter(ContextCompat.getColor(this, R.color.colordefault));

                tvTrendingPost.setTextColor(Color.parseColor("#8596A3"));
                imgTrendInfo.setColorFilter(ContextCompat.getColor(this, R.color.colordefault));
                break;
            case R.id.holdFriends:
                tvFriendsPost.setTextColor(Color.parseColor("#1388D1"));
                imgFriendsInfo.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));

                tvTrendingPost.setTextColor(Color.parseColor("#8596A3"));
                imgTrendInfo.setColorFilter(ContextCompat.getColor(this, R.color.colordefault));
                tvBreakingPost.setTextColor(Color.parseColor("#8596A3"));
                imgBreakingInfo.setColorFilter(ContextCompat.getColor(this, R.color.colordefault));
                break;

            case R.id.imgLink:
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLink));
                startActivity(browserIntent);
                break;
            case R.id.imgYoutube:
                if (!urlYoutube.startsWith("http://") && !urlYoutube.startsWith("https://"))
                    urlYoutube = "http://" + urlYoutube;
                Intent browserIntents = new Intent(Intent.ACTION_VIEW, Uri.parse(urlYoutube));
                startActivity(browserIntents);
                break;

//            case R.id.imgAdvanceSearch:
//
//                //   provide2CustomContactDialog();
//                startActivity(new Intent(this, AdvanceSearches.class));
//                break;
            case R.id.searchHolder:

                //   provide2CustomContactDialog();
                startActivity(new Intent(this, LikerSearch.class));
                break;
            case R.id.tvHome:
                break;
            case R.id.contentNewPost:
                startActivity(new Intent(this, PostNew.class));
                break;
        }
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {

        Surface surface = new Surface(surfaceTexture);
        try {
            // AssetFileDescriptor afd = getAssets().openFd(FILE_NAME);
            mMediaPlayer = new MediaPlayer();
            //  mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setDataSource(FILE_URL);
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IllegalArgumentException e) {
            Log.d(TAG, e.getMessage());
        } catch (SecurityException e) {
            Log.d(TAG, e.getMessage());
        } catch (IllegalStateException e) {
            Log.d(TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


}
