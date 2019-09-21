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
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.holder.TextHolder;
import com.doodle.Home.holder.mediaHolder.ImageViewHolder;
import com.doodle.Home.holder.mediaHolder.VideoViewHolder;
import com.doodle.Home.model.PostItem;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.model.PostVideo;
import com.doodle.Post.view.activity.GalleryView;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.doodle.App.getPosition;
import static com.doodle.Tool.Tools.isNullOrEmpty;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_IMAGE = 0;
    final int VIEW_TYPE_VIDEO = 1;

    Context context;

    List<PostVideo> postVideos;
    List<PostImage> postImages;
    public ImageViewHolder.ImageListener imageListener;
    public VideoViewHolder.VideoListen videoListen;
    List<String> deleteMediaFiles;

    public MediaAdapter(Context context, List<PostImage> postImages, List<PostVideo> postVideos, ImageViewHolder.ImageListener imageListener, VideoViewHolder.VideoListen videoListen) {
        this.context = context;
        this.postImages = postImages;
        this.postVideos = postVideos;
        this.imageListener = imageListener;
        this.videoListen = videoListen;
        deleteMediaFiles = new ArrayList<>();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_IMAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_image, parent, false);
            //    return new AdvanceSearchAd.UserViewHolder(view);
            return new ImageViewHolder(view, imageListener);
        }

        if (viewType == VIEW_TYPE_VIDEO) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_video, parent, false);
            //  return new AdvanceSearchAd.PostViewHolder(view);

            return new VideoViewHolder(view, videoListen);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ImageViewHolder) {
            ((ImageViewHolder) viewHolder).populate(postImages.get(position), position);
        }

        if (viewHolder instanceof VideoViewHolder) {
            ((VideoViewHolder) viewHolder).populate(postVideos.get(position - postImages.size()), position);
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


    public void addPagingData(List<PostVideo> postList) {

        for (PostVideo temp : postList
        ) {
            postVideos.add(temp);
        }
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {

/*
        PostImage postImage = postImages.get(position);
        String mediaId = postImage.getImageId();
        if (!isNullOrEmpty(mediaId)) {
            deleteMediaFiles.add(mediaId);
            App.setDeleteMediaIds(deleteMediaFiles);
        }*/

        notifyDataSetChanged();
    }
}
