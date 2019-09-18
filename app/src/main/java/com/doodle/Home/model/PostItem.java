
package com.doodle.Home.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostItem implements Serializable, Parcelable
{

    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("frame_number")
    @Expose
    private int frameNumber;
    @SerializedName("has_meme")
    @Expose
    private String hasMeme;
    @SerializedName("has_mention")
    @Expose
    private String hasMention;
    @SerializedName("has_shared")
    @Expose
    private int hasShared;
    @SerializedName("input_add_class_name")
    @Expose
    private String inputAddClassName;
    @SerializedName("is_notification_off")
    @Expose
    private boolean isNotificationOff;
    @SerializedName("is_shared")
    @Expose
    private String isShared;
    @SerializedName("is_wall")
    @Expose
    private String isWall;
    @SerializedName("list_class_name")
    @Expose
    private String listClassName;
    @SerializedName("meme_preview")
    @Expose
    private String memePreview;
    @SerializedName("mentioned_user_ids")
    @Expose
    private List<String> mentionedUserIds = new ArrayList<String>();
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("post_files")
    @Expose
    private List<PostFile> postFiles = new ArrayList<PostFile>();
    @SerializedName("post_footer")
    @Expose
    private PostFooter postFooter;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("post_image")
    @Expose
    private String postImage;
    @SerializedName("post_link_desc")
    @Expose
    private String postLinkDesc;
    @SerializedName("post_link_host")
    @Expose
    private String postLinkHost;
    @SerializedName("post_link_title")
    @Expose
    private String postLinkTitle;
    @SerializedName("post_link_url")
    @Expose
    private String postLinkUrl;
    @SerializedName("post_text")
    @Expose
    private String postText;
    @SerializedName("post_text_index")
    @Expose
    private List<PostTextIndex> postTextIndex = new ArrayList<PostTextIndex>();
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_userid")
    @Expose
    private String postUserid;
    @SerializedName("post_username")
    @Expose
    private String postUsername;
    @SerializedName("post_wall_first_name")
    @Expose
    private String postWallFirstName;
    @SerializedName("post_wall_last_name")
    @Expose
    private String postWallLastName;
    @SerializedName("post_wall_userid")
    @Expose
    private String postWallUserid;
    @SerializedName("post_wall_username")
    @Expose
    private String postWallUsername;
    @SerializedName("shared_has_meme")
    @Expose
    private String sharedHasMeme;
    @SerializedName("shared_has_mention")
    @Expose
    private boolean sharedHasMention;
    @SerializedName("shared_input_add_class_name")
    @Expose
    private String sharedInputAddClassName;
    @SerializedName("shared_list_class_name")
    @Expose
    private String sharedListClassName;
    @SerializedName("shared_meme_preview")
    @Expose
    private String sharedMemePreview;
    @SerializedName("shared_post_id")
    @Expose
    private String sharedPostId;
    @SerializedName("shared_post_text")
    @Expose
    private String sharedPostText;
    @SerializedName("shared_post_text_index")
    @Expose
    private List<SharedPostTextIndex> sharedPostTextIndex = new ArrayList<SharedPostTextIndex>();
    @SerializedName("shared_profile")
    @Expose
    private SharedProfile sharedProfile;
    @SerializedName("total_comment")
    @Expose
    private String totalComment;
    @SerializedName("uesr_profile_img")
    @Expose
    private String uesrProfileImg;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_founding_member")
    @Expose
    private String userFoundingMember;
    @SerializedName("user_gold_stars")
    @Expose
    private String userGoldStars;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("user_profile_likes")
    @Expose
    private String userProfileLikes;
    @SerializedName("user_silver_stars")
    @Expose
    private String userSilverStars;
    @SerializedName("user_top_commenter")
    @Expose
    private String userTopCommenter;
    @SerializedName("user_total_followers")
    @Expose
    private String userTotalFollowers;
    public final static Creator<PostItem> CREATOR = new Creator<PostItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PostItem createFromParcel(Parcel in) {
            return new PostItem(in);
        }

        public PostItem[] newArray(int size) {
            return (new PostItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = -859773688598186189L;

    protected PostItem(Parcel in) {
        this.catId = ((String) in.readValue((String.class.getClassLoader())));
        this.catName = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        this.frameNumber = ((int) in.readValue((int.class.getClassLoader())));
        this.hasMeme = ((String) in.readValue((String.class.getClassLoader())));
        this.hasMention = ((String) in.readValue((String.class.getClassLoader())));
        this.hasShared = ((int) in.readValue((int.class.getClassLoader())));
        this.inputAddClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.isNotificationOff = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.isShared = ((String) in.readValue((String.class.getClassLoader())));
        this.isWall = ((String) in.readValue((String.class.getClassLoader())));
        this.listClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.memePreview = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.mentionedUserIds, (String.class.getClassLoader()));
        this.permission = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.postFiles, (PostFile.class.getClassLoader()));
        this.postFooter = ((PostFooter) in.readValue((PostFooter.class.getClassLoader())));
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        this.postImage = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkHost = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.postText = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.postTextIndex, (PostTextIndex.class.getClassLoader()));
        this.postType = ((String) in.readValue((String.class.getClassLoader())));
        this.postUserid = ((String) in.readValue((String.class.getClassLoader())));
        this.postUsername = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallUserid = ((String) in.readValue((String.class.getClassLoader())));
        this.postWallUsername = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedHasMeme = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedHasMention = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.sharedInputAddClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedListClassName = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedMemePreview = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedPostId = ((String) in.readValue((String.class.getClassLoader())));
        this.sharedPostText = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.sharedPostTextIndex, (SharedPostTextIndex.class.getClassLoader()));
        this.sharedProfile = ((SharedProfile) in.readValue((SharedProfile.class.getClassLoader())));
        this.totalComment = ((String) in.readValue((String.class.getClassLoader())));
        this.uesrProfileImg = ((String) in.readValue((String.class.getClassLoader())));
        this.userFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.userFoundingMember = ((String) in.readValue((String.class.getClassLoader())));
        this.userGoldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.userProfileLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.userSilverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userTopCommenter = ((String) in.readValue((String.class.getClassLoader())));
        this.userTotalFollowers = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PostItem() {
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getHasMeme() {
        return hasMeme;
    }

    public void setHasMeme(String hasMeme) {
        this.hasMeme = hasMeme;
    }

    public String getHasMention() {
        return hasMention;
    }

    public void setHasMention(String hasMention) {
        this.hasMention = hasMention;
    }

    public int getHasShared() {
        return hasShared;
    }

    public void setHasShared(int hasShared) {
        this.hasShared = hasShared;
    }

    public String getInputAddClassName() {
        return inputAddClassName;
    }

    public void setInputAddClassName(String inputAddClassName) {
        this.inputAddClassName = inputAddClassName;
    }

    public boolean isIsNotificationOff() {
        return isNotificationOff;
    }

    public void setIsNotificationOff(boolean isNotificationOff) {
        this.isNotificationOff = isNotificationOff;
    }

    public String getIsShared() {
        return isShared;
    }

    public void setIsShared(String isShared) {
        this.isShared = isShared;
    }

    public String getIsWall() {
        return isWall;
    }

    public void setIsWall(String isWall) {
        this.isWall = isWall;
    }

    public String getListClassName() {
        return listClassName;
    }

    public void setListClassName(String listClassName) {
        this.listClassName = listClassName;
    }

    public String getMemePreview() {
        return memePreview;
    }

    public void setMemePreview(String memePreview) {
        this.memePreview = memePreview;
    }

    public List<String> getMentionedUserIds() {
        return mentionedUserIds;
    }

    public void setMentionedUserIds(List<String> mentionedUserIds) {
        this.mentionedUserIds = mentionedUserIds;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<PostFile> getPostFiles() {
        return postFiles;
    }

    public void setPostFiles(List<PostFile> postFiles) {
        this.postFiles = postFiles;
    }

    public PostFooter getPostFooter() {
        return postFooter;
    }

    public void setPostFooter(PostFooter postFooter) {
        this.postFooter = postFooter;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostLinkDesc() {
        return postLinkDesc;
    }

    public void setPostLinkDesc(String postLinkDesc) {
        this.postLinkDesc = postLinkDesc;
    }

    public String getPostLinkHost() {
        return postLinkHost;
    }

    public void setPostLinkHost(String postLinkHost) {
        this.postLinkHost = postLinkHost;
    }

    public String getPostLinkTitle() {
        return postLinkTitle;
    }

    public void setPostLinkTitle(String postLinkTitle) {
        this.postLinkTitle = postLinkTitle;
    }

    public String getPostLinkUrl() {
        return postLinkUrl;
    }

    public void setPostLinkUrl(String postLinkUrl) {
        this.postLinkUrl = postLinkUrl;
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
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

    public String getPostWallUserid() {
        return postWallUserid;
    }

    public void setPostWallUserid(String postWallUserid) {
        this.postWallUserid = postWallUserid;
    }

    public String getPostWallUsername() {
        return postWallUsername;
    }

    public void setPostWallUsername(String postWallUsername) {
        this.postWallUsername = postWallUsername;
    }

    public String getSharedHasMeme() {
        return sharedHasMeme;
    }

    public void setSharedHasMeme(String sharedHasMeme) {
        this.sharedHasMeme = sharedHasMeme;
    }

    public boolean isSharedHasMention() {
        return sharedHasMention;
    }

    public void setSharedHasMention(boolean sharedHasMention) {
        this.sharedHasMention = sharedHasMention;
    }

    public String getSharedInputAddClassName() {
        return sharedInputAddClassName;
    }

    public void setSharedInputAddClassName(String sharedInputAddClassName) {
        this.sharedInputAddClassName = sharedInputAddClassName;
    }

    public String getSharedListClassName() {
        return sharedListClassName;
    }

    public void setSharedListClassName(String sharedListClassName) {
        this.sharedListClassName = sharedListClassName;
    }

    public String getSharedMemePreview() {
        return sharedMemePreview;
    }

    public void setSharedMemePreview(String sharedMemePreview) {
        this.sharedMemePreview = sharedMemePreview;
    }

    public String getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(String sharedPostId) {
        this.sharedPostId = sharedPostId;
    }

    public String getSharedPostText() {
        return sharedPostText;
    }

    public void setSharedPostText(String sharedPostText) {
        this.sharedPostText = sharedPostText;
    }

    public List<SharedPostTextIndex> getSharedPostTextIndex() {
        return sharedPostTextIndex;
    }

    public void setSharedPostTextIndex(List<SharedPostTextIndex> sharedPostTextIndex) {
        this.sharedPostTextIndex = sharedPostTextIndex;
    }

    public SharedProfile getSharedProfile() {
        return sharedProfile;
    }

    public void setSharedProfile(SharedProfile sharedProfile) {
        this.sharedProfile = sharedProfile;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getUesrProfileImg() {
        return uesrProfileImg;
    }

    public void setUesrProfileImg(String uesrProfileImg) {
        this.uesrProfileImg = uesrProfileImg;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserFoundingMember() {
        return userFoundingMember;
    }

    public void setUserFoundingMember(String userFoundingMember) {
        this.userFoundingMember = userFoundingMember;
    }

    public String getUserGoldStars() {
        return userGoldStars;
    }

    public void setUserGoldStars(String userGoldStars) {
        this.userGoldStars = userGoldStars;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserProfileLikes() {
        return userProfileLikes;
    }

    public void setUserProfileLikes(String userProfileLikes) {
        this.userProfileLikes = userProfileLikes;
    }

    public String getUserSilverStars() {
        return userSilverStars;
    }

    public void setUserSilverStars(String userSilverStars) {
        this.userSilverStars = userSilverStars;
    }

    public String getUserTopCommenter() {
        return userTopCommenter;
    }

    public void setUserTopCommenter(String userTopCommenter) {
        this.userTopCommenter = userTopCommenter;
    }

    public String getUserTotalFollowers() {
        return userTotalFollowers;
    }

    public void setUserTotalFollowers(String userTotalFollowers) {
        this.userTotalFollowers = userTotalFollowers;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(catId);
        dest.writeValue(catName);
        dest.writeValue(dateTime);
        dest.writeValue(frameNumber);
        dest.writeValue(hasMeme);
        dest.writeValue(hasMention);
        dest.writeValue(hasShared);
        dest.writeValue(inputAddClassName);
        dest.writeValue(isNotificationOff);
        dest.writeValue(isShared);
        dest.writeValue(isWall);
        dest.writeValue(listClassName);
        dest.writeValue(memePreview);
        dest.writeList(mentionedUserIds);
        dest.writeValue(permission);
        dest.writeList(postFiles);
        dest.writeValue(postFooter);
        dest.writeValue(postId);
        dest.writeValue(postImage);
        dest.writeValue(postLinkDesc);
        dest.writeValue(postLinkHost);
        dest.writeValue(postLinkTitle);
        dest.writeValue(postLinkUrl);
        dest.writeValue(postText);
        dest.writeList(postTextIndex);
        dest.writeValue(postType);
        dest.writeValue(postUserid);
        dest.writeValue(postUsername);
        dest.writeValue(postWallFirstName);
        dest.writeValue(postWallLastName);
        dest.writeValue(postWallUserid);
        dest.writeValue(postWallUsername);
        dest.writeValue(sharedHasMeme);
        dest.writeValue(sharedHasMention);
        dest.writeValue(sharedInputAddClassName);
        dest.writeValue(sharedListClassName);
        dest.writeValue(sharedMemePreview);
        dest.writeValue(sharedPostId);
        dest.writeValue(sharedPostText);
        dest.writeList(sharedPostTextIndex);
        dest.writeValue(sharedProfile);
        dest.writeValue(totalComment);
        dest.writeValue(uesrProfileImg);
        dest.writeValue(userFirstName);
        dest.writeValue(userFoundingMember);
        dest.writeValue(userGoldStars);
        dest.writeValue(userLastName);
        dest.writeValue(userProfileLikes);
        dest.writeValue(userSilverStars);
        dest.writeValue(userTopCommenter);
        dest.writeValue(userTotalFollowers);
    }

    public int describeContents() {
        return  0;
    }

}
