package com.doodle.Setting.view;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.R;
import com.doodle.Setting.adapter.PeopleMayKnowAdapter;
import com.doodle.Setting.model.PeopleMayKnow;
import com.doodle.Setting.service.FollowUnfollowClickListener;
import com.doodle.Setting.service.SettingService;
import com.doodle.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestedFriendsFragment extends Fragment {

    View view;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;
    private TextView tvAlertText;

    private SettingService settingService;
    private PrefManager manager;
    private PeopleMayKnowAdapter peopleMayKnowAdapter;
    private ArrayList<PeopleMayKnow> peopleMayKnows;
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
        view = inflater.inflate(R.layout.suggested_friends_fragment_layout, container, false);

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
        peopleMayKnows = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userIds = manager.getProfileId();

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

        peopleMayKnowAdapter = new PeopleMayKnowAdapter(getActivity(), peopleMayKnows, followUnfollowClickListener);

        progressBar = view.findViewById(R.id.progress_bar);
        tvAlertText = view.findViewById(R.id.alertText);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(peopleMayKnowAdapter);

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
    }

    private void sendAllPeopleRequest(SwipeRefreshLayout refreshLayout) {
        Call<ArrayList<PeopleMayKnow>> call = settingService.peopleYouMayKnow(deviceId, token, userIds, userIds, "", limit, offset);
        call.enqueue(new Callback<ArrayList<PeopleMayKnow>>() {
            @Override
            public void onResponse(Call<ArrayList<PeopleMayKnow>> call, Response<ArrayList<PeopleMayKnow>> response) {
                peopleMayKnows.clear();
                ArrayList<PeopleMayKnow> arrayList = response.body();
                if (arrayList != null) {
                    peopleMayKnows.addAll(arrayList);
                    offset += limit;
                }
                if (peopleMayKnows.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                peopleMayKnowAdapter.notifyDataSetChanged();
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<PeopleMayKnow>> call, Throwable t) {
                peopleMayKnows.clear();
                peopleMayKnowAdapter.notifyDataSetChanged();
                tvAlertText.setVisibility(View.VISIBLE);
                progressDialog.hide();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void sendAllPeoplePaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<PeopleMayKnow>> call = settingService.peopleYouMayKnow(deviceId, token, userIds, userIds, "", limit, offset);
        call.enqueue(new Callback<ArrayList<PeopleMayKnow>>() {
            @Override
            public void onResponse(Call<ArrayList<PeopleMayKnow>> call, Response<ArrayList<PeopleMayKnow>> response) {
                ArrayList<PeopleMayKnow> arrayList = response.body();
                if (arrayList != null) {
                    peopleMayKnows.addAll(arrayList);
                    offset += limit;
                }
                peopleMayKnowAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<PeopleMayKnow>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setFollow(String followUserId, int position) {
        progressDialog.show();
        Call<String> call = settingService.setFollow(deviceId, token, userIds, userIds, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        peopleMayKnows.get(position).getPrivacy().setIsFollowing(true);
                        peopleMayKnowAdapter.notifyItemChanged(position);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
        Call<String> call = settingService.setUnFollow(deviceId, token, userIds, userIds, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        peopleMayKnows.get(position).getPrivacy().setIsFollowing(false);
                        peopleMayKnowAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
        Call<String> call = settingService.sendBrowserNotification(deviceId, token, userIds, userIds, followUserId, "0", "follow");
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
