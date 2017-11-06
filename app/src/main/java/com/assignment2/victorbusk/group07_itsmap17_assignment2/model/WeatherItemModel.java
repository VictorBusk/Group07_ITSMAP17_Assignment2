package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

/**
 * Created by victo on 06-11-2017.
 */

public class WeatherItemModel {
    private String cityName_;
    private String temperature_;
    private String humidity_;
    private String intentAction;    //intent action (for starting through implicit intent)
    private int resultCode;

    public WeatherItemModel(String cityName, String temperature, String humidity) {

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

    public String getTemperature() {
        return temperature_;
    }

    public void setTemperature(String description) {
        this.temperature_ = description;
    }

    public String getHumidity() {
        return humidity_;
    }

    public void setHumidity(String intentAction) {
        this.humidity_ = intentAction;
    }

}
