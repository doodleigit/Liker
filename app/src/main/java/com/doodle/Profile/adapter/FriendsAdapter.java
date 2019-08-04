package com.doodle.Profile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Profile.model.Friend;
import com.doodle.R;
import com.doodle.utils.AppConstants;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Friend> arrayList;

    public FriendsAdapter(Context context, ArrayList<Friend> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_friend, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String fullName, photo, likes, stars;
        fullName = arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName();
        photo = AppConstants.PROFILE_IMAGE + arrayList.get(i).getPhoto();
        likes = arrayList.get(i).getTotalLikes();
        stars = arrayList.get(i).getGoldStars();

        viewHolder.userName.setText(fullName);
        viewHolder.likes.setText(likes + context.getString(R.string.likes));
        viewHolder.stars.setText(stars + context.getString(R.string.stars));

        Glide.with(App.getAppContext())
                .load(photo)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.userImage);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView userName, likes, stars, follow;

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
