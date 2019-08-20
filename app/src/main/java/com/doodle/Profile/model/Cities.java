
package com.doodle.Profile.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cities implements Serializable, Parcelable
{

    @SerializedName("data")
    @Expose
    private List<City> data = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    public final static Creator<Cities> CREATOR = new Creator<Cities>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Cities createFromParcel(Parcel in) {
            return new Cities(in);
        }

        public Cities[] newArray(int size) {
            return (new Cities[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8535690050182416074L;

    protected Cities(Parcel in) {
        in.readList(this.data, (City.class.getClassLoader()));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Cities() {
    }

    public List<City> getData() {
        return data;
    }

    public void setData(List<City> data) {
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
