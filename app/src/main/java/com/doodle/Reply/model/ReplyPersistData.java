package com.doodle.Reply.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ReplyPersistData implements Parcelable {

    public String replyData;
    public String commentId;
    public List<String> mentionNameList;
    public List<String> mentionIdList;


    public ReplyPersistData() {
    }

    public ReplyPersistData(String replyData, String commentId) {
        this.replyData = replyData;
        this.commentId = commentId;
    }

    protected ReplyPersistData(Parcel in) {
        replyData = in.readString();
        commentId = in.readString();
        mentionNameList = in.createStringArrayList();
        mentionIdList = in.createStringArrayList();
    }

    public static final Creator<ReplyPersistData> CREATOR = new Creator<ReplyPersistData>() {
        @Override
        public ReplyPersistData createFromParcel(Parcel in) {
            return new ReplyPersistData(in);
        }

        @Override
        public ReplyPersistData[] newArray(int size) {
            return new ReplyPersistData[size];
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

    public String getReplyData() {
        return replyData;
    }

    public void setReplyData(String replyData) {
        this.replyData = replyData;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(replyData);
        dest.writeString(commentId);
        dest.writeStringList(mentionNameList);
        dest.writeStringList(mentionIdList);
    }
}
