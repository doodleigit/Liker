package com.doodle.Home.view.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.doodle.Home.adapter.PostAdapter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.holder.ImageHolder;
import com.doodle.Home.holder.LinkScriptHolder;
import com.doodle.Home.holder.LinkScriptYoutubeHolder;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Home.holder.TextMimHolder;
import com.doodle.Home.holder.VideoHolder;
import com.doodle.Home.service.VideoPlayerRecyclerView;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingPost extends Fragment   {

    public FollowingPost() {
        // Required empty public constructor
    }

    public List<PostItem> postItemList;
    private HomeService webService;
    private PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private int cat_id, filter = 1;
    private boolean isPublic;
    private boolean networkOk;
    private CircularProgressView progressView;
    //  private PostAdapter adapter;
    private PostAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private VideoPlayerRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private boolean isScrolling, isPaginationDone = true;
    int limit = 15;
    int offset = 0;
    private String catIds = "";
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView tvAlert;

    //Delete post item
    public static TextHolder.PostItemListener mCallback;
    public static TextMimHolder.PostItemListener mimListener;
    public static VideoHolder.PostItemListener videoListener;
    public static LinkScriptYoutubeHolder.PostItemListener youtubeListener;
    public static LinkScriptHolder.PostItemListener linkListener;
    public static ImageHolder.PostItemListener imageListener;

    PostItem deletePostItem;
    int deletePosition;


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

        IntentFilter postFooterIntentFilter = new IntentFilter();
        postFooterIntentFilter.addAction(AppConstants.POST_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(postFooterChangeBroadcast, postFooterIntentFilter);



        IntentFilter permissionIntent = new IntentFilter();
        permissionIntent.addAction(AppConstants.PERMISSION_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(permissionBroadcast, permissionIntent);


        manager = new PrefManager(getActivity());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        networkOk = NetworkHelper.hasNetworkAccess(getActivity());
        postItemList = new ArrayList<>();
        deletePostItem=new PostItem();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.following_post, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        progressView = (CircularProgressView) root.findViewById(R.id.progress_view);
        shimmerFrameLayout = (ShimmerFrameLayout) root.findViewById(R.id.shimmer_view_post_container);
        tvAlert = root.findViewById(R.id.alert);
        refreshLayout = root.findViewById(R.id.refreshLayout);
        recyclerView = root.findViewById(R.id.rvBreakingPost);
        recyclerView.setLayoutManager(layoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

                if (isScrolling && isPaginationDone && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    isPaginationDone = false;
                    PerformPagination();
                }


            }
        });

        mCallback=new TextHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition=position;
                deletePostItem=postItem;
                FollowingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        mimListener=new TextMimHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition=position;
                deletePostItem=postItem;
                FollowingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        videoListener = new VideoHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                FollowingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        youtubeListener =new LinkScriptYoutubeHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                FollowingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        linkListener = new LinkScriptHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                FollowingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        imageListener = new ImageHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                FollowingPost.this.deletePost(deletePostItem, deletePosition);
            }
        };

        adapter = new PostAdapter(getActivity(), postItemList, mCallback, mimListener, videoListener, youtubeListener, linkListener, imageListener, true);
        recyclerView.setMediaObjects(postItemList);
        recyclerView.setActivityContext(getActivity());
        recyclerView.setAdapter(adapter);
        getData();

        return root;
    }
    private void deletePost(PostItem deletePostItem, int deletePosition) {
        new AlertDialog.Builder(getActivity())
                //  .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this post? You will permanently lose this post !")

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (NetworkHelper.hasNetworkAccess(getContext())) {
                            Call<String> call = webService.postDelete(deviceId, profileId, token, userIds, deletePostItem.getPostId());
                            sendDeletePostRequest(call);
                        } else {
                            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
                        }


                    }
                })
                .setNegativeButton(android.R.string.no, null)
                // .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void sendDeletePostRequest(Call<String> call) {


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            boolean status = object.getBoolean("status");
                            if(status){
//                                postItemList.remove(deletePostItem);
//                                adapter.deleteItem(deletePosition);
                                postItemList.remove(deletePosition);
                                adapter.notifyDataSetChanged();
                                offset--;
                                recyclerView.smoothScrollToPosition(0);
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

    private void getData() {
        offset = 0;
        if (NetworkHelper.hasNetworkAccess(getContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "following", catIds, filter, false);
            sendPostItemRequest(call);
        } else {
            Tools.showNetworkDialog(getActivity().getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();
            refreshLayout.setRefreshing(false);
        }
    }

    private void PerformPagination() {
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        Call<List<PostItem>> call = webService.feed(deviceId, profileId, token, userIds, limit, offset, "following", catIds, filter, false);
        PostItemPagingRequest(call);

    }

    private void PostItemPagingRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                List<PostItem> list = response.body();

                if (list != null) {
                    postItemList.addAll(list);
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
//                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", 1, 0, "DESC", totalPostIDs, userIds);
//                    sendCommentItemPagingRequest(mCall);
                    offset += 15;
                    onPostResponsePagination();
                } else {
                    onPostResponsePagination();
                }

            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                onPostResponsePagination();
            }
        });
    }

    private void sendPostItemRequest(Call<List<PostItem>> call) {

        call.enqueue(new Callback<List<PostItem>>() {

            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {

                List<PostItem> itemList = response.body();
                if (itemList != null) {
                    postItemList.clear();
                    postItemList.addAll(itemList);

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

                    offset = limit;
                    onPostResponse();

//                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", 3, 0, "DESC", totalPostIDs, userIds);
//                    sendCommentItemRequest(mCall);


             /*        adapter = new PostAdapter(getActivity(), postItemList);

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
                } else {
                    postItemList.clear();
                    onPostResponseFailure();
                }

            }

            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
                postItemList.clear();
                onPostResponseFailure();
            }
        });

    }


    private void onPostResponse() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        try {
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onPostResponsePagination() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        isPaginationDone = true;
    }

    private void onPostResponseFailure() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        progressView.stopAnimation();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.VISIBLE);
        try {
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadComplete(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        recyclerView.pausePlayer();
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
            catIds = intent.getStringExtra("category_ids");
            filter = intent.getIntExtra("filter", 1);
            ((Home) Objects.requireNonNull(getActivity())).loadCompleteListener.onLoadInitial();
            recyclerView.scrollToPosition(0);
            getData();
        }
    };

    BroadcastReceiver postFooterChangeBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            boolean isFooterChange = intent.getBooleanExtra("isFooterChange", true);
            int position = intent.getIntExtra("position", -1);
            if (isFooterChange) {
                if (position != -1) {
                    if (postItemList.size() >= position + 1) {
                        if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {
                            postItemList.get(position).getPostFooter().setPostTotalLike(postItem.getPostFooter().getPostTotalLike());
                            postItemList.get(position).getPostFooter().setLikeUserStatus(postItem.getPostFooter().isLikeUserStatus());
                            postItemList.get(position).setTotalComment(postItem.getTotalComment());
                            adapter.notifyItemChanged(position);
                        }
                    }
                }
            } else {
                if (position != -1) {
                    if (postItemList.size() >= position + 1) {
                        if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {
                            postItemList.set(position, postItem);
                            adapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.releasePlayer();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(postFooterChangeBroadcast);
        Objects.requireNonNull(getActivity()).unregisterReceiver(permissionBroadcast);
    }



    BroadcastReceiver permissionBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            int position = intent.getIntExtra("position", -1);
            String type=intent.getStringExtra("type");

            if (position != -1) {
                if (postItemList.size() >= position + 1) {
                    if("permission".equalsIgnoreCase(type)){
                        postItemList.remove(position);
                        postItemList.add(position, postItem);
                        adapter.notifyItemChanged(position);
                    }else {
                        postItemList.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        }
    };


}
