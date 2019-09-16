package com.doodle.Post.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.Post.model.Category;
import com.doodle.Post.model.Subcatg;
import com.doodle.Post.view.activity.PostCategory;
import com.doodle.R;

import java.util.HashMap;
import java.util.List;

public class CategoryListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    //Group header titles
    private List<Category> listGroup;
    // child data in format of header title, child title
    private HashMap<Category, List<Subcatg>> listChild;

    public CategoryListAdapter(Context context, List<Category> listGroup,
                               HashMap<Category, List<Subcatg>> listChild) {
        this.mContext = context;
        this.listGroup = listGroup;
        this.listChild = listChild;
    }
    
    @Override
    public int getGroupCount() {
        return this.listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listChild.get(this.listGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listChild.get(this.listGroup.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Category headerTitle = (Category) getGroup(groupPosition);
        String name=headerTitle.getCategoryName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_group, null);
        }

        TextView lblListHeader =  convertView.findViewById(R.id.groupHeader);
        ImageView add = convertView.findViewById(R.id.add);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(name);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.setmCategory(listGroup.get(groupPosition));
                App.setmSubcatg(null);
                ((PostCategory) mContext).onBackPressed();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Subcatg childText = (Subcatg) getChild(groupPosition, childPosition);
        String subName=childText.getSubCategoryName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_child, null);
        }

        TextView txtListChild =  convertView.findViewById(R.id.childItem);
        ImageView add = convertView.findViewById(R.id.add);
        txtListChild.setText(subName);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.setmCategory(listGroup.get(groupPosition));
                App.setmSubcatg(listChild.get(listGroup.get(groupPosition)).get(childPosition));
                ((PostCategory) mContext).onBackPressed();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
