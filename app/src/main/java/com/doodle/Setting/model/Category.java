
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category implements Serializable, Parcelable
{

    @SerializedName("sub_categories")
    @Expose
    private ArrayList<SubCategory> subCategories = null;
    @SerializedName("info")
    @Expose
    private Info info;
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
    private final static long serialVersionUID = -9177896990206328615L;

    protected Category(Parcel in) {
        in.readList(this.subCategories, (com.doodle.Setting.model.SubCategory.class.getClassLoader()));
        this.info = ((Info) in.readValue((Info.class.getClassLoader())));
    }

    public Category() {
    }

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(subCategories);
        dest.writeValue(info);
    }

    public int describeContents() {
        return  0;
    }

}
