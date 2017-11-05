package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor.CustomAdaptor;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.ListViewItem;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Globals;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherJsonParser;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Globals.CONNECT;

public class CityListActivity extends AppCompatActivity {

    Bitmap weatherImage;

    private ListView weatherLV;
    private Button btnRefresh, btnAdd;
    private TextView txtCity;
//    ArrayAdapter<String> adapter;
    private CustomAdaptor customAdaptor;
    final List<String> stringList = new ArrayList<>();
    final ArrayList<ListViewItem> itemList = new ArrayList<>();

    String cityName, temp, humidity;

    List<String> web;
    Integer[] imageId = {
            R.drawable.mrpbh
    };

    // All static variables
    static final String URL = "https://api.androidhive.info/music/music.xml";
    // XML node keys
    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";

    ListView list;
    CustomAdaptor adapter;

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

        txtCity = findViewById(R.id.tvAddCity);
        weatherLV = findViewById(R.id.weatherListView);
        weatherLV.setAdapter(customAdaptor);
        weatherLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                startCityDetailsActivity();

                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Add button pressed: Persist city name, add to list and clear textview
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    persistCity();
//                    sendRequest();
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

//        reloadPreferences();
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

//        adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, stringList);
        weatherLV.setAdapter(customAdaptor);
    }

//    private List<ListViewItem> getWeatherInfo(){
//
//    }

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

//    private void setupListView(){
//        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
//
//        JSON parser = new WeatherJsonParser();
//        String xml = parser.(URL); // getting XML from URL
//        DocumentsContract.Document doc = parser.getDomElement(xml); // getting DOM element
//
//        NodeList nl = doc.getElementsByTagName(KEY_SONG);
//        // looping through all song nodes <song>
//        for (int I = 0; I < nl.getLength(); i++) {
//            // creating new HashMap
//            HashMap<String, String> map = new HashMap<String, String>();
//            Element e = (Element) nl.item(i);
//            // adding each child node to HashMap key => value
//            map.put(KEY_ID, parser.getValue(e, KEY_ID));
//            map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
//            map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
//            map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
//            map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
//
//            // adding HashList to ArrayList
//            songsList.add(map);
//        }
//
//        list=(ListView)findViewById(R.id.list);
//
//        // Getting adapter by passing xml data ArrayList
//        adapter=new CustomAdaptor(this, songsList);
//        list.setAdapter(adapter);
//
//        // Click event for single list row
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//            }
//        });
//    }

    private void startCityDetailsActivity() {
        Intent intent = new Intent(this, CityDetailsActivity.class);
        intent.putExtra(Const.CITY_NAME, cityName);
        intent.putExtra(Const.TEMP, temp);
        intent.putExtra(Const.HUMIDITY, humidity);
        intent.putExtra(Const.WEATHER_IMAGE, weatherImage);
        startActivityForResult(intent, Const.REQUEST_DETAILS_ACTIVITY);
    }
}
