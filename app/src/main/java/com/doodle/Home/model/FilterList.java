
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterList implements Serializable, Parcelable
{

    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("sub_cat_name")
    @Expose
    private String subCatName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    public final static Creator<FilterList> CREATOR = new Creator<FilterList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public FilterList createFromParcel(Parcel in) {
            return new FilterList(in);
        }

        public FilterList[] newArray(int size) {
            return (new FilterList[size]);
        }

    }
    ;
    private final static long serialVersionUID = -139734624069194122L;

    protected FilterList(Parcel in) {
        this.catId = ((String) in.readValue((String.class.getClassLoader())));
        this.subCatId = ((String) in.readValue((String.class.getClassLoader())));
        this.catName = ((String) in.readValue((String.class.getClassLoader())));
        this.subCatName = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
    }

    public FilterList() {
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(catId);
        dest.writeValue(subCatId);
        dest.writeValue(catName);
        dest.writeValue(subCatName);
        dest.writeValue(id);
        dest.writeValue(title);
    }

    public int describeContents() {
        return  0;
    }

}
