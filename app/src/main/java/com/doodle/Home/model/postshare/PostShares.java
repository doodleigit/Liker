package com.doodle.Home.model.postshare;

import android.os.Parcel;
import android.os.Parcelable;

import com.doodle.Home.model.Headers;

import java.io.Serializable;

public class PostShares implements Serializable,Parcelable {

    /*user_id
	to_user_id
	post_id*/
    public String userId;
    public String toUserId;
    public String postId;
    public Headers headers;

    public PostShares(String userId, String toUserId, String postId, Headers headers) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.postId = postId;
        this.headers = headers;
    }

    public PostShares() {
    }

    protected PostShares(Parcel in) {
        userId = in.readString();
        toUserId = in.readString();
        postId = in.readString();
        headers = in.readParcelable(Headers.class.getClassLoader());
    }

    public static final Creator<PostShares> CREATOR = new Creator<PostShares>() {
        @Override
        public PostShares createFromParcel(Parcel in) {
            return new PostShares(in);
        }

        @Override
        public PostShares[] newArray(int size) {
            return new PostShares[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(toUserId);
        dest.writeString(postId);
        dest.writeParcelable(headers, flags);
    }
}
