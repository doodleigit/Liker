package com.doodle.Post.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MentionUser implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    public final static Parcelable.Creator<MentionUser> CREATOR = new Creator<MentionUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MentionUser createFromParcel(Parcel in) {
            return new MentionUser(in);
        }

        public MentionUser[] newArray(int size) {
            return (new MentionUser[size]);
        }

    };
    private final static long serialVersionUID = 8634603193551459664L;

    protected MentionUser(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.display = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MentionUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGoldStars() {
        return goldStars;
    }

    public void setGoldStars(String goldStars) {
        this.goldStars = goldStars;
    }

    public String getSliverStars() {
        return sliverStars;
    }

    public void setSliverStars(String sliverStars) {
        this.sliverStars = sliverStars;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userName);
        dest.writeValue(display);
        dest.writeValue(photo);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
    }

    public int describeContents() {
        return 0;
    }

}