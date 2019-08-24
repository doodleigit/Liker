
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivacyInfo implements Serializable, Parcelable {

    @SerializedName("permissions")
    @Expose
    private Permissions permissions;
    @SerializedName("block_users")
    @Expose
    private ArrayList<BlockUser> blockUsers = null;
    public final static Creator<PrivacyInfo> CREATOR = new Creator<PrivacyInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PrivacyInfo createFromParcel(Parcel in) {
            return new PrivacyInfo(in);
        }

        public PrivacyInfo[] newArray(int size) {
            return (new PrivacyInfo[size]);
        }

    };
    private final static long serialVersionUID = 7706370832213392896L;

    protected PrivacyInfo(Parcel in) {
        this.permissions = ((Permissions) in.readValue((Permissions.class.getClassLoader())));
        in.readList(this.blockUsers, (BlockUser.class.getClassLoader()));
    }

    public PrivacyInfo() {
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public ArrayList<BlockUser> getBlockUsers() {
        return blockUsers;
    }

    public void setBlockUsers(ArrayList<BlockUser> blockUsers) {
        this.blockUsers = blockUsers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(permissions);
        dest.writeList(blockUsers);
    }

    public int describeContents() {
        return 0;
    }

}
