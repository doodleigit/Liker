package com.doodle.Search.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Profile.view.ProfileActivity;
import com.doodle.Search.model.SearchUser;
import com.doodle.App;
import com.doodle.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class SearchUsersAd extends ArrayAdapter<SearchUser> {

    private Context context;
    private List<SearchUser> mSearchUsers;
    private LayoutInflater mInflater;

    public SearchUsersAd(@NonNull Context context, @NonNull List<SearchUser> objects) {
        super(context, R.layout.advance_search_user_item, objects);
        this.context = context;
        mSearchUsers = objects;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.advance_search_user_item, parent, false);
        }
        ImageView mImage = (ImageView) convertView.findViewById(R.id.imgUser);
        TextView mName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView mLike = (TextView) convertView.findViewById(R.id.tvLike);
        TextView mStar = (TextView) convertView.findViewById(R.id.tvStar);

        SearchUser item = mSearchUsers.get(position);

        String totalaStar = item.goldStars + item.sliverStars;
        String image_url = POST_IMAGES + item.photo;
        mName.setText(item.fullname);

        mLike.setText(item.totalLikes);
        mStar.setText(totalaStar + " " + context.getString(R.string.stars));

        if (image_url.length() > 0) {
            Picasso.with(App.getAppContext())
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(mImage);

        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra("user_id", mSearchUsers.get(position).getUserId()).putExtra("user_name", mSearchUsers.get(position).getUserName()));
            }
        });

        return convertView;
    }
}