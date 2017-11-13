package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.Const;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.CityWeather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector.CONNECT;

public class WeatherService extends AsyncTask<String, Void, String> {

    private static final double TO_CELCIOUS_FROM_KELVIN = -273.15;
    Context context;

    public WeatherService(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL link;
        HttpURLConnection myconnection = null;

        try {
            Log.d(CONNECT, "Starting background task");
            link = new URL(urls[0]);
            myconnection = (HttpURLConnection) link.openConnection();
            InputStream in = myconnection.getInputStream();
            InputStreamReader myStreamReader = new InputStreamReader(in);
            int data = myStreamReader.read();
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = myStreamReader.read();
            }
            Log.d(CONNECT, "Returning result from call");
            return result;
        } catch (Exception e) {
            Log.d(CONNECT, "Exception was thrown from async task");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Gson gson = new GsonBuilder().create();
            CityWeather weatherData = gson.fromJson(result, CityWeather.class);
            String cityName = weatherData.name;
            DecimalFormat df = new DecimalFormat("#");
            String temperature = df.format(weatherData.main.temp.doubleValue() + TO_CELCIOUS_FROM_KELVIN);
            String humidity = df.format(weatherData.main.humidity.doubleValue());
            String img = weatherData.weather.get(0).icon;

            StringBuilder description = new StringBuilder();
            for(int i=0; i<weatherData.weather.size(); i++) {
                description.append(weatherData.weather.get(i).description);
                if(i+1 != weatherData.weather.size()){
                    description.append("/");
                }
            }
            sendMessage(cityName, temperature, humidity, description.toString(), img);
        }
    }

    private void sendMessage(String city, String temp, String humid, String desc, String img) {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("update-list-event");
        // You can also include some extra data.
        intent.putExtra(Const.CITY_NAME, city);
        intent.putExtra(Const.TEMP, temp);
        intent.putExtra(Const.HUMIDITY, humid);
        intent.putExtra(Const.DESCRIPTION, desc);
        intent.putExtra(Const.WEATHER_IMAGE, img);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}