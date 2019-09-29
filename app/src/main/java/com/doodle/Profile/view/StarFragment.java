package com.doodle.Profile.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doodle.Comment.model.CommentItem;
import com.doodle.Home.adapter.PostAdapter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.holder.ImageHolder;
import com.doodle.Home.holder.LinkScriptHolder;
import com.doodle.Home.holder.LinkScriptYoutubeHolder;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Home.holder.TextMimHolder;
import com.doodle.Home.holder.VideoHolder;
import com.doodle.Home.service.VideoPlayerRecyclerView;
import com.doodle.Profile.adapter.StarAdapter;
import com.doodle.Profile.model.Star;
import com.doodle.Profile.service.ProfileService;
import com.doodle.Profile.service.StarClickListener;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarFragment extends Fragment {

    View view;
    private TextView tvUserName;
    private RecyclerView recyclerView;
    private VideoPlayerRecyclerView feedRecyclerView;
    private LinearLayoutManager layoutManager;

    private ProgressDialog progressDialog;

    private PrefManager manager;
    private ProfileService profileService;
    private String deviceId, profileUserName, token, userId;
    private ArrayList<Star> arrayList;
    private StarAdapter starAdapter;
    private PostAdapter adapter;

    public List<PostItem> postItemList;
    private boolean isScrolling;
    int limit = 5;
    int offset = 0;
    private String catIds = "";
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.star_fragment_layout, container, false);

        initialComponent();
        getStarList();

        return view;
    }

    private void initialComponent() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.PROFILE_PAGE_PAGINATION_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(broadcastReceiver, filter);

        IntentFilter postFooterIntentFilter = new IntentFilter();
        postFooterIntentFilter.addAction(AppConstants.POST_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(postChangeBroadcast, postFooterIntentFilter);

        IntentFilter permissionIntent = new IntentFilter();
        permissionIntent.addAction(AppConstants.PERMISSION_CHANGE_BROADCAST);
        Objects.requireNonNull(getActivity()).registerReceiver(permissionBroadcast, permissionIntent);

        postItemList = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        profileService = ProfileService.mRetrofit.create(ProfileService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        profileUserName = getArguments().getString("user_name");
        token = manager.getToken();
        userId = manager.getProfileId();
        arrayList = new ArrayList<>();

        tvAlert = view.findViewById(R.id.alert);
        tvUserName = view.findViewById(R.id.user_name);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        feedRecyclerView = view.findViewById(R.id.feed_recycler_view);
        feedRecyclerView.setLayoutManager(layoutManager);
        feedRecyclerView.setNestedScrollingEnabled(false);

        StarClickListener starClickListener = new StarClickListener() {
            @Override
            public void onStarCategoryClick(String catId) {
                catIds = catId;
                progressDialog.show();
                getData();
            }
        };
        starAdapter = new StarAdapter(getActivity(), arrayList, starClickListener);
        recyclerView.setAdapter(starAdapter);

        mCallback = new TextHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                StarFragment.this.deletePost(deletePostItem, deletePosition);

            }
        };

        mimListener = new TextMimHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                StarFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        videoListener = new VideoHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                StarFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        youtubeListener = new LinkScriptYoutubeHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                StarFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        linkListener = new LinkScriptHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                StarFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        imageListener = new ImageHolder.PostItemListener() {
            @Override
            public void deletePost(PostItem postItem, int position) {
                deletePosition = position;
                deletePostItem = postItem;
                StarFragment.this.deletePost(deletePostItem, deletePosition);
            }
        };

        adapter = new PostAdapter(getActivity(), postItemList, mCallback, mimListener, videoListener, youtubeListener, linkListener, imageListener, true);
        feedRecyclerView.setMediaObjects(postItemList);
        feedRecyclerView.setActivityContext(getActivity());
        feedRecyclerView.setAdapter(adapter);
        getData();

    }

    private void getStarList() {
        Call<ArrayList<Star>> call = profileService.getStarList(deviceId, token, userId, profileUserName);
        call.enqueue(new Callback<ArrayList<Star>>() {
            @Override
            public void onResponse(Call<ArrayList<Star>> call, Response<ArrayList<Star>> response) {
                if (response.body() != null) {
                    arrayList.addAll(response.body());
                    starAdapter.notifyDataSetChanged();
                }
                if (arrayList.size() != 0) {
                    catIds = arrayList.get(0).getPostCategoryId();
                    getData();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<Star>> call, Throwable t) {
                progressDialog.hide();
            }
        });

    }

    private void deletePost(PostItem deletePostItem, int deletePosition) {
        new AlertDialog.Builder(getActivity())
                //  .setTitle("Delete entry")
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_post))

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<String> call = profileService.postDelete(deviceId, userId, token, userId, deletePostItem.getPostId());
                        sendDeletePostRequest(call);
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
                            if (status) {
                                postItemList.remove(deletePosition);
                                adapter.notifyDataSetChanged();
                                offset--;
                                feedRecyclerView.smoothScrollToPosition(0);
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
//        progressView.setVisibility(View.VISIBLE);
        offset = 0;
        Call<List<PostItem>> call = profileService.feed(deviceId, userId, token, userId, limit, offset, catIds, profileUserName, false);
        sendPostItemRequest(call);
    }

    private void PerformPagination() {
        isScrolling = false;
//        progressView.setVisibility(View.VISIBLE);
        Call<List<PostItem>> call = profileService.feed(deviceId, userId, token, userId, limit, offset, catIds, profileUserName, false);
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
//                    Call<CommentItem> mCall = webService.getPostComments(deviceId, profileId, token, "false", limit, offset, "DESC", totalPostIDs, userIds);
//                    sendCommentItemPagingRequest(mCall);
                    offset += 5;
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
//        progressView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
//        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        progressDialog.dismiss();
        isScrolling = true;
    }

    private void onPostResponsePagination() {
//        progressView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
//        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.GONE);
        isScrolling = true;
    }

    private void onPostResponseFailure() {
//        progressView.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
//        refreshLayout.setRefreshing(false);
        tvAlert.setVisibility(View.VISIBLE);
        progressDialog.dismiss();
        isScrolling = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        feedRecyclerView.pausePlayer();
    }

    private boolean isViewShown = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;
            // call your function
            feedRecyclerView.setVisibility(View.VISIBLE);
            feedRecyclerView.setAdapter(adapter);
        } else {
            isViewShown = false;
        }

      /*  if (isVisibleToUser) {
            // Refresh your fragment here
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("IsRefresh", "Yes");
        }*/
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isScrolling)
                PerformPagination();
        }
    };

    BroadcastReceiver postChangeBroadcast = new BroadcastReceiver() {
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

    BroadcastReceiver permissionBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PostItem postItem = (PostItem) intent.getSerializableExtra("post_item");
            int position = intent.getIntExtra("position", -1);
            String type=intent.getStringExtra("type");

            if (position != -1) {
                if (postItemList.size() >= position + 1) {
                    if (postItemList.get(position).getPostId().equals(postItem.getPostId())) {


                        if("permission".equalsIgnoreCase(type)){
                            postItemList.remove(position);
                            postItemList.add(position, postItem);
                            adapter.notifyItemChanged(position);
                        }else {
                            postItemList.remove(position);
                            // adapter.notifyItemChanged(position);
                            adapter.notifyDataSetChanged();
                        }


                    }
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        feedRecyclerView.releasePlayer();
        Objects.requireNonNull(getActivity()).unregisterReceiver(broadcastReceiver);
        Objects.requireNonNull(getActivity()).unregisterReceiver(postChangeBroadcast);
        Objects.requireNonNull(getActivity()).unregisterReceiver(permissionBroadcast);
    }
}
