package com.doodle.Home.service;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestManager;
import com.doodle.Home.holder.ImageHolder;
import com.doodle.Home.model.MediaTrackingList;
import com.doodle.Home.model.MediaViewObject;
import com.doodle.Home.model.PostItem;
import com.doodle.Tool.AppConstants;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerRecyclerView extends RecyclerView {

    private static final String TAG = "VideoPlayerRecyclerView";

    private enum VolumeState {ON, OFF}
    // ui
    private ImageView thumbnail, volumeControl, videoPlay, mediaVideoOneThumbnail, mediaVideoTwoThumbnail, mediaVideoThreeThumbnail, mediaVideoFourThumbnail, mediaVideoOneVolumeControl,
            mediaVideoTwoVolumeControl, mediaVideoThreeVolumeControl, mediaVideoFourVolumeControl, mediaVideoOnePlay, mediaVideoTwoPlay, mediaVideoThreePlay, mediaVideoFourPlay;
    private ProgressBar progressBar, mediaVideoOneProgressBar, mediaVideoTwoProgressBar, mediaVideoThreeProgressBar, mediaVideoFourProgressBar;
    private View viewHolderParent;
    private FrameLayout frameLayout, mediaVideoOne, mediaVideoTwo, mediaVideoThree, mediaVideoFour;
    private PlayerView videoSurfaceView;
    private SimpleExoPlayer videoPlayer;

    // vars
    private List<PostItem> mediaObjects = new ArrayList<>();
    private List<MediaTrackingList> mediaTrackingLists = new ArrayList<>();
    private ArrayList<MediaViewObject> mediaViewObjects = new ArrayList<>();
    private int trackingPosition = 0;
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private Context context;
    private int playPosition = -1;
    private boolean isVideoViewAdded;
    private RequestManager requestManager;

    // controlling playback state
    private VolumeState volumeState;

    public VideoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        this.context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        videoSurfaceDefaultHeight = point.x;
        screenDefaultHeight = point.y;

        videoSurfaceView = new PlayerView(this.context);
        videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        // Bind the player to the view.
        videoSurfaceView.setUseController(false);
        videoSurfaceView.setPlayer(videoPlayer);
        setVolumeControl(VolumeState.ON);

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called.");
                    if (thumbnail != null) { // show the old thumbnail
                        thumbnail.setVisibility(VISIBLE);
                    }
                    if (videoPlay != null) {
                        videoPlay.setVisibility(VISIBLE);
                    }
                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        playVideo(true);
                    } else {
                        playVideo(false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (viewHolderParent != null && viewHolderParent.equals(view)) {
                    resetVideoView();
                    Log.d("resetVideoView", "resetVideoView");
                }

            }
        });

        videoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                        if (progressBar != null) {
                            progressBar.setVisibility(VISIBLE);
                        }

                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
//                        videoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                        if (progressBar != null) {
                            progressBar.setVisibility(GONE);
                        }
                        if (!isVideoViewAdded) {
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    public void playVideo(boolean isEndOfList) {

        int targetPosition;

        if (!isEndOfList) {
            int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1;
            }

            // something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (startPosition != endPosition) {
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);

                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            } else {
                targetPosition = startPosition;
            }
        } else {
            targetPosition = mediaObjects.size() - 1;
        }

//        Log.d(TAG, "playVideo: target position: " + targetPosition);

        // video is already playing so return
        if (targetPosition == playPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        playPosition = targetPosition;
        if (videoSurfaceView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        videoSurfaceView.setVisibility(INVISIBLE);
        removeVideoView(videoSurfaceView);

        int currentPosition = targetPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();

        View child = getChildAt(currentPosition);
        if (child == null) {
            return;
        }

//        Log.d("videoautoplaycheck", "step 0 position = " + targetPosition);

        if (mediaObjects.get(targetPosition).getPostType().equals("2")) {
            RecyclerView.ViewHolder viewHolder = findViewHolderForAdapterPosition(targetPosition);
            ImageHolder holder = (ImageHolder) viewHolder;
            if (holder == null) {
                playPosition = -1;
//                Log.d("videoautoplaycheck", "step 23135 position = " + targetPosition);
                return;
            }
            mediaVideoOne = holder.mediaVideoOne;
            mediaVideoTwo = holder.mediaVideoTwo;
            mediaVideoThree = holder.mediaVideoThree;
            mediaVideoFour = holder.mediaVideoFour;

            mediaVideoOneThumbnail = holder.mediaVideoOneThumbnail;
            mediaVideoTwoThumbnail = holder.mediaVideoTwoThumbnail;
            mediaVideoThreeThumbnail = holder.mediaVideoThreeThumbnail;
            mediaVideoFourThumbnail = holder.mediaVideoFourThumbnail;

            mediaVideoOneVolumeControl = holder.mediaVideoOneVolumeControl;
            mediaVideoTwoVolumeControl = holder.mediaVideoTwoVolumeControl;
            mediaVideoThreeVolumeControl = holder.mediaVideoThreeVolumeControl;
            mediaVideoFourVolumeControl = holder.mediaVideoFourVolumeControl;

            mediaVideoOnePlay = holder.mediaVideoOnePlay;
            mediaVideoTwoPlay = holder.mediaVideoTwoPlay;
            mediaVideoThreePlay = holder.mediaVideoThreePlay;
            mediaVideoFourPlay = holder.mediaVideoFourPlay;

            mediaVideoOneProgressBar = holder.mediaVideoOneProgressBar;
            mediaVideoTwoProgressBar = holder.mediaVideoTwoProgressBar;
            mediaVideoThreeProgressBar = holder.mediaVideoThreeProgressBar;
            mediaVideoFourProgressBar = holder.mediaVideoFourProgressBar;

            viewHolderParent = holder.itemView;

            mediaViewObjects.clear();
            mediaTrackingLists.clear();
            trackingPosition = 0;

            mediaViewObjects.add(new MediaViewObject(mediaVideoOne, mediaVideoOneThumbnail, mediaVideoOneVolumeControl, mediaVideoOnePlay, mediaVideoOneProgressBar));
            mediaViewObjects.add(new MediaViewObject(mediaVideoTwo, mediaVideoTwoThumbnail, mediaVideoTwoVolumeControl, mediaVideoTwoPlay, mediaVideoTwoProgressBar));
            mediaViewObjects.add(new MediaViewObject(mediaVideoThree, mediaVideoThreeThumbnail, mediaVideoThreeVolumeControl, mediaVideoThreePlay, mediaVideoThreeProgressBar));
            mediaViewObjects.add(new MediaViewObject(mediaVideoFour, mediaVideoFourThumbnail, mediaVideoFourVolumeControl, mediaVideoFourPlay, mediaVideoFourProgressBar));

            int size = mediaObjects.get(targetPosition).getPostFiles().size() > 4 ? 4 : mediaObjects.get(targetPosition).getPostFiles().size();

//            Log.d("videoautoplaycheck", "step 1 Size = " + size);

            for (int i = 0; i < size; i++) {
                if (mediaObjects.get(targetPosition).getPostFiles().get(i).getPostType().equals("2")) {
                    mediaTrackingLists.add(new MediaTrackingList(mediaObjects.get(targetPosition).getPostFiles().get(i).getVideoName(), i));
//                    Log.d("videoautoplaycheck", "step 2 url = " + AppConstants.POST_VIDEOS);
                }
            }
            initialPlayerAndViews();

            mediaVideoOnePlay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualVideoPlay(0);
                }
            });

            mediaVideoTwoPlay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualVideoPlay(1);
                }
            });

            mediaVideoThreePlay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualVideoPlay(2);
                }
            });

            mediaVideoFourPlay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    manualVideoPlay(3);
                }
            });

        } else {
            playPosition = -1;
        }

    }

    private void manualVideoPlay(int position) {
        for (int i = 0; i < mediaTrackingLists.size(); i++) {
            if (mediaTrackingLists.get(i).getPosition() == position) {
                trackingPosition = i;
                resetVideoView();
                initialPlayerAndViews();
                break;
            }
        }
    }

    private void initialPlayerAndViews() {
        if (mediaTrackingLists.size() != 0) {
            int i = mediaTrackingLists.get(trackingPosition).getPosition();
            thumbnail = mediaViewObjects.get(i).getThumbnail();
            progressBar = mediaViewObjects.get(i).getProgressBar();
            volumeControl = mediaViewObjects.get(i).getVolumeControl();
            videoPlay = mediaViewObjects.get(i).getVideoPlay();
//        requestManager = holder.requestManager;
            frameLayout = mediaViewObjects.get(i).getFrameLayout();
            frameLayout.setVisibility(VISIBLE);

            videoSurfaceView.setPlayer(videoPlayer);

            viewHolderParent.setOnClickListener(videoViewClickListener);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                    context, Util.getUserAgent(context, "RecyclerView VideoPlayer"));
            if (mediaTrackingLists.get(trackingPosition).getVideoUrl() != null) {
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(AppConstants.POST_VIDEOS + mediaTrackingLists.get(trackingPosition).getVideoUrl()));
                videoPlayer.prepare(videoSource);
                videoPlayer.setPlayWhenReady(true);
            }
//            Log.d("videoautoplaycheck", "step 3 url = " + AppConstants.POST_VIDEOS);

            videoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        if (mediaTrackingLists.size() > trackingPosition + 1) {
                            trackingPosition++;
                            resetVideoView();
                            initialPlayerAndViews();
                        } else {
                            resetVideoView();
                        }
                    }
                }
            });
        } else {
            playPosition = -1;
        }
    }

    private OnClickListener videoViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleVolume();
        }
    };

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     *
     * @param playPosition
     * @return
     */
    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];
        }
    }


    // Remove the old player
    private void removeVideoView(PlayerView videoView) {
        videoView.getPlayer().stop();
        ViewGroup parent = (ViewGroup) videoView.getParent();
        if (parent == null) {
            return;
        }

        int index = parent.indexOfChild(videoView);
        if (index >= 0) {
            parent.removeViewAt(index);
            isVideoViewAdded = false;
            viewHolderParent.setOnClickListener(null);
        }

    }

    private void addVideoView() {
        frameLayout.addView(videoSurfaceView);
        isVideoViewAdded = true;
        videoSurfaceView.requestFocus();
        videoSurfaceView.setVisibility(VISIBLE);
        videoSurfaceView.setAlpha(1);
        thumbnail.setVisibility(GONE);
        videoPlay.setVisibility(GONE);
    }

    private void resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView);
            playPosition = -1;
            videoSurfaceView.setVisibility(INVISIBLE);
            thumbnail.setVisibility(VISIBLE);
            videoPlay.setVisibility(VISIBLE);
        }
    }

    public void releasePlayer() {
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }

        viewHolderParent = null;
    }

    private void toggleVolume() {
        if (videoPlayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(TAG, "togglePlaybackState: enabling volume.");
                setVolumeControl(VolumeState.ON);

            } else if (volumeState == VolumeState.ON) {
                Log.d(TAG, "togglePlaybackState: disabling volume.");
                setVolumeControl(VolumeState.OFF);

            }
        }
    }

    private void setVolumeControl(VolumeState state) {
        volumeState = state;
        if (state == VolumeState.OFF) {
            videoPlayer.setVolume(0f);
            animateVolumeControl();
        } else if (state == VolumeState.ON) {
            videoPlayer.setVolume(1f);
            animateVolumeControl();
        }
    }

    private void animateVolumeControl() {
        if (volumeControl != null) {
            volumeControl.bringToFront();
            if (volumeState == VolumeState.OFF) {
//                requestManager.load(R.drawable.close)
//                        .into(volumeControl);
            } else if (volumeState == VolumeState.ON) {
//                requestManager.load(R.drawable.plus)
//                        .into(volumeControl);
            }
            volumeControl.animate().cancel();

            volumeControl.setAlpha(1f);

            volumeControl.animate()
                    .alpha(0f)
                    .setDuration(600).setStartDelay(1000);
        }
    }

    public void setMediaObjects(List<PostItem> mediaObjects) {
        this.mediaObjects = mediaObjects;
    }

    public void pausePlayer() {
        if (videoPlayer != null) {
            videoPlayer.setPlayWhenReady(false);
        }
    }

}
