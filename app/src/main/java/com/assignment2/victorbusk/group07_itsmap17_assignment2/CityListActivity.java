package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.adaptor.CustomAdaptor;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends AppCompatActivity {

    Bitmap weatherImage;
    private ListView weatherLV;
    private Button btnRefresh, btnAdd;
    public static ListAdapter customAdaptor;
    public static List<String> stringList = new ArrayList<>();
    public static WeatherItemModel weatherItemModel;
    ArrayList<WeatherItemModel> weatherList = new ArrayList<WeatherItemModel>();
    StringBuilder stringBuilder = new StringBuilder();

    public static TextView txtCity;

    String cityName, temp, humidity;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list_activity);

        txtCity = findViewById(R.id.tvAddCity);
        weatherLV = findViewById(R.id.weatherListView);

        weatherLV = findViewById(R.id.weatherListView);
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
                loadDataFromPreferences();
            }
        });

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        preferences.edit().remove("words").commit();

        loadDataFromPreferences();
    }

    protected void persistCity() throws IOException {
        stringList.clear();
        stringList.add(txtCity.getText().toString());
        for (String s : stringList) {
            stringBuilder.append(s);
            stringBuilder.append("!");
        }
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("words", stringBuilder.toString());
        editor.commit();
        loadDataFromPreferences();
    }

    protected void loadDataFromPreferences() {
        CustomAdaptor clearAdapter = (CustomAdaptor) weatherLV.getAdapter();
        if(clearAdapter != null){
            clearAdapter.clearData();
            clearAdapter.notifyDataSetChanged();
        }

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        String wordString = settings.getString("words", "");
        String[] itemWords = wordString.split("!");
        int i = 0;
        for (String itemWord : itemWords) {
            WeatherData getData = new WeatherData();
            getData.execute(Connector.CallAPI(itemWord));
            if (weatherItemModel != null) {
                weatherList.add(i, weatherItemModel);
                i++;
            }
        }
        customAdaptor = new CustomAdaptor(this, weatherList);
        weatherLV.setAdapter(customAdaptor);
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
