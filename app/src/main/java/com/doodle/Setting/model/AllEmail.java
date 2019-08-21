
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllEmail implements Serializable, Parcelable
{

    @SerializedName("emails")
    @Expose
    private ArrayList<Email> emails = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    public final static Creator<AllEmail> CREATOR = new Creator<AllEmail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AllEmail createFromParcel(Parcel in) {
            return new AllEmail(in);
        }

        public AllEmail[] newArray(int size) {
            return (new AllEmail[size]);
        }

    }
    ;
    private final static long serialVersionUID = 964260428715340331L;

    protected AllEmail(Parcel in) {
        in.readList(this.emails, (com.doodle.Setting.model.Email.class.getClassLoader()));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public AllEmail() {
    }

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<Email> emails) {
        this.emails = emails;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(emails);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
