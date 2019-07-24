package com.doodle.Authentication.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error implements Serializable
{

    @SerializedName("email")
    @Expose
    private String email;
    private final static long serialVersionUID = 168035278519418076L;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
