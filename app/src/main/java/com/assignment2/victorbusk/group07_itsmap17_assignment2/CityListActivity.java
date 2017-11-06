package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor.CustomAdaptor;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.ListViewItem;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Globals;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Globals.CONNECT;

public class CityListActivity extends AppCompatActivity {

    Bitmap weatherImage;

    private ListView weatherLV;
    private Button btnRefresh, btnAdd;
    ListAdapter customAdaptor;
    public static List<String> stringList = new ArrayList<>();

    public static TextView cityTV, tempTV, txtCity;

    String cityName, temp, humidity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list_activity);

        if (savedInstanceState != null) {
            cityName = savedInstanceState.getString(Const.CITY_NAME, getString(R.string.tv_city_name));
            temp = savedInstanceState.getString(Const.TEMP, getString(R.string.tv_temp));
            humidity = savedInstanceState.getString(Const.HUMIDITY, getString(R.string.tv_humidity));
            weatherImage = savedInstanceState.getParcelable(Const.WEATHER_IMAGE);
        } else {
            cityName = getString(R.string.tv_city_name);
            temp = getString(R.string.tv_temp);
            humidity = getString(R.string.tv_humidity);
            weatherImage = null;
        }

        weatherLV = findViewById(R.id.weatherListView);

//        cityTV = (TextView)findViewById(R.id.place);
//        tempTV = (TextView)findViewById(R.id.temp);

        WeatherData getData = new WeatherData();
        getData.execute("http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=3c70f7d8f9e272cd6f73036a65228391");

        txtCity = findViewById(R.id.tvAddCity);

        weatherLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                startCityDetailsActivity();
            }
        });


        //Add button pressed: Persist city name, add to list and clear textview
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    persistCity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringList.clear();
                reloadPreferences();
            }
        });

        reloadPreferences();
    }

    protected void persistCity() throws IOException {
        stringList.add(txtCity.getText().toString());
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : stringList) {
            stringBuilder.append(s);
            stringBuilder.append("!");
        }

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("words", stringBuilder.toString());
        editor.apply();
        reloadPreferences();
    }

    protected void reloadPreferences() {
        weatherLV.setAdapter(null);
        stringList.clear();
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        String wordString = settings.getString("words", "");
        String[] itemWords = wordString.split("!");
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < itemWords.length; i++) {
            items.add(itemWords[i]);
        }
        stringList.addAll(items);

        customAdaptor = new CustomAdaptor(this, itemWords);
        weatherLV = findViewById(R.id.weatherListView);
        weatherLV.setAdapter(customAdaptor);
    }

    private void sendRequest(){
        DownloadTask d = new DownloadTask();
        d.execute(Globals.WEATHER_API_CALL);
    }

    //we extend Asynch task and can create any number of these new DownloadTasks as need - need to call execute()
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.d(CONNECT, "Starting background task");
            return callURL(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null) {
                //TODO: parse result
                reloadPreferences();
            } else {

            }
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

    private void startCityDetailsActivity() {
        Intent intent = new Intent(this, CityDetailsActivity.class);
        intent.putExtra(Const.CITY_NAME, cityName);
        intent.putExtra(Const.TEMP, temp);
        intent.putExtra(Const.HUMIDITY, humidity);
        intent.putExtra(Const.WEATHER_IMAGE, weatherImage);
        startActivityForResult(intent, Const.REQUEST_DETAILS_ACTIVITY);
    }
}
