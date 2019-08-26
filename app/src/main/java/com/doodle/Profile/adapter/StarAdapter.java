package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doodle.Profile.model.Star;
import com.doodle.Profile.service.StarClickListener;
import com.doodle.R;

import java.util.ArrayList;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Star> arrayList;
    private StarClickListener starClickListener;

    public StarAdapter(Context context, ArrayList<Star> arrayList, StarClickListener starClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.starClickListener = starClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_star, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvName.setText(arrayList.get(i).getCategoryName());

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starClickListener.onStarCategoryClick(arrayList.get(i).getPostCategoryId());
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
        ImageView ivStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.main_layout);
            tvName = itemView.findViewById(R.id.name);
            ivStar = itemView.findViewById(R.id.star);
        }
    }

}
