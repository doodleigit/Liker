package com.doodle.Home.holder.mediaHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.model.PostVideo;
import com.doodle.Post.view.activity.GalleryView;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.github.florent37.viewtooltip.ViewTooltip;

import java.util.ArrayList;
import java.util.List;

import static com.doodle.Tool.Tools.isNullOrEmpty;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageVideo, imagePlayVideo, imageDeleteVideo, imageDuplicate, imageUnique;
    TextView tvInfoGallery;
    RelativeLayout selectLayout;
    List<PostVideo> postVideos;
    List<String> deleteMediaFiles;



    public VideoListen videoListen;

    public interface VideoListen {
        void deleteVideo(PostVideo postVideo, int position);
    }

    public VideoViewHolder(View itemView, VideoListen videoListen) {
        super(itemView);
        this.videoListen = videoListen;
        imageVideo = (ImageView) itemView.findViewById(R.id.imageVideo);
        imagePlayVideo = (ImageView) itemView.findViewById(R.id.imagePlayVideo);
        imageDuplicate = (ImageView) itemView.findViewById(R.id.imageDuplicate);
        imageUnique = (ImageView) itemView.findViewById(R.id.imageUnique);
        tvInfoGallery = itemView.findViewById(R.id.tvInfoGallery);
        imageDeleteVideo = (ImageView) itemView.findViewById(R.id.imageDeleteVideo);
        selectLayout = (RelativeLayout) itemView.findViewById(R.id.selectLayout);
        deleteMediaFiles = new ArrayList<>();

    }

    private PostVideo postVideo;
    private int position;
    private Context mContext;
    @SuppressLint("ClickableViewAccessibility")
    public void populate(Context context, PostVideo postVideo, int position) {
        this.postVideo = postVideo;
        this.position = position;
        mContext=context;

        String videoPhoto = postVideo.getVideoPath();

         /*   Glide.with(context).load(videoPhoto)
                    .skipMemoryCache(false)
                    .into(imageVideo);
*/

        // String imagePhoto = postImage.getImagePath();

        if (videoPhoto.startsWith("file:")) {
            Glide.with(App.getAppContext()).load(videoPhoto)
                    .skipMemoryCache(false)
                    .into(imageVideo);
        } else {
            String postVideos = AppConstants.POST_VIDEOS + videoPhoto;
            Glide.with(App.getAppContext())
                    .load(postVideos)
                    .skipMemoryCache(false)
                    .into(imageVideo);
        }


        imagePlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_gallery = new Intent(App.getAppContext(), GalleryView.class);
                intent_gallery.putExtra("video", videoPhoto);
                intent_gallery.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getAppContext().startActivity(intent_gallery);
            }
        });


        if (postVideo.isDuplicate) {
            imageUnique.setVisibility(View.VISIBLE);
        } else {
            imageDuplicate.setVisibility(View.VISIBLE);
            imagePlayVideo.setVisibility(View.GONE);
            tvInfoGallery.setVisibility(View.VISIBLE);
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


        imageDeleteVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                videoListen.deleteVideo(postVideo, position);
                //  postVideos.remove(getPosition() - postImages.size());
//                String mediaId = postVideo.getVideoId();
//                if (!isNullOrEmpty(mediaId)) {
//                    deleteMediaFiles.add(mediaId);
//                    App.setDeleteMediaIds(deleteMediaFiles);
//                }

//                    postVideos.remove(getAdapterPosition());
//                notifyDataSetChanged();
                return false;
            }
        });
    }
}
