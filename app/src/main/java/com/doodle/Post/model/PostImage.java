package com.doodle.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostImage implements Parcelable {

    public String imagePath;

    public PostImage() {
    }

    public PostImage(String imagePath) {
        this.imagePath = imagePath;
    }

    protected PostImage(Parcel in) {
        imagePath = in.readString();
    }

    public static final Creator<PostImage> CREATOR = new Creator<PostImage>() {
        @Override
        public PostImage createFromParcel(Parcel in) {
            return new PostImage(in);
        }

        @Override
        public PostImage[] newArray(int size) {
            return new PostImage[size];
        }
    };

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagePath);
    }
}
