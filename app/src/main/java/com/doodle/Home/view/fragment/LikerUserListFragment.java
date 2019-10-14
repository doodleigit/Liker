package com.doodle.Home.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.Home.adapter.LikeUserAdapter;
import com.doodle.Home.model.LikeUser;
import com.doodle.Home.model.LikeUsers;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.LikeUserClickListener;
import com.doodle.R;
import com.doodle.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikerUserListFragment extends DialogFragment {

    View view;
    private RecyclerView recyclerView;
    private ProgressBar progressBar, progressBarLoading;
    private TextView tvAlertText, tvLikeUserCount;
    private ImageView close;

    private LinearLayoutManager layoutManager;

    private HomeService homeService;
    private PrefManager manager;
    private LikeUserAdapter likeUserAdapter;
    private List<LikeUser> likeUsers;
    private String deviceId, typeId, replyId, token, userId, totalLikes, likerType;
    int limit = 10;
    int offset = 0, current = 0;
    private boolean isScrolling;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.like_user_list_fragment_layout, container, false);

        initialComponent();
        sendLikerListRequest();

        return view;
    }

    Dialog dialog;

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void initialComponent() {
        homeService = HomeService.mRetrofit.create(HomeService.class);
        manager = new PrefManager(getContext());
        likeUsers = new ArrayList<>();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();
        typeId = getArguments().getString("type_id");
        totalLikes = getArguments().getString("total_likes");
        likerType = getArguments().getString("liker_type");
        replyId = getArguments().getString("reply_id") != null ? getArguments().getString("reply_id")  : "";

        progressBar = view.findViewById(R.id.progress_bar);
        progressBarLoading = view.findViewById(R.id.progress_bar_loading);
        close = view.findViewById(R.id.close);
        tvLikeUserCount = view.findViewById(R.id.likeUserCount);
        tvAlertText = view.findViewById(R.id.alertText);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        if (Integer.parseInt(totalLikes) > 1) {
            tvLikeUserCount.setText(totalLikes + " " + getString(R.string.people) + " " + getString(R.string.people_liked_this_post));
        } else {
            tvLikeUserCount.setText(totalLikes + " " + getString(R.string.person) + " " + getString(R.string.people_liked_this_post));
        }

        LikeUserClickListener likeUserClickListener = new LikeUserClickListener() {
            @Override
            public void onFollowClick(String followUserId, int position) {
                setFollow(followUserId, position);
            }

            @Override
            public void onUnFollowClick(String followUserId, int position) {
                setUnFollow(followUserId, position);
            }
        };

        likeUserAdapter = new LikeUserAdapter(getActivity(), likeUsers, userId, likeUserClickListener);

        recyclerView.setAdapter(likeUserAdapter);

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
                    sendLikerListPaginationRequest();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void sendLikerListRequest() {
        Call<LikeUsers> call = null;
        if (likerType.equals("post")) {
            call = homeService.postLiker(deviceId, userId, token, userId, typeId, offset, limit, current);
        } else if (likerType.equals("comment")){
            call = homeService.commentLiker(deviceId, userId, token, userId, typeId, offset, limit, current);
        } else if (likerType.equals("reply")) {
            call = homeService.commentReplyLiker(deviceId, userId, token, userId, typeId, replyId, offset, limit, current);
        }
        call.enqueue(new Callback<LikeUsers>() {
            @Override
            public void onResponse(Call<LikeUsers> call, Response<LikeUsers> response) {
                likeUsers.clear();
                LikeUsers likeUser = response.body();
                if (likeUser != null) {
                    if (likeUser.getLikeUser() != null) {
                        likeUsers.addAll(likeUser.getLikeUser());
                        offset += 10;
                    }
                }
                if (likeUsers.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                likeUserAdapter.notifyDataSetChanged();
                progressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LikeUsers> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                likeUsers.clear();
                likeUserAdapter.notifyDataSetChanged();
                progressBarLoading.setVisibility(View.GONE);
            }
        });

    }

    private void sendLikerListPaginationRequest() {
        progressBar.setVisibility(View.VISIBLE);
        Call<LikeUsers> call = homeService.postLiker(deviceId, userId, token, userId, typeId, limit, offset, current);
        call.enqueue(new Callback<LikeUsers>() {
            @Override
            public void onResponse(Call<LikeUsers> call, Response<LikeUsers> response) {
                LikeUsers likeUser = response.body();
                if (likeUser != null) {
                    likeUsers.addAll(likeUser.getLikeUser());
                    likeUserAdapter.notifyDataSetChanged();
                    offset += 10;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LikeUsers> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                likeUsers.clear();
                likeUserAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setFollow(String followUserId, int position) {
        progressBarLoading.setVisibility(View.VISIBLE);
        Call<String> call = homeService.setFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        likeUsers.get(position).setIsFollowed(true);
                        likeUserAdapter.notifyItemChanged(position);
                        sendBrowserNotification(followUserId);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBarLoading.setVisibility(View.GONE);;
            }
        });
    }

    private void setUnFollow(String followUserId, int position) {
        progressBarLoading.setVisibility(View.VISIBLE);
        Call<String> call = homeService.setUnFollow(deviceId, token, userId, userId, followUserId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String jsonResponse = response.body();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    boolean status = obj.getBoolean("status");
                    if (status) {
                        likeUsers.get(position).setIsFollowed(false);
                        likeUserAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBarLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBarLoading.setVisibility(View.GONE);
            }
        });
    }

    private void sendBrowserNotification(String followUserId) {
        Call<String> call = homeService.sendBrowserNotification(deviceId, userId, token, followUserId, userId, "0", "follow");
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
