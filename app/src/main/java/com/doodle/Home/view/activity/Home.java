package com.doodle.Home.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.view.activity.LoginAgain;
import com.doodle.Home.adapter.ViewPagerAdapter;
import com.doodle.Home.model.TopContributorStatus;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Home.view.fragment.BreakingPost;
import com.doodle.Home.view.fragment.FollowingPost;
import com.doodle.Home.view.fragment.TabFragment;
import com.doodle.Home.model.Headers;
import com.doodle.Home.model.SetUser;
import com.doodle.Home.view.fragment.TrendingPost;
import com.doodle.Post.view.activity.PostNew;
import com.doodle.R;
import com.doodle.Search.LikerSearch;
import com.doodle.utils.PrefManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cn.jzvd.JZVideoPlayer;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.Socket;


public class Home extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private PrefManager manager;
    private String image_url;
    private String token, deviceId, userId, socketId;
    private boolean isApps = true;
    private Socket socket;
    private static final String TAG = Home.class.getSimpleName();
    private SetUser setUser;
    private TopContributorStatus contributorStatus;
    private Headers headers;
    private String topContributorStatus;

    private CircleImageView imageNewPost, imageNotification, imageFriendRequest, imageStarContributor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        manager = new PrefManager(this);
        setUser = new SetUser();
        headers = new Headers();
        contributorStatus = new TopContributorStatus();
        topContributorStatus = getIntent().getStringExtra("STATUS");


        findViewById(R.id.tvSearchInput).setOnClickListener(this);
        imageNewPost = findViewById(R.id.imageNewPost);
        imageNewPost.setOnClickListener(this);
        imageNotification = findViewById(R.id.imageNotification);
        imageNotification.setOnClickListener(this);
        imageFriendRequest = findViewById(R.id.imageFriendRequest);
        imageFriendRequest.setOnClickListener(this);
        imageStarContributor = findViewById(R.id.imageStarContributor);
        imageStarContributor.setOnClickListener(this);
        profileImage = findViewById(R.id.profile_image);

        setupViewPager();

        setupCollapsingToolbar();
        setupToolbar();

        image_url = manager.getProfileImage();
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();


        if (image_url != null && image_url.length() > 0) {
            Picasso.with(Home.this)
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(profileImage);
        }


//        SocketIOManager ioManager = SocketIOManager.getInstance();
//        ioManager.start();

        socket = new SocketIOManager().getSocketInstance();

        if (topContributorStatus != null) {
            String categoryId = App.getCategoryId();
            headers.setDeviceId(deviceId);
            headers.setIsApps(true);
            headers.setSecurityToken(token);
            contributorStatus.setCategoryId(categoryId);
            contributorStatus.setUserId(userId);
            contributorStatus.setHeaders(headers);
            Gson gson = new Gson();
            String json = gson.toJson(contributorStatus);

            socket.emit("top_contributor", json, new Ack() {
                @Override
                public void call(Object... args) {

                }
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                socketId = App.getSocketId();
                if (socketId != null) {
                    headers.setDeviceId(deviceId);
                    headers.setIsApps(true);
                    headers.setSecurityToken(token);
                    setUser.setSocketId(App.getSocketId());
                    setUser.setUserId(userId);
                    setUser.setHeaders(headers);
                    Gson gson = new Gson();
                    String json = gson.toJson(setUser);
                    socket.emit("set_user", json, new Ack() {
                        @Override
                        public void call(Object... args) {
                            if (args != null) {
                                Log.e(TAG, "Event error: " + args[0].toString());
                            }
                        }
                    });
                    Log.d("SERIALIZATION DATA", json);
                }
            }
        }, 5000);


        /**
         * Either you can place socket.connect() and the corresponding methods here or leave
         * it in the SocketIOManager class. Up to you. For complete persistance, use a service
         */


        /**
         * Either you can place socket.connect() and the corresponding methods here or leave
         * it in the SocketIOManager class. Up to you. For complete persistance, use a service
         */


    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TabbedCoordinatorLayout");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_drop_down);
        toolbar.setOverflowIcon(drawable);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);
        collapsingToolbar.setTitleEnabled(false);
    }



    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Trending");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Breaking");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Following");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TrendingPost(), "Tab 1");
        adapter.addFrag(new BreakingPost(), "Tab 2");
        adapter.addFrag(new FollowingPost(), "Tab 3");

        viewPager.setAdapter(adapter);
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
            case R.id.action_log_out:
                startActivity(new Intent(this, LoginAgain.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvSearchInput:
                startActivity(new Intent(this, LikerSearch.class));
                break;
            case R.id.imageNewPost:
                startActivity(new Intent(this, PostNew.class));
                Toast.makeText(this, "new post", Toast.LENGTH_SHORT).show();
                imageNewPost.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
                imageNewPost.setImageResource(R.drawable.ic_mode_edit_blue_24dp);
                break;
            case R.id.imageNotification:
                startActivity(new Intent(this, PostNew.class));
                imageNotification.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
                imageNotification.setImageResource(R.drawable.ic_notifications_none_blue_24dp);
                break;

            case R.id.imageFriendRequest:
                startActivity(new Intent(this, PostNew.class));
                imageFriendRequest.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
                imageFriendRequest.setImageResource(R.drawable.ic_people_outline_blue_24dp);
                break;

            case R.id.imageStarContributor:
                startActivity(new Intent(this, PostNew.class));
                imageStarContributor.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
                imageStarContributor.setImageResource(R.drawable.ic_star_border_blue_24dp);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        JZVideoPlayer.releaseAllVideos();
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();

    }
}