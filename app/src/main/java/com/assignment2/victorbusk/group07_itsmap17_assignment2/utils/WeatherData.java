package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.CityListActivity;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor.CustomAdaptor;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Globals.CONNECT;

/**
 * Created by Vamshi on 5/14/2017.
 */

public class WeatherData extends AsyncTask<String, Void, String> {

    String result;

    @Override
    protected String doInBackground(String... urls) {
        Log.d(CONNECT, "Starting background task");//            getData.execute("http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=3c70f7d8f9e272cd6f73036a65228391");


        result = callURL(urls[0]);
        return result;
//        result = "";
//        URL link;
//        HttpURLConnection myconnection = null;
//
//        try {
//            link = new URL(urls[0]);
//            myconnection = (HttpURLConnection) link.openConnection();
//            InputStream in = myconnection.getInputStream();
//            InputStreamReader myStreamReader = new InputStreamReader(in);
//            int data = myStreamReader.read();
//            while (data != -1) {
//                char current = (char) data;
//                result += current;
//                data = myStreamReader.read();
//            }
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (result != null) {
                JSONObject myObject = new JSONObject(result);
                JSONObject main = new JSONObject(myObject.getString("main"));
                String cityName = myObject.getString("name");
                String temperature = main.getString("temp");
                String humidity = main.getString("humidity");

                CityListActivity.weatherItemModel = new WeatherItemModel(cityName, temperature, humidity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String callURL(String callUrl) {

        InputStream is = null;

        try {
            //create URL
            URL url = new URL(callUrl);

            //configure HttpURLConnetion object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //we could use HttpsURLConnection, weather API does not support SSL on free version
            //HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the request
            conn.connect();
            int response = conn.getResponseCode();

            //probably check on response code here!

            //give user feedback in case of error

            Log.d(CONNECT, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string

            String contentAsString = convertStreamToStringBuffered(is);
            return contentAsString;


        } catch (ProtocolException pe) {
            Log.d(CONNECT, "oh noes....ProtocolException");
        } catch (UnsupportedEncodingException uee) {
            Log.d(CONNECT, "oh noes....UnsuportedEncodingException");
        } catch (IOException ioe) {
            Log.d(CONNECT, "oh noes....IOException");
        } finally {
            if (is != null) {
                try {
                    Log.d(CONNECT, "CLOSING!!!!!!!!!!!!!!!!!!!!");
                    is.close();
                } catch (IOException ioe) {
                    Log.d(CONNECT, "oh noes....could not close stream, IOException");
                }
            }
        }
        return null;
    }

    private String convertStreamToStringBuffered(InputStream is) {
        String s = "";
        String line;

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((line = rd.readLine()) != null) { s += line; }
        } catch (IOException ex) {
            Log.e(CONNECT, "ERROR reading HTTP response", ex);
            //ex.printStackTrace();
        }

        // Return full string
        return s;
    }
}