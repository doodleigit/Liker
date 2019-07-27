
package com.doodle.Message.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chats implements Serializable, Parcelable
{

    @SerializedName("messages")
    @Expose
    private ArrayList<Messages> messages = null;
    @SerializedName("users")
    @Expose
    private ArrayList<User> users = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("privacy")
    @Expose
    private Privacy privacy;
    public final static Creator<Chats> CREATOR = new Creator<Chats>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Chats createFromParcel(Parcel in) {
            return new Chats(in);
        }

        public Chats[] newArray(int size) {
            return (new Chats[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1479661772829310598L;

    protected Chats(Parcel in) {
        in.readList(this.messages, (Message.class.getClassLoader()));
        in.readList(this.users, (User.class.getClassLoader()));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.privacy = ((Privacy) in.readValue((Privacy.class.getClassLoader())));
    }

    public Chats() {
    }

    public ArrayList<Messages> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Messages> messages) {
        this.messages = messages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(messages);
        dest.writeList(users);
        dest.writeValue(status);
        dest.writeValue(privacy);
    }

    public int describeContents() {
        return  0;
    }

}
