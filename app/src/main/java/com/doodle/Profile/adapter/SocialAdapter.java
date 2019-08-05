package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.model.Links;
import com.doodle.R;

import java.util.ArrayList;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Links> arrayList;

    public SocialAdapter(Context context, ArrayList<Links> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_social_links, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSocialLinkType;
        ImageView ivSocialIcon, ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSocialLinkType = itemView.findViewById(R.id.social_link_type);
            ivSocialIcon = itemView.findViewById(R.id.social_icon);
            ivEdit = itemView.findViewById(R.id.edit);
        }
    }

}
