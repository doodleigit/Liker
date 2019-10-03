package com.doodle.Comment.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MentionItem implements Parcelable {

    private String mentionFullName;
    private String mentionUserName;
    private String mentionUserId;

    public MentionItem(String mentionFullName, String mentionUserName, String mentionUserId) {
        this.mentionFullName = mentionFullName;
        this.mentionUserName = mentionUserName;
        this.mentionUserId = mentionUserId;
    }

    protected MentionItem(Parcel in) {
        mentionFullName = in.readString();
        mentionUserName = in.readString();
        mentionUserId = in.readString();
    }

    public static final Creator<MentionItem> CREATOR = new Creator<MentionItem>() {
        @Override
        public MentionItem createFromParcel(Parcel in) {
            return new MentionItem(in);
        }

        @Override
        public MentionItem[] newArray(int size) {
            return new MentionItem[size];
        }
    };

    public String getMentionFullName() {
        return mentionFullName;
    }

    public void setMentionFullName(String mentionFullName) {
        this.mentionFullName = mentionFullName;
    }

    public String getMentionUserName() {
        return mentionUserName;
    }

    public void setMentionUserName(String mentionUserName) {
        this.mentionUserName = mentionUserName;
    }

    public String getMentionUserId() {
        return mentionUserId;
    }

    public void setMentionUserId(String mentionUserId) {
        this.mentionUserId = mentionUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mentionFullName);
        dest.writeString(mentionUserName);
        dest.writeString(mentionUserId);
    }
}
