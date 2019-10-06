package com.doodle.Post.view.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Post.adapter.CategoryListAdapter;
import com.doodle.Post.model.Category;
import com.doodle.Post.model.CategoryItem;
import com.doodle.Post.model.Subcatg;
import com.doodle.Post.service.PostService;
import com.doodle.R;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.doodle.Tool.fragment.Network;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCategory extends AppCompatActivity {

    private PrefManager manager;
    private PostService webService;
    private final String TAG = "PostCategory";
    private boolean networkOk;
    private CircularProgressView progressView;

    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;
    private CategoryItem categoryItem;
    private EditText et_search;

    //ExpandableListView
    //initialize
    private Activity activity;
    ExpandableListView expandableListView;
    CategoryListAdapter adapter;
    private int previousGroup = -1;

    List<Category> mCategoryList;
    List<Subcatg> subcatgList;
    HashMap<Category, List<Subcatg>> childSubcatgList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audience);
        manager = new PrefManager(this);
        webService = PostService.mRetrofit.create(PostService.class);
        networkOk = NetworkHelper.hasNetworkAccess(this);

        toolbar = (Toolbar) findViewById(R.id.main_activiy_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Audience");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        childSubcatgList = new HashMap<Category, List<Subcatg>>();
        mCategoryList = new ArrayList<>();

        et_search = (EditText) findViewById(R.id.et_search);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);
        expandableListView = findViewById(R.id.ExpandableListView);
        // categoryItem = new CategoryItem();
        profileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        activity = PostCategory.this;

        if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation();
            Call<CategoryItem> call = webService.getCategories(deviceId, profileId, token);
            sendCategoryRequest(call);
        } else {
            Tools.showNetworkDialog(getSupportFragmentManager());
            progressView.setVisibility(View.GONE);
            progressView.stopAnimation();

        }

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = et_search.getText().toString();
                mCategoryList.clear();
                if (key.isEmpty()) {
                    mCategoryList.addAll(categoryItem.getCategories());
                } else {
                    for (Category category : categoryItem.getCategories()) {
                        ArrayList<Subcatg> arrayList = new ArrayList<>();
                        Category postFilterSubCategory = new Category();
                        for (Subcatg subcatg : category.getSubcatg()) {
                            if (subcatg.getSubCategoryName().toLowerCase().contains(key.toLowerCase())) {
                                arrayList.add(subcatg);
                            }
                        }
                        postFilterSubCategory.setCategoryId(category.getCategoryId());
                        postFilterSubCategory.setCategoryName(category.getCategoryName());
                        postFilterSubCategory.setSubcatg(arrayList);

                        if (postFilterSubCategory.getCategoryName().toLowerCase().contains(key.toLowerCase())) {
                            mCategoryList.add(postFilterSubCategory);
                        } else {
                            if (arrayList.size() != 0) {
                                mCategoryList.add(postFilterSubCategory);
                            }
                        }
                    }
                }
                for (Category temp : mCategoryList) {
                    subcatgList = temp.getSubcatg();
                    childSubcatgList.put(temp, subcatgList);

                }
                adapter = new CategoryListAdapter(PostCategory.this, mCategoryList, childSubcatgList);
                expandableListView.setAdapter(adapter);
            }
        });

        setListener();

    }

    private void sendCategoryRequest(Call<CategoryItem> call) {
        call.enqueue(new Callback<CategoryItem>() {


            @Override
            public void onResponse(Call<CategoryItem> call, Response<CategoryItem> response) {


                categoryItem = response.body();
                if (categoryItem != null) {
                    mCategoryList.addAll(categoryItem.getCategories());
                }
                for (Category temp : mCategoryList
                ) {
                    subcatgList = temp.getSubcatg();
                    childSubcatgList.put(temp, subcatgList);

                }

                adapter = new CategoryListAdapter(PostCategory.this, mCategoryList, childSubcatgList);
                expandableListView.setAdapter(adapter);

                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();


            }

            @Override
            public void onFailure(Call<CategoryItem> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                progressView.setVisibility(View.GONE);
                progressView.stopAnimation();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
    }

    private void setListener() {
        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(activity, "Group Clicked " + mCategoryList.get(groupPosition).getCategoryName(), Toast.LENGTH_SHORT).show();

                // only one group is populate using this
                if (expandableListView.isGroupExpanded(groupPosition)) {
                    expandableListView.collapseGroup(groupPosition);
                    previousGroup = -1;
                } else {
                    expandableListView.expandGroup(groupPosition);
                    if (previousGroup != -1) {
                        expandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = groupPosition;
                }
                return true;
            }
        });

        // Listview Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                // Toast.makeText(activity, mCategoryList.get(groupPosition).getCategoryName() + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(activity,
                        mCategoryList.get(groupPosition).getCategoryName() +
                                " Collapsed", Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

//                String postAudience = childSubcatgList.get(mCategoryList.get(groupPosition)).get(childPosition).getSubCategoryName();
//                Toast.makeText(activity, mCategoryList.get(groupPosition).getCategoryName() + " : " + postAudience, Toast.LENGTH_SHORT).show();
                App.setmCategory(mCategoryList.get(groupPosition));
                App.setmSubcatg(childSubcatgList.get(mCategoryList.get(groupPosition)).get(childPosition));
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("Category", mCategoryList.get(groupPosition));
//                bundle.putParcelable("Subcatg", childSubcatgList.get(mCategoryList.get(groupPosition)).get(childPosition));
//                Intent intent = new Intent(PostCategory.this, PostNew.class);
//
//                intent.putExtras(bundle);
//                startActivity(intent);
                onBackPressed();

                //  overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
                return false;
            }
        });
    }

    private void showNetworkDialog() {
        Network network = new Network();
        // TODO: Use setCancelable() to make the dialog non-cancelable
        network.setCancelable(false);
        network.show(getSupportFragmentManager(), "NetworkDialogFragment");
    }
}
