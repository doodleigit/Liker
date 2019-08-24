
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Privacy implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("global_permission")
    @Expose
    private String globalPermission;
    @SerializedName("friend_send_permission")
    @Expose
    private String friendSendPermission;
    @SerializedName("friend_see_permission")
    @Expose
    private String friendSeePermission;
    @SerializedName("wall_permission")
    @Expose
    private String wallPermission;
    @SerializedName("photos_see_permission")
    @Expose
    private String photosSeePermission;
    @SerializedName("message_send_permission")
    @Expose
    private String messageSendPermission;
    @SerializedName("modify_date")
    @Expose
    private String modifyDate;
    @SerializedName("is_friends")
    @Expose
    private Boolean isFriends;
    @SerializedName("is_following")
    @Expose
    private Boolean isFollowing;
    @SerializedName("is_request_pending")
    @Expose
    private Boolean isRequestPending;
    public final static Creator<Privacy> CREATOR = new Creator<Privacy>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Privacy createFromParcel(Parcel in) {
            return new Privacy(in);
        }

        public Privacy[] newArray(int size) {
            return (new Privacy[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5436861194610224955L;

    protected Privacy(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.globalPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.friendSendPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.friendSeePermission = ((String) in.readValue((String.class.getClassLoader())));
        this.wallPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.photosSeePermission = ((String) in.readValue((String.class.getClassLoader())));
        this.messageSendPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.modifyDate = ((String) in.readValue((String.class.getClassLoader())));
        this.isFriends = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isFollowing = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isRequestPending = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Privacy() {
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

    public String getGlobalPermission() {
        return globalPermission;
    }

    public void setGlobalPermission(String globalPermission) {
        this.globalPermission = globalPermission;
    }

    public String getFriendSendPermission() {
        return friendSendPermission;
    }

    public void setFriendSendPermission(String friendSendPermission) {
        this.friendSendPermission = friendSendPermission;
    }

    public String getFriendSeePermission() {
        return friendSeePermission;
    }

    public void setFriendSeePermission(String friendSeePermission) {
        this.friendSeePermission = friendSeePermission;
    }

    public String getWallPermission() {
        return wallPermission;
    }

    public void setWallPermission(String wallPermission) {
        this.wallPermission = wallPermission;
    }

    public String getPhotosSeePermission() {
        return photosSeePermission;
    }

    public void setPhotosSeePermission(String photosSeePermission) {
        this.photosSeePermission = photosSeePermission;
    }

    public String getMessageSendPermission() {
        return messageSendPermission;
    }

    public void setMessageSendPermission(String messageSendPermission) {
        this.messageSendPermission = messageSendPermission;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Boolean getIsFriends() {
        return isFriends;
    }

    public void setIsFriends(Boolean isFriends) {
        this.isFriends = isFriends;
    }

    public Boolean getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Boolean getIsRequestPending() {
        return isRequestPending;
    }

    public void setIsRequestPending(Boolean isRequestPending) {
        this.isRequestPending = isRequestPending;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(globalPermission);
        dest.writeValue(friendSendPermission);
        dest.writeValue(friendSeePermission);
        dest.writeValue(wallPermission);
        dest.writeValue(photosSeePermission);
        dest.writeValue(messageSendPermission);
        dest.writeValue(modifyDate);
        dest.writeValue(isFriends);
        dest.writeValue(isFollowing);
        dest.writeValue(isRequestPending);
    }

    public int describeContents() {
        return  0;
    }

}
