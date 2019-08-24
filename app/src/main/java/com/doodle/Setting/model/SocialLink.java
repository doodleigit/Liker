
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLink implements Serializable, Parcelable
{

    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("linkedin")
    @Expose
    private String linkedin;
    public final static Creator<SocialLink> CREATOR = new Creator<SocialLink>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SocialLink createFromParcel(Parcel in) {
            return new SocialLink(in);
        }

        public SocialLink[] newArray(int size) {
            return (new SocialLink[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5905272772378856139L;

    protected SocialLink(Parcel in) {
        this.facebook = ((String) in.readValue((String.class.getClassLoader())));
        this.twitter = ((String) in.readValue((String.class.getClassLoader())));
        this.linkedin = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SocialLink() {
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(facebook);
        dest.writeValue(twitter);
        dest.writeValue(linkedin);
    }

    public int describeContents() {
        return  0;
    }

}
