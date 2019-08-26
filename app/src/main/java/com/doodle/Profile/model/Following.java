
package com.doodle.Profile.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Following implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result")
    @Expose
    private List<FollowingResult> followingResult = null;
    public final static Creator<Following> CREATOR = new Creator<Following>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Following createFromParcel(Parcel in) {
            return new Following(in);
        }

        public Following[] newArray(int size) {
            return (new Following[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7814316619936034524L;

    protected Following(Parcel in) {
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        in.readList(this.followingResult, (FollowingResult.class.getClassLoader()));
    }

    public Following() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<FollowingResult> getFollowingResult() {
        return followingResult;
    }

    public void setFollowingResult(List<FollowingResult> followingResult) {
        this.followingResult = followingResult;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(followingResult);
    }

    public int describeContents() {
        return  0;
    }

}
