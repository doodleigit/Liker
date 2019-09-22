package com.doodle.Post.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.Post.model.LinkScriptItem;
import com.doodle.Post.model.MentionUser;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.List;

public class LinkScriptAdapter extends RecyclerView.Adapter<LinkScriptAdapter.ViewHolder> {

    private List<LinkScriptItem> linkScriptItems;
    private Context mContext;


    public LinkScriptAdapter(Context context, List<LinkScriptItem> linkScriptItems) {
        this.mContext = context;
        this.linkScriptItems = linkScriptItems;

    }


    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_item_link_script, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LinkScriptItem linkScriptItem = linkScriptItems.get(position);
        String title = linkScriptItem.getTitle();
        Bitmap image = linkScriptItem.getImage();
        String description=linkScriptItem.getDescription();

        holder.tvDescription.setText(description);
        holder.tvTitle.setText(title);

        String imageUrl = AppConstants.PROFILE_IMAGE + image;
//        Glide.with(mContext).load(imageUrl)
//                .skipMemoryCache(false)
//                .into(holder.imageMentionUser);

    }

    @Override
    public int getItemCount() {
        return linkScriptItems.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public LinearLayout mimContent;
        private RecyclerViewClickListener mListener;
        private ImageView image_post_set;
        private TextView tvTitle,tvClose, tvDescription;


        public ViewHolder(View itemView) {
            super(itemView);

            image_post_set = itemView.findViewById(R.id.image_post_set);
            tvClose = itemView.findViewById(R.id.tvClose);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }


    }


}