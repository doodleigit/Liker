package com.doodle.Profile.model;

public class Awards {

    private String id, userId, awardsId, instituteId, permissionType, description, entryDate, modifyDate, instituteName, locationId, locationName, websiteUrl, awardName, year, month;

    public Awards(String id, String userId, String awardsId, String instituteId, String permissionType, String description, String entryDate, String modifyDate, String instituteName, String locationId, String locationName, String websiteUrl, String awardName, String year, String month) {
        this.id = id;
        this.userId = userId;
        this.awardsId = awardsId;
        this.instituteId = instituteId;
        this.permissionType = permissionType;
        this.description = description;
        this.entryDate = entryDate;
        this.modifyDate = modifyDate;
        this.instituteName = instituteName;
        this.locationId = locationId;
        this.locationName = locationName;
        this.websiteUrl = websiteUrl;
        this.awardName = awardName;
        this.year = year;
        this.month = month;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAwardsId() {
        return awardsId;
    }

    public void setAwardsId(String awardsId) {
        this.awardsId = awardsId;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


}
