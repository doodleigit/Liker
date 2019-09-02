package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doodle.R;
import com.doodle.Setting.adapter.SocialFriendsAdapter;
import com.doodle.Setting.model.Social;
import com.doodle.Setting.model.SocialFriend;
import com.doodle.Setting.service.SettingService;
import com.doodle.Tool.PrefManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkedinFriendsFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;
    private LinearLayout sendInvitation;
    private TextView tvAlertText;

    private SettingService settingService;
    private PrefManager manager;
    private SocialFriendsAdapter socialFriendsAdapter;
    private ArrayList<SocialFriend> socialFriends;
    private String deviceId, token, userIds;
    private int limit = 20;
    private int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.linkedin_friends_fragment_layout, container, false);
        initialComponent();
        sendAllPeopleRequest(refreshLayout);
        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        settingService = SettingService.mRetrofit.create(SettingService.class);
        manager = new PrefManager(getContext());
        socialFriends = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userIds = manager.getProfileId();

        socialFriendsAdapter = new SocialFriendsAdapter(getActivity(), socialFriends);

        progressBar = view.findViewById(R.id.progress_bar);
        sendInvitation = view.findViewById(R.id.send_invitation);
        tvAlertText = view.findViewById(R.id.alertText);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(socialFriendsAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                sendAllPeopleRequest(refreshLayout);
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
                    sendAllPeoplePaginationRequest();
                }
            }
        });

        sendInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInvitation();
            }
        });

    }

    private void sendAllPeopleRequest(SwipeRefreshLayout refreshLayout) {
        Call<Social> call = settingService.getSocialFriends(deviceId, token, userIds, userIds, "3", limit, offset);
        call.enqueue(new Callback<Social>() {
            @Override
            public void onResponse(Call<Social> call, Response<Social> response) {
                socialFriends.clear();
                Social social = response.body();
                if (social != null) {
                    socialFriends.addAll(social.getSocialFriends());
                    offset += limit;
                }
                if (socialFriends.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                socialFriendsAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Social> call, Throwable t) {
                socialFriends.clear();
                socialFriendsAdapter.notifyDataSetChanged();
                tvAlertText.setVisibility(View.VISIBLE);
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void sendAllPeoplePaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<Social> call = settingService.getSocialFriends(deviceId, token, userIds, userIds, "3", limit, offset);
        call.enqueue(new Callback<Social>() {
            @Override
            public void onResponse(Call<Social> call, Response<Social> response) {
                Social social = response.body();
                if (social != null) {
                    socialFriends.addAll(social.getSocialFriends());
                    offset += limit;
                }
                socialFriendsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Social> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void sendInvitation() {
        if (FindFriendsFragment.socialLink != null) {
            String url = FindFriendsFragment.socialLink.getLinkedin();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

}
