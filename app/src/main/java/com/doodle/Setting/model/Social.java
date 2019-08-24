
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Social implements Serializable, Parcelable
{

    @SerializedName("social_friends")
    @Expose
    private List<SocialFriend> socialFriends = null;
    @SerializedName("is_referrer_exists")
    @Expose
    private Integer isReferrerExists;
    public final static Creator<Social> CREATOR = new Creator<Social>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Social createFromParcel(Parcel in) {
            return new Social(in);
        }

        public Social[] newArray(int size) {
            return (new Social[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4969376859319293596L;

    protected Social(Parcel in) {
        in.readList(this.socialFriends, (com.doodle.Setting.model.SocialFriend.class.getClassLoader()));
        this.isReferrerExists = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Social() {
    }

    public List<SocialFriend> getSocialFriends() {
        return socialFriends;
    }

    public void setSocialFriends(List<SocialFriend> socialFriends) {
        this.socialFriends = socialFriends;
    }

    public Integer getIsReferrerExists() {
        return isReferrerExists;
    }

    public void setIsReferrerExists(Integer isReferrerExists) {
        this.isReferrerExists = isReferrerExists;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(socialFriends);
        dest.writeValue(isReferrerExists);
    }

    public int describeContents() {
        return  0;
    }

}
