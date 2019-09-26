package com.doodle.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostVideo implements Parcelable {
    public String videoPath;
    public String videoId;
    public String mdFive;
    public MultipleMediaFile multipleMediaFile;



    public PostVideo(String videoPath, String videoId) {
        this.videoPath = videoPath;
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }



    public PostVideo() {
    }

    protected PostVideo(Parcel in) {
        videoPath = in.readString();

    }


    public static final Creator<PostVideo> CREATOR = new Creator<PostVideo>() {
        @Override
        public PostVideo createFromParcel(Parcel in) {
            return new PostVideo(in);
        }

        @Override
        public PostVideo[] newArray(int size) {
            return new PostVideo[size];
        }
    };

    public MultipleMediaFile getMultipleMediaFile() {
        return multipleMediaFile;
    }

    public void setMultipleMediaFile(MultipleMediaFile multipleMediaFile) {
        this.multipleMediaFile = multipleMediaFile;
    }

    public String getMdFive() {
        return mdFive;
    }

    public void setMdFive(String mdFive) {
        this.mdFive = mdFive;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoPath);

    }
}
