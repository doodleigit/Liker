
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Headers implements Serializable, Parcelable
{

    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("security_token")
    @Expose
    private String securityToken;
    @SerializedName("is_apps")
    @Expose
    private boolean isApps;
    public final static Creator<Headers> CREATOR = new Creator<Headers>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Headers createFromParcel(Parcel in) {
            return new Headers(in);
        }

        public Headers[] newArray(int size) {
            return (new Headers[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1684275393776627478L;

    protected Headers(Parcel in) {
        this.deviceId = ((String) in.readValue((String.class.getClassLoader())));
        this.securityToken = ((String) in.readValue((String.class.getClassLoader())));
        this.isApps = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public Headers() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public boolean isIsApps() {
        return isApps;
    }

    public void setIsApps(boolean isApps) {
        this.isApps = isApps;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(deviceId);
        dest.writeValue(securityToken);
        dest.writeValue(isApps);
    }

    public int describeContents() {
        return  0;
    }

}
