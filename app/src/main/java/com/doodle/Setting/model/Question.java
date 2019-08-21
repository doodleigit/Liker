
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question implements Serializable, Parcelable
{

    @SerializedName("security_id")
    @Expose
    private String securityId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<Question> CREATOR = new Creator<Question>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return (new Question[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5890301758378690167L;

    protected Question(Parcel in) {
        this.securityId = ((String) in.readValue((String.class.getClassLoader())));
        this.question = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Question() {
    }

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(securityId);
        dest.writeValue(question);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
