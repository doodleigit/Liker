
package com.doodle.Message.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatUser implements Serializable, Parcelable
{

    @SerializedName("message_data")
    @Expose
    private MessageData messageData;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("unread_total")
    @Expose
    private Integer unreadTotal;
    public final static Creator<ChatUser> CREATOR = new Creator<ChatUser>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ChatUser createFromParcel(Parcel in) {
            return new ChatUser(in);
        }

        public ChatUser[] newArray(int size) {
            return (new ChatUser[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7219408890585906949L;

    protected ChatUser(Parcel in) {
        this.messageData = ((MessageData) in.readValue((MessageData.class.getClassLoader())));
        this.userData = ((UserData) in.readValue((UserData.class.getClassLoader())));
        this.unreadTotal = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public ChatUser() {
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Integer getUnreadTotal() {
        return unreadTotal;
    }

    public void setUnreadTotal(Integer unreadTotal) {
        this.unreadTotal = unreadTotal;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(messageData);
        dest.writeValue(userData);
        dest.writeValue(unreadTotal);
    }

    public int describeContents() {
        return  0;
    }

}
