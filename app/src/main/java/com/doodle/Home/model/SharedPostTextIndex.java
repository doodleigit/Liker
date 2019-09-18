
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SharedPostTextIndex implements Serializable, Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<SharedPostTextIndex> CREATOR = new Creator<SharedPostTextIndex>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SharedPostTextIndex createFromParcel(Parcel in) {
            return new SharedPostTextIndex(in);
        }

        public SharedPostTextIndex[] newArray(int size) {
            return (new SharedPostTextIndex[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3599784854757155924L;

    protected SharedPostTextIndex(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SharedPostTextIndex() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(text);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
