package com.doodle.Message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.doodle.Home.model.Headers;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageSendParam implements Serializable, Parcelable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("to_user_id")
    @Expose
    private String toUserId;
    @SerializedName("message")
    @Expose
    private String message;

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

    protected MessageSendParam(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.toUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MessageSendParam() {
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

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(toUserId);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }

}