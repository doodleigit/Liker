package com.doodle.Home.model;

import android.widget.FrameLayout;
import android.widget.ImageView;

public class MediaViewHolder {

    private ImageView mediaImage, mediaThumbnail;
    private FrameLayout mediaVideoLayout;

    public MediaViewHolder(FrameLayout mediaVideoLayout, ImageView mediaImage, ImageView mediaThumbnail) {
        this.mediaVideoLayout = mediaVideoLayout;
        this.mediaImage = mediaImage;
        this.mediaThumbnail = mediaThumbnail;
    }

    public FrameLayout getMediaVideoLayout() {
        return mediaVideoLayout;
    }

    public void setMediaVideoLayout(FrameLayout mediaVideoLayout) {
        this.mediaVideoLayout = mediaVideoLayout;
    }

    public ImageView getMediaImage() {
        return mediaImage;
    }

    public void setMediaImage(ImageView mediaImage) {
        this.mediaImage = mediaImage;
    }

    public ImageView getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(ImageView mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }
}
