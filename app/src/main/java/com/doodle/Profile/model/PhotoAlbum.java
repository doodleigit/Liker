
package com.doodle.Profile.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoAlbum implements Serializable, Parcelable
{

    @SerializedName("album_type")
    @Expose
    private Integer albumType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("total_images")
    @Expose
    private String totalImages;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    public final static Creator<PhotoAlbum> CREATOR = new Creator<PhotoAlbum>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PhotoAlbum createFromParcel(Parcel in) {
            return new PhotoAlbum(in);
        }

        public PhotoAlbum[] newArray(int size) {
            return (new PhotoAlbum[size]);
        }

    }
    ;
    private final static long serialVersionUID = 379090709635488881L;

    protected PhotoAlbum(Parcel in) {
        this.albumType = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.totalImages = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PhotoAlbum() {
    }

    public Integer getAlbumType() {
        return albumType;
    }

    public void setAlbumType(Integer albumType) {
        this.albumType = albumType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(String totalImages) {
        this.totalImages = totalImages;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(albumType);
        dest.writeValue(title);
        dest.writeValue(totalImages);
        dest.writeValue(imageName);
    }

    public int describeContents() {
        return  0;
    }

}
