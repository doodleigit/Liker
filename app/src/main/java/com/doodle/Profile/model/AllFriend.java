
package com.doodle.Profile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllFriend implements Serializable, Parcelable
{

    @SerializedName("friends")
    @Expose
    private ArrayList<Friend> friends = null;
    @SerializedName("is_friends")
    @Expose
    private Boolean isFriends;
    public final static Creator<AllFriend> CREATOR = new Creator<AllFriend>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AllFriend createFromParcel(Parcel in) {
            return new AllFriend(in);
        }

        public AllFriend[] newArray(int size) {
            return (new AllFriend[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6354841124290372795L;

    protected AllFriend(Parcel in) {
        in.readList(this.friends, (com.doodle.Profile.model.Friend.class.getClassLoader()));
        this.isFriends = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public AllFriend() {
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
