
package com.doodle.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable, Parcelable
{

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("subcatg")
    @Expose
    private List<Subcatg> subcatg = new ArrayList<Subcatg>();
    public final static Creator<Category> CREATOR = new Creator<Category>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return (new Category[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6932837668317757318L;

    protected Category(Parcel in) {
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.subcatg, (Subcatg.class.getClassLoader()));
    }

    public Category() {
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Subcatg> getSubcatg() {
        return subcatg;
    }

    public void setSubcatg(List<Subcatg> subcatg) {
        this.subcatg = subcatg;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(categoryId);
        dest.writeValue(categoryName);
        dest.writeList(subcatg);
    }

    public int describeContents() {
        return  0;
    }

}
