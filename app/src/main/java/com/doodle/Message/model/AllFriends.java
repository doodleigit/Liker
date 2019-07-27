
package com.doodle.Message.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllFriends implements Serializable, Parcelable
{

    @SerializedName("friends")
    @Expose
    private ArrayList<Friend> friends = new ArrayList<>();
    @SerializedName("is_friends")
    @Expose
    private Boolean isFriends;
    public final static Creator<AllFriends> CREATOR = new Creator<AllFriends>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AllFriends createFromParcel(Parcel in) {
            return new AllFriends(in);
        }

        public AllFriends[] newArray(int size) {
            return (new AllFriends[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1040173001284258834L;

    protected AllFriends(Parcel in) {
        in.readList(this.friends, (Friend.class.getClassLoader()));
        this.isFriends = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public AllFriends() {
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public Boolean getIsFriends() {
        return isFriends;
    }

    public void setIsFriends(Boolean isFriends) {
        this.isFriends = isFriends;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(friends);
        dest.writeValue(isFriends);
    }

    public int describeContents() {
        return  0;
    }

}
