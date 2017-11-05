package com.assignment2.victorbusk.group07_itsmap17_assignment2.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by victo on 05-11-2017.
 */

public class ListViewItem {

    private static final String TAG = ListViewItem.class.getSimpleName();
    private static final String JSON_KEY_MAIN = "main";
    private static final String JSON_KEY_WEATHER = "weather";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_TEMP = "temp";

    private static final double kelvinCelsiusDiff = -272.15;

    public long id;
    public String description;
    public double temperature;
    public Date timestamp;

    public ListViewItem() {}
    public ListViewItem(String description, double temperature) {
        this.description = description;
        this.temperature = temperature;
        timestamp = Calendar.getInstance().getTime();
    }

    public ListViewItem(String json) {

        String description = "N/A";
        double temp = 404;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject mainObject = jsonObject.getJSONObject(JSON_KEY_MAIN);
            JSONArray weatherArray = jsonObject.getJSONArray(JSON_KEY_WEATHER);
            JSONObject weatherObject = weatherArray.getJSONObject(0); //when only getting info from one city, the current weahter is always in the first spot

            description = weatherObject.getString(JSON_KEY_DESCRIPTION);
            temp = mainObject.getDouble(JSON_KEY_TEMP);
            temp += kelvinCelsiusDiff;
        } catch (JSONException ex) {
            Log.e(TAG, ex.toString());
        }

        this.description = description;
        this.temperature = temp;
        timestamp = Calendar.getInstance().getTime();
    }
}
