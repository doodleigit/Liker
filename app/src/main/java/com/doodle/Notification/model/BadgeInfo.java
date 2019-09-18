
package com.doodle.Notification.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BadgeInfo implements Serializable, Parcelable
{

    public final static Creator<BadgeInfo> CREATOR = new Creator<BadgeInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public BadgeInfo createFromParcel(Parcel in) {
            return new BadgeInfo(in);
        }

        public BadgeInfo[] newArray(int size) {
            return (new BadgeInfo[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7110774886061981687L;

    protected BadgeInfo(Parcel in) {
    }

    public BadgeInfo() {
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    public int describeContents() {
        return  0;
    }

}
