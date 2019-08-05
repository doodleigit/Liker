package com.doodle.Profile.model;

public class Email {

    private String email, type, permissionType, isVerified;

    public Email(String email, String type, String permissionType, String isVerified) {
        this.email = email;
        this.type = type;
        this.permissionType = permissionType;
        this.isVerified = isVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

}
