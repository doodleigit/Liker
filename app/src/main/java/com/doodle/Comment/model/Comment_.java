
package com.doodle.Comment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Comment_ implements Serializable, Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("insert_id")
    @Expose
    private int insertId;
    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("comment_type")
    @Expose
    private String commentType;
    @SerializedName("has_mention")
    @Expose
    private String hasMention;
    @SerializedName("comment_text")
    @Expose
    private String commentText;
    @SerializedName("comment_text_index")
    @Expose
    private List<CommentTextIndex> commentTextIndex = new ArrayList<CommentTextIndex>();
    @SerializedName("comment_image")
    @Expose
    private String commentImage;
    @SerializedName("total_like")
    @Expose
    private String totalLike;
    @SerializedName("total_reply")
    @Expose
    private String totalReply;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
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
    @SerializedName("user_top_commenter")
    @Expose
    private String userTopCommenter;
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
    @SerializedName("link_data")
    @Expose
    private LinkData linkData = new LinkData();
    @SerializedName("like_user_status")
    @Expose
    private boolean likeUserStatus;
    @SerializedName("replies")
    @Expose
    private List<Reply> replies = new ArrayList<Reply>();
    public final static Creator<Comment_> CREATOR = new Creator<Comment_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Comment_ createFromParcel(Parcel in) {
            return new Comment_(in);
        }

        public Comment_[] newArray(int size) {
            return (new Comment_[size]);
        }

    };
    private final static long serialVersionUID = 6073528936061608029L;

    protected Comment_(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.insertId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.commentType = ((String) in.readValue((String.class.getClassLoader())));
        this.hasMention = ((String) in.readValue((String.class.getClassLoader())));
        this.commentText = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.commentTextIndex, (CommentTextIndex.class.getClassLoader()));
        this.commentImage = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLike = ((String) in.readValue((String.class.getClassLoader())));
        this.totalReply = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.userLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.userTotalLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.userTopCommenter = ((String) in.readValue((String.class.getClassLoader())));
        this.userGoldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userSliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userPhoto = ((String) in.readValue((String.class.getClassLoader())));
        this.deactivated = ((String) in.readValue((String.class.getClassLoader())));
        this.linkData = ((LinkData) in.readValue((LinkData.class.getClassLoader())));
        this.likeUserStatus = ((boolean) in.readValue((boolean.class.getClassLoader())));
        in.readList(this.replies, (Reply.class.getClassLoader()));
    }

    public Comment_() {
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getHasMention() {
        return hasMention;
    }

    public void setHasMention(String hasMention) {
        this.hasMention = hasMention;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public List<CommentTextIndex> getCommentTextIndex() {
        return commentTextIndex;
    }

    public void setCommentTextIndex(List<CommentTextIndex> commentTextIndex) {
        this.commentTextIndex = commentTextIndex;
    }

    public String getCommentImage() {
        return commentImage;
    }

    public void setCommentImage(String commentImage) {
        this.commentImage = commentImage;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getTotalReply() {
        return totalReply;
    }

    public void setTotalReply(String totalReply) {
        this.totalReply = totalReply;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getUserTopCommenter() {
        return userTopCommenter;
    }

    public void setUserTopCommenter(String userTopCommenter) {
        this.userTopCommenter = userTopCommenter;
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

    public LinkData getLinkData() {
        return linkData;
    }

    public void setLinkData(LinkData linkData) {
        this.linkData = linkData;
    }

    public boolean isLikeUserStatus() {
        return likeUserStatus;
    }

    public void setLikeUserStatus(boolean likeUserStatus) {
        this.likeUserStatus = likeUserStatus;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(insertId);
        dest.writeValue(postId);
        dest.writeValue(userId);
        dest.writeValue(commentType);
        dest.writeValue(hasMention);
        dest.writeValue(commentText);
        dest.writeList(commentTextIndex);
        dest.writeValue(commentImage);
        dest.writeValue(totalLike);
        dest.writeValue(totalReply);
        dest.writeValue(dateTime);
        dest.writeValue(userName);
        dest.writeValue(userFirstName);
        dest.writeValue(userLastName);
        dest.writeValue(userTotalLikes);
        dest.writeValue(userTopCommenter);
        dest.writeValue(userGoldStars);
        dest.writeValue(userSliverStars);
        dest.writeValue(userPhoto);
        dest.writeValue(deactivated);
        dest.writeValue(linkData);
        dest.writeValue(likeUserStatus);
        dest.writeList(replies);
    }

    public int describeContents() {
        return 0;
    }

}
