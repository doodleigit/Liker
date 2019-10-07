package com.doodle.Post.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.model.PostFile;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MediaSliderAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PostFile> arrayList;
    private ArrayList<PlayerView> videoViews;

    public MediaSliderAdapter(Context context, List<PostFile> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        videoViews = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            videoViews.add(null);
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.media_slider_item, view, false);

        assert imageLayout != null;
        final PhotoView imageView = imageLayout.findViewById(R.id.media_image);
        final PlayerView videoView = imageLayout.findViewById(R.id.video_view);
        videoViews.set(position, videoView);

        if (arrayList.get(position).getPostType().equals("2")) {
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            // Setup Exoplayer instance
            SimpleExoPlayer exoPlayerInstance = ExoPlayerFactory.newSimpleInstance(context);
// Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "Liker"));
            //Getting media from raw resource
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(AppConstants.POST_VIDEOS + arrayList.get(position).getVideoName()));
//Prepare the exoPlayerInstance with the source
            exoPlayerInstance.prepare(videoSource);
            videoView.setPlayer(exoPlayerInstance);
        } else {
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(App.getAppContext())
                    .load(AppConstants.POST_IMAGES + arrayList.get(position).getImageName())
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .dontAnimate()
                    .into(imageView);
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void pauseVideo(int position) {
        for (int i = 0; i < videoViews.size(); i++) {
            if (videoViews.get(i) != null) {
                if (position != i) {
                    videoViews.get(i).getPlayer().setPlayWhenReady(false);
                }
            }
        }
    }

}
