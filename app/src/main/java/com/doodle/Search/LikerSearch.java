package com.doodle.Search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.Home.view.activity.Home;
import com.doodle.Search.adapter.SearchHistoryAd;
import com.doodle.Search.adapter.SearchUsersAd;
import com.doodle.Search.model.AdvanceSearches;
import com.doodle.Search.model.Message;
import com.doodle.Search.model.SearchHistory;
import com.doodle.Search.model.SearchUser;
import com.doodle.Search.service.SearchService;
import com.doodle.Home.Liker;
import com.doodle.Search.view.SearchActivity;
import com.doodle.R;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;
import com.doodle.utils.fragment.Network;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikerSearch extends AppCompatActivity implements View.OnClickListener/*, Network.NetworkDialogListener*/ {


    private Toolbar toolbar;
    private Toolbar searchToolbar;
    private boolean isSearch = false;

    private List<SearchUser> searchUserList;
    private List<SearchHistory> searchHistoryList;

    private ListView listView;
    private PrefManager manager;
    private SearchUsersAd searchUsersAdapter;
    private SearchHistoryAd searchHistoryAdapter;
    private CircularProgressView progressView;
    private TextView tvShowSearchResult, tvClear;
    private String queryText;
    private SearchService webService;
    private final String TAG = "AdvanceSearches";
    private boolean networkOk;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;
    private SearchView search;
    private View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liker_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewpager);
        searchToolbar = (Toolbar) findViewById(R.id.toolbar_search);
        prepareActionBar(toolbar);

        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);

        searchUserList = new ArrayList<>();
        searchHistoryList = new ArrayList<>();
        webService = SearchService.mRetrofit.create(SearchService.class);

        tvShowSearchResult = findViewById(R.id.tvShowSearchResult);
        tvClear = findViewById(R.id.tvClear);
        tvClear.setOnClickListener(this);
        tvShowSearchResult.setOnClickListener(this);
        tvShowSearchResult.setVisibility(View.GONE);
        listView = (ListView) findViewById(android.R.id.list);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);

        profileId = manager.getProfileId();
        mProfileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        mView = new View(this);


        if (networkOk) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<List<SearchHistory>> call = webService.searchHistory(deviceId, profileId, token, mProfileId);
            sendHistoryRequest(call);
        } else {
            Utils.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(true ? R.menu.menu_search_toolbar : R.menu.menu_main, menu);
        if (true) {

            search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setIconified(false);
            search.setQueryHint("Search item...");
            search.clearFocus();
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    String searchData = s;

                    App.setQueryResult(searchData);

                    if (networkOk) {
                        progressView.setVisibility(View.VISIBLE);
                        progressView.startAnimation();
                        Call<AdvanceSearches> call = webService.advanceSearch(deviceId, profileId, token, mProfileId, searchData, 15, 0);

                        sendAdvanceSearchRequest(call);

                    } else {
                        Utils.showNetworkDialog(getSupportFragmentManager());
                        progressView.setVisibility(View.GONE);
                        progressView.stopAnimation();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    //   adapter.getFilter().filter(s);
                    App.setQueryResult(s);
                    if (s.length() >= 3) {
                        searchHistoryList.clear();
                        listView.setVisibility(View.VISIBLE);
                        findViewById(R.id.containerRecent).setVisibility(View.GONE);
                        queryText = s;

                        if (networkOk) {
                            progressView.setVisibility(View.VISIBLE);
                            progressView.startAnimation();
                            Call<List<SearchUser>> call = webService.searchUser(deviceId, profileId, token, s);
                            sendRequest(call);

                        } else {
                            Utils.showNetworkDialog(getSupportFragmentManager());
                            progressView.setVisibility(View.GONE);
                            progressView.stopAnimation();
                        }


                    } else {
                        findViewById(R.id.containerRecent).setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        tvShowSearchResult.setVisibility(View.GONE);

                        Utils.showCustomToast(LikerSearch.this, mView, " Write Minimum Three Characters !", Gravity.TOP);

                    }


                    return true;

                }


            });

            search.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    closeSearch();
                    return true;
                }
            });


        }

        return super.onCreateOptionsMenu(menu);
    }

    private void sendHistoryRequest(Call<List<SearchHistory>> call) {

        call.enqueue(new Callback<List<SearchHistory>>() {
            @Override
            public void onResponse(Call<List<SearchHistory>> call, Response<List<SearchHistory>> response) {

                SearchHistory searchHistory = new SearchHistory();
                searchHistoryList = response.body();
                if (searchHistoryList.size() > 0) {
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                    for (SearchHistory item : searchHistoryList) {
                        searchHistory.id = item.id;
                        searchHistory.userId = item.userId;
                        searchHistory.searchUserId = item.searchUserId;
                        searchHistory.searchText = item.searchText;
                        searchHistory.searchTime = item.searchTime;
                    }

                    findViewById(R.id.containerRecent).setVisibility(View.VISIBLE);
                    searchHistoryAdapter = new SearchHistoryAd(LikerSearch.this, searchHistoryList);
                    listView.setAdapter(searchHistoryAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            if (networkOk) {
                                progressView.setVisibility(View.VISIBLE);
                                progressView.startAnimation();
                                Call<AdvanceSearches> call = webService.advanceSearch(deviceId, profileId, token, mProfileId, searchHistoryList.get(position).searchText, 15, 0);

                                sendAdvanceSearchRequest(call);

                            } else {
                                Utils.showNetworkDialog(getSupportFragmentManager());
                                progressView.setVisibility(View.GONE);
                                progressView.stopAnimation();
                            }
                        }
                    });
                    tvShowSearchResult.setVisibility(View.GONE);

                } else {
                    Utils.toast(LikerSearch.this, "There is no history", R.drawable.ic_info_outline_blue_24dp);
                    findViewById(R.id.containerRecent).setVisibility(View.GONE);
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);

                }


            }

            @Override
            public void onFailure(Call<List<SearchHistory>> call, Throwable t) {
                Log.d("Message:", t.getMessage());
            }
        });
    }


    private void sendRequest(Call<List<SearchUser>> call) {
        call.enqueue(new Callback<List<SearchUser>>() {
            @Override
            public void onResponse(Call<List<SearchUser>> call, Response<List<SearchUser>> response) {

                SearchUser searchUser = new SearchUser();
                searchUserList = response.body();
                if (searchUserList.size() > 0) {
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                    for (SearchUser item : searchUserList) {
                        searchUser.userId = item.userId;
                        searchUser.firstName = item.firstName;
                        searchUser.lastName = item.lastName;
                        searchUser.fullname = item.fullname;
                        searchUser.userName = item.userName;
                        searchUser.totalBadge = item.totalBadge;
                        searchUser.photo = item.photo;
                        searchUser.totalLikes = item.totalLikes;
                        searchUser.goldStars = item.goldStars;
                        searchUser.sliverStars = item.sliverStars;
                        searchUser.foundingUser = item.foundingUser;

                    }

                    searchUsersAdapter = new SearchUsersAd(LikerSearch.this, searchUserList);
                    listView.setAdapter(searchUsersAdapter);
                    tvShowSearchResult.setVisibility(View.VISIBLE);
                    tvShowSearchResult.setText("Search results for " + queryText);


                } else {
                    progressView.stopAnimation();
                    progressView.setVisibility(View.GONE);
                    Utils.toast(LikerSearch.this, "We could not find anything for " + queryText, R.drawable.ic_info_outline_blue_24dp);

                }


            }

            @Override
            public void onFailure(Call<List<SearchUser>> call, Throwable t) {
                Log.d("Message:", t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: {
                isSearch = true;
                searchToolbar.setVisibility(View.VISIBLE);

                prepareActionBar(searchToolbar);
                supportInvalidateOptionsMenu();
                return true;
            }
            case android.R.id.home:
                closeSearch();
               // startActivity(new Intent(LikerSearch.this, Liker.class));
                startActivity(new Intent(LikerSearch.this, Home.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeSearch() {
        if (isSearch) {
            isSearch = false;
            prepareActionBar(toolbar);
            searchToolbar.setVisibility(View.GONE);
            supportInvalidateOptionsMenu();
        }
    }

    private void prepareActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.tvClear:
                if (networkOk) {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();
                    Call<Message> call = webService.clearHistory(deviceId, profileId, token, mProfileId);
                    sendClearRequest(call);

                } else {
                    Utils.showNetworkDialog(getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }
                break;

            case R.id.tvShowSearchResult:
                if (networkOk) {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.startAnimation();
                    Call<AdvanceSearches> call = webService.advanceSearch(deviceId, profileId, token, mProfileId, queryText, 15, 0);

                    sendAdvanceSearchRequest(call);

                } else {
                    Utils.showNetworkDialog(getSupportFragmentManager());
                    progressView.setVisibility(View.GONE);
                    progressView.stopAnimation();
                }
                break;

        }

    }

    private void sendAdvanceSearchRequest(Call<AdvanceSearches> call) {

        call.enqueue(new Callback<AdvanceSearches>() {


            @Override
            public void onResponse(Call<AdvanceSearches> call, Response<AdvanceSearches> response) {


                AdvanceSearches advanceSearches = response.body();


                Intent intent = new Intent(LikerSearch.this, SearchActivity.class);
                intent.putExtra("ADVANCE-SEARCH", (Parcelable) advanceSearches);
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
                startActivity(intent);


            }

            @Override
            public void onFailure(Call<AdvanceSearches> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });
    }

    private void sendClearRequest(Call<Message> call) {

        call.enqueue(new Callback<Message>() {


            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Message message = response.body();
                if (message.isStatus()) {
                    searchHistoryList.clear();
                    listView.setVisibility(View.GONE);
                    findViewById(R.id.containerRecent).setVisibility(View.GONE);

                    Utils.toast(LikerSearch.this, "History clear!", R.drawable.ic_check_black_24dp);
                } else {
                    Utils.toast(LikerSearch.this, "Please wait.!", R.drawable.ic_info_outline_blue_24dp);

                }
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });
    }



}
