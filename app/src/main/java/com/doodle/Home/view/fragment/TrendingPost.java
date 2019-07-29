package com.doodle.Home.view.fragment;


import android.content.Context;
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
import com.doodle.R;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingPost extends Fragment {


    public TrendingPost() {
        // Required empty public constructor
    }

    public List<PostItem> postItemList;
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
    private ShimmerFrameLayout shimmerFrameLayout;

    private String friends;
    List<String> friendSet = new ArrayList<>();
    private List<Comment> comments = new ArrayList<Comment>();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View root = inflater.inflate(R.layout.trending_post, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        progressView = (CircularProgressView) root.findViewById(R.id.progress_view);
        shimmerFrameLayout = (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_post_container);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvBreakingPost);

        if (networkOk) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "trending", "", 1, false);
            sendPostItemRequest(call);
        } else {
            Utils.showNetworkDialog(getActivity().getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }
        recyclerView.setLayoutManager(layoutManager);
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

    private void PerformPagination() {
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (networkOk) {
                    String queryResult = App.getQueryResult();
                    Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "trending", "", 1, false);
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
                    adapter.addPagingData(postItemList);
                    offset += 5;

                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
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

    private void sendPostItemRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                postItemList = response.body();
                if (postItemList != null) {

                    for (PostItem temp : postItemList) {

                        friendSet.add(temp.getPostId());
                    }
                    String separator = ", ";
                    int total = friendSet.size() * separator.length();
                    for (String s : friendSet) {
                        total += s.length();
                    }

                    StringBuilder sb = new StringBuilder(total);
                    for (String s : friendSet) {
                        sb.append(separator).append(s);
                    }

                    friends = sb.substring(separator.length()).replaceAll("\\s+", "");
                    Log.d("friends", friends);
                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", 3, 0, "DESC", friends, userIds);
                    sendCommentItemRequest(mCall);

                /*    adapter = new BreakingPostAdapter(getActivity(), postItemList);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }, 5000);*/


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
            }
        });

    }

    private void sendCommentItemRequest(Call<CommentItem> mCall) {

        mCall.enqueue(new Callback<CommentItem>() {

            @Override
            public void onResponse(Call<CommentItem> mCall, Response<CommentItem> response) {

                CommentItem commentItem = response.body();
                comments = commentItem.getComments();
                Log.d("commentItem", commentItem.toString());
                if (postItemList != null && comments != null) {
                    adapter = new BreakingPostAdapter(getActivity(), postItemList,comments);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);

                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }
                    }, 5000);


                    //  Log.d("PostItem: ", categoryItem.toString() + "");
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

}
