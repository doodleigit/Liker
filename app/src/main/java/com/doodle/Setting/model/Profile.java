
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile implements Serializable, Parcelable
{

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("is_deactivated")
    @Expose
    private String isDeactivated;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("security_question")
    @Expose
    private String securityQuestion;
    @SerializedName("security_answer")
    @Expose
    private String securityAnswer;
    @SerializedName("question")
    @Expose
    private String question;
    public final static Creator<Profile> CREATOR = new Creator<Profile>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        public Profile[] newArray(int size) {
            return (new Profile[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7198940007837135920L;

    protected Profile(Parcel in) {
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.isDeactivated = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.securityQuestion = ((String) in.readValue((String.class.getClassLoader())));
        this.securityAnswer = ((String) in.readValue((String.class.getClassLoader())));
        this.question = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Profile() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsDeactivated() {
        return isDeactivated;
    }

    public void setIsDeactivated(String isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(username);
        dest.writeValue(isDeactivated);
        dest.writeValue(userId);
        dest.writeValue(securityQuestion);
        dest.writeValue(securityAnswer);
        dest.writeValue(question);
    }

    public int describeContents() {
        return  0;
    }

}
