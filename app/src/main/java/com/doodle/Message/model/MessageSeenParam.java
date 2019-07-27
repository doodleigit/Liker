package com.doodle.Message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.doodle.Home.model.Headers;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageSeenParam implements Serializable, Parcelable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("chat_user_id")
    @Expose
    private String chatUserId;

    public final static Creator<MessageSendParam> CREATOR = new Creator<MessageSendParam>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MessageSendParam createFromParcel(Parcel in) {
            return new MessageSendParam(in);
        }

        public MessageSendParam[] newArray(int size) {
            return (new MessageSendParam[size]);
        }

    };

    protected MessageSeenParam(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.chatUserId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MessageSeenParam() {
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(String chatUserId) {
        this.chatUserId = chatUserId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(chatUserId);
    }

    public int describeContents() {
        return 0;
    }

}