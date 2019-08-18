
package com.doodle.Profile.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Countries implements Serializable, Parcelable
{

    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    public final static Creator<Countries> CREATOR = new Creator<Countries>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Countries createFromParcel(Parcel in) {
            return new Countries(in);
        }

        public Countries[] newArray(int size) {
            return (new Countries[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3440068817681916908L;

    protected Countries(Parcel in) {
        in.readList(this.data, (Data.class.getClassLoader()));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Countries() {
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}
