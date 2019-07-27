
package com.doodle.Notification.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("notif_type")
    @Expose
    private String notifType;
    @SerializedName("time_sent")
    @Expose
    private String timeSent;
    @SerializedName("has_seen")
    @Expose
    private String hasSeen;
    @SerializedName("has_seen_details")
    @Expose
    private String hasSeenDetails;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = 6309012768416134103L;

    protected Data(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.notifType = ((String) in.readValue((String.class.getClassLoader())));
        this.timeSent = ((String) in.readValue((String.class.getClassLoader())));
        this.hasSeen = ((String) in.readValue((String.class.getClassLoader())));
        this.hasSeenDetails = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.typeId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotifType() {
        return notifType;
    }

    public void setNotifType(String notifType) {
        this.notifType = notifType;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(String hasSeen) {
        this.hasSeen = hasSeen;
    }

    public String getHasSeenDetails() {
        return hasSeenDetails;
    }

    public void setHasSeenDetails(String hasSeenDetails) {
        this.hasSeenDetails = hasSeenDetails;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGoldStars() {
        return goldStars;
    }

    public void setGoldStars(String goldStars) {
        this.goldStars = goldStars;
    }

    public String getSliverStars() {
        return sliverStars;
    }

    public void setSliverStars(String sliverStars) {
        this.sliverStars = sliverStars;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(notifType);
        dest.writeValue(timeSent);
        dest.writeValue(hasSeen);
        dest.writeValue(hasSeenDetails);
        dest.writeValue(photo);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
        dest.writeValue(typeId);
    }

    public int describeContents() {
        return  0;
    }

}
