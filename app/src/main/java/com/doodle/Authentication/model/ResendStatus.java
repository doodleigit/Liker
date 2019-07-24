package com.doodle.Authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResendStatus implements Parcelable {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -1715529688850188447L;

    protected ResendStatus(Parcel in) {
        status = in.readInt();
        message = in.readString();
    }

    public static final Creator<ResendStatus> CREATOR = new Creator<ResendStatus>() {
        @Override
        public ResendStatus createFromParcel(Parcel in) {
            return new ResendStatus(in);
        }

        @Override
        public ResendStatus[] newArray(int size) {
            return new ResendStatus[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(message);
    }
}