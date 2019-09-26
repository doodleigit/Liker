package com.doodle.Home.holder.mediaHolder;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Post.model.PostImage;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.List;

import static com.doodle.Tool.Tools.isNullOrEmpty;



public  class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView imgPost, imgPostCancel;
    public ImageListener imageListener;
    public interface ImageListener {
        void deleteImage(PostImage postImage, int position);
    }

    List<PostImage> postImages;
    List<String> deleteMediaFiles;


    public ImageViewHolder(View itemView, ImageListener imageListener) {
        super(itemView);
        this.imageListener=imageListener;
        imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
        imgPostCancel = (ImageView) itemView.findViewById(R.id.imgPostCancel);
    }

    private PostImage postImage;
    private int position;

    @SuppressLint("ClickableViewAccessibility")
    public void populate(PostImage postImage, int position) {
        this.postImage = postImage;
        this.position = position;

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

                imageListener.deleteImage(postImage, position);
//                postImages.remove(getPosition());
//                String mediaId = postImage.getImageId();
//                if (!isNullOrEmpty(mediaId)) {
//                    deleteMediaFiles.add(mediaId);
//                    App.setDeleteMediaIds(deleteMediaFiles);
//                }
                return false;
            }
        });


    }
}
