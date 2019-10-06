
package com.doodle.Home.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryInfo implements Serializable, Parcelable
{

    @SerializedName("filter_list")
    @Expose
    private List<FilterList> filterList = null;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("title")
    @Expose
    private String title;
    public final static Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CategoryInfo createFromParcel(Parcel in) {
            return new CategoryInfo(in);
        }

        public CategoryInfo[] newArray(int size) {
            return (new CategoryInfo[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4660203587868838820L;

    protected CategoryInfo(Parcel in) {
        in.readList(this.filterList, (FilterList.class.getClassLoader()));
        this.catId = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CategoryInfo() {
    }

    public List<FilterList> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FilterList> filterList) {
        this.filterList = filterList;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(filterList);
        dest.writeValue(catId);
        dest.writeValue(title);
    }

    public int describeContents() {
        return  0;
    }

}
