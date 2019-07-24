package com.doodle.Post.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.doodle.Post.view.activity.GalleryView;
import com.doodle.R;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    List<String> al_video;
    Context context;
    Activity activity;


    public VideoAdapter(Context context, List<String> al_video, Activity activity) {

        this.al_video = al_video;
        this.context = context;
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image, imagePlayVideo, imageDeleteVideo;
        RelativeLayout rl_select;

        public ViewHolder(View v) {

            super(v);

            iv_image = (ImageView) v.findViewById(R.id.imageVideo);
            imageDeleteVideo = (ImageView) v.findViewById(R.id.imageDeleteVideo);
            rl_select = (RelativeLayout) v.findViewById(R.id.selectLayout);

        }
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_video, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);
        return viewHolder1;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        Glide.with(context).load("file://" + al_video.get(position).toString())
                .skipMemoryCache(false)
                .into(viewHolder.iv_image);
        //   Vholder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewHolder.rl_select.setAlpha(0);


        viewHolder.rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_gallery = new Intent(context, GalleryView.class);
                intent_gallery.putExtra("video",al_video.get(position));
                activity.startActivity(intent_gallery);
            }
        });
        viewHolder.imageDeleteVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                al_video.remove(viewHolder.getPosition());
                notifyDataSetChanged();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {

        return al_video.size();
    }

}

