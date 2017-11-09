package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

public class WeatherItemModel {
    private String cityName_;
    private String temperature_;
    private String humidity_;
    private String description_;

    public WeatherItemModel(String cityName, String temperature, String humidity, String description) {
        this.cityName_ = cityName;
        this.temperature_ = temperature;
        this.humidity_ = humidity;
        this.description_ = description;
    }

    public String getName() {
        return cityName_;
    }

    public void setName(String name) {
        this.cityName_ = name;
    }

    public String getTemperature() {
        return temperature_;
    }

    public void setTemperature(String temperature) {
        this.temperature_ = temperature;
    }

    public String getHumidity() {
        return humidity_;
    }

    public void setHumidity(String humidity) {
        this.humidity_ = humidity;
    }

    public String getDescription() {
        return description_;
    }

    public void setDescription(String description) {
        this.description_ = description;
    }

}
