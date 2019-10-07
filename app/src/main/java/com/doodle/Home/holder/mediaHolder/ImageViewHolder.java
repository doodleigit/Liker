package com.doodle.Home.holder.mediaHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.view.activity.Home;
import com.doodle.Post.model.PostImage;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.github.florent37.viewtooltip.ViewTooltip;

import java.util.List;

import static com.doodle.Tool.Tools.isNullOrEmpty;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ImageViewHolder extends RecyclerView.ViewHolder {
    ImageView imgPost, imgPostCancel, imageDuplicate, imageUnique;
    TextView tvInfoGallery;
    public ImageListener imageListener;

    public interface ImageListener {
        void deleteImage(PostImage postImage, int position);
    }

    List<PostImage> postImages;
    List<String> deleteMediaFiles;


    public ImageViewHolder(View itemView, ImageListener imageListener) {
        super(itemView);
        this.imageListener = imageListener;
        imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
        imgPostCancel = (ImageView) itemView.findViewById(R.id.imgPostCancel);
        imageUnique = (ImageView) itemView.findViewById(R.id.imageUnique);
        imageDuplicate = (ImageView) itemView.findViewById(R.id.imageDuplicate);
        tvInfoGallery = itemView.findViewById(R.id.tvInfoGallery);
    }

    private PostImage postImage;
    private int position;
    private Context mContext;
    @SuppressLint("ClickableViewAccessibility")
    public void populate(Context context, PostImage postImage, int position) {
        this.postImage = postImage;
        this.position = position;
        mContext=context;

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

        if (postImage.isDuplicate) {
            imageDuplicate.setVisibility(View.VISIBLE);
            tvInfoGallery.setVisibility(View.VISIBLE);

        } else {
            imageUnique.setVisibility(View.VISIBLE);
        }
        tvInfoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewTooltip
                        .on((Activity) mContext, v)
                        .autoHide(true, 3000)
                        .color(Color.WHITE)
                        .textColor(Color.parseColor("#1483c9"))
                        .corner(30)
                        .position(ViewTooltip.Position.RIGHT)
                        .text("You have already add this")
                        .show();




            }
        });

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
