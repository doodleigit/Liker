
package com.doodle.Home.model.postshare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LikeUser implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("who_liked")
    @Expose
    private String whoLiked;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("user_total_likes")
    @Expose
    private String userTotalLikes;
    @SerializedName("user_gold_stars")
    @Expose
    private String userGoldStars;
    @SerializedName("user_sliver_stars")
    @Expose
    private String userSliverStars;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("deactivated")
    @Expose
    private String deactivated;
    public final static Creator<LikeUser> CREATOR = new Creator<LikeUser>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LikeUser createFromParcel(Parcel in) {
            return new LikeUser(in);
        }

        public LikeUser[] newArray(int size) {
            return (new LikeUser[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3342408928822906623L;

    protected LikeUser(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        this.whoLiked = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.userLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.userTotalLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.userGoldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userSliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userPhoto = ((String) in.readValue((String.class.getClassLoader())));
        this.deactivated = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LikeUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getWhoLiked() {
        return whoLiked;
    }

    public void setWhoLiked(String whoLiked) {
        this.whoLiked = whoLiked;
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

    public String getUserTotalLikes() {
        return userTotalLikes;
    }

    public void setUserTotalLikes(String userTotalLikes) {
        this.userTotalLikes = userTotalLikes;
    }

    public String getUserGoldStars() {
        return userGoldStars;
    }

    public void setUserGoldStars(String userGoldStars) {
        this.userGoldStars = userGoldStars;
    }

    public String getUserSliverStars() {
        return userSliverStars;
    }

    public void setUserSliverStars(String userSliverStars) {
        this.userSliverStars = userSliverStars;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(postId);
        dest.writeValue(whoLiked);
        dest.writeValue(userId);
        dest.writeValue(userName);
        dest.writeValue(userFirstName);
        dest.writeValue(userLastName);
        dest.writeValue(userTotalLikes);
        dest.writeValue(userGoldStars);
        dest.writeValue(userSliverStars);
        dest.writeValue(userPhoto);
        dest.writeValue(deactivated);
    }

    public int describeContents() {
        return  0;
    }

}
