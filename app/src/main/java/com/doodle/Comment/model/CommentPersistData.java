package com.doodle.Comment.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CommentPersistData implements Parcelable {


    public String commentData;
    public String postId;
    public List<String> mentionNameList;
    public List<String> mentionIdList;

    public CommentPersistData() {
    }

    public CommentPersistData(String commentData, String postId) {
        this.commentData = commentData;
        this.postId = postId;
    }


    protected CommentPersistData(Parcel in) {
        commentData = in.readString();
        postId = in.readString();
        mentionNameList = in.createStringArrayList();
        mentionIdList = in.createStringArrayList();
    }

    public static final Creator<CommentPersistData> CREATOR = new Creator<CommentPersistData>() {
        @Override
        public CommentPersistData createFromParcel(Parcel in) {
            return new CommentPersistData(in);
        }

        @Override
        public CommentPersistData[] newArray(int size) {
            return new CommentPersistData[size];
        }
    };

    public List<String> getMentionNameList() {
        return mentionNameList;
    }

    public void setMentionNameList(List<String> mentionNameList) {
        this.mentionNameList = mentionNameList;
    }

    public List<String> getMentionIdList() {
        return mentionIdList;
    }

    public void setMentionIdList(List<String> mentionIdList) {
        this.mentionIdList = mentionIdList;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String commentData) {
        this.commentData = commentData;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentData);
        dest.writeString(postId);
        dest.writeStringList(mentionNameList);
        dest.writeStringList(mentionIdList);
    }
}
