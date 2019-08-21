
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("notification_name")
    @Expose
    private String notificationName;
    @SerializedName("push")
    @Expose
    private Integer push;
    @SerializedName("email")
    @Expose
    private Integer email;
    @SerializedName("browser")
    @Expose
    private Integer browser;
    public final static Creator<Status> CREATOR = new Creator<Status>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        public Status[] newArray(int size) {
            return (new Status[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3120191284629777616L;

    protected Status(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.notificationName = ((String) in.readValue((String.class.getClassLoader())));
        this.push = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.email = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.browser = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Status() {
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

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public Integer getPush() {
        return push;
    }

    public void setPush(Integer push) {
        this.push = push;
    }

    public Integer getEmail() {
        return email;
    }

    public void setEmail(Integer email) {
        this.email = email;
    }

    public Integer getBrowser() {
        return browser;
    }

    public void setBrowser(Integer browser) {
        this.browser = browser;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(notificationName);
        dest.writeValue(push);
        dest.writeValue(email);
        dest.writeValue(browser);
    }

    public int describeContents() {
        return  0;
    }

}
