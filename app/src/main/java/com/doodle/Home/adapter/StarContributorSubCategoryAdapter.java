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
import android.widget.TextView;

import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.service.CategoryExpandListener;
import com.doodle.Home.service.StarContributorCategoryListener;
import com.doodle.R;

import java.util.ArrayList;

public class StarContributorSubCategoryAdapter extends RecyclerView.Adapter<StarContributorSubCategoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostFilterSubCategory> arrayList;
    private StarContributorCategoryListener starContributorCategoryListener;
    private CategoryExpandListener categoryExpandListener;
    private String catId;

    public StarContributorSubCategoryAdapter(Context context, ArrayList<PostFilterSubCategory> arrayList, StarContributorCategoryListener starContributorCategoryListener,
                                             CategoryExpandListener categoryExpandListener, String catId) {
        this.context = context;
        this.arrayList = arrayList;
        this.starContributorCategoryListener = starContributorCategoryListener;
        this.categoryExpandListener = categoryExpandListener;
        this.catId = catId;
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

        if (arrayList.get(position).getSubCatId().equals(catId)) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        StarContributorFilterItemAdapter starContributorFilterItemAdapter = new StarContributorFilterItemAdapter(context, arrayList.get(position), starContributorCategoryListener, catId);
        viewHolder.recyclerView.setAdapter(starContributorFilterItemAdapter);

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starContributorCategoryListener.onCategoryClick(arrayList.get(position).getSubCatId(), arrayList.get(position).getSubCatName());
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
                    categoryExpandListener.onExpand(view);
                }
            }
        });
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
