package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Created by http://www.jsonschema2pojo.org/
public class Sys {

    @SerializedName("message")
    @Expose
    public Double message;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("sunrise")
    @Expose
    public Integer sunrise;
    @SerializedName("sunset")
    @Expose
    public Integer sunset;

}
