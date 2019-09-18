package com.doodle.Search.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.doodle.App;
import com.doodle.Home.model.PostItem;
import com.doodle.Post.view.activity.PostPopup;
import com.doodle.Search.adapter.AdvanceSearchAdapter;
import com.doodle.Search.adapter.AdvanceSearchPostAdapter;
import com.doodle.Search.model.Post;
import com.doodle.Search.model.User;
import com.doodle.Search.model.AdvanceSearches;
import com.doodle.Search.service.PostClickListener;
import com.doodle.Search.service.SearchService;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private ShimmerFrameLayout shimmerFrameLayout;

    private AdvanceSearches advanceSearches;
    private List<User> mUserList;
    private List<Post> mPostList;
    private AdvanceSearchAdapter mAdapter;
    private AdvanceSearchPostAdapter searchPostAdapter;
    private boolean networkOk;
    private SearchService webService;
    private PrefManager manager;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;

    int limit = 5;
    int offset = 5, postOffset = 5;
    boolean isUserLoadComplete = true, isPostLoadComplete = true;
    LinearLayout userLoadMoreLayout, postLoadMoreLayout;
    FrameLayout userLoadMore, postLoadMore;
    ProgressBar userLoadMoreProgress, postLoadMoreProgress;
    RecyclerView userRecyclerView, postRecyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.main_activiy_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Advance Search");

        mUserList = new ArrayList<>();
        mPostList = new ArrayList<>();
        networkOk = NetworkHelper.hasNetworkAccess(this);
        webService = SearchService.mRetrofit.create(SearchService.class);
        manager = new PrefManager(this);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        userLoadMoreLayout = findViewById(R.id.user_load_more_layout);
        postLoadMoreLayout = findViewById(R.id.post_load_more_layout);
        userLoadMore = findViewById(R.id.user_load_more);
        postLoadMore = findViewById(R.id.post_load_more);
        userLoadMoreProgress = findViewById(R.id.user_load_more_progress);
        postLoadMoreProgress = findViewById(R.id.post_load_more_progress);

        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileId = manager.getProfileId();
        mProfileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        advanceSearches = getIntent().getExtras().getParcelable("ADVANCE-SEARCH");

        mUserList = advanceSearches.getUser();
        mPostList = advanceSearches.getPost();

        PostClickListener postClickListener = new PostClickListener() {
            @Override
            public void onPostClickListener(Post post) {
                if (post.getPostType().equals("2")) {
                    sendPostItemRequest(post.getPostId());
                } else if (post.getPostType().equals("3") || post.getPostType().equals("4")) {
                    openLinkScript(post.getPostText());
                }
            }
        };

        mAdapter = new AdvanceSearchAdapter(this, mUserList);
        searchPostAdapter = new AdvanceSearchPostAdapter(this, mPostList, postClickListener);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        userRecyclerView.setAdapter(mAdapter);
        postRecyclerView.setAdapter(searchPostAdapter);

        setLoadMoreView();

        userLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoadComplete) {
                    isUserLoadComplete = false;
                    postLoadMoreProgress.setVisibility(View.VISIBLE);
                    String queryResult = App.getQueryResult();
                    Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 1);
                    sendAdvanceSearchRequest(call);
                }
            }
        });

        postLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPostLoadComplete) {
                    isPostLoadComplete = false;
                    postLoadMoreProgress.setVisibility(View.VISIBLE);
                    String queryResult = App.getQueryResult();
                    Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, postOffset, 1);
                    sendPostAdvanceSearchRequest(call);
                }
            }
        });

    }

    private void setLoadMoreView() {
        if (mUserList.size() == 0) {
            userLoadMoreLayout.setVisibility(View.GONE);
        } else {
            userLoadMoreLayout.setVisibility(View.VISIBLE);
        }
        if (mPostList.size() == 0) {
            postLoadMoreLayout.setVisibility(View.GONE);
        } else {
            postLoadMoreLayout.setVisibility(View.VISIBLE);
        }
    }

    private void openLinkScript(String url) {
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
        if (!url.isEmpty() && url.matches(pattern)) {
            /// Valid youtube URL
            Intent browserIntents = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getAppContext().startActivity(browserIntents);
        } else {
            // Not Valid youtube URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getAppContext().startActivity(browserIntent);
        }
    }

    private void sendAdvanceSearchRequest(Call<AdvanceSearches> call) {
        call.enqueue(new Callback<AdvanceSearches>() {
            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {
                AdvanceSearches advanceSearches = response.body();
                if (advanceSearches != null) {
                    if (advanceSearches.getUser() != null) {
                        if (advanceSearches.getUser().size() < limit) {
                            userLoadMoreLayout.setVisibility(View.GONE);
                        }
                        mUserList.addAll(advanceSearches.getUser());
                        offset += 5;
                        mAdapter.notifyDataSetChanged();
                    }
                }
                isUserLoadComplete = true;
                userLoadMoreProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                isUserLoadComplete = true;
                userLoadMoreProgress.setVisibility(View.GONE);
            }
        });
    }

    private void sendPostAdvanceSearchRequest(Call<AdvanceSearches> call) {
        call.enqueue(new Callback<AdvanceSearches>() {
            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {
                AdvanceSearches advanceSearches = response.body();
                if (advanceSearches != null) {
                    if (advanceSearches.getPost() != null) {
                        if (advanceSearches.getPost().size() < limit) {
                            postLoadMoreLayout.setVisibility(View.GONE);
                        }
                        mPostList.addAll(advanceSearches.getPost());
                        postOffset += 5;
                        searchPostAdapter.notifyDataSetChanged();
                    }
                }
                isPostLoadComplete = true;
                postLoadMoreProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                isPostLoadComplete = true;
                postLoadMoreProgress.setVisibility(View.GONE);
            }
        });
    }

    private void sendPostItemRequest(String postId) {
        Call<PostItem> call = webService.getPostDetails(deviceId, profileId, token, profileId, postId);
        call.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                PostItem postItem = response.body();
                if (postItem != null) {
                    Intent intent = new Intent(SearchActivity.this, PostPopup.class);
                    intent.putExtra(AppConstants.ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra("has_footer", false);
                    startActivity(intent);
                    overridePendingTransition(R.anim.bottom_up, R.anim.nothing);
                }
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }
}
