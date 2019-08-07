package com.doodle.Profile.model;

public class AdvanceSuggestion {

    private String instituteId, instituteName, locationId, websiteUrl, type, locationName;

    public AdvanceSuggestion(String instituteId, String instituteName, String locationId, String websiteUrl, String type, String locationName) {
        this.instituteId = instituteId;
        this.instituteName = instituteName;
        this.locationId = locationId;
        this.websiteUrl = websiteUrl;
        this.type = type;
        this.locationName = locationName;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
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

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}
