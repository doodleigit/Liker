package com.doodle.Profile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.model.Links;
import com.doodle.Profile.service.AboutComponentUpdateListener;
import com.doodle.R;
import com.doodle.Tool.Tools;

import java.util.ArrayList;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Links> arrayList;
    private AboutComponentUpdateListener aboutComponentUpdateListener;
    private boolean isOwnProfile;

    public SocialAdapter(Context context, ArrayList<Links> arrayList, AboutComponentUpdateListener aboutComponentUpdateListener, boolean isOwnProfile) {
        this.context = context;
        this.arrayList = arrayList;
        this.aboutComponentUpdateListener = aboutComponentUpdateListener;
        this.isOwnProfile = isOwnProfile;
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
        String socialLinkType, link;

        socialLinkType = arrayList.get(i).getType();
        link = arrayList.get(i).getLink();

        viewHolder.tvSocialLinkType.setText(Tools.getProfileType(socialLinkType));

        if (!link.isEmpty()) {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
        }

        if (isOwnProfile) {
            viewHolder.ivEdit.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivEdit.setVisibility(View.INVISIBLE);
        }

        String finalInstituteSite = link;
        viewHolder.tvSocialLinkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalInstituteSite));
                context.startActivity(browserIntent);
            }
        });

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutComponentUpdateListener.onLinkUpdate(arrayList.get(i));
            }
        });
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
