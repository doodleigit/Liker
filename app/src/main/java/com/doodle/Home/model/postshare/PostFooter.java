
package com.doodle.Home.model.postshare;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PostFooter implements Serializable, Parcelable
{

    @SerializedName("post_total_like")
    @Expose
    private String postTotalLike;
    @SerializedName("like_user_status")
    @Expose
    private boolean likeUserStatus;
    @SerializedName("post_total_share")
    @Expose
    private int postTotalShare;
    @SerializedName("like_user")
    @Expose
    private List<LikeUser> likeUser = new ArrayList<LikeUser>();
    public final static Creator<PostFooter> CREATOR = new Creator<PostFooter>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PostFooter createFromParcel(Parcel in) {
            return new PostFooter(in);
        }

        public PostFooter[] newArray(int size) {
            return (new PostFooter[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7240835774930077453L;

    protected PostFooter(Parcel in) {
        this.postTotalLike = ((String) in.readValue((String.class.getClassLoader())));
        this.likeUserStatus = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.postTotalShare = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.likeUser, (LikeUser.class.getClassLoader()));
    }

    public PostFooter() {
    }

    public String getPostTotalLike() {
        return postTotalLike;
    }

    public void setPostTotalLike(String postTotalLike) {
        this.postTotalLike = postTotalLike;
    }

    public boolean isLikeUserStatus() {
        return likeUserStatus;
    }

    public void setLikeUserStatus(boolean likeUserStatus) {
        this.likeUserStatus = likeUserStatus;
    }

    public int getPostTotalShare() {
        return postTotalShare;
    }

    public void setPostTotalShare(int postTotalShare) {
        this.postTotalShare = postTotalShare;
    }

    public List<LikeUser> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<LikeUser> likeUser) {
        this.likeUser = likeUser;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(postTotalLike);
        dest.writeValue(likeUserStatus);
        dest.writeValue(postTotalShare);
        dest.writeList(likeUser);
    }

    public int describeContents() {
        return  0;
    }

}
