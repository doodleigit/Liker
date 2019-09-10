package com.doodle.Authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SocialInfo implements Parcelable {

    private String firstName;
    private String lastName;
    private String email;
    private String authId;
    private String image;
    private String provider;
    private String socialName;


    public SocialInfo() {
    }

    public SocialInfo(String firstName, String lastName, String email, String authId, String image,String provider,String socialName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.authId = authId;
        this.image = image;
        this.provider = provider;
        this.socialName = socialName;
    }

    protected SocialInfo(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        authId = in.readString();
        image = in.readString();
        provider = in.readString();
        socialName = in.readString();
    }

    public static final Creator<SocialInfo> CREATOR = new Creator<SocialInfo>() {
        @Override
        public SocialInfo createFromParcel(Parcel in) {
            return new SocialInfo(in);
        }

        @Override
        public SocialInfo[] newArray(int size) {
            return new SocialInfo[size];
        }
    };

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(authId);
        dest.writeString(image);
        dest.writeString(provider);
        dest.writeString(socialName);
    }
}
