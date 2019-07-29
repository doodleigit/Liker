
package com.doodle.Comment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CommentItem implements Serializable, Parcelable
{

    @SerializedName("comments")
    @Expose
    private List<Comment> comments = new ArrayList<Comment>();
    public final static Creator<CommentItem> CREATOR = new Creator<CommentItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        public CommentItem[] newArray(int size) {
            return (new CommentItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7766364123542911055L;

    protected CommentItem(Parcel in) {
        in.readList(this.comments, (Comment.class.getClassLoader()));
    }

    public CommentItem() {
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(comments);
    }

    public int describeContents() {
        return  0;
    }

}
