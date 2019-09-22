package com.doodle.Home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.model.LikeUser;
import com.doodle.Home.service.LikeUserClickListener;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class LikeUserAdapter extends RecyclerView.Adapter<LikeUserAdapter.ViewHolder> {

    private Context context;
    private List<LikeUser> arrayList;
    private String userId;
    private LikeUserClickListener likeUserClickListener;

    public LikeUserAdapter(Context context, List<LikeUser> arrayList, String userId, LikeUserClickListener likeUserClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.userId = userId;
        this.likeUserClickListener = likeUserClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_follow, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String fullName, photo, likes, stars;
        fullName = arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName();
        photo = AppConstants.PROFILE_IMAGE + arrayList.get(i).getThumb();
        likes = arrayList.get(i).getLike();
        stars = arrayList.get(i).getGoldStar();

        viewHolder.userName.setText(fullName);
        viewHolder.likes.setText(likes + " " + context.getString(R.string.likes));
        viewHolder.stars.setText(stars + " " + context.getString(R.string.stars));

        if (arrayList.get(i).getId().equals(userId)) {
            viewHolder.follow.setVisibility(View.GONE);
        } else {
            viewHolder.follow.setVisibility(View.VISIBLE);
            if (arrayList.get(i).getIsFollowed()) {
                viewHolder.follow.setText(context.getString(R.string.unfollow));
            } else {
                viewHolder.follow.setText(context.getString(R.string.follow));
            }
        }

        Glide.with(App.getAppContext())
                .load(photo)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.userImage);

        viewHolder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(i).getIsFollowed()) {
                    likeUserClickListener.onUnFollowClick(arrayList.get(i).getId(), i);
                } else {
                    likeUserClickListener.onFollowClick(arrayList.get(i).getUsername(), i);
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", arrayList.get(i).getId()).putExtra("user_name", arrayList.get(i).getUsername()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, likes, stars;
        Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            likes = itemView.findViewById(R.id.likes);
            stars = itemView.findViewById(R.id.stars);
            follow = itemView.findViewById(R.id.follow);
        }
    }

}
