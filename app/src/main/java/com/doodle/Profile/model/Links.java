package com.doodle.Profile.model;

public class Links {

    private String link, type, permissionType;

    public Links(String link, String type, String permissionType) {
        this.link = link;
        this.type = type;
        this.permissionType = permissionType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

}
