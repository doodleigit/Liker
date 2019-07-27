
package com.doodle.Notification.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationItem implements Serializable, Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Creator<NotificationItem> CREATOR = new Creator<NotificationItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NotificationItem createFromParcel(Parcel in) {
            return new NotificationItem(in);
        }

        public NotificationItem[] newArray(int size) {
            return (new NotificationItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5761018973195461633L;

    protected NotificationItem(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
    }

    public NotificationItem() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(text);
        dest.writeValue(data);
    }

    public int describeContents() {
        return  0;
    }

}
