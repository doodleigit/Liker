
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_selected")
    @Expose
    private Boolean isSelected;
    public final static Creator<Info> CREATOR = new Creator<Info>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Info createFromParcel(Parcel in) {
            return new Info(in);
        }

        public Info[] newArray(int size) {
            return (new Info[size]);
        }

    }
    ;
    private final static long serialVersionUID = 7916138718618351759L;

    protected Info(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.isSelected = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public Info() {
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

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(isSelected);
    }

    public int describeContents() {
        return  0;
    }

}
