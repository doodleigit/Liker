
package com.doodle.Profile.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Followers implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result")
    @Expose
    private List<FollowersResult> followersResult = null;
    public final static Creator<Followers> CREATOR = new Creator<Followers>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Followers createFromParcel(Parcel in) {
            return new Followers(in);
        }

        public Followers[] newArray(int size) {
            return (new Followers[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5157181537763231131L;

    protected Followers(Parcel in) {
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.followersResult, (FollowersResult.class.getClassLoader()));
    }

    public Followers() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<FollowersResult> getFollowersResult() {
        return followersResult;
    }

    public void setFollowersResult(List<FollowersResult> followersResult) {
        this.followersResult = followersResult;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(followersResult);
    }

    public int describeContents() {
        return  0;
    }

}
