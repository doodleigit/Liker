
package com.doodle.Home.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostItem implements Serializable, Parcelable {

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
    @SerializedName("post_link_title")
    @Expose
    private String postLinkTitle;
    @SerializedName("post_link_desc")
    @Expose
    private String postLinkDesc;
    @SerializedName("post_link_url")
    @Expose
    private String postLinkUrl;
    @SerializedName("frame_number")
    @Expose
    private int frameNumber;
    @SerializedName("shared_post_id")
    @Expose
    private String sharedPostId;
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
    @SerializedName("is_notification_off")
    @Expose
    private boolean isNotificationOff;

    @SerializedName("mentioned_user_ids")
    @Expose
    private List<String> mentionedUserIds = new ArrayList<String>();
    @SerializedName("post_link_host")
    @Expose
    private String postLinkHost;

    @SerializedName("post_files")
    @Expose
    private List<PostFile> postFiles = new ArrayList<PostFile>();

    public List<PostFile> getPostFiles() {
        return postFiles;
    }

    public void setPostFiles(List<PostFile> postFiles) {
        this.postFiles = postFiles;
    }

    public List<String> getMentionedUserIds() {
        return mentionedUserIds;
    }

    public void setMentionedUserIds(List<String> mentionedUserIds) {
        this.mentionedUserIds = mentionedUserIds;
    }

    public String getPostLinkHost() {
        return postLinkHost;
    }

    public void setPostLinkHost(String postLinkHost) {
        this.postLinkHost = postLinkHost;
    }

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

    };
    private final static long serialVersionUID = 6776697018148002004L;

    protected PostItem(Parcel in) {
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
        in.readList(this.postTextIndex, (com.doodle.Home.model.PostTextIndex.class.getClassLoader()));
        this.postImage = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.frameNumber = ((int) in.readValue((String.class.getClassLoader())));
        this.sharedPostId = ((String) in.readValue((String.class.getClassLoader())));
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
        this.isNotificationOff = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.mentionedUserIds, (java.lang.String.class.getClassLoader()));
        this.postLinkHost = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.postFiles, (PostFile.class.getClassLoader()));
    }

    public PostItem() {
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

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(String sharedPostId) {
        this.sharedPostId = sharedPostId;
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
        dest.writeValue(postLinkTitle);
        dest.writeValue(postLinkDesc);
        dest.writeValue(postLinkUrl);
        dest.writeValue(frameNumber);
        dest.writeValue(sharedPostId);
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
        dest.writeValue(isNotificationOff);
        dest.writeList(mentionedUserIds);
        dest.writeValue(postLinkHost);
        dest.writeList(postFiles);
    }

    public int describeContents() {
        return 0;
    }

}
