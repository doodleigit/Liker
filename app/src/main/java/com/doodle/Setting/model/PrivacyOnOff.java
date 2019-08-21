
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivacyOnOff implements Serializable, Parcelable
{

    @SerializedName("statuses")
    @Expose
    private ArrayList<Status> statuses = null;
    public final static Creator<PrivacyOnOff> CREATOR = new Creator<PrivacyOnOff>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PrivacyOnOff createFromParcel(Parcel in) {
            return new PrivacyOnOff(in);
        }

        public PrivacyOnOff[] newArray(int size) {
            return (new PrivacyOnOff[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8448067413476292640L;

    protected PrivacyOnOff(Parcel in) {
        in.readList(this.statuses, (com.doodle.Setting.model.Status.class.getClassLoader()));
    }

    public PrivacyOnOff() {
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(statuses);
    }

    public int describeContents() {
        return  0;
    }

}
