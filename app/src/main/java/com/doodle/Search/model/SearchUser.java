
package com.doodle.Search.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchUser implements Serializable, Parcelable
{

    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("fullname")
    @Expose
    public String fullname;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("total_badge")
    @Expose
    public String totalBadge;
    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("total_likes")
    @Expose
    public String totalLikes;
    @SerializedName("gold_stars")
    @Expose
    public String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    public String sliverStars;
    @SerializedName("founding_user")
    @Expose
    public String foundingUser;
    public final static long serialVersionUID = -8505728857057370522L;

    public SearchUser() {
    }

    public SearchUser(Parcel in) {
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        fullname = in.readString();
        userName = in.readString();
        totalBadge = in.readString();
        photo = in.readString();
        totalLikes = in.readString();
        goldStars = in.readString();
        sliverStars = in.readString();
        foundingUser = in.readString();
    }

    public static final Creator<SearchUser> CREATOR = new Creator<SearchUser>() {
        @Override
        public SearchUser createFromParcel(Parcel in) {
            return new SearchUser(in);
        }

        @Override
        public SearchUser[] newArray(int size) {
            return new SearchUser[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(fullname);
        dest.writeString(userName);
        dest.writeString(totalBadge);
        dest.writeString(photo);
        dest.writeString(totalLikes);
        dest.writeString(goldStars);
        dest.writeString(sliverStars);
        dest.writeString(foundingUser);
    }
}