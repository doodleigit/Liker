
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contribution implements Serializable, Parcelable
{

    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories = null;
    public final static Creator<Contribution> CREATOR = new Creator<Contribution>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Contribution createFromParcel(Parcel in) {
            return new Contribution(in);
        }

        public Contribution[] newArray(int size) {
            return (new Contribution[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7121384981564622991L;

    protected Contribution(Parcel in) {
        in.readList(this.categories, (Category.class.getClassLoader()));
    }

    public Contribution() {
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(categories);
    }

    public int describeContents() {
        return  0;
    }

}
