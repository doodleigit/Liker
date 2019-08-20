package com.doodle.Home.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.model.StarContributor;
import com.doodle.Home.service.StarContributorPaginationListener;
import com.doodle.R;
import com.doodle.utils.AppConstants;

import java.util.ArrayList;

public class StarContributorsAdapter extends RecyclerView.Adapter<StarContributorsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<StarContributor> arrayList;

    public StarContributorsAdapter(Context context, ArrayList<StarContributor> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_star_contributors, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvRank.setText(arrayList.get(i).getRank());
        viewHolder.tvScore.setText(arrayList.get(i).getRankPercent() + "%");
        viewHolder.tvName.setText(arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName());
        viewHolder.tvLikes.setText(String.valueOf(Integer.valueOf(arrayList.get(i).getGoldStars()) + Integer.valueOf(arrayList.get(i).getSliverStars())));

        if (Integer.valueOf(arrayList.get(i).getRankPercent()) > 5) {
            viewHolder.ivStarImage.setImageResource(R.drawable.ic_star_silver_24dp);
        } else {
            viewHolder.ivStarImage.setImageResource(R.drawable.ic_star_gold_24dp);
        }

        Glide.with(App.getAppContext())
                .load(AppConstants.PROFILE_IMAGE + arrayList.get(i).getPhoto())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.ivUserImage);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvRank, tvScore, tvName, tvLikes;
        ImageView ivStarImage, ivUserImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRank = itemView.findViewById(R.id.rank);
            tvScore = itemView.findViewById(R.id.top_score);
            tvName = itemView.findViewById(R.id.user_name);
            tvLikes = itemView.findViewById(R.id.likes);
            ivStarImage = itemView.findViewById(R.id.star_image);
            ivUserImage = itemView.findViewById(R.id.user_image);
        }
    }

}
