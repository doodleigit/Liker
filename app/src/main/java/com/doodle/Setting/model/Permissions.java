
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permissions implements Serializable, Parcelable
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
    public final static Creator<Permissions> CREATOR = new Creator<Permissions>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Permissions createFromParcel(Parcel in) {
            return new Permissions(in);
        }

        public Permissions[] newArray(int size) {
            return (new Permissions[size]);
        }

    }
    ;
    private final static long serialVersionUID = 869216059064784419L;

    protected Permissions(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.globalPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.friendSendPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.friendSeePermission = ((String) in.readValue((String.class.getClassLoader())));
        this.wallPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.photosSeePermission = ((String) in.readValue((String.class.getClassLoader())));
        this.messageSendPermission = ((String) in.readValue((String.class.getClassLoader())));
        this.modifyDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Permissions() {
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
    }

    public int describeContents() {
        return  0;
    }

}
