package com.doodle.Profile.model;

public class Story {

    private String description, type, permissionType;

    public Story(String description, String type, String permissionType) {
        this.description = description;
        this.type = type;
        this.permissionType = permissionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
