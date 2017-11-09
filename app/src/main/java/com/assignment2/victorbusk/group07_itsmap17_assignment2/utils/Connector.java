package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

//Inspired by Kasper's WeatherServiceDemo

public class Connector {
    public static final String CONNECT = "CONNECTIVITY";

    public static final String WEATHER_API_KEY = "49720ce9b50d4b200644f71f818d1c33";

    public static String CallAPI(String cityName) {
        return "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&APPID=" + WEATHER_API_KEY;
    }
}
