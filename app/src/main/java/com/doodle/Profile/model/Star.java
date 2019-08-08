
package com.doodle.Profile.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Star implements Serializable, Parcelable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("post_category_id")
    @Expose
    private String postCategoryId;
    @SerializedName("badge")
    @Expose
    private String badge;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    public final static Creator<Star> CREATOR = new Creator<Star>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Star createFromParcel(Parcel in) {
            return new Star(in);
        }

        public Star[] newArray(int size) {
            return (new Star[size]);
        }

    };
    private final static long serialVersionUID = -4483646546854461447L;

    protected Star(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.postCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.badge = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Star() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(String postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(postCategoryId);
        dest.writeValue(badge);
        dest.writeValue(categoryName);
    }

    public int describeContents() {
        return 0;
    }

}
