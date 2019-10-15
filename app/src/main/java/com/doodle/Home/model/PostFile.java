package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostFile implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("media_id")
    @Expose
    private String mediaId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("video_name")
    @Expose
    private String videoName;
    @SerializedName("upload_status")
    @Expose
    private String uploadStatus;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;

    private int playTime = 0;
    public final static Parcelable.Creator<PostFile> CREATOR = new Creator<PostFile>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PostFile createFromParcel(Parcel in) {
            return new PostFile(in);
        }

        public PostFile[] newArray(int size) {
            return (new PostFile[size]);
        }

    }
            ;
    private final static long serialVersionUID = 8662565114220250173L;

    protected PostFile(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.mediaId = ((String) in.readValue((String.class.getClassLoader())));
        this.postType = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
        this.videoName = ((String) in.readValue((String.class.getClassLoader())));
        this.uploadStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.duration = ((String) in.readValue((String.class.getClassLoader())));
        this.width = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PostFile() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(mediaId);
        dest.writeValue(postType);
        dest.writeValue(imageName);
        dest.writeValue(videoName);
        dest.writeValue(uploadStatus);
        dest.writeValue(duration);
        dest.writeValue(width);
        dest.writeValue(height);
    }

    public int describeContents() {
        return 0;
    }

}
