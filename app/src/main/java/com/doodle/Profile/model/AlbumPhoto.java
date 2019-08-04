
package com.doodle.Profile.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumPhoto implements Serializable, Parcelable
{

    @SerializedName("image_name")
    @Expose
    private String imageName;
    public final static Creator<AlbumPhoto> CREATOR = new Creator<AlbumPhoto>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AlbumPhoto createFromParcel(Parcel in) {
            return new AlbumPhoto(in);
        }

        public AlbumPhoto[] newArray(int size) {
            return (new AlbumPhoto[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6652046646812953786L;

    protected AlbumPhoto(Parcel in) {
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AlbumPhoto() {
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imageName);
    }

    public int describeContents() {
        return  0;
    }

}
