package com.doodle.Search.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message implements Serializable, Parcelable
{

    @SerializedName("status")
    @Expose
    private boolean status;
    public final static Parcelable.Creator<Message> CREATOR = new Creator<Message>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return (new Message[size]);
        }

    }
            ;
    private final static long serialVersionUID = 2208576285642834295L;

    protected Message(Parcel in) {
        this.status = ((boolean) in.readValue((boolean.class.getClassLoader())));
    }

    public Message() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}