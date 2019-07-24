
package com.doodle.Search.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdvanceSearches implements Serializable, Parcelable {

    @SerializedName("user")
    @Expose
    private List<User> user = new ArrayList<>();

    @SerializedName("post")
    @Expose
    private List<Post> post = new ArrayList<>();




    public static final int USER_ADVANCE_SEARCH = 0;
    public static final int POST_ADVANCE_SEARCH = 1;


    public final static Creator<AdvanceSearches> CREATOR = new Creator<AdvanceSearches>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AdvanceSearches createFromParcel(Parcel in) {
            return new AdvanceSearches(in);
        }

        public AdvanceSearches[] newArray(int size) {
            return (new AdvanceSearches[size]);
        }

    };
    private final static long serialVersionUID = 1874518630322118930L;

    protected AdvanceSearches(Parcel in) {
        in.readList(this.user, (User.class.getClassLoader()));
        in.readList(this.post, (Post.class.getClassLoader()));

    }



    public AdvanceSearches() {
    }



    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(user);
        dest.writeList(post);
    }

    public int describeContents() {
        return 0;
    }

}
