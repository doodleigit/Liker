package com.doodle.Home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doodle.Home.model.PostFilterItem;
import com.doodle.Home.model.PostFilterSubCategory;
import com.doodle.Home.service.StarContributorCategoryListener;
import com.doodle.R;

import java.util.ArrayList;

public class StarContributorFilterItemAdapter extends RecyclerView.Adapter<StarContributorFilterItemAdapter.ViewHolder> {

    private Context context;
    private boolean isSelectedAll;
    private ArrayList<PostFilterItem> arrayList;
    private String catId;
    private StarContributorCategoryListener starContributorCategoryListener;

    public StarContributorFilterItemAdapter(Context context, PostFilterSubCategory postFilterSubCategory, StarContributorCategoryListener starContributorCategoryListener, String catId) {
        this.context = context;
        this.arrayList = postFilterSubCategory.getPostFilterItems();
        this.catId = catId;
        this.starContributorCategoryListener = starContributorCategoryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        int i = viewHolder.getAdapterPosition();
        viewHolder.tvName.setText(arrayList.get(i).getItemName());

        if (arrayList.get(i).getItemId().equals(catId)) {
            viewHolder.ivAdd.setImageResource(R.drawable.ok);
        } else {
            viewHolder.ivAdd.setImageResource(R.drawable.plus);
        }

        viewHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starContributorCategoryListener.onCategoryClick(arrayList.get(position).getItemId(), arrayList.get(position).getItemName());
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
