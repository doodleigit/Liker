
package com.doodle.Home.model.postshare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostShareItem implements Serializable, Parcelable
{

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("is_shared")
    @Expose
    private String isShared;
    @SerializedName("has_shared")
    @Expose
    private String hasShared;
    @SerializedName("has_mention")
    @Expose
    private String hasMention;
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
    @SerializedName("post_text")
    @Expose
    private String postText;
    @SerializedName("post_text_index")
    @Expose
    private List<PostTextIndex> postTextIndex = new ArrayList<PostTextIndex>();
    @SerializedName("post_image")
    @Expose
    private String postImage;
    @SerializedName("files")
    @Expose
    private boolean files;
    @SerializedName("post_link_title")
    @Expose
    private String postLinkTitle;
    @SerializedName("post_link_desc")
    @Expose
    private String postLinkDesc;
    @SerializedName("post_link_url")
    @Expose
    private String postLinkUrl;
    @SerializedName("video_name")
    @Expose
    private String videoName;
    @SerializedName("shared_post_id")
    @Expose
    private String sharedPostId;
    @SerializedName("post_wall_userid")
    @Expose
    private String postWallUserid;
    @SerializedName("is_wall")
    @Expose
    private String isWall;
    @SerializedName("post_wall_username")
    @Expose
    private String postWallUsername;
    @SerializedName("post_wall_first_name")
    @Expose
    private String postWallFirstName;
    @SerializedName("post_wall_last_name")
    @Expose
    private String postWallLastName;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("post_footer")
    @Expose
    private PostFooter postFooter;
    @SerializedName("has_meme")
    @Expose
    private String hasMeme;
    @SerializedName("total_comment")
    @Expose
    private String totalComment;
    @SerializedName("user_total_followers")
    @Expose
    private String userTotalFollowers;
    @SerializedName("meme_preview")
    @Expose
    private String memePreview;
    @SerializedName("list_class_name")
    @Expose
    private String listClassName;
    @SerializedName("input_add_class_name")
    @Expose
    private String inputAddClassName;
    @SerializedName("video_upload_status")
    @Expose
    private String videoUploadStatus;
    @SerializedName("video_duration")
    @Expose
    private int videoDuration;
    @SerializedName("is_notification_off")
    @Expose
    private boolean isNotificationOff;
    public final static Creator<PostShareItem> CREATOR = new Creator<PostShareItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PostShareItem createFromParcel(Parcel in) {
            return new PostShareItem(in);
        }

        public PostShareItem[] newArray(int size) {
            return (new PostShareItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7714660490339995088L;

    protected PostShareItem(Parcel in) {
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        this.postType = ((String) in.readValue((String.class.getClassLoader())));
        this.isShared = ((String) in.readValue((String.class.getClassLoader())));
        this.hasShared = ((String) in.readValue((String.class.getClassLoader())));
        this.hasMention = ((String) in.readValue((String.class.getClassLoader())));
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
        this.postText = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.postTextIndex, (PostTextIndex.class.getClassLoader()));
        this.postImage = ((String) in.readValue((String.class.getClassLoader())));
        this.files = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.postLinkTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.videoName = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedPostId = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallUserid = ((String) in.readValue((String.class.getClassLoader())));
        this.isWall = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallUsername = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.catName = ((String) in.readValue((String.class.getClassLoader())));
        this.catId = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        this.permission = ((String) in.readValue((String.class.getClassLoader())));
        this.postFooter = ((PostFooter) in.readValue((PostFooter.class.getClassLoader())));
        this.hasMeme = ((String) in.readValue((String.class.getClassLoader())));
        this.totalComment = ((String) in.readValue((String.class.getClassLoader())));
        this.userTotalFollowers = ((String) in.readValue((String.class.getClassLoader())));
        this.memePreview = ((String) in.readValue((String.class.getClassLoader())));
        this.listClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.inputAddClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.videoUploadStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.videoDuration = ((int) in.readValue((int.class.getClassLoader())));
        this.isNotificationOff = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public PostShareItem() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getHasShared() {
        return hasShared;
    }

    public void setHasShared(String hasShared) {
        this.hasShared = hasShared;
    }

    public String getHasMention() {
        return hasMention;
    }

    public void setHasMention(String hasMention) {
        this.hasMention = hasMention;
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

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public List<PostTextIndex> getPostTextIndex() {
        return postTextIndex;
    }

    public void setPostTextIndex(List<PostTextIndex> postTextIndex) {
        this.postTextIndex = postTextIndex;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public boolean isFiles() {
        return files;
    }

    public void setFiles(boolean files) {
        this.files = files;
    }

    public String getPostLinkTitle() {
        return postLinkTitle;
    }

    public void setPostLinkTitle(String postLinkTitle) {
        this.postLinkTitle = postLinkTitle;
    }

    public String getPostLinkDesc() {
        return postLinkDesc;
    }

    public void setPostLinkDesc(String postLinkDesc) {
        this.postLinkDesc = postLinkDesc;
    }

    public String getPostLinkUrl() {
        return postLinkUrl;
    }

    public void setPostLinkUrl(String postLinkUrl) {
        this.postLinkUrl = postLinkUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(String sharedPostId) {
        this.sharedPostId = sharedPostId;
    }

    public String getPostWallUserid() {
        return postWallUserid;
    }

    public void setPostWallUserid(String postWallUserid) {
        this.postWallUserid = postWallUserid;
    }

    public String getIsWall() {
        return isWall;
    }

    public void setIsWall(String isWall) {
        this.isWall = isWall;
    }

    public String getPostWallUsername() {
        return postWallUsername;
    }

    public void setPostWallUsername(String postWallUsername) {
        this.postWallUsername = postWallUsername;
    }

    public String getPostWallFirstName() {
        return postWallFirstName;
    }

    public void setPostWallFirstName(String postWallFirstName) {
        this.postWallFirstName = postWallFirstName;
    }

    public String getPostWallLastName() {
        return postWallLastName;
    }

    public void setPostWallLastName(String postWallLastName) {
        this.postWallLastName = postWallLastName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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

    public PostFooter getPostFooter() {
        return postFooter;
    }

    public void setPostFooter(PostFooter postFooter) {
        this.postFooter = postFooter;
    }

    public String getHasMeme() {
        return hasMeme;
    }

    public void setHasMeme(String hasMeme) {
        this.hasMeme = hasMeme;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getUserTotalFollowers() {
        return userTotalFollowers;
    }

    public void setUserTotalFollowers(String userTotalFollowers) {
        this.userTotalFollowers = userTotalFollowers;
    }

    public String getMemePreview() {
        return memePreview;
    }

    public void setMemePreview(String memePreview) {
        this.memePreview = memePreview;
    }

    public String getListClassName() {
        return listClassName;
    }

    public void setListClassName(String listClassName) {
        this.listClassName = listClassName;
    }

    public String getInputAddClassName() {
        return inputAddClassName;
    }

    public void setInputAddClassName(String inputAddClassName) {
        this.inputAddClassName = inputAddClassName;
    }

    public String getVideoUploadStatus() {
        return videoUploadStatus;
    }

    public void setVideoUploadStatus(String videoUploadStatus) {
        this.videoUploadStatus = videoUploadStatus;
    }

    public int getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
    }

    public boolean isIsNotificationOff() {
        return isNotificationOff;
    }

    public void setIsNotificationOff(boolean isNotificationOff) {
        this.isNotificationOff = isNotificationOff;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postId);
        dest.writeValue(postType);
        dest.writeValue(isShared);
        dest.writeValue(hasShared);
        dest.writeValue(hasMention);
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
        dest.writeValue(postText);
        dest.writeList(postTextIndex);
        dest.writeValue(postImage);
        dest.writeValue(files);
        dest.writeValue(postLinkTitle);
        dest.writeValue(postLinkDesc);
        dest.writeValue(postLinkUrl);
        dest.writeValue(videoName);
        dest.writeValue(sharedPostId);
        dest.writeValue(postWallUserid);
        dest.writeValue(isWall);
        dest.writeValue(postWallUsername);
        dest.writeValue(postWallFirstName);
        dest.writeValue(postWallLastName);
        dest.writeValue(catName);
        dest.writeValue(catId);
        dest.writeValue(dateTime);
        dest.writeValue(permission);
        dest.writeValue(postFooter);
        dest.writeValue(hasMeme);
        dest.writeValue(totalComment);
        dest.writeValue(userTotalFollowers);
        dest.writeValue(memePreview);
        dest.writeValue(listClassName);
        dest.writeValue(inputAddClassName);
        dest.writeValue(videoUploadStatus);
        dest.writeValue(videoDuration);
        dest.writeValue(isNotificationOff);
    }

    public int describeContents() {
        return  0;
    }

}
