package com.doodle.Home.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.doodle.Home.adapter.StarContributorSubCategoryAdapter;
import com.doodle.Home.adapter.StarContributorsAdapter;
import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.model.StarContributor;
import com.doodle.Home.service.CategoryExpandListener;
import com.doodle.Home.service.HomeService;
import com.doodle.Home.service.StarContributorCategoryListener;
import com.doodle.R;
import com.doodle.Tool.PrefManager;
import com.github.florent37.viewtooltip.ViewTooltip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarContributorActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    NestedScrollView scrollView;
    SearchView searchView;
    TextView tvFilterItem, tvAlertText, tvCategoryName;
    ImageView ivStarContributorInfo;
    ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    private HomeService webService;
    private PrefManager manager;
    private StarContributorsAdapter starContributorsAdapter;

    private String token, deviceId, userId, selectedCategory = "", selectedCategoryName = "", searchKey = "";
    private int limit = 20, offset = 0;
    private boolean isScrolling = true;

    private ArrayList<PostFilterSubCategory> subCategories, allCategories;
    private ArrayList<StarContributor> starContributors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_contributor);

        initialComponent();
        sendCategoryListRequest();
    }

    private void initialComponent() {
        selectedCategory = getIntent().getStringExtra("category_id");
        selectedCategoryName = getIntent().getStringExtra("category_name");

        layoutManager = new LinearLayoutManager(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        scrollView = findViewById(R.id.scrollView);
        searchView = findViewById(R.id.search_view);
        tvFilterItem = findViewById(R.id.filterItem);
        tvAlertText = findViewById(R.id.alertText);
        tvCategoryName = findViewById(R.id.categoryName);
        ivStarContributorInfo = findViewById(R.id.star_contributor_info);
        progressBar = findViewById(R.id.progress_bar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        manager = new PrefManager(this);
        webService = HomeService.mRetrofit.create(HomeService.class);
        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();
        subCategories = new ArrayList<>();
        allCategories = new ArrayList<>();
        starContributors = new ArrayList<>();

        starContributorsAdapter = new StarContributorsAdapter(this, starContributors);
        recyclerView.setAdapter(starContributorsAdapter);

        tvFilterItem.setText(selectedCategoryName.isEmpty() ? getString(R.string.all_categories) : selectedCategoryName);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivStarContributorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewTooltip
                        .on(StarContributorActivity.this, ivStarContributorInfo)
                        .autoHide(true, 2000)
                        .color(Color.WHITE)
                        .textColor(Color.parseColor("#1483c9"))
                        .corner(30)
                        .position(ViewTooltip.Position.RIGHT)
                        .text(getString(R.string.the_star_contributors_tab_ranks))
                        .show();
            }
        });

        tvFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchKey = s;
                if (s.isEmpty()) {
                    offset = 0;
                    sendUserRankingRequest();
                } else if (s.length() >= 2) {
                    offset = 0;
                    sendUserRankingRequest();
                }
                return false;
            }
        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentItems = layoutManager.getChildCount();
//                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
//                totalItems = layoutManager.getItemCount();
//
//                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
//                    isScrolling = false;
////                    sendUserRankingPaginationRequest();
//                }
//            }
//        });
//
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff == 0) {
                    if (isScrolling)
                        sendUserRankingPaginationRequest();
                }
            }
        });

    }

    private void showFilterDialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.single_post_category_filter_layout);

        Toolbar toolbar = dialog.findViewById(R.id.toolbar);
        EditText etSearchCategory;
        RecyclerView recyclerView;
        LinearLayoutManager linearLayoutManager;
        etSearchCategory = dialog.findViewById(R.id.search_category);
        recyclerView = dialog.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        etSearchCategory.setVisibility(View.VISIBLE);

        StarContributorCategoryListener starContributorCategoryListener = new StarContributorCategoryListener() {
            @Override
            public void onCategoryClick(String catId, String catName) {
                tvFilterItem.setText(catName);
                selectedCategory = catId;
                offset = 0;
                progressDialog.show();
                sendUserRankingRequest();
                dialog.dismiss();
            }
        };

        subCategories.addAll(allCategories);

        CategoryExpandListener categoryExpandListener = new CategoryExpandListener() {
            @Override
            public void onExpand(View view) {
                int itemToScroll = recyclerView.getChildLayoutPosition(view);
                int centerOfScreen = recyclerView.getHeight() / 2 - view.getHeight() / 2;
                linearLayoutManager.scrollToPositionWithOffset(itemToScroll, centerOfScreen);
            }
        };

        StarContributorSubCategoryAdapter subCategoryAdapter = new StarContributorSubCategoryAdapter(this, subCategories, starContributorCategoryListener, categoryExpandListener, selectedCategory);
        recyclerView.setAdapter(subCategoryAdapter);

        etSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = etSearchCategory.getText().toString();
                subCategories.clear();
                if (key.isEmpty()) {
                    subCategories.addAll(allCategories);
                } else {
                    for (PostFilterSubCategory category : allCategories) {
                        ArrayList<PostFilterItem> arrayList = new ArrayList<>();
                        PostFilterSubCategory postFilterSubCategory = new PostFilterSubCategory();
                        for (PostFilterItem postFilterItem : category.getPostFilterItems()) {
                            if (postFilterItem.getItemName().toLowerCase().contains(key.toLowerCase())) {
                                arrayList.add(postFilterItem);
                            }
                        }
                        postFilterSubCategory.setCatId(category.getCatId());
                        postFilterSubCategory.setSubCatId(category.getSubCatId());
                        postFilterSubCategory.setSubCatName(category.getSubCatName());
                        postFilterSubCategory.setSelectedAll(category.isSelectedAll());
                        postFilterSubCategory.setPostFilterItems(arrayList);

                        if (postFilterSubCategory.getSubCatName().toLowerCase().contains(key.toLowerCase())) {
                            subCategories.add(postFilterSubCategory);
                        } else {
                            if (arrayList.size() != 0) {
                                subCategories.add(postFilterSubCategory);
                            }
                        }
                    }
                }
                subCategoryAdapter.notifyDataSetChanged();
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendUserRankingRequest() {
        Call<ArrayList<StarContributor>> call = webService.getUserRanking(deviceId, userId, token, selectedCategory, searchKey, offset, limit);
        call.enqueue(new Callback<ArrayList<StarContributor>>() {
            @Override
            public void onResponse(Call<ArrayList<StarContributor>> call, Response<ArrayList<StarContributor>> response) {
                ArrayList<StarContributor> arrayList = response.body();
                starContributors.clear();
                if (arrayList != null) {
                    starContributors.addAll(arrayList);
                    offset += limit;
                }
                starContributorsAdapter.notifyDataSetChanged();

                if (starContributors.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }

                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ArrayList<StarContributor>> call, Throwable t) {
                starContributors.clear();
                starContributorsAdapter.notifyDataSetChanged();
                if (starContributors.size() == 0) {
                    tvAlertText.setVisibility(View.VISIBLE);
                } else {
                    tvAlertText.setVisibility(View.GONE);
                }
                progressDialog.hide();
            }
        });
    }

    private void sendUserRankingPaginationRequest() {
        isScrolling = false;
        progressBar.setVisibility(View.VISIBLE);
        Call<ArrayList<StarContributor>> call = webService.getUserRanking(deviceId, userId, token, selectedCategory, searchKey, offset, limit);
        call.enqueue(new Callback<ArrayList<StarContributor>>() {
            @Override
            public void onResponse(Call<ArrayList<StarContributor>> call, Response<ArrayList<StarContributor>> response) {
                ArrayList<StarContributor> arrayList = response.body();
                if (arrayList != null) {
                    starContributors.addAll(arrayList);
                    starContributorsAdapter.notifyDataSetChanged();
                    offset += limit;
                }
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
                isScrolling = true;
            }

            @Override
            public void onFailure(Call<ArrayList<StarContributor>> call, Throwable t) {
                progressDialog.hide();
                progressBar.setVisibility(View.GONE);
                isScrolling = true;
            }
        });
    }

    private void sendCategoryListRequest() {
        Call<String> call = webService.sendFilterCategories(deviceId, userId, token, userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            JSONObject object = new JSONObject(response.body());
                            JSONArray jsonArray = object.getJSONArray("categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String subCatId = jsonObject.getString("category_id");
                                String subCatName = jsonObject.getString("category_name");
                                ArrayList<PostFilterItem> singleArrayList = new ArrayList<>();
                                ArrayList<PostFilterItem> multipleArrayList = new ArrayList<>();
                                JSONArray array = jsonObject.getJSONArray("subcatg");
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject obj = array.getJSONObject(j);
                                    String itemId = obj.getString("sub_category_id");
                                    String itemName = obj.getString("sub_category_name");
                                    singleArrayList.add(new PostFilterItem("", subCatId, itemId, itemName, false));
                                    multipleArrayList.add(new PostFilterItem("", subCatId, itemId, itemName, false));
                                }
                                allCategories.add(new PostFilterSubCategory("", subCatId, subCatName, false, singleArrayList));
                            }
                            if (allCategories.size() > 0) {
                                allCategories.get(0).setSubCatId("0");
                                allCategories.get(0).setSubCatName("All Category");
                            }
                            sendUserRankingRequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                        Log.i("onSuccess", response.body().toString());
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        progressDialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }


}
