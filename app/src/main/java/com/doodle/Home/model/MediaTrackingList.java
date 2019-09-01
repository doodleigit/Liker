package com.doodle.Home.model;

public class MediaTrackingList {

    private String videoUrl;
    private int position;

    public MediaTrackingList(String videoUrl, int position) {
        this.videoUrl = videoUrl;
        this.position = position;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
