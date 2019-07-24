package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopContributorStatus implements Serializable, Parcelable
{

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    public final static Parcelable.Creator<TopContributorStatus> CREATOR = new Creator<TopContributorStatus>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TopContributorStatus createFromParcel(Parcel in) {
            return new TopContributorStatus(in);
        }

        public TopContributorStatus[] newArray(int size) {
            return (new TopContributorStatus[size]);
        }

    }
            ;
    private final static long serialVersionUID = -7563008524312210609L;

    protected TopContributorStatus(Parcel in) {
        this.headers = ((Headers) in.readValue((Headers.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TopContributorStatus() {
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(headers);
        dest.writeValue(userId);
        dest.writeValue(categoryId);
    }

    public int describeContents() {
        return 0;
    }

}