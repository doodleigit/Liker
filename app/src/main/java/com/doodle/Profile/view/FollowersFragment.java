package com.doodle.Profile.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.Profile.adapter.FollowersAdapter;
import com.doodle.Profile.model.Followers;
import com.doodle.Profile.model.FollowersResult;
import com.doodle.Profile.service.FollowUnfollowClickListener;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvAlertText;

    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    private ProfileService profileService;
    private PrefManager manager;
    private FollowersAdapter followersAdapter;
    private ArrayList<FollowersResult> followers;
    private String deviceId, profileUserId, token, userId;
    int limit = 10;
    int offset = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.followers_fragment_layout, container, false);

        initialComponent();
        sendFriendListRequest();

        return view;
    }

    private void initialComponent() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        followers = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();
        profileUserId = getArguments().getString("user_id");

        progressBar = view.findViewById(R.id.progress_bar);
        tvAlertText = view.findViewById(R.id.alertText);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        FollowUnfollowClickListener followUnfollowClickListener = new FollowUnfollowClickListener() {
            @Override
            public void onFollowClick(String followUserId, int position) {
                setFollow(followUserId, position);
            }

            @Override
            public void onUnFollowClick(String followUserId, int position) {
                setUnFollow(followUserId, position);
            }
        };

        followersAdapter = new FollowersAdapter(getActivity(), followers, followUnfollowClickListener);

        recyclerView.setAdapter(followersAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                sendFriendListRequest();
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
                    sendFriendListPaginationRequest();
                }
            }
        });
    }

    private void sendFriendListRequest() {
        Call<Followers> call = profileService.getFollowers(deviceId, token, userId, userId, profileUserId, limit, offset, false);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                followers.clear();
                Followers follower = response.body();
                if (follower != null) {
                    followers.addAll(follower.getFollowersResult());
                    offset += 10;
                }
                if (followers.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                followersAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                followers.clear();
                followersAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void sendFriendListPaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<Followers> call = profileService.getFollowers(deviceId, token, userId, userId, profileUserId, limit, offset, false);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                Followers follower = response.body();
                if (follower != null) {
                    followers.addAll(follower.getFollowersResult());
                    followersAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                followers.clear();
                followersAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setFollow(String followUserId, int position) {
        progressDialog.show();
        Call<String> call = profileService.setFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        followers.get(position).setIsFollowed(true);
                        followersAdapter.notifyItemChanged(position);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }

    private void setUnFollow(String followUserId, int position) {
        progressDialog.show();
        Call<String> call = profileService.setUnFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        followers.get(position).setIsFollowed(false);
                        followersAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }

    private void sendBrowserNotification(String followUserId) {
        Call<String> call = profileService.sendBrowserNotification(deviceId, token, userId, userId, followUserId, "0", "follow");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

}
