package com.doodle.Home.view.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.view.activity.LoginAgain;
import com.doodle.Home.adapter.SubCategoryAdapter;
import com.doodle.Home.adapter.ViewPagerAdapter;
import com.doodle.Home.model.PostFilterCategory;
import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.model.TopContributorStatus;
import com.doodle.Home.service.FilterClickListener;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Home.view.fragment.BreakingPost;
import com.doodle.Home.view.fragment.FollowingPost;
import com.doodle.Home.view.fragment.TabFragment;
import com.doodle.Home.model.Headers;
import com.doodle.Home.model.SetUser;
import com.doodle.Home.view.fragment.TrendingPost;
import com.doodle.Message.model.NewMessage;
import com.doodle.Message.model.SenderData;
import com.doodle.Message.view.MessageActivity;
import com.doodle.Notification.view.NotificationActivity;
import com.doodle.Post.view.activity.PostNew;
import com.doodle.R;
import com.doodle.Search.LikerSearch;
import com.doodle.utils.AppConstants;
import com.doodle.utils.PrefManager;
import com.doodle.utils.ScreenOnOffBroadcast;
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

import static com.doodle.utils.AppConstants.IN_CHAT_MODE;


public class Home extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private TabLayout tabLayout;
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private Spinner categorySpinner;
    private PrefManager manager;
    private String image_url;
    private String token, deviceId, userId, socketId, mSocketId;
    int categoryPosition = 0;
    int limit = 5;
    int offset = 0;
    private boolean isApps = true;
    private Socket socket, mSocket;
    private HomeService webService;
    private static final String TAG = Home.class.getSimpleName();
    private SetUser setUser;
    private TopContributorStatus contributorStatus;
    private Headers headers;
    private String topContributorStatus;
    private BroadcastReceiver mReceiver;
    private ArrayList<PostFilterCategory> categories;
    private ArrayList<PostFilterSubCategory> subCategories, multipleSubCategories;

    private ImageView imageNewPost, imageNotification, imageFriendRequest, imageStarContributor;
    private TextView newNotificationCount, newMessageNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        getData();
        setData();
        setBroadcast();
        sendCategoryListRequest();
    }

    private void initialComponent() {
        manager = new PrefManager(this);
        webService = HomeService.mRetrofit.create(HomeService.class);
        setUser = new SetUser();
        headers = new Headers();
        categories = new ArrayList<>();
        subCategories = new ArrayList<>();
        multipleSubCategories = new ArrayList<>();
        contributorStatus = new TopContributorStatus();
        topContributorStatus = getIntent().getStringExtra("STATUS");

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.NEW_NOTIFICATION_BROADCAST);
        registerReceiver(broadcastReceiver, filter);
        IntentFilter reFilter = new IntentFilter();
        reFilter.addAction(AppConstants.RECONNECT_SOCKET_BROADCAST);
        registerReceiver(reconnectSocketBroadcast, reFilter);

        findViewById(R.id.tvSearchInput).setOnClickListener(this);
        imageNewPost = findViewById(R.id.imageNewPost);
        imageNewPost.setOnClickListener(this);
        imageNotification = findViewById(R.id.imageNotification);
        imageNotification.setOnClickListener(this);
        imageFriendRequest = findViewById(R.id.imageFriendRequest);
        imageFriendRequest.setOnClickListener(this);
        imageStarContributor = findViewById(R.id.imageStarContributor);
// imageStarContributor.setOnClickListener(this);
        profileImage = findViewById(R.id.profile_image);
        categorySpinner = findViewById(R.id.spinnerCategoryType);
        newNotificationCount = findViewById(R.id.newNotificationCount);
        newMessageNotificationCount = findViewById(R.id.newMessageNotificationCount);

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

        socket = new SocketIOManager().getWSocketInstance();
        mSocket = new SocketIOManager().getMSocketInstance();
    }

    private void setData() {
        ArrayList<PostFilterSubCategory> postFilterSubCategories = new ArrayList<>();
        categories.add(new PostFilterCategory("1", "All Category", postFilterSubCategories));
        categories.add(new PostFilterCategory("2", "My Category", postFilterSubCategories));
        categories.add(new PostFilterCategory("3", "Single Category", postFilterSubCategories));
        categoryFilter();
    }

    private void setBroadcast() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOffBroadcast();
        registerReceiver(mReceiver, filter);
    }

    private void getData() {
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("user_data").getString("description"));

                    newMessage.setSenderData(senderData);
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 0)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!IN_CHAT_MODE)
                    sendBroadcast((new Intent().putExtra("type", "1")).setAction(AppConstants.NEW_NOTIFICATION_BROADCAST));
            }
        });

        mSocket.on("message_own", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("to_user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("to_user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("to_user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("to_user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("to_user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("to_user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("to_user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("to_user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("to_user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("to_user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("to_user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("to_user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("to_user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("to_user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("to_user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("to_user_data").getString("description"));

                    newMessage.setSenderData(senderData);
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 1)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                                ArrayList<PostFilterItem> arrayList = new ArrayList<>();
                                JSONArray array = jsonObject.getJSONArray("subcatg");
                                for (int j = 0; j < array.length(); j++) {
                                    String itemId = jsonObject.getString("sub_category_id");
                                    String itemName = jsonObject.getString("sub_category_name");
                                    arrayList.add(new PostFilterItem("", subCatId, itemId, itemName, false));
                                }
                                subCategories.add(new PostFilterSubCategory("", subCatId, subCatName, false, arrayList));
                                multipleSubCategories.add(new PostFilterSubCategory("", subCatId, subCatName, false, arrayList));
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
        ArrayList<String> arrayList = new ArrayList<>();
        for (PostFilterCategory postFilterCategory : categories) {
            arrayList.add(postFilterCategory.getCatName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(dataAdapter);

//        showFilterDialog();
    }

    private void showSingleFilterDialog() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
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
            tvFilterItemName.setText(subCategories.get(0).getPostFilterItems().get(0).getItemName());
        } else {
            tvFilterItemName.setText(getString(R.string.personalize_your_feed));
        }

        FilterClickListener filterClickListener = new FilterClickListener() {
            @Override
            public void onSingleSubCategorySelect(PostFilterSubCategory postFilterSubCategory) {
                categories.get(categoryPosition).getPostFilterSubCategories().clear();
                categories.get(categoryPosition).getPostFilterSubCategories().add(postFilterSubCategory);
                dialog.dismiss();
                //Api Request
                //Progress Dialog

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
            public void onSingleFilterItemSelect(PostFilterSubCategory postFilterSubCategory) {
                categories.get(categoryPosition).getPostFilterSubCategories().clear();
                categories.get(categoryPosition).getPostFilterSubCategories().add(postFilterSubCategory);
                dialog.dismiss();
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

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(this, subCategories, filterClickListener);
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
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.multiple_post_category_filter_layout);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        TextView tvCategoryName, tvFilterItemName, tvDone;
        RecyclerView recyclerView;
        tvCategoryName = dialog.findViewById(R.id.categoryName);
        tvFilterItemName = dialog.findViewById(R.id.filterItem);
        tvDone = dialog.findViewById(R.id.done);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvCategoryName.setText(categories.get(categoryPosition).getCatName());
        if (categoryPosition == 2) {
            tvFilterItemName.setText(multipleSubCategories.get(0).getPostFilterItems().get(0).getItemName());
        } else {
            tvFilterItemName.setText(getString(R.string.personalize_your_feed));
        }

        FilterClickListener filterClickListener = new FilterClickListener() {
            @Override
            public void onSingleSubCategorySelect(PostFilterSubCategory postFilterSubCategory) {
                categories.get(categoryPosition).getPostFilterSubCategories().add(postFilterSubCategory);
            }

            @Override
            public void onSingleSubCategoryDeselect(PostFilterSubCategory postFilterSubCategory) {
                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
                        categories.get(categoryPosition).getPostFilterSubCategories().remove(i);
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

                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
                        categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().add(postFilterSubCategory.getPostFilterItems().get(0));
                        isNotExist = false;
                        break;
                    }
                }
                if (isNotExist) {
                    categories.get(categoryPosition).getPostFilterSubCategories().add(postFilterSubCategory);
                }
            }

            @Override
            public void onSingleFilterItemDeselect(PostFilterSubCategory postFilterSubCategory) {
                for (int i = 0; i < categories.get(categoryPosition).getPostFilterSubCategories().size(); i++) {
                    if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getSubCatId().equals(postFilterSubCategory.getSubCatId())) {
                        for (int j = 0; j < categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().size(); j++) {
                            if (categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().get(j).getItemId().equals(postFilterSubCategory.getPostFilterItems().get(0).getItemId())) {
                                categories.get(categoryPosition).getPostFilterSubCategories().get(i).getPostFilterItems().remove(j);
                                break;
                            }
                        }
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

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(this, multipleSubCategories, filterClickListener);
        recyclerView.setAdapter(subCategoryAdapter);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorySpinner.setSelection(1);
                categoryPosition = 1;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        socket.on("web_notification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                sendBroadcast((new Intent().putExtra("type", "0")).setAction(AppConstants.NEW_NOTIFICATION_BROADCAST));
            }
        });
        if (!socket.connected()) {
            socket = new SocketIOManager().getWSocketInstance();
        }
        if (!mSocket.connected()) {
            mSocket = new SocketIOManager().getMSocketInstance();
        }
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TabbedCoordinatorLayout");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_drop_down);
        toolbar.setOverflowIcon(drawable);
// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
// imageNewPost.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
                imageNewPost.setImageResource(R.drawable.ic_mode_edit_blue_24dp);
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
                startActivity(new Intent(this, PostNew.class));
// imageStarContributor.setCircleBackgroundColor(getResources().getColor(R.color.colorWhite));
// imageStarContributor.setImageResource(R.drawable.ic_star_border_blue_24dp);
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

    BroadcastReceiver reconnectSocketBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!socket.connected()) {
                socket = new SocketIOManager().getWSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
            if (!mSocket.connected()) {
                mSocket = new SocketIOManager().getMSocketInstance();
//                Toast.makeText(context, "Message Socket Reconnected", Toast.LENGTH_LONG).show();
            }
            Log.d("Working", "Working");

        }
    };

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(mReceiver);
        socket.off("web_notification");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        categoryPosition = i;

        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
}