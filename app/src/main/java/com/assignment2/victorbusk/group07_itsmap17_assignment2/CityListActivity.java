package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor.CustomAdaptor;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.UpdateManager;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CityListActivity extends AppCompatActivity {

    private ListView weatherLV;
    public static int rowNum;
    public static SharedPreferences preferences;
    public static ArrayList<WeatherItemModel> weatherList;
    int currentDeletePos;
    CustomAdaptor customAdaptor;
    private static final int NOTIFY_ID = 142;
    NotificationManager notificationManager;
    public TextView txtCity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list_activity);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(msgReceiver,
                new IntentFilter("update-list-event"));

        preferences = getSharedPreferences("PREFS", 0);

        txtCity = findViewById(R.id.tvAddCity);
        weatherLV = findViewById(R.id.weatherListView);
        weatherList = new ArrayList<WeatherItemModel>();

        CustomAdaptor clearAdapter = (CustomAdaptor) weatherLV.getAdapter();
        if(clearAdapter != null){ //Clear listview
            clearAdapter.clearData();
            clearAdapter.notifyDataSetChanged();
            weatherLV.setAdapter(clearAdapter);
        }

        weatherLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                startCityDetailsActivity(position);
            }
        });

        //Add button pressed: Persist city name.
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    persistCity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startAlarm();
                    callAPI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if(!preferences.getString(Const.KEY, "").isEmpty()) { //Checking for data. If there is any then refresh, otherwise just skip.
            try {
                startAlarm();
                callAPI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void persistCity() throws Exception { //Saved city name and refresh all data
        String newCity = preferences.getString(Const.KEY, "") + txtCity.getText().toString() + "!"; //Expand sharedpreference list
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Const.KEY, newCity);
        editor.apply();
        callAPI();
    }

    public void callAPI() throws ExecutionException, InterruptedException { //Refresh listview from API call
        CustomAdaptor clearAdapter = (CustomAdaptor) weatherLV.getAdapter();
        if(clearAdapter != null){ //Clear listview
            clearAdapter.clearData();
            clearAdapter.notifyDataSetChanged();
            weatherLV.setAdapter(clearAdapter);
        }
        weatherList = new ArrayList<WeatherItemModel>();
        String wordString = preferences.getString(Const.KEY, "");
        String[] itemWords = wordString.split("!");
        rowNum = 0;
        for (String itemWord : itemWords) {
            if(itemWord != "") { new WeatherService(this ).execute(Connector.CallAPI(itemWord));}
        }
        sendNotification();
    }

    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            String city = intent.getStringExtra(Const.CITY_NAME);
            String temp = intent.getStringExtra(Const.TEMP);
            String humid = intent.getStringExtra(Const.HUMIDITY);
            String desc = intent.getStringExtra(Const.DESCRIPTION);
            String img = intent.getStringExtra(Const.WEATHER_IMAGE);

            WeatherItemModel newWeatherItemModel = new WeatherItemModel(city, temp, humid, desc, img);
            weatherList.add(rowNum, newWeatherItemModel);
            customAdaptor = new CustomAdaptor(context, weatherList);
            customAdaptor.notifyDataSetChanged();
            weatherLV.setAdapter(customAdaptor);
            rowNum++;
        }
    };

    public void startAlarm() throws ExecutionException, InterruptedException {
        Intent intent = new Intent(CityListActivity.this, UpdateManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5*60*1000, pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQUEST_DETAILS_ACTIVITY && resultCode == Const.RESULT_REMOVED) {
            String wordString = preferences.getString(Const.KEY, "");
            String deletionTarget = data.getStringExtra(Const.CITY_NAME) + "!";
            wordString = wordString.replaceAll("(?i)" + deletionTarget, "");
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putString(Const.KEY, wordString);
            editor.apply();

            weatherList.remove(currentDeletePos);
            try {
                callAPI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendNotification() {
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getText(R.string.app_name))
                        .setContentText("Weather data has been updated");
        notificationManager.notify(NOTIFY_ID, notification.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(msgReceiver);
    }


    private void startCityDetailsActivity(int position) { //Start details activity
        currentDeletePos = position;
        Intent intent = new Intent(this, CityDetailsActivity.class);
        intent.putExtra(Const.CITY_NAME, weatherList.get(position).getName());
        intent.putExtra(Const.TEMP, weatherList.get(position).getTemperature());
        intent.putExtra(Const.HUMIDITY, weatherList.get(position).getHumidity());
        intent.putExtra(Const.DESCRIPTION, weatherList.get(position).getDescription());
        intent.putExtra(Const.WEATHER_IMAGE, weatherList.get(position).getImage());

        startActivityForResult(intent, Const.REQUEST_DETAILS_ACTIVITY);
    }
}
