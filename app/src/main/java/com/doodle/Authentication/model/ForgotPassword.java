package com.doodle.Authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassword implements Parcelable {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("bounce_data")
    @Expose
    private String bounceData;

    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("error")
    @Expose
    private Error error;
    private final static long serialVersionUID = -5635064372146150157L;

    protected ForgotPassword(Parcel in) {
        status = in.readByte() != 0;
        bounceData = in.readString();
        userId = in.readString();
        isVerified = in.readString();
    }

    public static final Creator<ForgotPassword> CREATOR = new Creator<ForgotPassword>() {
        @Override
        public ForgotPassword createFromParcel(Parcel in) {
            return new ForgotPassword(in);
        }

        @Override
        public ForgotPassword[] newArray(int size) {
            return new ForgotPassword[size];
        }
    };

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBounceData() {
        return bounceData;
    }

    public void setBounceData(String bounceData) {
        this.bounceData = bounceData;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(bounceData);
        dest.writeString(userId);
        dest.writeString(isVerified);
    }
}
