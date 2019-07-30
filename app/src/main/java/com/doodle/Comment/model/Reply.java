
package com.doodle.Comment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Reply implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment_type")
    @Expose
    private String commentType;
    @SerializedName("text_size")
    @Expose
    private String textSize;
    @SerializedName("has_mention")
    @Expose
    private String hasMention;
    @SerializedName("is_block")
    @Expose
    private String isBlock;
    @SerializedName("total_like")
    @Expose
    private String totalLike;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("is_like_replied")
    @Expose
    private boolean isLikeReplied;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_total_likes")
    @Expose
    private String userTotalLikes;
    @SerializedName("user_gold_stars")
    @Expose
    private String userGoldStars;
    @SerializedName("user_sliver_stars")
    @Expose
    private String userSliverStars;
    @SerializedName("user_top_commenter")
    @Expose
    private String userTopCommenter;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("deactivated")
    @Expose
    private String deactivated;
    @SerializedName("comment_text")
    @Expose
    private String commentText;
    @SerializedName("comment_image")
    @Expose
    private String commentImage;
    @SerializedName("comment_text_index")
    @Expose
    private List<CommentTextIndex_> commentTextIndex = new ArrayList<CommentTextIndex_>();
    @SerializedName("link_data")
    @Expose
    private LinkData_ linkData;
    public final static Creator<Reply> CREATOR = new Creator<Reply>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        public Reply[] newArray(int size) {
            return (new Reply[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7066296240955379444L;

    protected Reply(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.commentId = ((String) in.readValue((String.class.getClassLoader())));
        this.commentType = ((String) in.readValue((String.class.getClassLoader())));
        this.textSize = ((String) in.readValue((String.class.getClassLoader())));
        this.hasMention = ((String) in.readValue((String.class.getClassLoader())));
        this.isBlock = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLike = ((String) in.readValue((String.class.getClassLoader())));
        this.dateTime = ((String) in.readValue((String.class.getClassLoader())));
        this.isLikeReplied = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.userTotalLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.userGoldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userSliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userTopCommenter = ((String) in.readValue((String.class.getClassLoader())));
        this.userPhoto = ((String) in.readValue((String.class.getClassLoader())));
        this.deactivated = ((String) in.readValue((String.class.getClassLoader())));
        this.commentText = ((String) in.readValue((String.class.getClassLoader())));
        this.commentImage = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.commentTextIndex, (CommentTextIndex_.class.getClassLoader()));
        this.linkData = ((LinkData_) in.readValue((LinkData_.class.getClassLoader())));
    }

    public Reply() {
    }

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

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getHasMention() {
        return hasMention;
    }

    public void setHasMention(String hasMention) {
        this.hasMention = hasMention;
    }

    public String getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(String isBlock) {
        this.isBlock = isBlock;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isIsLikeReplied() {
        return isLikeReplied;
    }

    public void setIsLikeReplied(boolean isLikeReplied) {
        this.isLikeReplied = isLikeReplied;
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

    public String getUserTopCommenter() {
        return userTopCommenter;
    }

    public void setUserTopCommenter(String userTopCommenter) {
        this.userTopCommenter = userTopCommenter;
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

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentImage() {
        return commentImage;
    }

    public void setCommentImage(String commentImage) {
        this.commentImage = commentImage;
    }

    public List<CommentTextIndex_> getCommentTextIndex() {
        return commentTextIndex;
    }

    public void setCommentTextIndex(List<CommentTextIndex_> commentTextIndex) {
        this.commentTextIndex = commentTextIndex;
    }

    public LinkData_ getLinkData() {
        return linkData;
    }

    public void setLinkData(LinkData_ linkData) {
        this.linkData = linkData;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(commentId);
        dest.writeValue(commentType);
        dest.writeValue(textSize);
        dest.writeValue(hasMention);
        dest.writeValue(isBlock);
        dest.writeValue(totalLike);
        dest.writeValue(dateTime);
        dest.writeValue(isLikeReplied);
        dest.writeValue(userName);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(userTotalLikes);
        dest.writeValue(userGoldStars);
        dest.writeValue(userSliverStars);
        dest.writeValue(userTopCommenter);
        dest.writeValue(userPhoto);
        dest.writeValue(deactivated);
        dest.writeValue(commentText);
        dest.writeValue(commentImage);
        dest.writeList(commentTextIndex);
        dest.writeValue(linkData);
    }

    public int describeContents() {
        return  0;
    }

}
