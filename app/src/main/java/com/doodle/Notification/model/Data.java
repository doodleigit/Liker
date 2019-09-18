
package com.doodle.Notification.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("from_user_id")
    @Expose
    private String fromUserId;
    @SerializedName("notif_type")
    @Expose
    private String notifType;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("time_sent")
    @Expose
    private String timeSent;
    @SerializedName("has_seen")
    @Expose
    private String hasSeen;
    @SerializedName("has_seen_details")
    @Expose
    private String hasSeenDetails;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_catg_id")
    @Expose
    private String subCatgId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("badgeInfo")
    @Expose
    private BadgeInfo badgeInfo;
    @SerializedName("is_followed")
    @Expose
    private Boolean isFollowed;
    @SerializedName("post_user_username")
    @Expose
    private String postUserUsername;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1195821140011805011L;

    protected Data(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.fromUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.notifType = ((String) in.readValue((String.class.getClassLoader())));
        this.typeId = ((String) in.readValue((String.class.getClassLoader())));
        this.timeSent = ((String) in.readValue((String.class.getClassLoader())));
        this.hasSeen = ((String) in.readValue((String.class.getClassLoader())));
        this.hasSeenDetails = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.subCatgId = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.badgeInfo = ((BadgeInfo) in.readValue((BadgeInfo.class.getClassLoader())));
        this.isFollowed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.postUserUsername = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getNotifType() {
        return notifType;
    }

    public void setNotifType(String notifType) {
        this.notifType = notifType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(String hasSeen) {
        this.hasSeen = hasSeen;
    }

    public String getHasSeenDetails() {
        return hasSeenDetails;
    }

    public void setHasSeenDetails(String hasSeenDetails) {
        this.hasSeenDetails = hasSeenDetails;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCatgId() {
        return subCatgId;
    }

    public void setSubCatgId(String subCatgId) {
        this.subCatgId = subCatgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BadgeInfo getBadgeInfo() {
        return badgeInfo;
    }

    public void setBadgeInfo(BadgeInfo badgeInfo) {
        this.badgeInfo = badgeInfo;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public String getPostUserUsername() {
        return postUserUsername;
    }

    public void setPostUserUsername(String postUserUsername) {
        this.postUserUsername = postUserUsername;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(fromUserId);
        dest.writeValue(notifType);
        dest.writeValue(typeId);
        dest.writeValue(timeSent);
        dest.writeValue(hasSeen);
        dest.writeValue(hasSeenDetails);
        dest.writeValue(categoryId);
        dest.writeValue(subCatgId);
        dest.writeValue(username);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(photo);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
        dest.writeValue(categoryName);
        dest.writeValue(badgeInfo);
        dest.writeValue(isFollowed);
        dest.writeValue(postUserUsername);
    }

    public int describeContents() {
        return  0;
    }

}
