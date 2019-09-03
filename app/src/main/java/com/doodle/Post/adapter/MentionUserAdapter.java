package com.doodle.Post.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.Post.model.MentionUser;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.List;

public class MentionUserAdapter extends RecyclerView.Adapter<MentionUserAdapter.ViewHolder> {

    private List<MentionUser> mentionUsers;
    private Context mContext;
    private RecyclerViewClickListener mListener;

    public MentionUserAdapter(Context context, List<MentionUser> mentionUsers, RecyclerViewClickListener listener) {
        this.mContext = context;
        this.mentionUsers = mentionUsers;
        mListener = listener;
    }


    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_mention_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MentionUser mentionUser = mentionUsers.get(position);
        String name = mentionUser.getDisplay();
        String image = mentionUser.getPhoto();
        String imageUrl = AppConstants.PROFILE_IMAGE + image;
        Glide.with(mContext).load(imageUrl)
                .skipMemoryCache(false)
                .into(holder.imageMentionUser);
        holder.tvMentionUserName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mentionUsers.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;
        public LinearLayout mimContent;
        private RecyclerViewClickListener mListener;
        private ImageView imageMentionUser;
        private TextView tvMentionUserName;


        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mimContent = (LinearLayout) itemView.findViewById(R.id.mimContent);
            imageMentionUser = itemView.findViewById(R.id.imageMentionUser);
            tvMentionUserName = itemView.findViewById(R.id.tvMentionUserName);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }


}