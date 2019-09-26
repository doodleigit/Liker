package com.doodle.Post.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class LinkScriptItem implements Parcelable {

    private Bitmap image;
    private String title;
    private String description;
    private String host;

    public LinkScriptItem() {
    }

    public LinkScriptItem(Bitmap image, String title, String description, String host) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.host = host;
    }


    protected LinkScriptItem(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        host = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(image, flags);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(host);
    }

    public static final Creator<LinkScriptItem> CREATOR = new Creator<LinkScriptItem>() {
        @Override
        public LinkScriptItem createFromParcel(Parcel in) {
            return new LinkScriptItem(in);
        }

        @Override
        public LinkScriptItem[] newArray(int size) {
            return new LinkScriptItem[size];
        }
    };

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
