
package com.doodle.Comment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReportReason implements Serializable, Parcelable
{

    @SerializedName("reason")
    @Expose
    private List<Reason> reason = new ArrayList<Reason>();
    @SerializedName("is_friends")
    @Expose
    private boolean isFriends;

    @SerializedName("is_followed")
    @Expose
    private boolean isFollowed;

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public final static Creator<ReportReason> CREATOR = new Creator<ReportReason>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ReportReason createFromParcel(Parcel in) {
            return new ReportReason(in);
        }

        public ReportReason[] newArray(int size) {
            return (new ReportReason[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3534312064470004422L;

    protected ReportReason(Parcel in) {
        in.readList(this.reason, (Reason.class.getClassLoader()));
        this.isFriends = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.isFollowed = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public ReportReason() {
    }

    public List<Reason> getReason() {
        return reason;
    }

    public void setReason(List<Reason> reason) {
        this.reason = reason;
    }

    public boolean isIsFriends() {
        return isFriends;
    }

    public void setIsFriends(boolean isFriends) {
        this.isFriends = isFriends;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(reason);
        dest.writeValue(isFriends);
        dest.writeValue(isFollowed);
    }

    public int describeContents() {
        return  0;
    }

}
