package com.doodle.Home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.service.FilterClickListener;
import com.doodle.Home.service.SelectChangeListener;
import com.doodle.R;

import java.util.ArrayList;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.ViewHolder> {

    private Context context;
    private String catId, subCatId, subCatName;
    private boolean isSelectedAll;
    private ArrayList<PostFilterItem> arrayList;
    private FilterClickListener filterClickListener;
    private SelectChangeListener selectChangeListener;

    public FilterItemAdapter(Context context, PostFilterSubCategory postFilterSubCategory, FilterClickListener filterClickListener, SelectChangeListener selectChangeListener) {
        this.context = context;
        this.catId = postFilterSubCategory.getCatId();
        this.subCatId = postFilterSubCategory.getSubCatId();
        this.subCatName = postFilterSubCategory.getSubCatName();
        this.isSelectedAll = postFilterSubCategory.isSelectedAll();
        this.arrayList = postFilterSubCategory.getPostFilterItems();
        this.selectChangeListener = selectChangeListener;
        this.filterClickListener = filterClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(arrayList.get(i).getItemName());

        if (arrayList.get(i).isSelected()) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(i).isSelected()) {
                    viewHolder.ivAdd.setImageResource(R.drawable.plus);
                    arrayList.get(i).setSelected(false);
                    ArrayList<PostFilterItem> postFilterItems = new ArrayList<>();
                    postFilterItems.add(arrayList.get(i));
                    selectChangeListener.onSelectChange(false);
                    filterClickListener.onSingleFilterItemDeselect(new PostFilterSubCategory(catId, subCatId, subCatName, isSelectedAll, postFilterItems));
                } else {
                    viewHolder.ivAdd.setImageResource(R.drawable.ok);
                    arrayList.get(i).setSelected(true);
                    ArrayList<PostFilterItem> postFilterItems = new ArrayList<>();
                    postFilterItems.add(arrayList.get(i));
                    filterClickListener.onSingleFilterItemSelect(new PostFilterSubCategory(catId, subCatId, subCatName, isSelectedAll, postFilterItems));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mainLayout;
        TextView tvName;
        ImageView ivAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvName = itemView.findViewById(R.id.name);
            ivAdd = itemView.findViewById(R.id.add);
        }
    }

}
