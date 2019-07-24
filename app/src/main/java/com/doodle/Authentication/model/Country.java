
package com.doodle.Authentication.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Country implements Serializable
{

    @SerializedName("country")
    @Expose
    private List<CountryInfo> country = null;
    private final static long serialVersionUID = 4951241481328849730L;

    public List<CountryInfo> getCountry() {
        return country;
    }

    public void setCountry(List<CountryInfo> country) {
        this.country = country;
    }

}
