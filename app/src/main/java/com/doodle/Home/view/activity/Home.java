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
import android.os.Parcelable;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.doodle.Home.model.PostFilterCategory;
import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.TopContributorStatus;
import com.doodle.Home.service.CategoryRemoveListener;
import com.doodle.Home.service.FilterClickListener;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.LoadCompleteListener;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Home.view.fragment.BreakingPost;
import com.doodle.Home.view.fragment.FollowingPost;
import com.doodle.Home.model.Headers;
import com.doodle.Home.model.SetUser;
import com.doodle.Home.view.fragment.TrendingPost;
import com.doodle.Message.model.NewMessage;
import com.doodle.Message.model.SenderData;
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
import com.doodle.Tool.ScreenOnOffBroadcast;
import com.doodle.Tool.Service.DataFetchingService;
import com.doodle.Tool.Tools;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.jzvd.JZVideoPlayer;
import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.IN_CHAT_MODE;
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
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView mainNavigationView, navigationView, footerNavigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
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

    private ImageView navClose, imageNewPost, imageNotification, imageFriendRequest, imageStarContributor;
    private TextView tvHome, navUserName, navLogout, newNotificationCount, newMessageNotificationCount, filterItem;
    private RecyclerView categoryRecyclerView;

    public LoadCompleteListener loadCompleteListener;
    public UserInfo userInfo;
    private boolean isFriend;
    private CommentService commentService;
    private boolean networkOk;
    private String profileId;
    private String blockUserId;



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

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NEW_NOTIFICATION_BROADCAST);
        registerReceiver(broadcastReceiver, filter);

        IntentFilter catFilter = new IntentFilter();
        catFilter.addAction(AppConstants.POST_FILTER_CAT_BROADCAST);
        registerReceiver(filterBroadcast, catFilter);

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
                showProgressBar();
            }

            @Override
            public void onLoadComplete(int position) {
                if (viewPager.getCurrentItem() == position)
                    hideProgressBar();
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

        navLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginInfo loginInfo = new LoginInfo(manager.getUserInfo(), manager.getToken(), manager.getProfileName(), manager.getProfileImage(), manager.getProfileId(), manager.getUserName(), manager.getDeviceId());
                Intent loginAgain = new Intent(Home.this, LoginAgain.class);
                loginAgain.putExtra("login_info", loginInfo);
                manager.pref.edit().clear().apply();
                stopService(new Intent(Home.this, DataFetchingService.class));
                startActivity(loginAgain);
                finish();
            }
        });

    }

    private void setData() {
        int newNotificationCount = manager.getNotificationCount();
        setNotificationCount(newNotificationCount);
        int newMessageCount = manager.getMessageNotificationCount();
        setMessageNotificationCount(newMessageCount);
        categories.add(new PostFilterCategory("1", "All Categories", new ArrayList<>()));
        categories.add(new PostFilterCategory("2", "My Favourites", new ArrayList<>()));
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
                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(commonCategory.getCatId())) {
                        categories.get(categoryPosition).getPostFilterSubCategories().get(i).setSelectedAll(false);
                        break;
                    }
                    for (int j = 0; j < categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().size(); j++) {
                        if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().get(j).getItemId().equals(commonCategory.getCatId())) {
                            categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().remove(j);
                            break;
                        }
                    }
                }

                if (categoryPosition == 3) {
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
                } else {
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
                }

                ArrayList<String> arrayList = new ArrayList<>();
                commonCategories.clear();
                for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
                    if (postFilterSubCategory.isSelectedAll()) {
                        arrayList.add(postFilterSubCategory.getSubCatId());
                        commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
                    }
                    for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
                        arrayList.add(postFilterItem.getItemId());
                        commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
                    }
                }
                sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
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
            String catId = intent.getStringExtra("category_id");
            categoryPosition = 2;
            for (int i = 0; i < subCategories.size(); i++) {
                subCategories.get(i).setSelectedAll(false);
                for (int j = 0; j < subCategories.get(i).getPostFilterItems().size(); j++) {
                    subCategories.get(i).getPostFilterItems().get(j).setSelected(false);
                }
            }
            for (PostFilterSubCategory subCategory : subCategories) {
                if (subCategory.getSubCatId().equals(catId)) {
                    subCategory.setSelectedAll(true);
                    categories.get(categoryPosition).getPostFilterSubCategories().clear();
                    categories.get(categoryPosition).getPostFilterSubCategories().add(subCategory);
//                    ArrayList<String> arrayList = new ArrayList<>();
//                    commonCategories.clear();
                    for (int i = 0; i < subCategory.getPostFilterItems().size(); i++) {
                        subCategory.getPostFilterItems().get(i).setSelected(true);
                    }
//                    for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
//                        if (postFilterSubCategory.isSelectedAll()) {
//                            arrayList.add(postFilterSubCategory.getSubCatId());
//                            commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
//                        }
//                        for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
//                            arrayList.add(postFilterItem.getItemId());
//                            commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
//                        }
//                    }
//                    updateCategoryTitles();

                    selectedCategory = subCategory.getSubCatName();
//                    filterItem.setText(selectedCategory);
                    categorySpinner.setSelection(2);
//                    sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
                    break;
                } else {
                    for (PostFilterItem postFilterItem : subCategory.getPostFilterItems()) {
                        if (postFilterItem.getItemId().equals(catId)) {
//                            selectChangeListener.onSelectClear();
                            postFilterItem.setSelected(true);
                            ArrayList<PostFilterItem> postFilterItems = new ArrayList<>();
                            String categoryId, subCategoryId, itemId, itemName;
                            boolean isSelected;
                            categoryId = "";
                            subCategoryId =postFilterItem.getSubCatId();
                            itemId = postFilterItem.getItemId();
                            itemName = postFilterItem.getItemName();
                            isSelected = postFilterItem.isSelected();
                            PostFilterItem item = new PostFilterItem(categoryId, subCategoryId, itemId, itemName, isSelected);
                            postFilterItems.add(item);
                            subCategory.getPostFilterItems().clear();
                            subCategory.getPostFilterItems().addAll(postFilterItems);
                            categories.get(categoryPosition).getPostFilterSubCategories().clear();
                            categories.get(categoryPosition).getPostFilterSubCategories().add(subCategory);
//                            ArrayList<String> arrayList = new ArrayList<>();
//                            commonCategories.clear();
//                            for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
//                                for (PostFilterItem filterItem : postFilterSubCategory.getPostFilterItems()) {
//                                    arrayList.add(filterItem.getItemId());
//                                    commonCategories.add(new CommonCategory(filterItem.getItemId(), filterItem.getItemName()));
//                                }
//                            }
//                            updateCategoryTitles();

                            selectedCategory = subCategory.getSubCatName();
//                            filterItem.setText(selectedCategory);
                            categorySpinner.setSelection(2);
//                            sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
                            break;
                        }
                    }
                }
            }
        }
    };

    private void showSingleFilterDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.single_post_category_filter_layout);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        TextView tvCategoryName, tvFilterItemName;
        RecyclerView recyclerView;
        tvCategoryName = dialog.findViewById(R.id.categoryName);
        tvFilterItemName = dialog.findViewById(R.id.filterItem);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvCategoryName.setText(categories.get(categoryPosition).getCatName());
        if (categoryPosition == 2) {
            tvFilterItemName.setText(selectedCategory.isEmpty() ? getString(R.string.select_category) : selectedCategory);
        } else {
            tvFilterItemName.setText(getString(R.string.select_categories));
        }

        FilterClickListener filterClickListener = new FilterClickListener() {
            @Override
            public void onSingleSubCategorySelect(PostFilterSubCategory filterSubCategory) {
                categories.get(categoryPosition).getPostFilterSubCategories().clear();
                categories.get(categoryPosition).getPostFilterSubCategories().add(filterSubCategory);
                ArrayList<String> arrayList = new ArrayList<>();
                commonCategories.clear();
                for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
                    if (postFilterSubCategory.isSelectedAll()) {
                        arrayList.add(postFilterSubCategory.getSubCatId());
                        commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
                    }
                    for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
                        arrayList.add(postFilterItem.getItemId());
                        commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
                    }
                }
                dialog.dismiss();

                updateCategoryTitles();

                selectedCategory = filterSubCategory.getSubCatName();
                filterItem.setText(selectedCategory);
                sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));

            }

            @Override
            public void onSingleSubCategoryDeselect(PostFilterSubCategory postFilterSubCategory) {
//                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
//                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
//                        categories.get(categoryPosition).getPostFilterSubCategories().remove(i);
//                        break;
//                    }
//                }
            }

            @Override
            public void onSingleFilterItemSelect(PostFilterSubCategory filterSubCategory) {
                categories.get(categoryPosition).getPostFilterSubCategories().clear();
                categories.get(categoryPosition).getPostFilterSubCategories().add(filterSubCategory);
                ArrayList<String> arrayList = new ArrayList<>();
                commonCategories.clear();
                for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
                    if (postFilterSubCategory.isSelectedAll()) {
                        arrayList.add(postFilterSubCategory.getSubCatId());
                        commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
                    }
                    for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
                        arrayList.add(postFilterItem.getItemId());
                        commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
                    }
                }
                dialog.dismiss();

                updateCategoryTitles();

                selectedCategory = filterSubCategory.getSubCatName();
                filterItem.setText(selectedCategory);
                sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
                //Api Request
                //Progress Dialog


//                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
//                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterItem.getSubCatId())) {
//                        categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().clear();
//                        categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().add(postFilterItem);
//                        break;
//                    }
//                }
            }

            @Override
            public void onSingleFilterItemDeselect(PostFilterSubCategory postFilterSubCategory) {
//                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
//                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
//                        categories.get(categoryPosition).getPostFilterSubCategories().get(i).setSelectedAll(false);
//                        for (int j = 0; j < categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().size(); j++) {
//                            if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().get(j).getItemId().equals(postFilterSubCategory.getPostFilterItems().get(0).getItemId())) {
//                                categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().remove(j);
//                                break;
//                            }
//                        }
//                        break;
//                    }
//                }
            }
        };

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(this, subCategories, filterClickListener, 0);
        recyclerView.setAdapter(subCategoryAdapter);

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

        int pos = categoryPosition == 0 || categoryPosition == 1 ? 1 : 3;
        SubCategoryAdapter subCategoryAdapter;

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        TextView tvCategoryName, tvFilterItemName;
        FloatingActionButton done, clear;
        RecyclerView recyclerView;
        tvCategoryName = dialog.findViewById(R.id.categoryName);
        tvFilterItemName = dialog.findViewById(R.id.filterItem);
        done = dialog.findViewById(R.id.done);
        clear = dialog.findViewById(R.id.clear);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvCategoryName.setText(categories.get(categoryPosition).getCatName());
        if (categoryPosition == 2) {
            tvFilterItemName.setText(getString(R.string.select_category));
        } else {
            tvFilterItemName.setText(getString(R.string.select_categories));
        }

        FilterClickListener filterClickListener = new FilterClickListener() {
            @Override
            public void onSingleSubCategorySelect(PostFilterSubCategory postFilterSubCategory) {
                categories.get(pos).getPostFilterSubCategories().add(postFilterSubCategory);
            }

            @Override
            public void onSingleSubCategoryDeselect(PostFilterSubCategory postFilterSubCategory) {
                for (int i = 0; i < categories.get(pos).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(pos).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
                        categories.get(pos).getPostFilterSubCategories().remove(i);
                        break;
                    }
                }
            }

            @Override
            public void onSingleFilterItemSelect(PostFilterSubCategory postFilterSubCategory) {
                boolean isNotExist = true;
//                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
//                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterItem.getSubCatId())) {
//                        categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().add(postFilterItem);
//                        isNotExist = false;
//                        break;
//                    }
//                }

                for (int i = 0; i < categories.get(pos).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(pos).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
                        categories.get(pos).getPostFilterSubCategories().get(i).getPostFilterItems().add(postFilterSubCategory.getPostFilterItems().get(0));
                        isNotExist = false;
                        break;
                    }
                }
                if (isNotExist) {
                    categories.get(pos).getPostFilterSubCategories().add(postFilterSubCategory);
                }
            }

            @Override
            public void onSingleFilterItemDeselect(PostFilterSubCategory postFilterSubCategory) {
                for (int i = 0; i < categories.get(pos).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(pos).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
                        categories.get(pos).getPostFilterSubCategories().get(i).setSelectedAll(false);
                        for (int j = 0; j < categories.get(pos).getPostFilterSubCategories().get(i).getPostFilterItems().size(); j++) {
                            if (categories.get(pos).getPostFilterSubCategories().get(i).getPostFilterItems().get(j).getItemId().equals(postFilterSubCategory.getPostFilterItems().get(0).getItemId())) {
                                categories.get(pos).getPostFilterSubCategories().get(i).getPostFilterItems().remove(j);
                                break;
                            }
                        }
                        break;
                    }
                }


//                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
//                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterItem.getSubCatId())) {
//                        for (int j = 0; j < categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().size(); j++) {
//                            if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().get(j).getItemId().equals(postFilterItem.getItemId())) {
//                                categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().remove(j);
//                                break;
//                            }
//                        }
//                        break;
//                    }
//                }
            }
        };


        if (categoryPosition == 3) {
            subCategoryAdapter = new SubCategoryAdapter(this, exceptMultipleSubCategories, filterClickListener, 1);
            recyclerView.setAdapter(subCategoryAdapter);
        } else {
            subCategoryAdapter = new SubCategoryAdapter(this, multipleSubCategories, filterClickListener, 1);
            recyclerView.setAdapter(subCategoryAdapter);
        }

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryPosition == 0) {
                    categorySpinner.setSelection(1);
                    categoryPosition = 1;
                }
                ArrayList<String> arrayList = new ArrayList<>();
                commonCategories.clear();
                for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
                    if (postFilterSubCategory.isSelectedAll()) {
                        arrayList.add(postFilterSubCategory.getSubCatId());
                        commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
                    }
                    for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
                        arrayList.add(postFilterItem.getItemId());
                        commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
                    }
                }
                dialog.dismiss();

                updateCategoryTitles();

                filterItem.setText(getString(R.string.select_categories));
                sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryPosition == 3) {
                    for (int i = 0; i < exceptMultipleSubCategories.size(); i++) {
                        exceptMultipleSubCategories.get(i).setSelectedAll(false);
                        for (int j = 0; j < exceptMultipleSubCategories.get(i).getPostFilterItems().size(); j ++) {
                            exceptMultipleSubCategories.get(i).getPostFilterItems().get(j).setSelected(false);
                        }
                    }
                } else {
                    for (int i = 0; i < multipleSubCategories.size(); i++) {
                        multipleSubCategories.get(i).setSelectedAll(false);
                        for (int j = 0; j < multipleSubCategories.get(i).getPostFilterItems().size(); j ++) {
                            multipleSubCategories.get(i).getPostFilterItems().get(j).setSelected(false);
                        }
                    }
                }
                categories.get(categoryPosition).getPostFilterSubCategories().clear();
                commonCategories.clear();
                categoryTitleAdapter.notifyDataSetChanged();
                subCategoryAdapter.notifyDataSetChanged();
            }
        });

        dialog.show();
    }

    private void showProgressBar() {
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapse_toolbar);
        LinearLayout viewCategory = findViewById(R.id.viewCategory);
        final AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        collapsingToolbar.setTitleEnabled(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    viewCategory.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() <= -200) {
                    //Expanded
                    viewCategory.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Adding custom view to tab
     */
    TextView tabOne, tabTwo, tabThree;

    private void setupTabIcons() {

        tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Trending");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabOne.setTextColor(Color.parseColor("#1483C9"));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Breaking");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Following");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        tabOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ViewTooltip
                        .on(Home.this, tabOne)
                        .autoHide(true, 2000)
                        .color(Color.parseColor("#1483c9"))
                        .textColor(Color.WHITE)
                        .corner(30)
                        .position(ViewTooltip.Position.BOTTOM)
                        .text(getString(R.string.the_trending_feed_includes_the_hottest_posts))
                        .show();
                return false;
            }
        });

        tabTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        tabTwo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ViewTooltip
                        .on(Home.this, tabTwo)
                        .autoHide(true, 2000)
                        .color(Color.parseColor("#1483c9"))
                        .textColor(Color.WHITE)
                        .corner(30)
                        .position(ViewTooltip.Position.BOTTOM)
                        .text(getString(R.string.the_breaking_feed_includes_the_newest_posts))
                        .show();
                return false;
            }
        });

        tabThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        tabThree.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ViewTooltip
                        .on(Home.this, tabThree)
                        .autoHide(true, 2000)
                        .color(Color.parseColor("#1483c9"))
                        .textColor(Color.WHITE)
                        .corner(30)
                        .position(ViewTooltip.Position.BOTTOM)
                        .text(getString(R.string.the_following_feed_includes_the_most_recent_posts))
                        .show();
                return false;
            }
        });
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here

                //  viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
                    tabOne.setTextColor(Color.parseColor("#1483C9"));

                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
                    tabTwo.setTextColor(Color.parseColor("#AAAAAA"));

                    tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
                    tabThree.setTextColor(Color.parseColor("#AAAAAA"));

                } else if (tab.getPosition() == 1) {
                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
                    tabTwo.setTextColor(Color.parseColor("#1483C9"));

                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
                    tabOne.setTextColor(Color.parseColor("#AAAAAA"));

                    tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
                    tabThree.setTextColor(Color.parseColor("#AAAAAA"));

                } else {
                    tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_blue_24dp, 0);
                    tabThree.setTextColor(Color.parseColor("#1483C9"));

                    tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
                    tabTwo.setTextColor(Color.parseColor("#AAAAAA"));

                    tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_info_outline_black_24dp, 0);
                    tabOne.setTextColor(Color.parseColor("#AAAAAA"));
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(filterBroadcast);
        Tools.dismissDialog();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        categoryPosition = position;
        if (position == 1 || position == 3) {
            categoryRecyclerView.setVisibility(View.VISIBLE);
        } else {
            categoryRecyclerView.setVisibility(View.GONE);
        }
        if (position == 2) {
            if (selectedCategory.isEmpty()) {
                showSingleFilterDialog();
            }
            filterItem.setText(selectedCategory.isEmpty() ? getString(R.string.select_category) : selectedCategory);
        } else {
            if (position == 1 || position == 3) {
                if (categories.get(categoryPosition).getPostFilterSubCategories().size() == 0) {
                    showMultipleFilterDialog();
                }
            } else if (position == 0) {
                categories.get(categoryPosition).getPostFilterSubCategories().clear();
            }
            filterItem.setText(getString(R.string.select_categories));
        }
        ArrayList<String> arrayList = new ArrayList<>();
        commonCategories.clear();
        for (PostFilterSubCategory postFilterSubCategory : categories.get(categoryPosition).getPostFilterSubCategories()) {
            if (postFilterSubCategory.isSelectedAll()) {
                arrayList.add(postFilterSubCategory.getSubCatId());
                commonCategories.add(new CommonCategory(postFilterSubCategory.getSubCatId(), postFilterSubCategory.getSubCatName()));
            }
            for (PostFilterItem postFilterItem : postFilterSubCategory.getPostFilterItems()) {
                arrayList.add(postFilterItem.getItemId());
                commonCategories.add(new CommonCategory(postFilterItem.getItemId(), postFilterItem.getItemName()));
            }
        }

        updateCategoryTitles();

        sendBroadcast((new Intent().putExtra("category_ids", Tools.setCategoryIds(arrayList)).putExtra("filter", (categoryPosition == 3 ? 8 : 1))).setAction(AppConstants.CATEGORY_CHANGE_BROADCAST));
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
        reply=null;
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


        if (networkOk) {
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
}
