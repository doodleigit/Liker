
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeUser implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("like")
    @Expose
    private String like;
    @SerializedName("star")
    @Expose
    private Integer star;
    @SerializedName("gold_star")
    @Expose
    private String goldStar;
    @SerializedName("silver_star")
    @Expose
    private String silverStar;
    @SerializedName("is_followed")
    @Expose
    private Boolean isFollowed;
    public final static Creator<LikeUser> CREATOR = new Creator<LikeUser>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LikeUser createFromParcel(Parcel in) {
            return new LikeUser(in);
        }

        public LikeUser[] newArray(int size) {
            return (new LikeUser[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7936171665233110766L;

    protected LikeUser(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.thumb = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.like = ((String) in.readValue((String.class.getClassLoader())));
        this.star = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.goldStar = ((String) in.readValue((String.class.getClassLoader())));
        this.silverStar = ((String) in.readValue((String.class.getClassLoader())));
        this.isFollowed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public LikeUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getGoldStar() {
        return goldStar;
    }

    public void setGoldStar(String goldStar) {
        this.goldStar = goldStar;
    }

    public String getSilverStar() {
        return silverStar;
    }

    public void setSilverStar(String silverStar) {
        this.silverStar = silverStar;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(thumb);
        dest.writeValue(username);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(like);
        dest.writeValue(star);
        dest.writeValue(goldStar);
        dest.writeValue(silverStar);
        dest.writeValue(isFollowed);
    }

    public int describeContents() {
        return  0;
    }

}
