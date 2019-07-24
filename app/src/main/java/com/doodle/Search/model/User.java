
package com.doodle.Search.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable, Parcelable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("total_badge")
    @Expose
    private String totalBadge;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    @SerializedName("founding_user")
    @Expose
    private String foundingUser;
    public static final int USER_ADVANCE_SEARCH = 0;



    public final static Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8228634251931947795L;

    protected User(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.fullname = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.totalBadge = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.foundingUser = ((String) in.readValue((String.class.getClassLoader())));

    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTotalBadge() {
        return totalBadge;
    }

    public void setTotalBadge(String totalBadge) {
        this.totalBadge = totalBadge;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
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

    public String getFoundingUser() {
        return foundingUser;
    }

    public void setFoundingUser(String foundingUser) {
        this.foundingUser = foundingUser;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(fullname);
        dest.writeValue(userName);
        dest.writeValue(totalBadge);
        dest.writeValue(photo);
        dest.writeValue(totalLikes);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
        dest.writeValue(foundingUser);

    }

    public int describeContents() {
        return  0;
    }

}
