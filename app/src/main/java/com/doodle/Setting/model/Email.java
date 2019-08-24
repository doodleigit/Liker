
package com.doodle.Setting.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Email implements Serializable, Parcelable {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("permission_type")
    @Expose
    private String permissionType;
    @SerializedName("is_verified")
    @Expose
    private String isVerified;
    public final static Creator<Email> CREATOR = new Creator<Email>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Email createFromParcel(Parcel in) {
            return new Email(in);
        }

        public Email[] newArray(int size) {
            return (new Email[size]);
        }

    };
    private final static long serialVersionUID = -5123783708540407044L;

    protected Email(Parcel in) {
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.permissionType = ((String) in.readValue((String.class.getClassLoader())));
        this.isVerified = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Email(String email, String type, String permissionType, String isVerified) {
        this.email = email;
        this.type = type;
        this.permissionType = permissionType;
        this.isVerified = isVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(type);
        dest.writeValue(permissionType);
        dest.writeValue(isVerified);
    }

    public int describeContents() {
        return 0;
    }

}
