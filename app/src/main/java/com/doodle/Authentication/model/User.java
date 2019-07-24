package com.doodle.Authentication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
//        first_name (length 2 - 24)
//    last_name (length 2 - 24)
//    email (valid & unique)
//    password (length 8 - 20)
//    retype_password
//    gender (Male = 1, Female = 2, Other = 3)
//    day (01 - 31)
//    month (01 - 12)
//    year (4 digit year)
//    country (country id)
//    city (city id)
//    provider (facebook, google, twitter / optional)
//    oauth_id (optional)
//    token (optional)
//    secret (optional)
//    social_name (optional)
//    img_url (optional)


    @SerializedName("first_name")
    @Expose
    public String first_name;

    @SerializedName("status")
    @Expose
    public boolean status;

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("last_name")
    @Expose
    public String last_name;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("retype_password")
    @Expose
    public String retype_password;

    @SerializedName("gender")
    @Expose
    public String gender;

    @SerializedName("gender")
    @Expose
    public String dob;

    @SerializedName("day")
    @Expose
    public String day;

    @SerializedName("month")
    @Expose
    public String month;

    @SerializedName("year")
    @Expose
    public String year;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("provider")
    @Expose
    public String provider;

    @SerializedName("oauth_id")
    @Expose
    public String oauth_id;

    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("secret")
    @Expose
    public String secret;

    @SerializedName("social_name")
    @Expose
    public String social_name;

    @SerializedName("img_url")
    @Expose
    public String img_url;

    protected User(Parcel in) {
        first_name = in.readString();
        status = in.readByte() != 0;
        userId = in.readString();
        last_name = in.readString();
        email = in.readString();
        password = in.readString();
        retype_password = in.readString();
        gender = in.readString();
        dob = in.readString();
        day = in.readString();
        month = in.readString();
        year = in.readString();
        country = in.readString();
        city = in.readString();
        provider = in.readString();
        oauth_id = in.readString();
        token = in.readString();
        secret = in.readString();
        social_name = in.readString();
        img_url = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetype_password() {
        return retype_password;
    }

    public void setRetype_password(String retype_password) {
        this.retype_password = retype_password;
    }

    public String getGender() {
        return gender;
    }
    public String getDob() {
        return dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOauth_id() {
        return oauth_id;
    }

    public void setOauth_id(String oauth_id) {
        this.oauth_id = oauth_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSocial_name() {
        return social_name;
    }

    public void setSocial_name(String social_name) {
        this.social_name = social_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", retype_password='" + retype_password + '\'' +
                ", gender='" + gender + '\'' +
                ", dob='" + dob + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", provider='" + provider + '\'' +
                ", oauth_id='" + oauth_id + '\'' +
                ", token='" + token + '\'' +
                ", secret='" + secret + '\'' +
                ", social_name='" + social_name + '\'' +
                ", img_url='" + img_url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(userId);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(retype_password);
        dest.writeString(gender);
        dest.writeString(dob);
        dest.writeString(day);
        dest.writeString(month);
        dest.writeString(year);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(provider);
        dest.writeString(oauth_id);
        dest.writeString(token);
        dest.writeString(secret);
        dest.writeString(social_name);
        dest.writeString(img_url);
    }
}
