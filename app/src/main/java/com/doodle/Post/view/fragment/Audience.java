package com.doodle.Post.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Post.adapter.CategoryListAdapter;
import com.doodle.Post.model.Category;
import com.doodle.Post.model.CategoryItem;
import com.doodle.Post.model.Subcatg;
import com.doodle.R;
import com.doodle.Tool.PrefManager;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Audience extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetListener mListener;
    public static final String MESSAGE_key = "message_key";
    private PrefManager manager;

    private int previousGroup = -1;
    private Activity activity;
    private Context mContext;
    private CategoryListAdapter adapter;
    private CategoryItem categoryItem;
    private List<Category> mCategoryList;
    private List<Subcatg> subcatgList;
    private HashMap<Category, List<Subcatg>> childSubcatgList;
    private String profileId;
    private String deviceId;
    private String mProfileId;
    private String token;

    private ExpandableListView expandableListView;
    private CircularProgressView progressView;

    public static Audience newInstance(CategoryItem message) {

        Bundle args = new Bundle();
        args.putParcelable(Audience.MESSAGE_key, message);
        Audience fragment = new Audience();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private String message;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(App.getAppContext());
        mCategoryList = new ArrayList<>();
        subcatgList = new ArrayList<>();
        childSubcatgList = new HashMap<Category, List<Subcatg>>();
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetStyle);
        Bundle argument = getArguments();
        if (argument != null) {
            categoryItem = argument.getParcelable(MESSAGE_key);
        }
        mCategoryList = categoryItem.getCategories();
        for (Category temp : mCategoryList
        ) {
            subcatgList = temp.getSubcatg();
            childSubcatgList.put(temp, subcatgList);
        }

        profileId = manager.getProfileId();
        deviceId = manager.getDeviceId();
        token = manager.getToken();
        activity = getActivity();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.audience, container, false);

        progressView = root.findViewById(R.id.progress_view);
        expandableListView = root.findViewById(R.id.ExpandableListView);

        init();
        setListener();
      /*  switch (message) {
            case "Public":
                break;
            case "Friends":
                break;
            case "Only me":
                break;

        }*/
        return root;
    }

    @Override
    public void onClick(View v) {

        int myId = v.getId();
        switch (myId) {


        }
    }

    public interface BottomSheetListener {
        void onButtonClicked(int image, String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    private void init() {
        //  loadListData();
        adapter = new CategoryListAdapter(mContext, mCategoryList, childSubcatgList);
        expandableListView.setAdapter(adapter);
    }

    private void setListener() {
        // Listview Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                Toast.makeText(activity,
                        "Group Clicked " + mCategoryList.get(groupPosition).getCategoryName(),
                        Toast.LENGTH_SHORT).show();

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
                Toast.makeText(activity,
                        mCategoryList.get(groupPosition).getCategoryName() +
                                " Expanded", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(
                        activity,
                        mCategoryList.get(groupPosition).getCategoryName()
                                + " : "
                                + childSubcatgList.get(
                                mCategoryList.get(groupPosition)).get(
                                childPosition).getSubCategoryName(), Toast.LENGTH_SHORT)
                        .show();

                return false;
            }
        });
    }
}

