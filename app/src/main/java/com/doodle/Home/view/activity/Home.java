package com.doodle.Home.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.model.LoginInfo;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Authentication.view.activity.LoginAgain;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.service.CommentService;
import com.doodle.Comment.view.fragment.BlockUserDialog;
import com.doodle.Comment.view.fragment.FollowSheet;
import com.doodle.Comment.view.fragment.ReportLikerMessageSheet;
import com.doodle.Comment.view.fragment.ReportPersonMessageSheet;
import com.doodle.Comment.view.fragment.ReportReasonSheet;
import com.doodle.Comment.view.fragment.ReportSendCategorySheet;
import com.doodle.Home.adapter.CategoryTitleAdapter;
import com.doodle.Home.adapter.SubCategoryAdapter;
import com.doodle.Home.adapter.ViewPagerAdapter;
import com.doodle.Home.model.CommonCategory;
import com.doodle.Home.model.FilterList;
import com.doodle.Home.model.PostFilterCategory;
import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.model.PostFilters;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.SinglePostFilters;
import com.doodle.Home.model.TopContributorStatus;
import com.doodle.Home.service.CategoryExpandListener;
import com.doodle.Home.service.CategoryRemoveListener;
import com.doodle.Home.service.FilterClickListener;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.LoadCompleteListener;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Home.view.fragment.BreakingPost;
import com.doodle.Home.view.fragment.FollowingPost;
import com.doodle.Home.model.Headers;
import com.doodle.Home.model.SetUser;
import com.doodle.Home.view.fragment.PostPermissionSheet;
import com.doodle.Home.view.fragment.TrendingPost;
import com.doodle.Message.view.MessageActivity;
import com.doodle.Notification.view.NotificationActivity;
import com.doodle.Post.view.activity.PostNew;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Search.LikerSearch;
import com.doodle.Setting.view.SettingActivity;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Service.DataFetchingService;
import com.doodle.Tool.Tools;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.jzvd.JZVideoPlayer;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;
import static com.doodle.Tool.Tools.isEmpty;

public class Home extends AppCompatActivity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        ReportReasonSheet.BottomSheetListener,
        ReportSendCategorySheet.BottomSheetListener,
        ReportPersonMessageSheet.BottomSheetListener,
        ReportLikerMessageSheet.BottomSheetListener,
        FollowSheet.BottomSheetListener,
        BlockUserDialog.BlockListener,
        PostPermissionSheet.BottomSheetListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView mainNavigationView, navigationView, footerNavigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    public CollapsingToolbarLayout collapsingToolbar;
    public AppBarLayout appBarLayout;
    private CircleImageView profileImage, navProfileImage;
    private Spinner categorySpinner;
    private ProgressDialog progressDialog;
    private PrefManager manager;
    private String image_url;
    private String token, deviceId, userId, userName, profileName, selectedCategory = "";
    int categoryPosition = 0;
    private Socket socket, mSocket;
    private HomeService webService;
    private static final String TAG = Home.class.getSimpleName();
    private SetUser setUser;
    private TopContributorStatus contributorStatus;
    private Headers headers;
    private String topContributorStatus;
    private ArrayList<PostFilterCategory> categories;
    private ArrayList<PostFilterSubCategory> subCategories, multipleSubCategories, exceptMultipleSubCategories;
    private ArrayList<CommonCategory> commonCategories;
    private CategoryTitleAdapter categoryTitleAdapter;

    private ImageView navClose, imageNewPost, imageNotification, imageFriendRequest, imageStarContributor, spinnerDropDown;
    private TextView tvHome, navUserName, navLogout, newNotificationCount, newMessageNotificationCount, filterItem;
    private RecyclerView categoryRecyclerView;

    public LoadCompleteListener loadCompleteListener;
    public UserInfo userInfo;
    private boolean isFriend;
    private CommentService commentService;
    private boolean networkOk, isCatSelectFromPost;
    private String profileId;
    private String blockUserId;
    boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(Home.this, DataFetchingService.class));

        initialComponent();

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

        setData();
        sendCategoryListRequest();

    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        webService = HomeService.mRetrofit.create(HomeService.class);
        commentService = CommentService.mRetrofit.create(CommentService.class);
        setUser = new SetUser();
        headers = new Headers();
        categories = new ArrayList<>();
        subCategories = new ArrayList<>();
        multipleSubCategories = new ArrayList<>();
        exceptMultipleSubCategories = new ArrayList<>();
        commonCategories = new ArrayList<>();
        contributorStatus = new TopContributorStatus();
        topContributorStatus = getIntent().getStringExtra("STATUS");

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        manager.setDeviceWidth(metrics.widthPixels);

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NEW_NOTIFICATION_BROADCAST);
        registerReceiver(broadcastReceiver, filter);

        IntentFilter catFilter = new IntentFilter();
        catFilter.addAction(AppConstants.POST_FILTER_CAT_BROADCAST);
        registerReceiver(filterBroadcast, catFilter);

        IntentFilter newPostFilter = new IntentFilter();
        newPostFilter.addAction(AppConstants.NEW_POST_ADD_BROADCAST);
        registerReceiver(newPostBroadcastReceiver, newPostFilter);

        findViewById(R.id.tvSearchInput).setOnClickListener(this);
        drawer = findViewById(R.id.drawer_layout);
        mainNavigationView = findViewById(R.id.main_nav_view);
        navigationView = findViewById(R.id.nav_view);
        footerNavigationView = findViewById(R.id.footer_nav_view);
        mainNavigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        tvHome = findViewById(R.id.tvHome);
        navProfileImage = navigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        navUserName = navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        navLogout = footerNavigationView.getHeaderView(0).findViewById(R.id.nav_log_out);
        navClose = navigationView.getHeaderView(0).findViewById(R.id.nav_close);
        imageNewPost = findViewById(R.id.imageNewPost);
        imageNewPost.setOnClickListener(this);
        imageNotification = findViewById(R.id.imageNotification);
        imageNotification.setOnClickListener(this);
        imageFriendRequest = findViewById(R.id.imageFriendRequest);
        imageFriendRequest.setOnClickListener(this);
        imageStarContributor = findViewById(R.id.imageStarContributor);
        imageStarContributor.setOnClickListener(this);
        spinnerDropDown = findViewById(R.id.spinner_drop_down);
        profileImage = findViewById(R.id.profile_image);
        categorySpinner = findViewById(R.id.spinnerCategoryType);
        categorySpinner.setOnItemSelectedListener(this);
        newNotificationCount = findViewById(R.id.newNotificationCount);
        newMessageNotificationCount = findViewById(R.id.newMessageNotificationCount);
        filterItem = findViewById(R.id.filterItem);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        setupViewPager();

        setupCollapsingToolbar();
        setupToolbar();

        image_url = manager.getProfileImage();
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        userName = manager.getUserName();
        profileName = manager.getProfileName();
        profileId = manager.getProfileId();
        token = manager.getToken();

        if (image_url != null && image_url.length() > 0) {
            Picasso.with(Home.this)
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(profileImage);
            Picasso.with(Home.this)
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(navProfileImage);
        }
        if (profileName != null && profileName.length() > 0) {
            navUserName.setText(profileName);
        }

        socket = SocketIOManager.wSocket;
        mSocket = SocketIOManager.mSocket;

        filterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryPosition == 2) {
                    showSingleFilterDialog();
                } else {
                    showMultipleFilterDialog();
                }
            }
        });

        loadCompleteListener = new LoadCompleteListener() {
            @Override
            public void onLoadInitial() {
                showProgressBar(getString(R.string.loading));
            }

            @Override
            public void onLoadComplete(int position) {
                if (viewPager.getCurrentItem() == position) {
                    hideProgressBar();
                    if (manager.getPostCategoryIntro().equals("0")) {
                        manager.setPostCategoryIntro("1");
                        showIntroTooltip();
                    }
                }
            }
        };

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ProfileActivity.class).putExtra("user_id", userId).putExtra("user_name", userName));
            }
        });

        navClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(Gravity.END);
            }
        });

        spinnerDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySpinner.performClick();
            }
        });

        navLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LoginInfo loginInfo = new LoginInfo(manager.getUserInfo(), manager.getToken(), manager.getProfileName(), manager.getProfileImage(), manager.getProfileId(), manager.getUserName(), manager.getDeviceId());
//                Intent loginAgain = new Intent(Home.this, LoginAgain.class);
//                loginAgain.putExtra("login_info", loginInfo);
//                manager.pref.edit().clear().apply();
//                stopService(new Intent(Home.this, DataFetchingService.class));
//                startActivity(loginAgain);
//                finish();

                Call<String> call = webService.setLogout(deviceId, token, userId, userId);
                sendLogoutRequest(call);
            }
        });

    }

    private void setData() {
        int newNotificationCount = manager.getNotificationCount();
        setNotificationCount(newNotificationCount);
        int newMessageCount = manager.getMessageNotificationCount();
        setMessageNotificationCount(newMessageCount);
        categories.add(new PostFilterCategory("1", "All Categories", new ArrayList<>()));
        categories.add(new PostFilterCategory("2", "My Favorites", new ArrayList<>()));
        categories.add(new PostFilterCategory("3", "Single Category", new ArrayList<>()));
        categories.add(new PostFilterCategory("4", "Everything Except", new ArrayList<>()));
        categoryFilter();
    }

    private void sendCategoryListRequest() {
        Call<String> call = webService.sendFilterCategories(deviceId, userId, token, userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            JSONArray jsonArray = object.getJSONArray("categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String subCatId = jsonObject.getString("category_id");
                                String subCatName = jsonObject.getString("category_name");
                                ArrayList<PostFilterItem> singleArrayList = new ArrayList<>();
                                ArrayList<PostFilterItem> multipleArrayList = new ArrayList<>();
                                JSONArray array = jsonObject.getJSONArray("subcatg");
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject obj = array.getJSONObject(j);
                                    String itemId = obj.getString("sub_category_id");
                                    String itemName = obj.getString("sub_category_name");
                                    singleArrayList.add(new PostFilterItem("", subCatId, itemId, itemName, false));
                                    multipleArrayList.add(new PostFilterItem("", subCatId, itemId, itemName, false));
                                }
                                subCategories.add(new PostFilterSubCategory("", subCatId, subCatName, false, singleArrayList));
                                multipleSubCategories.add(new PostFilterSubCategory("", subCatId, subCatName, false, multipleArrayList));
                                exceptMultipleSubCategories.add(new PostFilterSubCategory("", subCatId, subCatName, false, multipleArrayList));
                            }
                            if (subCategories.size() > 0) {
                                subCategories.remove(0);
                            }
                            if (multipleSubCategories.size() > 0) {
                                multipleSubCategories.remove(0);
                            }
                            if (exceptMultipleSubCategories.size() > 0) {
                                exceptMultipleSubCategories.remove(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void categoryFilter() {
        CategoryRemoveListener categoryRemoveListener = new CategoryRemoveListener() {
            @Override
            public void onCategoryRemove(CommonCategory commonCategory) {
                if (categoryPosition == 1) {
                    for (int i = 0; i < multipleSubCategories.size(); i++) {
                        if (multipleSubCategories.get(i).getSubCatId().equals(commonCategory.getCatId())) {
                            multipleSubCategories.get(i).setSelectedAll(false);
                            break;
                        }
                        for (int j = 0; j < multipleSubCategories.get(i).getPostFilterItems().size(); j++) {
                            if (multipleSubCategories.get(i).getPostFilterItems().get(j).getItemId().equals(commonCategory.getCatId())) {
                                multipleSubCategories.get(i).getPostFilterItems().get(j).setSelected(false);
                                break;
                            }
                        }
                    }
                    filterSubCategory(multipleSubCategories, "1", true);
                } else {
                    for (int i = 0; i < exceptMultipleSubCategories.size(); i++) {
                        if (exceptMultipleSubCategories.get(i).getSubCatId().equals(commonCategory.getCatId())) {
                            exceptMultipleSubCategories.get(i).setSelectedAll(false);
                            break;
                        }
                        for (int j = 0; j < exceptMultipleSubCategories.get(i).getPostFilterItems().size(); j++) {
                            if (exceptMultipleSubCategories.get(i).getPostFilterItems().get(j).getItemId().equals(commonCategory.getCatId())) {
                                exceptMultipleSubCategories.get(i).getPostFilterItems().get(j).setSelected(false);
                                break;
                            }
                        }
                    }
                    filterSubCategory(exceptMultipleSubCategories, "8", true);
                }
            }

            @Override
            public void onCategorySelect(CommonCategory commonCategory) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(commonCategory.getCatId());
                sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
            }

            @Override
            public void onCategoryDeSelect() {
                ArrayList<String> arrayList = new ArrayList<>();
                for (CommonCategory commonCategory : commonCategories) {
                    arrayList.add(commonCategory.getCatId());
                }
                sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
            }

            @Override
            public void onCategorySelectChange(CommonCategory oldCommonCategory, CommonCategory newCommonCategory) {

            }
        };

        ArrayList<String> arrayList = new ArrayList<>();
        for (PostFilterCategory postFilterCategory : categories) {
            arrayList.add(postFilterCategory.getCatName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(dataAdapter);

        categoryTitleAdapter = new CategoryTitleAdapter(this, commonCategories, categoryRemoveListener);
        categoryRecyclerView.setAdapter(categoryTitleAdapter);

//        showFilterDialog();
    }

    private void updateCategoryTitles() {
        if (categoryPosition == 3) {
            categoryTitleAdapter.setSelectable(false);
        } else {
            categoryTitleAdapter.setSelectable(true);
        }
        categoryTitleAdapter.notifyDataSetChanged();
    }

    BroadcastReceiver filterBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isCatSelectFromPost = true;
            appBarLayout.setExpanded(true);
            String catId = intent.getStringExtra("category_id");
//            ArrayList<String> arrayList = new ArrayList<>();
//            arrayList.add(postFilterSubCategory.getSubCatId());
//            for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
//                arrayList.add(postFilterItem.getItemId());
//            }
            addedFilters(catId, "7", false);
//            categorySpinner.setSelection(2);
//            for (int i = 0; i < subCategories.size(); i++) {
//                subCategories.get(i).setSelectedAll(false);
//                for (int j = 0; j < subCategories.get(i).getPostFilterItems().size(); j++) {
//                    subCategories.get(i).getPostFilterItems().get(j).setSelected(false);
//                }
//            }
//            for (PostFilterSubCategory subCategory : subCategories) {
//                if (subCategory.getSubCatId().equals(catId)) {
//                    subCategory.setSelectedAll(true);
//
//                    String categoryId, subCatId, subCatName;
//                    boolean isSelectedAll;
//                    categoryId = subCategory.getCatId();
//                    subCatId = subCategory.getSubCatId();
//                    subCatName= subCategory.getSubCatName();
//                    isSelectedAll = subCategory.isSelectedAll();
//                    ArrayList<PostFilterItem> postFilterItems = new ArrayList<>();
//                    for (PostFilterItem postFilterItem :subCategory.getPostFilterItems()) {
//                        postFilterItems.add(postFilterItem);
//                    }
//                    PostFilterSubCategory postFilterSubCategory = new PostFilterSubCategory(categoryId, subCatId, subCatName, isSelectedAll, postFilterItems);
//                    categories.get(categoryPosition).getPostFilterSubCategories().clear();
//                    categories.get(categoryPosition).getPostFilterSubCategories().add(postFilterSubCategory);
//                    for (int i = 0; i < subCategory.getPostFilterItems().size(); i++) {
//                        postFilterSubCategory.getPostFilterItems().get(i).setSelected(true);
//                    }
//                    ArrayList<String> arrayList = new ArrayList<>();
//                    commonCategories.clear();
//
//                    for (PostFilterSubCategory filterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
//                        if (filterSubCategory.isSelectedAll()) {
//                            arrayList.add(filterSubCategory.getSubCatId());
//                            commonCategories.add(new CommonCategory(filterSubCategory.getSubCatId(), filterSubCategory.getSubCatName()));
//                        }
//                        for (PostFilterItem postFilterItem : filterSubCategory.getPostFilterItems()) {
//                            arrayList.add(postFilterItem.getItemId());
//                            commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
//                        }
//                    }
//                    updateCategoryTitles();
//
//                    selectedCategory = postFilterSubCategory.getSubCatName();
//                    filterItem.setText(selectedCategory);
//                    categorySpinner.setSelection(2);
//                    sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
//                    break;
//                } else {
//                    for (PostFilterItem postFilterItem : subCategory.getPostFilterItems()) {
//                        if (postFilterItem.getItemId().equals(catId)) {
////                            selectChangeListener.onSelectClear();
//                            postFilterItem.setSelected(true);
//                            ArrayList<PostFilterItem> postFilterItems = new ArrayList<>();
//                            String categoryId, subCategoryId, itemId, itemName;
//                            boolean isSelected;
//                            categoryId = "";
//                            subCategoryId =postFilterItem.getSubCatId();
//                            itemId = postFilterItem.getItemId();
//                            itemName = postFilterItem.getItemName();
//                            isSelected = postFilterItem.isSelected();
//                            PostFilterItem item = new PostFilterItem(categoryId, subCategoryId, itemId, itemName, isSelected);
//                            postFilterItems.add(item);
//
//                            PostFilterSubCategory postFilterSubCategory = new PostFilterSubCategory(subCategory.getCatId(), subCategory.getSubCatId(), subCategory.getSubCatName(), subCategory.isSelectedAll(), postFilterItems);
//                            categories.get(categoryPosition).getPostFilterSubCategories().clear();
//                            categories.get(categoryPosition).getPostFilterSubCategories().add(postFilterSubCategory);
//                            ArrayList<String> arrayList = new ArrayList<>();
//                            commonCategories.clear();
//                            for (PostFilterSubCategory filterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
//                                for (PostFilterItem filterItem : filterSubCategory.getPostFilterItems()) {
//                                    arrayList.add(filterItem.getItemId());
//                                    commonCategories.add(new CommonCategory(filterItem.getItemId(), filterItem.getItemName()));
//                                }
//                            }
//                            updateCategoryTitles();
//
//                            selectedCategory = itemName;
//                            filterItem.setText(selectedCategory);
//                            categorySpinner.setSelection(2);
//                            sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
//                            break;
//                        }
//                    }
//                }
//            }
        }
    };

    private void showSingleFilterDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.single_post_category_filter_layout);

        ArrayList<PostFilterSubCategory> filterSubCategories = new ArrayList<>();

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        EditText etSearchCategory;
        RecyclerView recyclerView;
        LinearLayoutManager linearLayoutManager;
        etSearchCategory = dialog.findViewById(R.id.search_category);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FilterClickListener filterClickListener = new FilterClickListener() {
            @Override
            public void onSingleSubCategorySelect(PostFilterSubCategory postFilterSubCategory) {
                dialog.dismiss();
                filterSubCategory(subCategories, "7", false);
            }

            @Override
            public void onSingleSubCategoryDeselect(PostFilterSubCategory postFilterSubCategory) {

            }

            @Override
            public void onSingleFilterItemSelect(PostFilterSubCategory postFilterSubCategory) {
                dialog.dismiss();
                filterSubCategory(subCategories, "7", false);
            }

            @Override
            public void onSingleFilterItemDeselect(PostFilterSubCategory postFilterSubCategory) {

            }
        };

        filterSubCategories.addAll(subCategories);

        CategoryExpandListener categoryExpandListener = new CategoryExpandListener() {
            @Override
            public void onExpand(View view) {
                int itemToScroll = recyclerView.getChildLayoutPosition(view);
                int centerOfScreen = recyclerView.getHeight() / 2 - view.getHeight() / 2;
                linearLayoutManager.scrollToPositionWithOffset(itemToScroll, centerOfScreen);
            }
        };

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(this, filterSubCategories, subCategories, filterClickListener, categoryExpandListener, 0);
        recyclerView.setAdapter(subCategoryAdapter);

        etSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = etSearchCategory.getText().toString();
                filterSubCategories.clear();
                if (key.isEmpty()) {
                    filterSubCategories.addAll(subCategories);
                    subCategoryAdapter.setSearchParam(false);
                } else {
                    for (PostFilterSubCategory category : subCategories) {
                        ArrayList<PostFilterItem> arrayList = new ArrayList<>();
                        PostFilterSubCategory postFilterSubCategory = new PostFilterSubCategory();
                        for (PostFilterItem postFilterItem : category.getPostFilterItems()) {
                            if (postFilterItem.getItemName().toLowerCase().contains(key.toLowerCase())) {
                                arrayList.add(postFilterItem);
                            }
                        }
                        postFilterSubCategory.setCatId(category.getCatId());
                        postFilterSubCategory.setSubCatId(category.getSubCatId());
                        postFilterSubCategory.setSubCatName(category.getSubCatName());
                        postFilterSubCategory.setSelectedAll(category.isSelectedAll());
                        postFilterSubCategory.setPostFilterItems(arrayList);

                        if (postFilterSubCategory.getSubCatName().toLowerCase().contains(key.toLowerCase())) {
                            filterSubCategories.add(postFilterSubCategory);
                        } else {
                            if (arrayList.size() != 0) {
                                filterSubCategories.add(postFilterSubCategory);
                            }
                        }
                    }
                    subCategoryAdapter.setSearchParam(true);
                }
                subCategoryAdapter.notifyDataSetChanged();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showMultipleFilterDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.multiple_post_category_filter_layout);

        ArrayList<PostFilterSubCategory> filterSubCategories = new ArrayList<>();
        SubCategoryAdapter subCategoryAdapter;

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        EditText etSearchCategory;
        FloatingActionButton done, clear;
        RecyclerView recyclerView;
        LinearLayoutManager linearLayoutManager;
        done = dialog.findViewById(R.id.done);
        clear = dialog.findViewById(R.id.clear);
        etSearchCategory = dialog.findViewById(R.id.search_category);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FilterClickListener filterClickListener = new FilterClickListener() {
            @Override
            public void onSingleSubCategorySelect(PostFilterSubCategory postFilterSubCategory) {
                if (categoryPosition == 3) {
                    filterSubCategory(exceptMultipleSubCategories, "8", false);
                } else {
                    filterSubCategory(multipleSubCategories, "1", false);
                }
            }

            @Override
            public void onSingleSubCategoryDeselect(PostFilterSubCategory postFilterSubCategory) {
                if (categoryPosition == 3) {
                    filterSubCategory(exceptMultipleSubCategories, "8", false);
                } else {
                    filterSubCategory(multipleSubCategories, "1", false);
                }
            }

            @Override
            public void onSingleFilterItemSelect(PostFilterSubCategory postFilterSubCategory) {
                if (categoryPosition == 3) {
                    filterSubCategory(exceptMultipleSubCategories, "8", false);
                } else {
                    filterSubCategory(multipleSubCategories, "1", false);
                }
            }

            @Override
            public void onSingleFilterItemDeselect(PostFilterSubCategory postFilterSubCategory) {
                if (categoryPosition == 3) {
                    filterSubCategory(exceptMultipleSubCategories, "8", false);
                } else {
                    filterSubCategory(multipleSubCategories, "1", false);
                }
            }
        };

        CategoryExpandListener categoryExpandListener = new CategoryExpandListener() {
            @Override
            public void onExpand(View view) {
                int itemToScroll = recyclerView.getChildLayoutPosition(view);
                int centerOfScreen = recyclerView.getHeight() / 2 - view.getHeight() / 2;
                linearLayoutManager.scrollToPositionWithOffset(itemToScroll, centerOfScreen);
            }
        };

        if (categoryPosition == 3) {
            filterSubCategories.addAll(exceptMultipleSubCategories);
            subCategoryAdapter = new SubCategoryAdapter(this, filterSubCategories, new ArrayList<>(), filterClickListener, categoryExpandListener, 1);
            recyclerView.setAdapter(subCategoryAdapter);
        } else {
            filterSubCategories.addAll(multipleSubCategories);
            subCategoryAdapter = new SubCategoryAdapter(this, filterSubCategories, new ArrayList<>(), filterClickListener, categoryExpandListener, 1);
            recyclerView.setAdapter(subCategoryAdapter);
        }

        etSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = etSearchCategory.getText().toString();
                filterSubCategories.clear();
                if (key.isEmpty()) {
                    if (categoryPosition == 3) {
                        filterSubCategories.addAll(exceptMultipleSubCategories);
                    } else {
                        filterSubCategories.addAll(multipleSubCategories);
                    }
                    subCategoryAdapter.setSearchParam(false);
                } else {
                    for (PostFilterSubCategory category : (categoryPosition == 3 ? exceptMultipleSubCategories : multipleSubCategories)) {
                        ArrayList<PostFilterItem> arrayList = new ArrayList<>();
                        PostFilterSubCategory postFilterSubCategory = new PostFilterSubCategory();
                        for (PostFilterItem postFilterItem : category.getPostFilterItems()) {
                            if (postFilterItem.getItemName().toLowerCase().contains(key.toLowerCase())) {
                                arrayList.add(postFilterItem);
                            }
                        }
                        postFilterSubCategory.setCatId(category.getCatId());
                        postFilterSubCategory.setSubCatId(category.getSubCatId());
                        postFilterSubCategory.setSubCatName(category.getSubCatName());
                        postFilterSubCategory.setSelectedAll(category.isSelectedAll());
                        postFilterSubCategory.setPostFilterItems(arrayList);

                        if (postFilterSubCategory.getSubCatName().toLowerCase().contains(key.toLowerCase())) {
                            filterSubCategories.add(postFilterSubCategory);
                        } else {
                            if (arrayList.size() != 0) {
                                filterSubCategories.add(postFilterSubCategory);
                            }
                        }
                    }
                    subCategoryAdapter.setSearchParam(true);
                }
                subCategoryAdapter.notifyDataSetChanged();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (categoryPosition) {
                    case 0:
                        categorySpinner.setSelection(1);
                        break;
                    case 1:
                        filterItem.setText(getString(R.string.select_categories));
                        getPostFilters("1", false);
                        break;
                    case 2:
                        filterItem.setText(selectedCategory.isEmpty() ? getString(R.string.select_category) : selectedCategory);
                        categoryRecyclerView.setVisibility(View.GONE);
                        getSinglePostFilters("4", false);
                        break;
                    case 3:
                        filterItem.setText(getString(R.string.select_categories));
                        getPostFilters("8", false);
                        break;
                }
                dialog.dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryPosition == 3) {
                    addedFilters("", "8", true);
                    clearSubCategories(exceptMultipleSubCategories, subCategoryAdapter);
                } else {
                    clearSubCategories(multipleSubCategories, subCategoryAdapter);
                    addedFilters("", "1", true);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showProgressBar(String title) {
        progressDialog.setMessage(title);
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }

    private void showIntroTooltip() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        categorySpinner.performClick();

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "1");

        sequence.setConfig(config);

        sequence.addSequenceItem(categorySpinner,
                getString(R.string.use_this_dropdown_to_quick_switch_back), getString(R.string.ok_i_got_it));

        sequence.addSequenceItem(filterItem,
                getString(R.string.take_control_of_your_feeds), getString(R.string.ok_i_got_it));

        sequence.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        active = false;
        if (App.isIsBockComment()) {
            App.setIsBockComment(false);
            startActivity(getIntent());
            finish();
        }
        int newNotificationCount = manager.getNotificationCount();
        setNotificationCount(newNotificationCount);
        // code to update the UI in the fragment
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TabbedCoordinatorLayout");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.settings);
        toolbar.setOverflowIcon(drawable);
// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    int range = -180;

    private void setupCollapsingToolbar() {
        collapsingToolbar = findViewById(R.id.collapse_toolbar);
        LinearLayout viewCategory = findViewById(R.id.viewCategory);
        appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbar.setTitleEnabled(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange()) < range) {
                    range = (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange());
                }

                Log.d("sxrollPosition", "" + range);

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    viewCategory.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() <= range) {
                    //Expanded
                    viewCategory.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Adding custom view to tab
     */
    LinearLayout tabOne, tabTwo, tabThree;
    TextView tabOneText, tabTwoText, tabThreeText;
    ImageView tabOneInfo, tabTwoInfo, tabThreeInfo;
    ViewTooltip.TooltipView viewTooltipOne, viewTooltipTwo, viewTooltipThree;

    private void tooltipClose(ViewTooltip.TooltipView one, ViewTooltip.TooltipView two) {
        if (one != null) {
            one.close();
        }
        if (two != null) {
            two.close();
        }
    }

    private void setupTabIcons() {

        tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOneText = tabOne.findViewById(R.id.tab);
        tabOneInfo = tabOne.findViewById(R.id.tab_info);
        tabOneText.setText("Trending");
        tabOneText.setTextColor(Color.parseColor("#1483C9"));
        tabOneInfo.setImageResource(R.drawable.ic_info_outline_blue_24dp);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwoText = tabTwo.findViewById(R.id.tab);
        tabTwoInfo = tabTwo.findViewById(R.id.tab_info);
        tabTwoText.setText("Breaking");
        tabTwoInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThreeText = tabThree.findViewById(R.id.tab);
        tabThreeInfo = tabThree.findViewById(R.id.tab_info);
        tabThreeText.setText("Following");
        tabThreeInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
        tabLayout.getTabAt(2).setCustomView(tabThree);

//        tabOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewPager.setCurrentItem(0);
//            }
//        });

        tabOneInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewTooltipOne != null) {
                    viewTooltipOne.close();
                    viewTooltipOne = null;
                } else {
                    viewTooltipOne = ViewTooltip
                            .on(Home.this, tabOneInfo)
                            .autoHide(true, 3000)
                            .color(Color.WHITE)
                            .textColor(Color.parseColor("#1483c9"))
                            .corner(30)
                            .position(ViewTooltip.Position.RIGHT)
                            .text(getString(R.string.the_trending_feed_includes_the_hottest_posts))
                            .show();
                }
                tooltipClose(viewTooltipTwo, viewTooltipThree);
            }
        });

//        tabTwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewPager.setCurrentItem(1);
//            }
//        });

        tabTwoInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewTooltipTwo != null) {
                    viewTooltipTwo.close();
                    viewTooltipTwo = null;
                } else {
                    viewTooltipTwo = ViewTooltip
                            .on(Home.this, tabTwoInfo)
                            .autoHide(true, 3000)
                            .color(Color.WHITE)
                            .textColor(Color.parseColor("#1483c9"))
                            .corner(30)
                            .position(ViewTooltip.Position.BOTTOM)
                            .text(getString(R.string.the_breaking_feed_includes_the_newest_posts))
                            .show();
                }
                tooltipClose(viewTooltipOne, viewTooltipThree);

            }
        });

//        tabThree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewPager.setCurrentItem(2);
//            }
//        });

        tabThreeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewTooltipThree != null) {
                    viewTooltipThree.close();
                    viewTooltipThree = null;
                } else {
                    viewTooltipThree = ViewTooltip
                            .on(Home.this, tabThree)
                            .autoHide(true, 3000)
                            .color(Color.WHITE)
                            .textColor(Color.parseColor("#1483c9"))
                            .corner(30)
                            .position(ViewTooltip.Position.LEFT)
                            .text(getString(R.string.the_following_feed_includes_the_most_recent_posts))
                            .show();
                }
                tooltipClose(viewTooltipOne, viewTooltipTwo);
            }
        });

    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here

                //  viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    tabOneInfo.setImageResource(R.drawable.ic_info_outline_blue_24dp);
                    tabOneText.setTextColor(Color.parseColor("#1483C9"));

                    tabTwoInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    tabTwoText.setTextColor(Color.parseColor("#AAAAAA"));

                    tabThreeInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    tabThreeText.setTextColor(Color.parseColor("#AAAAAA"));
                } else if (tab.getPosition() == 1) {
                    tabOneInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    tabOneText.setTextColor(Color.parseColor("#AAAAAA"));

                    tabTwoInfo.setImageResource(R.drawable.ic_info_outline_blue_24dp);
                    tabTwoText.setTextColor(Color.parseColor("#1483C9"));

                    tabThreeInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    tabThreeText.setTextColor(Color.parseColor("#AAAAAA"));
                } else {
                    tabOneInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    tabOneText.setTextColor(Color.parseColor("#AAAAAA"));

                    tabTwoInfo.setImageResource(R.drawable.ic_info_outline_black_24dp);
                    tabTwoText.setTextColor(Color.parseColor("#AAAAAA"));

                    tabThreeInfo.setImageResource(R.drawable.ic_info_outline_blue_24dp);
                    tabThreeText.setTextColor(Color.parseColor("#1483C9"));
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFrag(new TrendingPost(), "Tab 1");
        adapter.addFrag(new BreakingPost(), "Tab 2");
        adapter.addFrag(new FollowingPost(), "Tab 3");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

    }

    private void setNotificationCount(int count) {
        if (count > 0) {
            newNotificationCount.setVisibility(View.VISIBLE);
            newNotificationCount.setText(String.valueOf(count));
        } else {
            newNotificationCount.setVisibility(View.GONE);
            newNotificationCount.setText("");
        }
    }

    private void setMessageNotificationCount(int count) {
        if (count > 0) {
            newMessageNotificationCount.setVisibility(View.VISIBLE);
            newMessageNotificationCount.setText(String.valueOf(count));
        } else {
            newMessageNotificationCount.setVisibility(View.GONE);
            newMessageNotificationCount.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
//        MenuCompat.setGroupDividerEnabled(menu, true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navigation_setting) {
            drawer.openDrawer(Gravity.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tvSearchInput:
                startActivity(new Intent(this, LikerSearch.class));
                break;
            case R.id.imageNewPost:
                active = true;
                startActivity(new Intent(this, PostNew.class));

// imageNewPost.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
//                imageNewPost.setImageResource(R.drawable.ic_mode_edit_blue_24dp);
                break;
            case R.id.imageNotification:
                manager.setNotificationCountClear();
                setNotificationCount(0);
                startActivity(new Intent(this, NotificationActivity.class));
// imageNotification.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
// imageNotification.setImageResource(R.drawable.ic_notifications_none_blue_24dp);
                break;

            case R.id.imageFriendRequest:
                manager.setMessageNotificationCountClear();
                setMessageNotificationCount(0);
                startActivity(new Intent(this, MessageActivity.class));
// imageFriendRequest.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
// imageFriendRequest.setImageResource(R.drawable.ic_people_outline_blue_24dp);
                break;

            case R.id.imageStarContributor:
                startActivity(new Intent(this, StarContributorActivity.class).putExtra("category_id", "").putExtra("category_name", ""));
                break;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            if (type.equals("0")) {
                manager.setNotificationCount();
                int newCount = manager.getNotificationCount();
                setNotificationCount(newCount);
            } else {
                manager.setMessageNotificationCount();
                int newCount = manager.getMessageNotificationCount();
                setMessageNotificationCount(newCount);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        JZVideoPlayer.releaseAllVideos();
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
//        unregisterReceiver(newPostBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(filterBroadcast);
        unregisterReceiver(newPostBroadcastReceiver);
        Tools.dismissDialog();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        categoryPosition = position;
//        if (!isCatSelectFromPost) {
//            categoryPosition = position;
//            if (position == 1 || position == 3) {
//                categoryRecyclerView.setVisibility(View.VISIBLE);
//            } else {
//                categoryRecyclerView.setVisibility(View.GONE);
//            }
//            if (position == 2) {
//                if (selectedCategory.isEmpty()) {
//                    showSingleFilterDialog();
//                }
//                filterItem.setText(selectedCategory.isEmpty() ? getString(R.string.select_category) : selectedCategory);
//            } else {
//                if (position == 1 || position == 3) {
//                    if (categories.get(categoryPosition).getPostFilterSubCategories().size() == 0) {
//                        showMultipleFilterDialog();
//                    }
//                } else if (position == 0) {
//                    categories.get(categoryPosition).getPostFilterSubCategories().clear();
//                }
//                filterItem.setText(getString(R.string.select_categories));
//            }
//            ArrayList<String> arrayList = new ArrayList<>();
//            commonCategories.clear();
//            for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
//                if (postFilterSubCategory.isSelectedAll()) {
//                    arrayList.add(postFilterSubCategory.getSubCatId());
//                    commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
//                }
//                for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
//                    arrayList.add(postFilterItem.getItemId());
//                    commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
//                }
//            }
//
//            updateCategoryTitles();
//
//            sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
//        } else {
//            isCatSelectFromPost = false;
//        }

        switch (position) {
            case 0:
                filterItem.setText(getString(R.string.select_categories));
                categoryRecyclerView.setVisibility(View.GONE);
                getPostFilters("1", true);
                sendBroadcast((new Intent().putExtra("category_ids", "").putExtra("filter", 1)).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
                break;
            case 1:
                filterItem.setText(getString(R.string.select_categories));
                getPostFilters("1", false);
                break;
            case 2:
                filterItem.setText(selectedCategory.isEmpty() ? getString(R.string.select_category) : selectedCategory);
                categoryRecyclerView.setVisibility(View.GONE);
                getSinglePostFilters("4", false);
                break;
            case 3:
                filterItem.setText(getString(R.string.select_categories));
                getPostFilters("8", false);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    String reportId;

    @Override
    public void onButtonClicked(int image, String text) {
        reportId = text;
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
        reply = null;
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


        PostItem item = new PostItem();
        item = App.getItem();
        if (!isEmpty(item)) {

            blockUserId = item.getPostUserid();
        }


        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            Call<String> call = commentService.blockedUser(deviceId, profileId, token, blockUserId, userId);
            sendBlockUserRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
        }

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
                                // Tools.toast(Home.this, "your message was successfully sent", R.drawable.icon_checked);
                                startActivity(getIntent());
                                finish();

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

    private void addedFilters(String catIds, String filter, boolean isReload) {
        Call<String> call = webService.addedFilter(deviceId, userId, token, userId, catIds, filter);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject object = new JSONObject(response.body());
                    String status = object.getString("status");
                    if (status.equals("1")) {
                        if (filter.equals("7")) {
                            if (categoryPosition == 2) {
                                filterItem.setText(selectedCategory.isEmpty() ? getString(R.string.select_category) : selectedCategory);
                                categoryRecyclerView.setVisibility(View.GONE);
                                getSinglePostFilters("4", false);
                            } else {
                                categorySpinner.setSelection(2);
                            }
                        } else {
                            if (isReload) {
                                sendBroadcast((new Intent().putExtra("category_ids", catIds).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
                            }
                        }

                    }
                } catch (JSONException ignored) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void getSinglePostFilters(String filter, boolean isAllCategory) {
        showProgressBar(getString(R.string.loading));
        Call<SinglePostFilters> call = webService.sendSinglePostFilters(deviceId, userId, token, userId);
        call.enqueue(new Callback<SinglePostFilters>() {
            @Override
            public void onResponse(Call<SinglePostFilters> call, Response<SinglePostFilters> response) {
                SinglePostFilters postFilters = response.body();
                if (postFilters != null && postFilters.getCategoryInfo() != null) {
                    if (postFilters.getCategoryInfo().getFilterList().size() == 0) {
                        switch (filter) {
                            case "1":
                            case "8":
                                if (!isAllCategory)
                                    showMultipleFilterDialog();
                                break;
                            case "4":
                                showSingleFilterDialog();
                                break;
                        }
                        categoryRecyclerView.setVisibility(View.GONE);
                        hideProgressBar();
                    } else {
                        switch (filter) {
                            case "1":
                                setPostFilterCategories(postFilters.getCategoryInfo().getFilterList(), multipleSubCategories, filter, isAllCategory);
                                if (!isAllCategory)
                                    categoryRecyclerView.setVisibility(View.VISIBLE);
                                break;
                            case "4":
                                setPostFilterCategories(postFilters.getCategoryInfo().getFilterList(), subCategories, filter, isAllCategory);
                                categoryRecyclerView.setVisibility(View.GONE);
                                break;
                            case "8":
                                setPostFilterCategories(postFilters.getCategoryInfo().getFilterList(), exceptMultipleSubCategories, filter, isAllCategory);
                                categoryRecyclerView.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SinglePostFilters> call, Throwable t) {
                hideProgressBar();
            }
        });
    }

    private void getPostFilters(String filter, boolean isAllCategory) {
        showProgressBar(getString(R.string.loading));
        Call<PostFilters> call = webService.sendPostFilters(deviceId, userId, token, userId, filter);
        call.enqueue(new Callback<PostFilters>() {
            @Override
            public void onResponse(Call<PostFilters> call, Response<PostFilters> response) {
                PostFilters postFilters = response.body();
                if (postFilters != null && postFilters.getFilterList() != null) {
                    if (postFilters.getFilterList().size() == 0) {
                        switch (filter) {
                            case "1":
                            case "8":
                                if (!isAllCategory)
                                    showMultipleFilterDialog();
                                break;
                            case "4":
                                showSingleFilterDialog();
                                break;
                        }
                        categoryRecyclerView.setVisibility(View.GONE);
                        hideProgressBar();
                    } else {
                        switch (filter) {
                            case "1":
                                setPostFilterCategories(postFilters.getFilterList(), multipleSubCategories, filter, isAllCategory);
                                if (!isAllCategory)
                                    categoryRecyclerView.setVisibility(View.VISIBLE);
                                break;
                            case "4":
                                setPostFilterCategories(postFilters.getFilterList(), subCategories, filter, isAllCategory);
                                categoryRecyclerView.setVisibility(View.GONE);
                                break;
                            case "8":
                                setPostFilterCategories(postFilters.getFilterList(), exceptMultipleSubCategories, filter, isAllCategory);
                                categoryRecyclerView.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PostFilters> call, Throwable t) {
                hideProgressBar();
            }
        });
    }

    private void setPostFilterCategories(List<FilterList> filterLists, ArrayList<PostFilterSubCategory> subCategories, String filter, boolean isAllCategory) {
        commonCategories.clear();
        ArrayList<String> arrayList = new ArrayList<>();
        clearSubCategories(subCategories, null);

        for (FilterList filterList : filterLists) {
            for (int i = 0; i < subCategories.size(); i++) {
                if (subCategories.get(i).getSubCatId().equals(filterList.getCatId())) {
                    if (!filterList.getSubCatId().equals("0")) {
                        for (int j = 0; j < subCategories.get(i).getPostFilterItems().size(); j++) {
                            if (subCategories.get(i).getPostFilterItems().get(j).getItemId().equals(filterList.getSubCatId())) {
                                if (filter.equals("4"))
                                    selectedCategory = subCategories.get(i).getPostFilterItems().get(j).getItemName();
                                subCategories.get(i).getPostFilterItems().get(j).setSelected(true);
                                break;
                            }
                        }
                    } else {
                        if (filter.equals("4"))
                            selectedCategory = subCategories.get(i).getSubCatName();
                        subCategories.get(i).setSelectedAll(true);
                    }
                    break;
                }
            }
            if (!filterList.getSubCatId().equals("0")) {
                arrayList.add(filterList.getSubCatId());
                commonCategories.add(new CommonCategory(filterList.getSubCatId(), filterList.getSubCatName()));
            } else {
                arrayList.add(filterList.getCatId());
                commonCategories.add(new CommonCategory(filterList.getCatId(), filterList.getCatName()));
            }
        }
        if (filter.equals("4"))
            filterItem.setText(selectedCategory);
        if (!isAllCategory) {
            updateCategoryTitles();
            sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
        } else {
            hideProgressBar();
        }
    }

    private void filterSubCategory(ArrayList<PostFilterSubCategory> subCategories, String filter, boolean isReload) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (PostFilterSubCategory postFilterSubCategory : subCategories) {
            if (postFilterSubCategory.isSelectedAll()) {
                arrayList.add(postFilterSubCategory.getSubCatId());
            }
            for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
                if (postFilterItem.isSelected()) {
                    arrayList.add(postFilterItem.getItemId());
                }
            }
        }
        addedFilters(Tools.setCategoryIds(arrayList), filter, isReload);
    }

    private void clearSubCategories(ArrayList<PostFilterSubCategory> subCategories, SubCategoryAdapter subCategoryAdapter) {
        for (int i = 0; i < subCategories.size(); i++) {
            subCategories.get(i).setSelectedAll(false);
            for (int j = 0; j < subCategories.get(i).getPostFilterItems().size(); j++) {
                subCategories.get(i).getPostFilterItems().get(j).setSelected(false);
            }
        }
        if (subCategoryAdapter != null)
            subCategoryAdapter.notifyDataSetChanged();
    }

    private void sendLogoutRequest(Call<String> call) {
        showProgressBar(getString(R.string.logging_out));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if (status) {
                                LoginInfo loginInfo = new LoginInfo(manager.getUserInfo(), manager.getToken(), manager.getProfileName(), manager.getProfileImage(), manager.getProfileId(), manager.getUserName(), manager.getDeviceId());
                                Intent loginAgain = new Intent(Home.this, LoginAgain.class);
                                loginAgain.putExtra("login_info", loginInfo);
                                manager.pref.edit().clear().apply();
                                stopService(new Intent(Home.this, DataFetchingService.class));
                                startActivity(loginAgain);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Tools.toast(Home.this, "Logout failed!", R.drawable.icon_unchecked);
                    }
                } else {
                    Tools.toast(Home.this, "Logout failed!", R.drawable.icon_unchecked);
                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("message", t.getMessage());
                Tools.toast(Home.this, "Logout failed!", R.drawable.icon_unchecked);
                hideProgressBar();
            }
        });
    }

    @Override
    public void onCancelResult(DialogFragment dlg) {
        // Toast.makeText(this, "Neutral button", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_account_setting:
                Intent accountIntent = new Intent(this, SettingActivity.class);
                accountIntent.putExtra("type", "account");
                accountIntent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(accountIntent);
                break;

            case R.id.action_find_friends:
                Intent findFriendsIntent = new Intent(this, SettingActivity.class);
                findFriendsIntent.putExtra("type", "findFriends");
                findFriendsIntent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(findFriendsIntent);
                break;

            case R.id.action_privacy_security:
                Intent privacyIntent = new Intent(this, SettingActivity.class);
                privacyIntent.putExtra("type", "privacy");
                privacyIntent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(privacyIntent);
                break;

            case R.id.action_notification_settings:
                Intent notificationIntent = new Intent(this, SettingActivity.class);
                notificationIntent.putExtra("type", "notification");
                notificationIntent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(notificationIntent);
                break;

            case R.id.action_contributor_settings:
                Intent contributorIntent = new Intent(this, SettingActivity.class);
                contributorIntent.putExtra("type", "contributor");
                contributorIntent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(contributorIntent);
                break;

            case R.id.action_how_to_use_liker:
                Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra("type", getString(R.string.how_to_use_liker));
                intent.putExtra("link", getString(R.string.how_to_use_liker_link));
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void postPermissionEnable(int image, String reasonId) {
        Toast.makeText(this, "post permission enable..", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver newPostBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (active) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        }
    };

}
