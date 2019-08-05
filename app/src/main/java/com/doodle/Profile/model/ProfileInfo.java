package com.doodle.Profile.model;

import java.util.ArrayList;

public class ProfileInfo {

    private String userName, firstName, lastName, headLine, sex, birthYear, birthMonth, birthDate, yearPermission, dayMonthPermission, address, currentCityId, currentCityLocationName, currentCityCountryId,
            currentCityCityId, currentCityCity, currentCityCountry, intro;

    private ArrayList<Email> emails;
    private ArrayList<Phone> phones;
    private ArrayList<Story> stories;
    private ArrayList<Education> educations;
    private ArrayList<Experience> experiences;
    private ArrayList<Awards> awards;
    private ArrayList<Certification> certifications;
    private ArrayList<Links> links;

    public ProfileInfo(String userName, String firstName, String lastName, String headLine, String sex, String birthYear, String birthMonth, String birthDate, String yearPermission, String dayMonthPermission,
                       String address, String currentCityId, String currentCityLocationName, String currentCityCountryId, String currentCityCityId, String currentCityCity, String currentCityCountry,
                       ArrayList<Email> emails, ArrayList<Phone> phones, ArrayList<Story> stories, ArrayList<Education> educations, ArrayList<Experience> experiences, ArrayList<Awards> awards,
                       ArrayList<Certification> certifications, ArrayList<Links> links) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.headLine = headLine;
        this.sex = sex;
        this.birthYear = birthYear;
        this.birthMonth = birthMonth;
        this.birthDate = birthDate;
        this.yearPermission = yearPermission;
        this.dayMonthPermission = dayMonthPermission;
        this.address = address;
        this.currentCityId = currentCityId;
        this.currentCityLocationName = currentCityLocationName;
        this.currentCityCountryId = currentCityCountryId;
        this.currentCityCityId = currentCityCityId;
        this.currentCityCity = currentCityCity;
        this.currentCityCountry = currentCityCountry;
        this.intro = intro;
        this.emails = emails;
        this.phones = phones;
        this.stories = stories;
        this.educations = educations;
        this.experiences = experiences;
        this.awards = awards;
        this.certifications = certifications;
        this.links = links;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getYearPermission() {
        return yearPermission;
    }

    public void setYearPermission(String yearPermission) {
        this.yearPermission = yearPermission;
    }

    public String getDayMonthPermission() {
        return dayMonthPermission;
    }

    public void setDayMonthPermission(String dayMonthPermission) {
        this.dayMonthPermission = dayMonthPermission;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(String currentCityId) {
        this.currentCityId = currentCityId;
    }

    public String getCurrentCityLocationName() {
        return currentCityLocationName;
    }

    public void setCurrentCityLocationName(String currentCityLocationName) {
        this.currentCityLocationName = currentCityLocationName;
    }

    public String getCurrentCityCountryId() {
        return currentCityCountryId;
    }

    public void setCurrentCityCountryId(String currentCityCountryId) {
        this.currentCityCountryId = currentCityCountryId;
    }

    public String getCurrentCityCityId() {
        return currentCityCityId;
    }

    public void setCurrentCityCityId(String currentCityCityId) {
        this.currentCityCityId = currentCityCityId;
    }

    public String getCurrentCityCity() {
        return currentCityCity;
    }

    public void setCurrentCityCity(String currentCityCity) {
        this.currentCityCity = currentCityCity;
    }

    public String getCurrentCityCountry() {
        return currentCityCountry;
    }

    public void setCurrentCityCountry(String currentCityCountry) {
        this.currentCityCountry = currentCityCountry;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<Email> emails) {
        this.emails = emails;
    }

    public ArrayList<Phone> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<Phone> phones) {
        this.phones = phones;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    public ArrayList<Education> getEducations() {
        return educations;
    }

    public void setEducations(ArrayList<Education> educations) {
        this.educations = educations;
    }

    public ArrayList<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(ArrayList<Experience> experiences) {
        this.experiences = experiences;
    }

    public ArrayList<Awards> getAwards() {
        return awards;
    }

    public void setAwards(ArrayList<Awards> awards) {
        this.awards = awards;
    }

    public ArrayList<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(ArrayList<Certification> certifications) {
        this.certifications = certifications;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Links> links) {
        this.links = links;
    }

}
