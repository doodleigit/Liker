
package com.doodle.Message.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable, Parcelable
{

    @SerializedName("chat_users")
    @Expose
    private ArrayList<ChatUser> chatUsers = new ArrayList<ChatUser>();
    @SerializedName("actual_user_fetched")
    @Expose
    private Integer actualUserFetched;
    @SerializedName("status")
    @Expose
    private Boolean status;
    public final static Creator<Message> CREATOR = new Creator<Message>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return (new Message[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2589574594540963863L;

    protected Message(Parcel in) {
        in.readList(this.chatUsers, (ChatUser.class.getClassLoader()));
        this.actualUserFetched = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Message() {
    }

    public ArrayList<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(ArrayList<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public Integer getActualUserFetched() {
        return actualUserFetched;
    }

    public void setActualUserFetched(Integer actualUserFetched) {
        this.actualUserFetched = actualUserFetched;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(chatUsers);
        dest.writeValue(actualUserFetched);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
