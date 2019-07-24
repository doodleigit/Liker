
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PostTextIndex implements Serializable, Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<PostTextIndex> CREATOR = new Creator<PostTextIndex>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PostTextIndex createFromParcel(Parcel in) {
            return new PostTextIndex(in);
        }

        public PostTextIndex[] newArray(int size) {
            return (new PostTextIndex[size]);
        }

    }
    ;
    private final static long serialVersionUID = 4320508577288929998L;

    protected PostTextIndex(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PostTextIndex() {
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
