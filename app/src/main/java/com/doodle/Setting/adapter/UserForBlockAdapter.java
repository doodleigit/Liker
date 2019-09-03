package com.doodle.Setting.adapter;

import android.content.Context;
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
import com.doodle.R;
import com.doodle.Setting.model.Friend;
import com.doodle.Setting.service.UserBlockClickListener;
import com.doodle.Tool.AppConstants;

import java.util.ArrayList;

public class UserForBlockAdapter extends RecyclerView.Adapter<UserForBlockAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Friend> arrayList;
    private UserBlockClickListener userBlockClickListener;

    public UserForBlockAdapter(Context context, ArrayList<Friend> arrayList, UserBlockClickListener userBlockClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.userBlockClickListener = userBlockClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_user_for_block, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvUserName.setText(arrayList.get(i).getFirstName() + " " + arrayList.get(i).getLastName());
        viewHolder.tvLikes.setText(arrayList.get(i).getTotalLikes() + " " + context.getString(R.string.likes));
        viewHolder.tvStars.setText(arrayList.get(i).getGoldStars() + " " + context.getString(R.string.stars));

        Glide.with(App.getAppContext())
                .load(AppConstants.PROFILE_IMAGE + arrayList.get(i).getPhoto())
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .centerCrop()
                .dontAnimate()
                .into(viewHolder.ivUserImage);

        viewHolder.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userBlockClickListener.onBlockClick(arrayList.get(i), i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserImage;
        TextView tvUserName, tvStars, tvLikes;
        Button btnBlock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserImage = itemView.findViewById(R.id.userImage);
            tvUserName = itemView.findViewById(R.id.userName);
            tvStars = itemView.findViewById(R.id.stars);
            tvLikes = itemView.findViewById(R.id.likes);
            btnBlock = itemView.findViewById(R.id.block);
        }
    }

}
