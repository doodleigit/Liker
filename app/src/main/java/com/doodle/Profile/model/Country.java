
package com.doodle.Profile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Country implements Serializable
{

    @SerializedName("country")
    @Expose
    private ArrayList<CountryInfo> country = null;
    private final static long serialVersionUID = 4951241481328849730L;

    public ArrayList<CountryInfo> getCountry() {
        return country;
    }

    public void setCountry(ArrayList<CountryInfo> country) {
        this.country = country;
    }

}
