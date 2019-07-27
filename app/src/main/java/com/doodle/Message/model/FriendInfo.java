package com.doodle.Message.model;

import java.io.Serializable;

public class FriendInfo implements Serializable {

    private String userName, toUserId, fullName, likes, stars;

    public FriendInfo(String userName, String toUserId, String fullName, String likes, String stars) {
        this.userName = userName;
        this.toUserId = toUserId;
        this.fullName = fullName;
        this.likes = likes;
        this.stars = stars;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}
