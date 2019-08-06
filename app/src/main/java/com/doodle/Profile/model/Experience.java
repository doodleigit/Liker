package com.doodle.Profile.model;

public class Experience {

    private String id, userId, designationId, instituteId, locationId, permissionType, introStatus, description, entryDate, modifyDate, designationName, companyName, websiteUrl, locationName, fromYear,
            fromMonth, toYear, toMonth;
    private boolean currentlyWorked;

    public Experience(String id, String userId, String designationId, String instituteId, String locationId, String permissionType, String introStatus, String description, String entryDate, String modifyDate,
                      String designationName, String companyName, String websiteUrl, String locationName, String fromYear, String fromMonth, String toYear, String toMonth, boolean currentlyWorked) {
        this.id = id;
        this.userId = userId;
        this.designationId = designationId;
        this.instituteId = instituteId;
        this.locationId = locationId;
        this.permissionType = permissionType;
        this.introStatus = introStatus;
        this.description = description;
        this.entryDate = entryDate;
        this.modifyDate = modifyDate;
        this.designationName = designationName;
        this.companyName = companyName;
        this.websiteUrl = websiteUrl;
        this.locationName = locationName;
        this.fromYear = fromYear;
        this.fromMonth = fromMonth;
        this.toYear = toYear;
        this.toMonth = toMonth;
        this.currentlyWorked = currentlyWorked;
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

    public String getDesignationId() {
        return designationId;
    }

    public void setDesignationId(String designationId) {
        this.designationId = designationId;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getIntroStatus() {
        return introStatus;
    }

    public void setIntroStatus(String introStatus) {
        this.introStatus = introStatus;
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

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(String fromMonth) {
        this.fromMonth = fromMonth;
    }

    public Boolean getCurrentlyWorked() {
        return currentlyWorked;
    }

    public void setCurrentlyWorked(Boolean currentlyWorked) {
        this.currentlyWorked = currentlyWorked;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getToMonth() {
        return toMonth;
    }

    public void setToMonth(String toMonth) {
        this.toMonth = toMonth;
    }


}
