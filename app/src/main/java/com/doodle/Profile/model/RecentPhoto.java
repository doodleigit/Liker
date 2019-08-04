
package com.doodle.Profile.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentPhoto implements Serializable, Parcelable
{

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    public final static Creator<RecentPhoto> CREATOR = new Creator<RecentPhoto>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RecentPhoto createFromParcel(Parcel in) {
            return new RecentPhoto(in);
        }

        public RecentPhoto[] newArray(int size) {
            return (new RecentPhoto[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3882874179249823135L;

    protected RecentPhoto(Parcel in) {
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RecentPhoto() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postId);
        dest.writeValue(imageName);
    }

    public int describeContents() {
        return  0;
    }

}
