package com.doodle.Home.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestManager;
import com.doodle.Home.model.PostFile;
import com.doodle.Post.adapter.MediaSliderAdapter;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class SingleVideoPlayerViewpager extends ViewPager {

    private static final String TAG = "VideoPlayerRecyclerView";

    private enum VolumeState {ON, OFF}

    ;

    // ui
    private ImageView thumbnail, volumeControl, videoPlay;
    private ProgressBar progressBar;
    private View viewHolderParent;
    private FrameLayout frameLayout, mediaController;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;

    // vars
    private List<PostFile> mediaObjects = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    // controlling playback state
    private VolumeState volumeState;

    public SingleVideoPlayerViewpager(@NonNull Context context) {
        super(context);
    }

    public SingleVideoPlayerViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        MediaSliderAdapter adapter = (MediaSliderAdapter) getAdapter();

    }

}
