package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Created by http://www.jsonschema2pojo.org/
public class Coord {

    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("lat")
    @Expose
    public Double lat;

}
