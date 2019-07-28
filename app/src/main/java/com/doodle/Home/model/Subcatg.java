
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subcatg implements Serializable, Parcelable
{

    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;

    private boolean isSelected = false;

    public final static Creator<Subcatg> CREATOR = new Creator<Subcatg>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Subcatg createFromParcel(Parcel in) {
            return new Subcatg(in);
        }

        public Subcatg[] newArray(int size) {
            return (new Subcatg[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1658787078966373880L;

    protected Subcatg(Parcel in) {
        this.subCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.subCategoryName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Subcatg() {
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(subCategoryId);
        dest.writeValue(subCategoryName);
    }

    public int describeContents() {
        return  0;
    }

}
