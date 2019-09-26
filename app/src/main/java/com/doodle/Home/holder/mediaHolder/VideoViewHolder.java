package com.doodle.Home.holder.mediaHolder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Post.model.PostImage;
import com.doodle.Post.model.PostVideo;
import com.doodle.Post.view.activity.GalleryView;
import com.doodle.R;
import com.doodle.Tool.AppConstants;

import java.util.ArrayList;
import java.util.List;

import static com.doodle.Tool.Tools.isNullOrEmpty;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageVideo, imagePlayVideo, imageDeleteVideo;
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
        imageDeleteVideo = (ImageView) itemView.findViewById(R.id.imageDeleteVideo);
        selectLayout = (RelativeLayout) itemView.findViewById(R.id.selectLayout);
        deleteMediaFiles = new ArrayList<>();

    }

    private PostVideo postVideo;
    private int position;

    @SuppressLint("ClickableViewAccessibility")
    public void populate(PostVideo postVideo, int position) {
        this.postVideo = postVideo;
        this.position = position;

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
