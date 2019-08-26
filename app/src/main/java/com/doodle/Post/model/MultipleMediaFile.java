package com.doodle.Post.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MultipleMediaFile implements Serializable, Parcelable {

    @SerializedName("file_type")
    @Expose
    private String fileType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("base_64_md5")
    @Expose
    private String base64Md5;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("large_image_name")
    @Expose
    private String largeImageName;


    public final static Parcelable.Creator<MultipleMediaFile> CREATOR = new Creator<MultipleMediaFile>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MultipleMediaFile createFromParcel(Parcel in) {
            return new MultipleMediaFile(in);
        }

        public MultipleMediaFile[] newArray(int size) {
            return (new MultipleMediaFile[size]);
        }

    };
    private final static long serialVersionUID = 8855657295096492767L;

    protected MultipleMediaFile(Parcel in) {
        this.fileType = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.duration = ((String) in.readValue((String.class.getClassLoader())));
        this.base64Md5 = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
        this.largeImageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MultipleMediaFile() {
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBase64Md5() {
        return base64Md5;
    }

    public void setBase64Md5(String base64Md5) {
        this.base64Md5 = base64Md5;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getLargeImageName() {
        return largeImageName;
    }

    public void setLargeImageName(String largeImageName) {
        this.largeImageName = largeImageName;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fileType);
        dest.writeValue(name);
        dest.writeValue(duration);
        dest.writeValue(base64Md5);
        dest.writeValue(imageName);
        dest.writeValue(largeImageName);
    }

    public int describeContents() {
        return 0;
    }

}