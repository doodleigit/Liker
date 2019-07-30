
package com.doodle.Comment.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CommentTextIndex_ implements Serializable, Parcelable
{

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<CommentTextIndex_> CREATOR = new Creator<CommentTextIndex_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CommentTextIndex_ createFromParcel(Parcel in) {
            return new CommentTextIndex_(in);
        }

        public CommentTextIndex_[] newArray(int size) {
            return (new CommentTextIndex_[size]);
        }

    }
    ;
    private final static long serialVersionUID = -294573074357503120L;

    protected CommentTextIndex_(Parcel in) {
        this.text = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CommentTextIndex_() {
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
