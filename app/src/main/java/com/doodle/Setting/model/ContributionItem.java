
package com.doodle.Setting.model;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContributionItem implements Serializable, Parcelable
{

    @SerializedName("categories")
    @Expose
    private List<AddedCategory> categories = null;
    public final static Creator<ContributionItem> CREATOR = new Creator<ContributionItem>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ContributionItem createFromParcel(Parcel in) {
            return new ContributionItem(in);
        }

        public ContributionItem[] newArray(int size) {
            return (new ContributionItem[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4514551336612489886L;

    protected ContributionItem(Parcel in) {
        in.readList(this.categories, (Category.class.getClassLoader()));
    }

    public ContributionItem() {
    }

    public List<AddedCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<AddedCategory> categories) {
        this.categories = categories;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(categories);
    }

    public int describeContents() {
        return  0;
    }

}
