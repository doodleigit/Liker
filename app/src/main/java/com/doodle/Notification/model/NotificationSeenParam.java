package com.doodle.Notification.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.doodle.Home.model.Headers;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationSeenParam implements Serializable, Parcelable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("notification_id")
    @Expose
    private String notificationId;

    public final static Creator<NotificationSeenParam> CREATOR = new Creator<NotificationSeenParam>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NotificationSeenParam createFromParcel(Parcel in) {
            return new NotificationSeenParam(in);
        }

        public NotificationSeenParam[] newArray(int size) {
            return (new NotificationSeenParam[size]);
        }

    };

    protected NotificationSeenParam(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public NotificationSeenParam() {
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

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(notificationId);
    }

    public int describeContents() {
        return 0;
    }
}
