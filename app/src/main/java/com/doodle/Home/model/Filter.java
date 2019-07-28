
package com.doodle.Home.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filter implements Serializable, Parcelable
{

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    public final static Creator<Filter> CREATOR = new Creator<Filter>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        public Filter[] newArray(int size) {
            return (new Filter[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1099661696446325167L;

    protected Filter(Parcel in) {
        in.readList(this.categories, (Category.class.getClassLoader()));
    }

    public Filter() {
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
