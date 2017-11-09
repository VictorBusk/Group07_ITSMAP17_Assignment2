package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

import android.os.AsyncTask;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.CityWeather;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class WeatherService extends AsyncTask<String, Void, String> {

    String result;
    private static final double TO_CELCIOUS_FROM_KELVIN = -273.15;
    private static WeatherItemModel newWeatherItemModel;

    @Override
    protected String doInBackground(String... urls) {
        result = "";
        URL link;
        HttpURLConnection myconnection = null;

        try {
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
            return result;
        } catch (Exception e) {
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
            weatherData.main.humidity.doubleValue();
            DecimalFormat df = new DecimalFormat("#");
            String temperature = df.format(weatherData.main.temp.doubleValue() + TO_CELCIOUS_FROM_KELVIN);
            String humidity = df.format(weatherData.main.humidity.doubleValue());

            String description = "";
            for(int i=0; i<weatherData.weather.size(); i++) {
                description += weatherData.weather.get(i).description;
                if(i+1 != weatherData.weather.size()){
                    description += "/";
                }
            }
            newWeatherItemModel = new WeatherItemModel(cityName, temperature, humidity, description);
        }
    }

    public WeatherItemModel parseDataFromAPI() {
        return newWeatherItemModel;
    }
/////////////KASPER//////////////
//        Log.d(CONNECT, "Starting background task");//            getData.execute("http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=3c70f7d8f9e272cd6f73036a65228391");
//
//
//        result = callURL(urls[0]);
//        return result;
//
//    private String callURL(String callUrl) {
//
//        InputStream is = null;
//
//        try {
//            //create URL
//            URL url = new URL(callUrl);
//
//            //configure HttpURLConnetion object
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            //we could use HttpsURLConnection, weather API does not support SSL on free version
//            //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod("GET");
//            conn.setDoInput(true);
//
//            // Starts the request
//            conn.connect();
//            int response = conn.getResponseCode();
//
//            //probably check on response code here!
//
//            //give user feedback in case of error
//
//            Log.d(CONNECT, "The response is: " + response);
//            is = conn.getInputStream();
//
//            // Convert the InputStream into a string
//
//            String contentAsString = convertStreamToStringBuffered(is);
//            return contentAsString;
//
//
//        } catch (ProtocolException pe) {
//            Log.d(CONNECT, "oh noes....ProtocolException");
//        } catch (UnsupportedEncodingException uee) {
//            Log.d(CONNECT, "oh noes....UnsuportedEncodingException");
//        } catch (IOException ioe) {
//            Log.d(CONNECT, "oh noes....IOException");
//        } finally {
//            if (is != null) {
//                try {
//                    Log.d(CONNECT, "CLOSING!!!!!!!!!!!!!!!!!!!!");
//                    is.close();
//                } catch (IOException ioe) {
//                    Log.d(CONNECT, "oh noes....could not close stream, IOException");
//                }
//            }
//        }
//        return null;
//    }
//
//    private String convertStreamToStringBuffered(InputStream is) {
//        String s = "";
//        String line;
//
//        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//
//        try {
//            while ((line = rd.readLine()) != null) { s += line; }
//        } catch (IOException ex) {
//            Log.e(CONNECT, "ERROR reading HTTP response", ex);
//            //ex.printStackTrace();
//        }
//
//        // Return full string
//        return s;
//    }
//
}