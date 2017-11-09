package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor.CustomAdaptor;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.UpdateManager;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends AppCompatActivity {

    private ListView weatherLV;
    private Button btnRefresh, btnAdd;
    public static List<String> stringList = new ArrayList<>();
    public static WeatherItemModel weatherItemModel;
    static ArrayList<WeatherItemModel> weatherList = new ArrayList<WeatherItemModel>();
    StringBuilder stringBuilder = new StringBuilder();
    public static int deletePos;
    static CustomAdaptor customAdaptor;
    static SharedPreferences preferences;
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    public static TextView txtCity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list_activity);

        Intent alarmIntent = new Intent(this, UpdateManager.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        LocalBroadcastManager.getInstance(this).registerReceiver(msgReceiver,
                new IntentFilter("update-event"));

        preferences = getSharedPreferences("PREFS", 0);

        txtCity = findViewById(R.id.tvAddCity);
        weatherLV = findViewById(R.id.weatherListView);

        weatherLV = findViewById(R.id.weatherListView);
        weatherLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                deletePos = position;
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
                loadDataFromPreferences();
            }
        });

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        preferences.edit().remove(Const.KEY).commit();
        loadDataFromPreferences();
    }

    protected void persistCity() throws IOException {
        stringList.clear();
        stringList.add(txtCity.getText().toString());
        for (String s : stringList) {
            stringBuilder.append(s);
            stringBuilder.append("!");
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Const.KEY, stringBuilder.toString());
        editor.commit();
        loadDataFromPreferences();
    }

    protected void loadDataFromPreferences() {
        weatherItemModel = null;
        CustomAdaptor clearAdapter = (CustomAdaptor) weatherLV.getAdapter();
        if(clearAdapter != null){
            clearAdapter.clearData();
            clearAdapter.notifyDataSetChanged();
        }

        preferences = getSharedPreferences("PREFS", 0);
        String wordString = preferences.getString(Const.KEY, "");
        String[] itemWords = wordString.split("!");
        int i = 0;
        for (String itemWord : itemWords) {
            WeatherService getData = new WeatherService();
            getData.execute(Connector.CallAPI(itemWord));
            weatherItemModel = getData.parseDataFromAPI();
            if (weatherItemModel != null) {
                weatherList.add(i, weatherItemModel);
                i++;
            }
        }
        customAdaptor = new CustomAdaptor(this, weatherList);
        customAdaptor.notifyDataSetChanged();
        weatherLV.setAdapter(customAdaptor);
    }

    private void startCityDetailsActivity() {
        Intent intent = new Intent(this, CityDetailsActivity.class);
        intent.putExtra(Const.CITY_NAME, weatherItemModel.getName());
        intent.putExtra(Const.TEMP, weatherItemModel.getTemperature() + "°C");
        intent.putExtra(Const.HUMIDITY, weatherItemModel.getHumidity() + "%");
        intent.putExtra(Const.DESCRIPTION, weatherItemModel.getDescription());
        customAdaptor.notifyDataSetChanged();
        startActivityForResult(intent, Const.REQUEST_DETAILS_ACTIVITY);
    }

    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };

    public void startAlarm(View view) {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 300000; //5 min

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }
}

//////ROD///////
//        if (savedInstanceState != null) {
//            cityName = savedInstanceState.getString(Const.CITY_NAME, getString(R.string.tv_city_name));
//            temp = savedInstanceState.getString(Const.TEMP, getString(R.string.tv_temp));
//            humidity = savedInstanceState.getString(Const.HUMIDITY, getString(R.string.tv_humidity));
//            weatherImage = savedInstanceState.getParcelable(Const.WEATHER_IMAGE);
//        } else {
//            cityName = getString(R.string.tv_city_name);
//            temp = getString(R.string.tv_temp);
//            humidity = getString(R.string.tv_humidity);
//            weatherImage = null;
//        }
