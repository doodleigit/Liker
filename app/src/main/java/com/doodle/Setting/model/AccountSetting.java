
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountSetting implements Serializable, Parcelable
{

    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("questions")
    @Expose
    private ArrayList<Question> questions = null;
    public final static Creator<AccountSetting> CREATOR = new Creator<AccountSetting>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AccountSetting createFromParcel(Parcel in) {
            return new AccountSetting(in);
        }

        public AccountSetting[] newArray(int size) {
            return (new AccountSetting[size]);
        }

    }
    ;
    private final static long serialVersionUID = 489239693856128728L;

    protected AccountSetting(Parcel in) {
        this.profile = ((Profile) in.readValue((Profile.class.getClassLoader())));
        in.readList(this.questions, (com.doodle.Setting.model.Question.class.getClassLoader()));
    }

    public AccountSetting() {
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(profile);
        dest.writeList(questions);
    }

    public int describeContents() {
        return  0;
    }

}
