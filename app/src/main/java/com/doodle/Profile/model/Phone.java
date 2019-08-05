package com.doodle.Profile.model;

public class Phone {

    private String phoneNumber, countryId, type, permissionType, isVerified, countryName, countryIsoCode2, countryPhoneCode;

    public Phone(String phoneNumber, String countryId, String type, String permissionType, String isVerified, String countryName, String countryIsoCode2, String countryPhoneCode) {
        this.phoneNumber = phoneNumber;
        this.countryId = countryId;
        this.type = type;
        this.permissionType = permissionType;
        this.isVerified = isVerified;
        this.countryName = countryName;
        this.countryIsoCode2 = countryIsoCode2;
        this.countryPhoneCode = countryPhoneCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryIsoCode2() {
        return countryIsoCode2;
    }

    public void setCountryIsoCode2(String countryIsoCode2) {
        this.countryIsoCode2 = countryIsoCode2;
    }

    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }


}
