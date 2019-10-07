package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.doodle.R;
import com.doodle.Setting.adapter.ContributionAddedItemAdapter;
import com.doodle.Setting.adapter.ContributorCategoryAdapter;
import com.doodle.Setting.model.AddedCategory;
import com.doodle.Setting.model.Category;
import com.doodle.Setting.model.Contribution;
import com.doodle.Setting.model.ContributionItem;
import com.doodle.Setting.model.Info;
import com.doodle.Setting.model.SubCategory;
import com.doodle.Setting.service.ContributionAddListener;
import com.doodle.Setting.service.SettingService;
import com.doodle.Tool.PrefManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContributorSettingFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private LinearLayout mainLayout;
    private EditText etAddCategory;
    private RecyclerView categoryRecyclerView, selectedRecyclerView;

    private ProgressDialog progressDialog;
    private ArrayList<Category> allCategories;
    private ArrayList<Category> categories;
    private ArrayList<AddedCategory> addedCategories;
    private ContributionAddedItemAdapter contributionAddedItemAdapter;
    private ContributorCategoryAdapter contributorCategoryAdapter;
    private SettingService settingService;
    private PrefManager manager;
    private String deviceId, token, userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contributor_setting_fragment_layout, container, false);

        initialComponent();
        getAllContributorCategory();
        getContributorCategory();

        return view;
    }

    private void initialComponent() {
        allCategories = new ArrayList<>();
        categories = new ArrayList<>();
        addedCategories = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        settingService = SettingService.mRetrofit.create(SettingService.class);
        manager = new PrefManager(getContext());
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        userId = manager.getProfileId();

        toolbar = view.findViewById(R.id.toolbar);
        mainLayout = view.findViewById(R.id.main_layout);
        etAddCategory = view.findViewById(R.id.add_category);

        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryRecyclerView.setNestedScrollingEnabled(false);
        selectedRecyclerView = view.findViewById(R.id.selected_recycler_view);

        // Create the FlexboxLayoutMananger, only flexbox library version 0.3.0 or higher support.
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        // Set flex direction.
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        // Set JustifyContent.
        flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        selectedRecyclerView.setLayoutManager(flexboxLayoutManager);


//        selectedRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        selectedRecyclerView.setNestedScrollingEnabled(false);

        ContributionAddListener contributionAddListener = new ContributionAddListener() {
            @Override
            public void onContributionAddListener(String id, String name) {
                AddedCategory addedCategory = new AddedCategory();
                addedCategory.setId(id);
                addedCategory.setName(name);
                addedCategories.add(addedCategory);
                Objects.requireNonNull(selectedRecyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onContributionRemoveListener(String id, String name) {
                for (int i = 0; i < addedCategories.size(); i++) {
                    if (addedCategories.get(i).getId().equals(id)) {
                        addedCategories.remove(i);
                        break;
                    }
                }
                Objects.requireNonNull(selectedRecyclerView.getAdapter()).notifyDataSetChanged();
            }
        };

        contributionAddedItemAdapter = new ContributionAddedItemAdapter(getActivity(), addedCategories, progressDialog, settingService, deviceId, token, userId);
        selectedRecyclerView.setAdapter(contributionAddedItemAdapter);

        contributorCategoryAdapter = new ContributorCategoryAdapter(getActivity(), categories, contributionAddListener, progressDialog, settingService, deviceId, token, userId);
        categoryRecyclerView.setAdapter(contributorCategoryAdapter);

        etAddCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = etAddCategory.getText().toString();
                categories.clear();
                if (key.isEmpty()) {
                    categories.addAll(allCategories);
                    contributorCategoryAdapter.setSearchParam(false);
                } else {
                    for (Category category : allCategories) {
                        ArrayList<SubCategory> arrayList = new ArrayList<>();
                        Category cat = new Category();
                        for (SubCategory subCategory : category.getSubCategories()) {
                            if (subCategory.getName().toLowerCase().contains(key.toLowerCase())) {
                                arrayList.add(subCategory);
                            }
                        }
                        cat.setInfo(category.getInfo());
                        cat.setSubCategories(arrayList);

                        if (cat.getInfo().getName().toLowerCase().contains(key.toLowerCase())) {
                            categories.add(cat);
                        } else {
                            if (arrayList.size() != 0) {
                                categories.add(cat);
                            }
                        }
                    }
                    contributorCategoryAdapter.setSearchParam(true);
                }
                contributorCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etAddCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    categories.clear();
                    contributorCategoryAdapter.notifyDataSetChanged();
                    contributorCategoryAdapter.setSearchParam(false);
                } else {
                    String key = etAddCategory.getText().toString();
                    categories.clear();
                    if (key.isEmpty()) {
                        categories.addAll(allCategories);
                    } else {
                        for (Category category : allCategories) {
                            ArrayList<SubCategory> arrayList = new ArrayList<>();
                            Category cat = new Category();
                            for (SubCategory subCategory : category.getSubCategories()) {
                                if (subCategory.getName().toLowerCase().contains(key.toLowerCase())) {
                                    arrayList.add(subCategory);
                                }
                            }
                            cat.setInfo(category.getInfo());
                            cat.setSubCategories(arrayList);

                            if (cat.getInfo().getName().toLowerCase().contains(key.toLowerCase())) {
                                categories.add(cat);
                            } else {
                                if (arrayList.size() != 0) {
                                    categories.add(cat);
                                }
                            }
                        }
                        contributorCategoryAdapter.setSearchParam(true);
                    }
                    contributorCategoryAdapter.notifyDataSetChanged();
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private void getAllContributorCategory() {
        Call<Contribution> call = settingService.getContributorCategory(deviceId, userId, token, userId);
        call.enqueue(new Callback<Contribution>() {
            @Override
            public void onResponse(Call<Contribution> call, Response<Contribution> response) {
                Contribution contribution = response.body();
                if (contribution != null) {
                    allCategories.addAll(contribution.getCategories());
                    if (allCategories.size() > 0) {
                        allCategories.remove(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contribution> call, Throwable t) {

            }
        });
    }

    private void getContributorCategory() {
        progressDialog.show();
        Call<ContributionItem> call = settingService.getContributorView(deviceId, userId, token, userId);
        call.enqueue(new Callback<ContributionItem>() {
            @Override
            public void onResponse(Call<ContributionItem> call, Response<ContributionItem> response) {
                ContributionItem contributionItem = response.body();
                if (contributionItem != null) {
                    addedCategories.addAll(contributionItem.getCategories());
                    contributionAddedItemAdapter.notifyDataSetChanged();
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<ContributionItem> call, Throwable t) {
                progressDialog.hide();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            categories.clear();
            contributorCategoryAdapter.notifyDataSetChanged();
        }
    }



}
