
package com.doodle.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Subcatg implements Serializable, Parcelable
{

    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
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
    private final static long serialVersionUID = 3105557025891322980L;

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



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(subCategoryId);
        dest.writeValue(subCategoryName);
    }

    public int describeContents() {
        return  0;
    }

}
