
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SetUser implements Serializable, Parcelable
{

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("socket_id")
    @Expose
    private String socketId;
    @SerializedName("is_apps")
    @Expose
    private boolean isApps;
    public final static Creator<SetUser> CREATOR = new Creator<SetUser>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SetUser createFromParcel(Parcel in) {
            return new SetUser(in);
        }

        public SetUser[] newArray(int size) {
            return (new SetUser[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7414551382332176747L;

    protected SetUser(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.socketId = ((String) in.readValue((String.class.getClassLoader())));
        this.isApps = ((boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public SetUser() {
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

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public boolean isApps() {
        return isApps;
    }

    public void setApps(boolean apps) {
        isApps = apps;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(socketId);
        dest.writeValue(isApps);
    }

    public int describeContents() {
        return  0;
    }

}
