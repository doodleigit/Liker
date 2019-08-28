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

import com.doodle.Profile.adapter.FollowingAdapter;
import com.doodle.Profile.model.Following;
import com.doodle.Profile.model.FollowingResult;
import com.doodle.Profile.service.FollowUnfollowClickListener;
import com.doodle.Profile.service.ProfileService;
import com.doodle.R;
import com.doodle.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvAlertText;

    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    private ProfileService profileService;
    private PrefManager manager;
    private FollowingAdapter followingAdapter;
    private ArrayList<FollowingResult> followings;
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
        view = inflater.inflate(R.layout.followings_fragment_layout, container, false);

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
        followings = new ArrayList<>();
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

        followingAdapter = new FollowingAdapter(getActivity(), followings, followUnfollowClickListener);

        recyclerView.setAdapter(followingAdapter);

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
        Call<Following> call = profileService.getFollowings(deviceId, token, userId, userId, profileUserId, limit, offset, false);
        call.enqueue(new Callback<Following>() {
            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                followings.clear();
                Following following = response.body();
                if (following != null) {
                    followings.addAll(following.getFollowingResult());
                    offset += 10;
                }
                if (followings.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                followingAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Following> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                followings.clear();
                followingAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void sendFriendListPaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<Following> call = profileService.getFollowings(deviceId, token, userId, userId, profileUserId, limit, offset, false);
        call.enqueue(new Callback<Following>() {
            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                Following following = response.body();
                if (following != null) {
                    followings.addAll(following.getFollowingResult());
                    followingAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Following> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                followings.clear();
                followingAdapter.notifyDataSetChanged();
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
                        followings.get(position).setIsFollowed(true);
                        followingAdapter.notifyItemChanged(position);
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
                        followings.get(position).setIsFollowed(false);
                        followingAdapter.notifyItemChanged(position);
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
