
package com.doodle.Profile.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneCountry implements Serializable, Parcelable
{

    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("country_iso_code_2")
    @Expose
    private String countryIsoCode2;
    @SerializedName("country_phone_code")
    @Expose
    private String countryPhoneCode;
    public final static Creator<PhoneCountry> CREATOR = new Creator<PhoneCountry>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PhoneCountry createFromParcel(Parcel in) {
            return new PhoneCountry(in);
        }

        public PhoneCountry[] newArray(int size) {
            return (new PhoneCountry[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2930766865839362223L;

    protected PhoneCountry(Parcel in) {
        this.countryId = ((String) in.readValue((String.class.getClassLoader())));
        this.countryName = ((String) in.readValue((String.class.getClassLoader())));
        this.countryIsoCode2 = ((String) in.readValue((String.class.getClassLoader())));
        this.countryPhoneCode = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PhoneCountry() {
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(countryId);
        dest.writeValue(countryName);
        dest.writeValue(countryIsoCode2);
        dest.writeValue(countryPhoneCode);
    }

    public int describeContents() {
        return  0;
    }

}
