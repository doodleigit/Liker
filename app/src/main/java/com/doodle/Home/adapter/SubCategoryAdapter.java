package com.doodle.Home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.service.FilterClickListener;
import com.doodle.Home.service.SelectChangeListener;
import com.doodle.R;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostFilterSubCategory> arrayList, mainArrayList;
    private FilterClickListener filterClickListener;
    private int type;

    public SubCategoryAdapter(Context context, ArrayList<PostFilterSubCategory> arrayList, ArrayList<PostFilterSubCategory> mainArrayList, FilterClickListener filterClickListener, int type) {
        this.context = context;
        this.arrayList = arrayList;
        this.mainArrayList = mainArrayList;
        this.filterClickListener = filterClickListener;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subcategory_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(arrayList.get(position).getSubCatName());

        if (arrayList.get(position).isSelectedAll()) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        SelectChangeListener selectChangeListener = new SelectChangeListener() {
            @Override
            public void onSelectChange(boolean isSelect) {
                arrayList.get(position).setSelectedAll(isSelect);
                viewHolder.ivAdd.setImageResource(R.drawable.plus);
            }

            @Override
            public void onSelectClear() {
                clearAllSelect();
            }
        };

        FilterItemAdapter filterItemAdapter = new FilterItemAdapter(context, arrayList.get(position), filterClickListener, selectChangeListener, type);
        viewHolder.recyclerView.setAdapter(filterItemAdapter);

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    if (!arrayList.get(position).isSelectedAll()) {
                        clearAllSelect();
                        viewHolder.ivAdd.setImageResource(R.drawable.ok);
                        for (int i = 0; i < arrayList.get(position).getPostFilterItems().size(); i++) {
                            arrayList.get(position).getPostFilterItems().get(i).setSelected(true);
                        }
                        arrayList.get(position).setSelectedAll(true);
                        filterClickListener.onSingleSubCategorySelect(null);
                        filterItemAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (arrayList.get(position).isSelectedAll()) {
                        viewHolder.ivAdd.setImageResource(R.drawable.plus);
                        for (int i = 0; i < arrayList.get(position).getPostFilterItems().size(); i++) {
                            arrayList.get(position).getPostFilterItems().get(i).setSelected(false);
                        }
                        arrayList.get(position).setSelectedAll(false);
                        filterClickListener.onSingleSubCategoryDeselect(null);
                        filterItemAdapter.notifyDataSetChanged();
                    } else {
                        viewHolder.ivAdd.setImageResource(R.drawable.ok);
                        for (int i = 0; i < arrayList.get(position).getPostFilterItems().size(); i++) {
                            arrayList.get(position).getPostFilterItems().get(i).setSelected(true);
                        }
                        arrayList.get(position).setSelectedAll(true);
                        filterClickListener.onSingleSubCategorySelect(null);
                        filterItemAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.recyclerView.getVisibility() == View.VISIBLE) {
                    viewHolder.recyclerView.setVisibility(View.GONE);
                    viewHolder.ivArrow.setImageResource(R.drawable.arrow_right);
                } else {
                    viewHolder.recyclerView.setVisibility(View.VISIBLE);
                    viewHolder.ivArrow.setImageResource(R.drawable.arrow_down);
                }
            }
        });
    }

    private void clearAllSelect() {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setSelectedAll(false);
            for (int j = 0; j < arrayList.get(i).getPostFilterItems().size(); j++) {
                arrayList.get(i).getPostFilterItems().get(j).setSelected(false);
            }
        }
        for (int i = 0; i < mainArrayList.size(); i++) {
            mainArrayList.get(i).setSelectedAll(false);
            for (int j = 0; j < mainArrayList.get(i).getPostFilterItems().size(); j++) {
                mainArrayList.get(i).getPostFilterItems().get(j).setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
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
