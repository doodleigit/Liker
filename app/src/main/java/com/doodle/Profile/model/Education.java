package com.doodle.Profile.model;

public class Education {

    private String id, userId, instituteId, degreeId, fieldStudy, grade, permissionType, introStatus, startYear, endYear, description, entryDate, modifyDate, instituteName, locationId, instituteType,
            locationName, websiteUrl, degreeName, fieldStudyName;

    public Education(String id, String userId, String instituteId, String degreeId, String fieldStudy, String grade, String permissionType, String introStatus, String startYear, String endYear, String description,
                     String entryDate, String modifyDate, String instituteName, String locationId, String instituteType, String locationName, String websiteUrl, String degreeName, String fieldStudyName) {
        this.id = id;
        this.userId = userId;
        this.instituteId = instituteId;
        this.degreeId = degreeId;
        this.fieldStudy = fieldStudy;
        this.grade = grade;
        this.permissionType = permissionType;
        this.introStatus = introStatus;
        this.startYear = startYear;
        this.endYear = endYear;
        this.description = description;
        this.entryDate = entryDate;
        this.modifyDate = modifyDate;
        this.instituteName = instituteName;
        this.locationId = locationId;
        this.instituteType = instituteType;
        this.locationName = locationName;
        this.websiteUrl = websiteUrl;
        this.degreeName = degreeName;
        this.fieldStudyName = fieldStudyName;
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

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public String getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(String degreeId) {
        this.degreeId = degreeId;
    }

    public String getFieldStudy() {
        return fieldStudy;
    }

    public void setFieldStudy(String fieldStudy) {
        this.fieldStudy = fieldStudy;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
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

    public String getInstituteType() {
        return instituteType;
    }

    public void setInstituteType(String instituteType) {
        this.instituteType = instituteType;
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

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getFieldStudyName() {
        return fieldStudyName;
    }

    public void setFieldStudyName(String fieldStudyName) {
        this.fieldStudyName = fieldStudyName;
    }

}
