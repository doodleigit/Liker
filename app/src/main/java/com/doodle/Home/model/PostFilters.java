
package com.doodle.Home.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostFilters implements Serializable, Parcelable
{

    @SerializedName("filter_list")
    @Expose
    private List<FilterList> filterList = null;
    @SerializedName("active_status")
    @Expose
    private String activeStatus;
    @SerializedName("filter_type")
    @Expose
    private String filterType;
    public final static Creator<PostFilters> CREATOR = new Creator<PostFilters>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PostFilters createFromParcel(Parcel in) {
            return new PostFilters(in);
        }

        public PostFilters[] newArray(int size) {
            return (new PostFilters[size]);
        }

    }
    ;
    private final static long serialVersionUID = 6316922694444582792L;

    protected PostFilters(Parcel in) {
        in.readList(this.filterList, (com.doodle.Home.model.FilterList.class.getClassLoader()));
        this.activeStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.filterType = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PostFilters() {
    }

    public List<FilterList> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FilterList> filterList) {
        this.filterList = filterList;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(filterList);
        dest.writeValue(activeStatus);
        dest.writeValue(filterType);
    }

    public int describeContents() {
        return  0;
    }

}
