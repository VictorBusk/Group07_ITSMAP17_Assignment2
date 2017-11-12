package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Listener;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.UpdateManager;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherService;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CityListActivity extends AppCompatActivity implements Listener {

    private ListView weatherLV;
    public static int rowNum;
    public static SharedPreferences preferences;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    public static ArrayList<WeatherItemModel> weatherList = new ArrayList<WeatherItemModel>();
    int currentDeletePos;

    public TextView txtCity;

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
                    refreshData();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if(!preferences.getString(Const.KEY, "").isEmpty()) { //Checking for data. If there is any then refresh, otherwise just skip.
            try {
            startAlarm();
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
        refreshData();
    }

    protected void refreshData() throws ExecutionException, InterruptedException { //Refresh listview from API call
        CustomAdaptor clearAdapter = (CustomAdaptor) weatherLV.getAdapter();
        if(clearAdapter != null){ //Clear listview
            clearAdapter.clearData();
            clearAdapter.notifyDataSetChanged();
        }
        String wordString = preferences.getString(Const.KEY, "");
        String[] itemWords = wordString.split("!");
        rowNum = 0;
        for (String itemWord : itemWords) {
            new WeatherService(this).execute(Connector.CallAPI(itemWord));
        }
    }

    private void startCityDetailsActivity(int position) { //Start details activity
        currentDeletePos = position;
        Intent intent = new Intent(this, CityDetailsActivity.class);
        intent.putExtra(Const.CITY_NAME, weatherList.get(position).getName());
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

    public void startAlarm() throws ExecutionException, InterruptedException {
        refreshData();
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 300000; //5 min

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    @Override
    public void onRemoteCallComplete(WeatherItemModel weather) {
        weatherList.add(rowNum, weather);
        CustomAdaptor customAdaptor = new CustomAdaptor(this, weatherList);
        customAdaptor.notifyDataSetChanged();
        weatherLV.setAdapter(customAdaptor);
        rowNum++;
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
                refreshData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
