package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

//Inspired by Kasper's WeatherServiceDemo

public class Globals {
    public static final String CONNECT = "CONNECTIVITY";


    // 367e4bbdc3dcab00e1592ccbf6c4d6fb

    public static final String WEATHER_API_KEY = "49720ce9b50d4b200644f71f818d1c33";

    public static final long CITY_ID_AARHUS = 2624652;

    public static final String WEATHER_API_CALL = "http://api.openweathermap.org/data/2.5/weather?id=" + CITY_ID_AARHUS + "&APPID=" + WEATHER_API_KEY;
    //private static final String WEATHER_API_CALL = "http://api.openweathermap.org/data/2.5/weather?q=aarhus,dk&APPID=" + WEATHER_API_KEY;
}
