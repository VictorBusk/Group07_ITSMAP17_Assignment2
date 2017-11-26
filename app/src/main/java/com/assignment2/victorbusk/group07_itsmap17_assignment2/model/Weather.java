package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Created by http://www.jsonschema2pojo.org/
public class Weather {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("main")
    @Expose
    public String main;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("icon")
    @Expose
    public String icon;

}
