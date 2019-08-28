package com.doodle.Post.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.model.PostVideo;
import com.doodle.Post.view.activity.GalleryView;
import com.doodle.R;
import com.doodle.utils.AppConstants;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_IMAGE = 0;
    final int VIEW_TYPE_VIDEO = 1;

    Context context;
    List<PostImage> postImages;
    List<PostVideo> postVideos;

    public MediaAdapter(Context context, List<PostImage> postImages, List<PostVideo> postVideos) {
        this.context = context;
        this.postImages = postImages;
        this.postVideos = postVideos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_image, parent, false);
            //    return new AdvanceSearchAd.UserViewHolder(view);
            return new ImageViewHolder(view);
        }

        if (viewType == VIEW_TYPE_VIDEO) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_video, parent, false);
            //  return new AdvanceSearchAd.PostViewHolder(view);


            return new VideoViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ImageViewHolder) {
            ((ImageViewHolder) viewHolder).populate(postImages.get(position));
        }

        if (viewHolder instanceof VideoViewHolder) {
            ((VideoViewHolder) viewHolder).populate(postVideos.get(position - postImages.size()));
            // if not first item check if item above has the same header

        }
    }

    @Override
    public int getItemCount() {
        return postImages.size() + postVideos.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < postImages.size()) {
            return VIEW_TYPE_IMAGE;
        }

        if (position - postImages.size() < postVideos.size()) {
            return VIEW_TYPE_VIDEO;
        }

        return -1;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPost, imgPostCancel;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
            imgPostCancel = (ImageView) itemView.findViewById(R.id.imgPostCancel);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void populate(PostImage postImage) {

            String imagePhoto = postImage.getImagePath();

            if (imagePhoto.startsWith("file:")) {
                Glide.with(App.getAppContext()).load(imagePhoto)
                        .skipMemoryCache(false)
                        .into(imgPost);
            } else {
                String postImages = AppConstants.POST_IMAGES + imagePhoto;
                Glide.with(App.getAppContext())
                        .load(postImages)
                        .skipMemoryCache(false)
                        .into(imgPost);
            }


            imgPostCancel.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    postImages.remove(getPosition());
                    notifyDataSetChanged();
                    return false;
                }
            });


        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageVideo, imagePlayVideo, imageDeleteVideo;
        RelativeLayout selectLayout;


        public VideoViewHolder(View itemView) {
            super(itemView);

            imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
            imagePlayVideo = (ImageView) itemView.findViewById(R.id.imagePlayVideo);
            imageDeleteVideo = (ImageView) itemView.findViewById(R.id.imageDeleteVideo);
            selectLayout = (RelativeLayout) itemView.findViewById(R.id.selectLayout);



        }

        @SuppressLint("ClickableViewAccessibility")
        public void populate(PostVideo postVideo) {

            String videoPhoto = postVideo.getVideoPath();

            Glide.with(context).load(videoPhoto)
                    .skipMemoryCache(false)
                    .into(imageVideo);
            imagePlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_gallery = new Intent(context, GalleryView.class);
                    intent_gallery.putExtra("video", videoPhoto);
                    intent_gallery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getAppContext().startActivity(intent_gallery);
                }
            });
            imageDeleteVideo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    postVideos.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    return false;
                }
            });
        }
    }

    public void addPagingData(List<PostVideo> postList) {

        for (PostVideo temp : postList
        ) {
            postVideos.add(temp);
        }
        notifyDataSetChanged();
    }
}
