
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddedCategory implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<AddedCategory> CREATOR = new Creator<AddedCategory>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddedCategory createFromParcel(Parcel in) {
            return new AddedCategory(in);
        }

        public AddedCategory[] newArray(int size) {
            return (new AddedCategory[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4366089001900836953L;

    protected AddedCategory(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AddedCategory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
