
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SinglePostFilters implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("category_info")
    @Expose
    private CategoryInfo categoryInfo;
    @SerializedName("active_status")
    @Expose
    private String activeStatus;
    public final static Creator<SinglePostFilters> CREATOR = new Creator<SinglePostFilters>() {


        @SuppressWarnings({
            "unchecked"
        })
        public SinglePostFilters createFromParcel(Parcel in) {
            return new SinglePostFilters(in);
        }

        public SinglePostFilters[] newArray(int size) {
            return (new SinglePostFilters[size]);
        }

    }
    ;
    private final static long serialVersionUID = -3444976039620581752L;

    protected SinglePostFilters(Parcel in) {
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.categoryInfo = ((CategoryInfo) in.readValue((CategoryInfo.class.getClassLoader())));
        this.activeStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SinglePostFilters() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public CategoryInfo getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(CategoryInfo categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(categoryInfo);
        dest.writeValue(activeStatus);
    }

    public int describeContents() {
        return  0;
    }

}
