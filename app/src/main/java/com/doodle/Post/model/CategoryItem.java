
package com.doodle.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryItem implements Serializable, Parcelable
{

    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    public final static Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CategoryItem createFromParcel(Parcel in) {
            return new CategoryItem(in);
        }

        public CategoryItem[] newArray(int size) {
            return (new CategoryItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = 3916581264594316389L;

    protected CategoryItem(Parcel in) {
        in.readList(this.categories, (com.doodle.Post.model.Category.class.getClassLoader()));
    }

    public CategoryItem() {
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(categories);
    }

    public int describeContents() {
        return  0;
    }

}
