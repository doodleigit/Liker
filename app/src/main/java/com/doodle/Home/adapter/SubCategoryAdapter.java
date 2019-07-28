package com.doodle.Home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.service.FilterClickListener;
import com.doodle.Home.service.SelectChangeListener;
import com.doodle.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostFilterSubCategory> arrayList;
    private FilterClickListener filterClickListener;

    public SubCategoryAdapter(Context context, ArrayList<PostFilterSubCategory> arrayList, FilterClickListener filterClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.filterClickListener = filterClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subcategory_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(arrayList.get(i).getSubCatName());

        if (arrayList.get(i).isSelectedAll()) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        SelectChangeListener selectChangeListener = new SelectChangeListener() {
            @Override
            public void onSelectChange(boolean isSelect) {
                arrayList.get(i).setSelectedAll(isSelect);
            }
        };

        FilterItemAdapter filterItemAdapter = new FilterItemAdapter(context, arrayList.get(i), filterClickListener, selectChangeListener);
        viewHolder.recyclerView.setAdapter(filterItemAdapter);

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(i).isSelectedAll()) {
                    viewHolder.ivAdd.setImageResource(R.drawable.plus);
                    for (int i = 0; i < arrayList.get(i).getPostFilterItems().size(); i++) {
                        arrayList.get(i).getPostFilterItems().get(i).setSelected(false);
                    }
                    arrayList.get(i).setSelectedAll(false);
                    filterClickListener.onSingleSubCategoryDeselect(arrayList.get(i));
                    filterItemAdapter.notifyDataSetChanged();
                } else {
                    viewHolder.ivAdd.setImageResource(R.drawable.ok);
                    for (int i = 0; i < arrayList.get(i).getPostFilterItems().size(); i++) {
                        arrayList.get(i).getPostFilterItems().get(i).setSelected(true);
                    }
                    arrayList.get(i).setSelectedAll(true);
                    filterClickListener.onSingleSubCategorySelect(arrayList.get(i));
                    filterItemAdapter.notifyDataSetChanged();
                }
            }
        });

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.recyclerView.getVisibility() == View.VISIBLE) {
                    viewHolder.recyclerView.setVisibility(View.GONE);
                } else {
                    viewHolder.recyclerView.setVisibility(View.VISIBLE);
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
        ImageView ivArrow, ivAdd;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvName = itemView.findViewById(R.id.name);
            ivArrow = itemView.findViewById(R.id.arrow);
            ivAdd = itemView.findViewById(R.id.add);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

}
