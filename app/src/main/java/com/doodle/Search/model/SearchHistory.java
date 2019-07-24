package com.doodle.Search.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchHistory implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("search_user_id")
    @Expose
    public String searchUserId;
    @SerializedName("search_text")
    @Expose
    public String searchText;
    @SerializedName("search_time")
    @Expose
    public String searchTime;
    public final static Parcelable.Creator<SearchHistory> CREATOR = new Creator<SearchHistory>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SearchHistory createFromParcel(Parcel in) {
            return new SearchHistory(in);
        }

        public SearchHistory[] newArray(int size) {
            return (new SearchHistory[size]);
        }

    }
            ;
    private final static long serialVersionUID = 6142767657730629243L;

    protected SearchHistory(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.searchUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.searchText = ((String) in.readValue((String.class.getClassLoader())));
        this.searchTime = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SearchHistory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchUserId() {
        return searchUserId;
    }

    public void setSearchUserId(String searchUserId) {
        this.searchUserId = searchUserId;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(searchUserId);
        dest.writeValue(searchText);
        dest.writeValue(searchTime);
    }

    public int describeContents() {
        return 0;
    }

}