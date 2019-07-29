
package com.doodle.Comment.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Comment implements Serializable, Parcelable
{

    @SerializedName("post_id")
    @Expose
    private String postId;
    @SerializedName("comments")
    @Expose
    private List<Comment_> comments = new ArrayList<Comment_>();
    public final static Creator<Comment> CREATOR = new Creator<Comment>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return (new Comment[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1759689432428337053L;

    protected Comment(Parcel in) {
        this.postId = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.comments, (Comment_.class.getClassLoader()));
    }

    public Comment() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public List<Comment_> getComments() {
        return comments;
    }

    public void setComments(List<Comment_> comments) {
        this.comments = comments;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postId);
        dest.writeList(comments);
    }

    public int describeContents() {
        return  0;
    }

}
