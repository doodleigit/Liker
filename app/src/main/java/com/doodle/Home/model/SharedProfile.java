
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SharedProfile implements Serializable, Parcelable
{

    @SerializedName("post_userid")
    @Expose
    private String postUserid;
    @SerializedName("post_username")
    @Expose
    private String postUsername;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("uesr_profile_img")
    @Expose
    private String uesrProfileImg;
    @SerializedName("user_profile_likes")
    @Expose
    private String userProfileLikes;
    @SerializedName("user_gold_stars")
    @Expose
    private String userGoldStars;
    @SerializedName("user_silver_stars")
    @Expose
    private String userSilverStars;
    @SerializedName("user_founding_member")
    @Expose
    private String userFoundingMember;
    @SerializedName("user_top_commenter")
    @Expose
    private String userTopCommenter;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("permission")
    @Expose
    private String permission;
    public final static Creator<SharedProfile> CREATOR = new Creator<SharedProfile>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SharedProfile createFromParcel(Parcel in) {
            return new SharedProfile(in);
        }

        public SharedProfile[] newArray(int size) {
            return (new SharedProfile[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2780135736259057640L;

    protected SharedProfile(Parcel in) {
        this.postUserid = ((String) in.readValue((String.class.getClassLoader())));
        this.postUsername = ((String) in.readValue((String.class.getClassLoader())));
        this.userFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.userLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.uesrProfileImg = ((String) in.readValue((String.class.getClassLoader())));
        this.userProfileLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.userGoldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userSilverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userFoundingMember = ((String) in.readValue((String.class.getClassLoader())));
        this.userTopCommenter = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        this.permission = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SharedProfile() {
    }

    public String getPostUserid() {
        return postUserid;
    }

    public void setPostUserid(String postUserid) {
        this.postUserid = postUserid;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUesrProfileImg() {
        return uesrProfileImg;
    }

    public void setUesrProfileImg(String uesrProfileImg) {
        this.uesrProfileImg = uesrProfileImg;
    }

    public String getUserProfileLikes() {
        return userProfileLikes;
    }

    public void setUserProfileLikes(String userProfileLikes) {
        this.userProfileLikes = userProfileLikes;
    }

    public String getUserGoldStars() {
        return userGoldStars;
    }

    public void setUserGoldStars(String userGoldStars) {
        this.userGoldStars = userGoldStars;
    }

    public String getUserSilverStars() {
        return userSilverStars;
    }

    public void setUserSilverStars(String userSilverStars) {
        this.userSilverStars = userSilverStars;
    }

    public String getUserFoundingMember() {
        return userFoundingMember;
    }

    public void setUserFoundingMember(String userFoundingMember) {
        this.userFoundingMember = userFoundingMember;
    }

    public String getUserTopCommenter() {
        return userTopCommenter;
    }

    public void setUserTopCommenter(String userTopCommenter) {
        this.userTopCommenter = userTopCommenter;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postUserid);
        dest.writeValue(postUsername);
        dest.writeValue(userFirstName);
        dest.writeValue(userLastName);
        dest.writeValue(uesrProfileImg);
        dest.writeValue(userProfileLikes);
        dest.writeValue(userGoldStars);
        dest.writeValue(userSilverStars);
        dest.writeValue(userFoundingMember);
        dest.writeValue(userTopCommenter);
        dest.writeValue(dateTime);
        dest.writeValue(permission);
    }

    public int describeContents() {
        return  0;
    }

}
