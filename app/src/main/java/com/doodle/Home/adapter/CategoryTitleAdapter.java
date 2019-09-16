package com.doodle.Home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doodle.Home.model.CommonCategory;
import com.doodle.Home.service.CategoryRemoveListener;
import com.doodle.R;

import java.util.ArrayList;

public class CategoryTitleAdapter extends RecyclerView.Adapter<CategoryTitleAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CommonCategory> arrayList;
    private CategoryRemoveListener categoryRemoveListener;
    private int position = -1;
    private boolean isSelectable = true;

    public CategoryTitleAdapter(Context context, ArrayList<CommonCategory> arrayList, CategoryRemoveListener categoryRemoveListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.categoryRemoveListener = categoryRemoveListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_title_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tvCatName.setText(arrayList.get(i).getCatName());
        viewHolder.mainLayout.setBackgroundResource(R.drawable.rectangle_corner_round_five);
        viewHolder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryRemoveListener.onCategoryRemove(arrayList.get(i));
                arrayList.remove(i);
                notifyDataSetChanged();
            }
        });

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelectable) {
                    if (position == -1) {
                        position = i;
                        categoryRemoveListener.onCategorySelect(arrayList.get(i));
                        viewHolder.mainLayout.setBackgroundResource(R.drawable.rectangle_corner_round_six);
                    } else {
                        if (position == i) {
                            position = -1;
                            categoryRemoveListener.onCategoryDeSelect();
                            viewHolder.mainLayout.setBackgroundResource(R.drawable.rectangle_corner_round_five);
                        } else {
                            notifyItemChanged(position);
                            position = i;
                            categoryRemoveListener.onCategorySelect(arrayList.get(i));
                            viewHolder.mainLayout.setBackgroundResource(R.drawable.rectangle_corner_round_six);
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLayout;
        TextView tvCatName;
        ImageView ivClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvCatName = itemView.findViewById(R.id.categoryName);
            ivClose = itemView.findViewById(R.id.close);
        }
    }

}
