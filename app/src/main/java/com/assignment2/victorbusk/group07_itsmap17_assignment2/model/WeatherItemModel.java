package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

public class WeatherItemModel {
    private String cityName_;
    private Double temperature_;
    private Double humidity_;

    public WeatherItemModel(String cityName, Double temperature, Double humidity) {

        this.cityName_ = cityName;
        this.temperature_ = temperature;
        this.humidity_ = humidity;
    }

    public String getName() {
        return cityName_;
    }

    public void setName(String name) {
        this.cityName_ = name;
    }

    public Double getTemperature() {
        return temperature_;
    }

    public void setTemperature(Double temperature) {
        this.temperature_ = temperature;
    }

    public Double getHumidity() {
        return humidity_;
    }

    public void setHumidity(Double humidity) {
        this.humidity_ = humidity;
    }

}
