
package com.doodle.Search.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable, Parcelable {

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("has_mention")
    @Expose
    private String hasMention;
    @SerializedName("is_wall")
    @Expose
    private String isWall;
    @SerializedName("is_shared")
    @Expose
    private String isShared;
    @SerializedName("post_category_id")
    @Expose
    private String postCategoryId;
    @SerializedName("post_permission")
    @Expose
    private String postPermission;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("post_text")
    @Expose
    private String postText;
    @SerializedName("post_image")
    @Expose
    private String postImage;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_name")
    @Expose
    private String userName;
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
    @SerializedName("founding_user")
    @Expose
    private String foundingUser;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("total_like")
    @Expose
    private String totalLike;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("video_name")
    @Expose
    private Object videoName;
    @SerializedName("video_duration")
    @Expose
    private Object videoDuration;
    @SerializedName("video_upload_status")
    @Expose
    private Object videoUploadStatus;

    public static final int POST_ADVANCE_SEARCH = 1;



    public final static Creator<Post> CREATOR = new Creator<Post>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        public Post[] newArray(int size) {
            return (new Post[size]);
        }

    };
    private final static long serialVersionUID = -8682318362760810529L;

    protected Post(Parcel in) {
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        this.hasMention = ((String) in.readValue((String.class.getClassLoader())));
        this.isWall = ((String) in.readValue((String.class.getClassLoader())));
        this.isShared = ((String) in.readValue((String.class.getClassLoader())));
        this.postCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.postPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.postDate = ((String) in.readValue((String.class.getClassLoader())));
        this.postText = ((String) in.readValue((String.class.getClassLoader())));
        this.postImage = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.foundingUser = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLike = ((String) in.readValue((String.class.getClassLoader())));
        this.postType = ((String) in.readValue((String.class.getClassLoader())));
        this.videoName = ((Object) in.readValue((Object.class.getClassLoader())));
        this.videoDuration = ((Object) in.readValue((Object.class.getClassLoader())));
        this.videoUploadStatus = ((Object) in.readValue((Object.class.getClassLoader())));

    }

    public Post() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getHasMention() {
        return hasMention;
    }

    public void setHasMention(String hasMention) {
        this.hasMention = hasMention;
    }

    public String getIsWall() {
        return isWall;
    }

    public void setIsWall(String isWall) {
        this.isWall = isWall;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(String postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public String getPostPermission() {
        return postPermission;
    }

    public void setPostPermission(String postPermission) {
        this.postPermission = postPermission;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getFoundingUser() {
        return foundingUser;
    }

    public void setFoundingUser(String foundingUser) {
        this.foundingUser = foundingUser;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Object getVideoName() {
        return videoName;
    }

    public void setVideoName(Object videoName) {
        this.videoName = videoName;
    }

    public Object getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Object videoDuration) {
        this.videoDuration = videoDuration;
    }

    public Object getVideoUploadStatus() {
        return videoUploadStatus;
    }

    public void setVideoUploadStatus(Object videoUploadStatus) {
        this.videoUploadStatus = videoUploadStatus;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postId);
        dest.writeValue(hasMention);
        dest.writeValue(isWall);
        dest.writeValue(isShared);
        dest.writeValue(postCategoryId);
        dest.writeValue(postPermission);
        dest.writeValue(userId);
        dest.writeValue(postDate);
        dest.writeValue(postText);
        dest.writeValue(postImage);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(userName);
        dest.writeValue(totalLikes);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
        dest.writeValue(photo);
        dest.writeValue(foundingUser);
        dest.writeValue(categoryName);
        dest.writeValue(totalLike);
        dest.writeValue(postType);
        dest.writeValue(videoName);
        dest.writeValue(videoDuration);
        dest.writeValue(videoUploadStatus);
    }

    public int describeContents() {
        return 0;
    }

}
