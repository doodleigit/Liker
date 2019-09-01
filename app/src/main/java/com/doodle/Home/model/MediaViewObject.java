package com.doodle.Home.model;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MediaViewObject {

    private FrameLayout frameLayout;
    private ImageView thumbnail, volumeControl, videoPlay;
    private ProgressBar progressBar;

    public MediaViewObject(FrameLayout frameLayout, ImageView thumbnail, ImageView volumeControl, ImageView videoPlay, ProgressBar progressBar) {
        this.frameLayout = frameLayout;
        this.thumbnail = thumbnail;
        this.volumeControl = volumeControl;
        this.videoPlay = videoPlay;
        this.progressBar = progressBar;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public ImageView getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageView getVolumeControl() {
        return volumeControl;
    }

    public void setVolumeControl(ImageView volumeControl) {
        this.volumeControl = volumeControl;
    }

    public ImageView getVideoPlay() {
        return videoPlay;
    }

    public void setVideoPlay(ImageView videoPlay) {
        this.videoPlay = videoPlay;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
