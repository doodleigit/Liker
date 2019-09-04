package com.doodle.Home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.model.PostFile;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<PostFile> postFiles;
    private Context mContext;
    public static final String ITEM_KEY = "item_key";
    Drawable bitmapDrawable;
    private RecyclerViewClickListener mListener;


    public GalleryAdapter(Context context, List<PostFile> postFiles, RecyclerViewClickListener listener) {
        this.mContext = context;
        this.postFiles = postFiles;
        mListener = listener;
    }


    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.list_gallery_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostFile item = postFiles.get(position);

        if (item.getPostType().equals("2")) {
            holder.mediaVideoLayout.setVisibility(View.VISIBLE);
            holder.imageMedia.setVisibility(View.GONE);
            String postVideoThumbnail = AppConstants.POST_VIDEOS_THUMBNAIL + item.getImageName();

            Glide.with(App.getAppContext())
                    .load(postVideoThumbnail)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.mediaVideoThumbnail);
        } else {
            holder.mediaVideoLayout.setVisibility(View.GONE);
            holder.imageMedia.setVisibility(View.VISIBLE);
            String postImages = AppConstants.POST_IMAGES + item.getImageName();

            Glide.with(App.getAppContext())
                    .load(postImages)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.imageMedia);
        }

    }

    @Override
    public int getItemCount() {
        return postFiles.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;

        private RecyclerViewClickListener mListener;

        public FrameLayout mediaVideoLayout;
        public ImageView imageMedia, mediaVideoThumbnail, mediaVideoVolumeControl;
        public ProgressBar mediaVideoProgressBar;

        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mediaVideoLayout = itemView.findViewById(R.id.media_video);
            imageMedia = itemView.findViewById(R.id.media_image);
            mediaVideoThumbnail = itemView.findViewById(R.id.media_video_thumbnail);
            mediaVideoVolumeControl = itemView.findViewById(R.id.media_video_volume_control);
            mediaVideoProgressBar = itemView.findViewById(R.id.media_video_progressBar);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // mBitmap = bitmap;
            bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };
}