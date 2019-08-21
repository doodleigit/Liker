package com.doodle.Home.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.doodle.App;
import com.doodle.Comment.model.Comment;
import com.doodle.Comment.model.CommentItem;
import com.doodle.Home.adapter.BreakingPostAdapter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.TextHolder;
import com.doodle.Home.view.activity.Home;
import com.doodle.Post.model.CategoryItem;
import com.doodle.Post.service.PostService;
import com.doodle.Post.view.fragment.ContributorStatus;
import com.doodle.R;
import com.doodle.Search.model.AdvanceSearches;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BreakingPost extends Fragment {


    public BreakingPost() {
        // Required empty public constructor
    }

    public List<PostItem> postItemList;
  //  private List<Comment> comments = new ArrayList<Comment>();
    private HomeService webService;
    private PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private int cat_id, filter;
    private boolean isPublic;
    private boolean networkOk;
    private CircularProgressView progressView;
    //  private PostAdapter adapter;
    private BreakingPostAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private boolean isScrolling;
    int limit = 5;
    int offset = 0;
    private String catIds = "";
    private ShimmerFrameLayout shimmerFrameLayout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.CATEGORY_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(broadcastReceiver, intentFilter);

        manager = new PrefManager(getActivity());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.breaking_post, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        progressView = (CircularProgressView) root.findViewById(R.id.progress_view);
        shimmerFrameLayout = (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_post_container);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvBreakingPost);
        recyclerView.setLayoutManager(layoutManager);

        getData();

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
                    PerformPagination();
                }


            }
        });


        return root;
    }

    private void getData() {
        if (networkOk) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "breaking", catIds, 1, false);
            sendPostItemRequest(call);
        } else {
            Utils.showNetworkDialog(getActivity().getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
    }

    private void PerformPagination() {
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (networkOk) {
                    String queryResult = App.getQueryResult();
                    Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "breaking", catIds, 1, false);
                    PostItemPagingRequest(call);

                } else {
                    Utils.showNetworkDialog(getActivity().getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }
            }
        }, 2000);

    }

    private void PostItemPagingRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                postItemList = response.body();
                if (postItemList != null) {
                    String totalPostIDs;
                    List<String> postIdSet = new ArrayList<>();
                    for (PostItem temp : postItemList) {

                        postIdSet.add(temp.getPostId());
                    }
                    String separator = ", ";
                    int total = postIdSet.size() * separator.length();
                    for (String s : postIdSet) {
                        total += s.length();
                    }

                    StringBuilder sb = new StringBuilder(total);
                    for (String s : postIdSet) {
                        sb.append(separator).append(s);
                    }

                    totalPostIDs = sb.substring(separator.length()).replaceAll("\\s+", "");
                    Log.d("friends", totalPostIDs);
                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", 1, 0, "DESC", totalPostIDs, userIds);
                    sendCommentItemPagingRequest(mCall);


                }

            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });
    }

    private void sendCommentItemPagingRequest(Call<CommentItem> mCall) {

        mCall.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {

                CommentItem commentItem = response.body();
              //  comments = commentItem.getComments();
                Log.d("commentItem", commentItem.toString());
                if (postItemList != null) {
                    adapter.addPagingData(postItemList);
                    offset += 5;
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }

            }

            @Override
            public void onFailure(Call<CommentItem> mCall, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });
    }

    private void sendPostItemRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                postItemList = response.body();
                if (postItemList != null) {

                    String totalPostIDs;
                    List<String> postIdSet = new ArrayList<>();
                    for (PostItem temp : postItemList) {

                        postIdSet.add(temp.getPostId());
                    }
                    String separator = ", ";
                    int total = postIdSet.size() * separator.length();
                    for (String s : postIdSet) {
                        total += s.length();
                    }

                    StringBuilder sb = new StringBuilder(total);
                    for (String s : postIdSet) {
                        sb.append(separator).append(s);
                    }

                    totalPostIDs = sb.substring(separator.length()).replaceAll("\\s+", "");
                    Log.d("friends", totalPostIDs);
                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", 1, 0, "DESC", totalPostIDs, userIds);
                     sendCommentItemRequest(mCall);

//             adapter = new BreakingPostAdapter(getActivity(), postItemList);
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            shimmerFrameLayout.stopShimmer();
//                            shimmerFrameLayout.setVisibility(View.GONE);
//
//                            recyclerView.setVisibility(View.VISIBLE);
//                            recyclerView.setAdapter(adapter);
//                        }
//                    }, 5000);
//

                    //  Log.d("PostItem: ", categoryItem.toString() + "");
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }

            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(1);

            }
        });

    }

    private void sendCommentItemRequest(Call<CommentItem> mCall) {

        mCall.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {

                CommentItem commentItem = response.body();
              //  comments = commentItem.getComments();
                Log.d("commentItem", commentItem.toString());
                if (postItemList != null ) {
                    adapter = new BreakingPostAdapter(getActivity(), postItemList);
                    offset += 5;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }, 1000);


                    //  Log.d("PostItem: ", categoryItem.toString() + "");
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }
                try {
                    ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<CommentItem> mCall, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    private boolean isViewShown = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;
            // call your function
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        } else {
            isViewShown = false;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadInitial();
            catIds = intent.getStringExtra("category_ids");
            getData();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
    }
}
