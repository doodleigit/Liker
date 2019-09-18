package com.doodle.Notification.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.doodle.Home.model.Headers;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Notification.adapter.NotificationAdapter;
import com.doodle.Notification.model.NotificationItem;
import com.doodle.Notification.model.NotificationSeenParam;
import com.doodle.Notification.service.NotificationClickListener;
import com.doodle.Notification.service.NotificationService;
import com.doodle.Post.view.activity.PostPopup;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.google.gson.Gson;

import java.util.ArrayList;

import io.socket.client.Ack;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private Socket socket;
    private Headers headers;
    private PrefManager manager;
    private boolean networkOk;
    private NotificationService webService;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationItem> notificationItems;
    private String deviceId, profileId, token, userIds;
    int limit = 20;
    int offset = 0;
    private boolean isScrolling, isPaginationComplete = true;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initialComponent();
    }

    private void initialComponent() {
        socket = new SocketIOManager().getWSocketInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        networkOk = NetworkHelper.hasNetworkAccess(this);
        webService = NotificationService.mRetrofit.create(NotificationService.class);
        notificationItems = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress_bar);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        NotificationClickListener notificationClickListener = new NotificationClickListener() {
            @Override
            public void onNotificationPostActionClick(String postId, boolean isCommentAction) {
                sendPostItemRequest(postId, isCommentAction);
            }

            @Override
            public void onNotificationClick(String notificationId) {
                sendNotificationReadRequest(notificationId);
            }
        };

        notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationItems, notificationClickListener);
        recyclerView.setAdapter(notificationAdapter);

        getData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                getData();
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
                    if (isPaginationComplete) {
                        isScrolling = false;
                        isPaginationComplete = false;
                        getPagination();
                    }
                }
            }
        });

    }

    private void getData() {
        if (networkOk) {
            progressDialog.show();
            Call<ArrayList<NotificationItem>> call = webService.getNotificationList(deviceId, profileId, token, true, userIds, limit, offset);
            sendNotificationItemRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
        }
    }

    private void getPagination() {
        if (networkOk) {
            progressBar.setVisibility(View.VISIBLE);
            Call<ArrayList<NotificationItem>> call = webService.getNotificationList(deviceId, profileId, token, true, userIds, limit, offset);
            sendNotificationItemPaginationRequest(call);
        } else {
            isPaginationComplete = true;
        }
    }

    private void sendNotificationItemRequest(Call<ArrayList<NotificationItem>> call) {

        call.enqueue(new Callback<ArrayList<NotificationItem>>() {

            @Override
            public void onResponse(Call<ArrayList<NotificationItem>> call, Response<ArrayList<NotificationItem>> response) {

                ArrayList<NotificationItem> arrayList = response.body();
                if (arrayList != null) {
                    notificationItems.clear();
                    notificationItems.addAll(arrayList);
                    notificationAdapter.notifyDataSetChanged();
                    offset = 20;
                }
                progressDialog.dismiss();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.dismiss();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void sendNotificationItemPaginationRequest(Call<ArrayList<NotificationItem>> call) {

        call.enqueue(new Callback<ArrayList<NotificationItem>>() {

            @Override
            public void onResponse(Call<ArrayList<NotificationItem>> call, Response<ArrayList<NotificationItem>> response) {

                ArrayList<NotificationItem> arrayList = response.body();
                if (arrayList != null) {
                    notificationItems.addAll(arrayList);
                    notificationAdapter.notifyDataSetChanged();
                    offset += 20;
                }
                progressBar.setVisibility(View.GONE);
                isPaginationComplete = true;
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
                isPaginationComplete = true;
            }
        });

    }

    private void sendPostItemRequest(String postId, boolean isCommentAction) {
        Call<PostItem> call = webService.getPostDetails(deviceId, profileId, token, profileId, postId);
        call.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                PostItem postItem = response.body();
                if (postItem != null) {
                    Intent intent = new Intent(NotificationActivity.this, PostPopup.class);
                    intent.putExtra(AppConstants.ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra("has_footer", false);
                    intent.putExtra("is_comment_action", isCommentAction);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                }
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
            }
        });
    }

    private void sendNotificationReadRequest(String notificationId) {
        Headers headers = new Headers();
        NotificationSeenParam notificationSeenParam = new NotificationSeenParam();
        headers.setDeviceId(deviceId);
        headers.setIsApps(true);
        headers.setSecurityToken(token);
        notificationSeenParam.setNotificationId(notificationId);
        notificationSeenParam.setUserId(userIds);
        notificationSeenParam.setHeaders(headers);
        Gson gson = new Gson();
        String json = gson.toJson(notificationSeenParam);
        socket.emit(AppConstants.NOTIFICATION_SEEN, json, new Ack() {
            @Override
            public void call(Object... args) {
                Log.d("NOTIFICATION_SEEN", args[0].toString());
            }
        });
    }

}
