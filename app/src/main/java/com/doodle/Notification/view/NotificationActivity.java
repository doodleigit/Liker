package com.doodle.Notification.view;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.doodle.Notification.adapter.NotificationAdapter;
import com.doodle.Notification.model.NotificationItem;
import com.doodle.Notification.service.NotificationService;
import com.doodle.R;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    private PrefManager manager;
    private boolean networkOk;
    private NotificationService webService;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationItem> notificationItems;
    private String deviceId, profileId, token, userIds;
    int limit = 10;
    int offset = 0;
    private boolean isScrolling;
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        getData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    isScrolling = false;
                    getPagination();
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
        }
    }

    private void sendNotificationItemRequest(Call<ArrayList<NotificationItem>> call) {

        call.enqueue(new Callback<ArrayList<NotificationItem>>() {

            @Override
            public void onResponse(Call<ArrayList<NotificationItem>> call, Response<ArrayList<NotificationItem>> response) {

                notificationItems = response.body();
                if (notificationItems != null) {
                    notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationItems);
                    recyclerView.setAdapter(notificationAdapter);
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressDialog.hide();
            }
        });

    }

    private void sendNotificationItemPaginationRequest(Call<ArrayList<NotificationItem>> call) {

        call.enqueue(new Callback<ArrayList<NotificationItem>>() {

            @Override
            public void onResponse(Call<ArrayList<NotificationItem>> call, Response<ArrayList<NotificationItem>> response) {

                notificationItems = response.body();
                if (notificationItems != null) {
                    notificationAdapter.addPagingData(notificationItems);
                    offset += 10;
                    progressBar.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
