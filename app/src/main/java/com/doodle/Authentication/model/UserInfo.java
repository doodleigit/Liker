
package com.doodle.Authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("deactivated")
    @Expose
    private String deactivated;
    @SerializedName("founding_user")
    @Expose
    private String foundingUser;
    @SerializedName("learn_about_site")
    @Expose
    private String learnAboutSite;
    @SerializedName("is_top_commenter")
    @Expose
    private String isTopCommenter;
    @SerializedName("is_master")
    @Expose
    private String isMaster;
    private final static long serialVersionUID = 7429974380666001919L;

    protected UserInfo(Parcel in) {
        id = in.readString();
        userId = in.readString();
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        totalLikes = in.readString();
        goldStars = in.readString();
        sliverStars = in.readString();
        photo = in.readString();
        email = in.readString();
        deactivated = in.readString();
        foundingUser = in.readString();
        learnAboutSite = in.readString();
        isTopCommenter = in.readString();
        isMaster = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }

    public String getFoundingUser() {
        return foundingUser;
    }

    public void setFoundingUser(String foundingUser) {
        this.foundingUser = foundingUser;
    }

    public String getLearnAboutSite() {
        return learnAboutSite;
    }

    public void setLearnAboutSite(String learnAboutSite) {
        this.learnAboutSite = learnAboutSite;
    }

    public String getIsTopCommenter() {
        return isTopCommenter;
    }

    public void setIsTopCommenter(String isTopCommenter) {
        this.isTopCommenter = isTopCommenter;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(totalLikes);
        dest.writeString(goldStars);
        dest.writeString(sliverStars);
        dest.writeString(photo);
        dest.writeString(email);
        dest.writeString(deactivated);
        dest.writeString(foundingUser);
        dest.writeString(learnAboutSite);
        dest.writeString(isTopCommenter);
        dest.writeString(isMaster);
    }
}
