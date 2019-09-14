package com.doodle.Authentication.model;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    private String userInfo, token, profileName, profileImage, profileId, userName, deviceId;

    public LoginInfo(String userInfo, String token, String profileName, String profileImage, String profileId, String userName, String deviceId) {
        this.userInfo = userInfo;
        this.token = token;
        this.profileName = profileName;
        this.profileImage = profileImage;
        this.profileId = profileId;
        this.userName = userName;
        this.deviceId = deviceId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
