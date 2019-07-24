
package com.doodle.Authentication.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("status")
    @Expose
    private boolean status;
    private final static long serialVersionUID = -6589078729033571261L;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
