
package com.doodle.Comment.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CommentTextIndex implements Serializable, Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<CommentTextIndex> CREATOR = new Creator<CommentTextIndex>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CommentTextIndex createFromParcel(Parcel in) {
            return new CommentTextIndex(in);
        }

        public CommentTextIndex[] newArray(int size) {
            return (new CommentTextIndex[size]);
        }

    }
    ;
    private final static long serialVersionUID = 2944081824760458486L;

    protected CommentTextIndex(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CommentTextIndex() {
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
