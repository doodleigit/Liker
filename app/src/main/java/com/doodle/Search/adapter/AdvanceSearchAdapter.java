package com.doodle.Search.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Search.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class AdvanceSearchAdapter extends RecyclerView.Adapter<AdvanceSearchAdapter.UserViewHolder> {

    private Context context;
    private List<User> mUser;

    public AdvanceSearchAdapter(Context context, List<User> mUser) {
        this.context = context;
        this.mUser = mUser;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_search_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder viewHolder, int position) {
        viewHolder.populate(mUser.get(position));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLike, tvStar;
        ImageView imgUser;

        public UserViewHolder(View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvStar = itemView.findViewById(R.id.tvStar);
            imgUser = itemView.findViewById(R.id.imgUser);
        }

        public void populate(User user) {
            tvUserName.setText(user.getFullname());
            tvLike.setText(user.getTotalLikes() + " " + context.getString(R.string.likes));
            int totalStar = Integer.parseInt(user.getSliverStars()) + Integer.parseInt(user.getGoldStars());
            tvStar.setText(String.valueOf(totalStar) + " " + context.getString(R.string.stars));
            tvUserName.setText(user.getFullname());
            String imagePhoto = POST_IMAGES + user.getPhoto();

            if (imagePhoto.length() > 0) {
                Picasso.with(App.getAppContext())
                        .load(imagePhoto)
                        .placeholder(R.drawable.drawable_comment)
                        .into(imgUser);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", user.getUserId()).putExtra("user_name", user.getUserName()));
                }
            });
        }
    }

}
