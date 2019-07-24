package com.doodle.Post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mim  implements Parcelable {

    public String mimColor;
    public int id;


    public Mim() {
    }

    public Mim( int id,String mimColor) {
        this.mimColor = mimColor;
        this.id = id;
    }

    protected Mim(Parcel in) {
        mimColor = in.readString();
        id = in.readInt();
    }

    public static final Creator<Mim> CREATOR = new Creator<Mim>() {
        @Override
        public Mim createFromParcel(Parcel in) {
            return new Mim(in);
        }

        @Override
        public Mim[] newArray(int size) {
            return new Mim[size];
        }
    };

    public String getMimColor() {
        return mimColor;
    }

    public void setMimColor(String mimColor) {
        this.mimColor = mimColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mimColor);
        dest.writeInt(id);
    }
}
