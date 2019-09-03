package com.doodle.Search.view;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;

import com.doodle.App;
import com.doodle.Search.adapter.AdvanceSearchAdapter;
import com.doodle.Search.model.Post;
import com.doodle.Search.model.User;
import com.doodle.Search.model.AdvanceSearches;
import com.doodle.Search.service.SearchService;
import com.doodle.R;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    private ShimmerFrameLayout shimmerFrameLayout;

    private AdvanceSearches advanceSearches;
    private CircularProgressView progressView;
    private List<User> mUserList;
    private List<Post> mPostList;
    private AdvanceSearchAdapter mAdapter;
    private boolean networkOk;
    private SearchService webService;
    private PrefManager manager;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;

    LinearLayoutManager layoutManager;
    private int totalItems;
    private int scrollOutItems;
    private int currentItems;
    private boolean isScrolling;
    int limit = 10;
    int offset = 10;
    RecyclerView recyclerView;
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
        layoutManager = new LinearLayoutManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        webService = SearchService.mRetrofit.create(SearchService.class);
        manager = new PrefManager(this);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        recyclerView = (RecyclerView) findViewById(R.id.rvItems);
        profileId = manager.getProfileId();
        mProfileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        advanceSearches = getIntent().getExtras().getParcelable("ADVANCE-SEARCH");

        mUserList = advanceSearches.getUser();
        mPostList = advanceSearches.getPost();
        mAdapter = new AdvanceSearchAdapter(this, mUserList, mPostList);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(mAdapter);
            }
        }, 5000);


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


    }

    private void PerformPagination() {
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (networkOk) {
                    String queryResult = App.getQueryResult();
                    Call<AdvanceSearches> call = webService.advanceSearchPaging(deviceId, profileId, token, mProfileId, queryResult, limit, offset, 1);
                    sendAdvanceSearchRequest(call);

                } else {
                    Tools.showNetworkDialog(getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }
            }
        }, 5000);

    }

    private void sendAdvanceSearchRequest(Call<AdvanceSearches> call) {

        call.enqueue(new Callback<AdvanceSearches>() {


            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {


                AdvanceSearches advanceSearches = response.body();
                mUserList = advanceSearches.getUser();
                mPostList = advanceSearches.getPost();
                mAdapter.addPagingData(mPostList);
                offset += 10;
                progressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {

                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
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
