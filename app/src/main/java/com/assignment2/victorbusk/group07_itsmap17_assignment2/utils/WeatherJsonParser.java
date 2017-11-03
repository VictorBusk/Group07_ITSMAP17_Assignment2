package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.CityWeather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

//Inspired by Kasper's WeatherServiceDemo

public class WeatherJsonParser {

    private static final double TO_CELCIOUS_FROM_KELVIN = -273.15;

    //example of simple JSON parsing
    public static String parseCityWeatherJson(String jsonString) {
        String weatherString = "could not parse json";
        try {
            JSONObject cityWeatherJson = new JSONObject(jsonString);
            String name = cityWeatherJson.getString("name");
            JSONObject measurements = cityWeatherJson.getJSONObject("main");
            weatherString = name + " " + measurements.toString(); // measurements.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main") + " : " + measurements.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherString;
    }

    //example of parsing with Gson - note that the Gson parser uses the model object CityWeather, Clouds, Coord, Main, Sys, Weather and Wind extracted with http://www.jsonschema2pojo.org/
    public static String parseCityWeatherJsonWithGson(String jsonString) {

        Gson gson = new GsonBuilder().create();
        CityWeather weatherInfo = gson.fromJson(jsonString, CityWeather.class);
        if (weatherInfo != null) {
            return weatherInfo.name + "\n" + "Temp: " + (weatherInfo.main.temp.doubleValue() + TO_CELCIOUS_FROM_KELVIN) + "\u2103" + //unicode for celcius
                    "\nCountry: " + weatherInfo.sys.country;
        } else {
            return "could not parse with gson";
        }
    }
}
