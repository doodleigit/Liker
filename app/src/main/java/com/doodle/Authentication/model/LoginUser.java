
package com.doodle.Authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUser implements Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    @SerializedName("bounce_data")
    @Expose
    private String bounceData;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;
    @SerializedName("token")
    @Expose
    private String token;
    private final static long serialVersionUID = -4915801485631823249L;

    protected LoginUser(Parcel in) {
        status = in.readByte() != 0;
        isVerified = in.readString();
        bounceData = in.readString();
        userInfo = in.readParcelable(UserInfo.class.getClassLoader());
        token = in.readString();
    }

    public static final Creator<LoginUser> CREATOR = new Creator<LoginUser>() {
        @Override
        public LoginUser createFromParcel(Parcel in) {
            return new LoginUser(in);
        }

        @Override
        public LoginUser[] newArray(int size) {
            return new LoginUser[size];
        }
    };

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getBounceData() {
        return bounceData;
    }

    public void setBounceData(String bounceData) {
        this.bounceData = bounceData;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(isVerified);
        dest.writeString(bounceData);
        dest.writeParcelable(userInfo, flags);
        dest.writeString(token);
    }
}
