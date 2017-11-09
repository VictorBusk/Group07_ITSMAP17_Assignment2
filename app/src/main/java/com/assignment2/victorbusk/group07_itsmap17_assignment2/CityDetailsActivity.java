package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherService;

import java.util.ArrayList;

public class CityDetailsActivity extends AppCompatActivity {

    private Button btnRemove, btnOk;
    public static TextView txtCity, txtTemp, txtHumidity, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_activity);

        txtCity = findViewById(R.id.tvCityName);
        txtTemp = findViewById(R.id.tvTemp);
        txtHumidity = findViewById(R.id.tvHumidity);
        txtDescription = findViewById(R.id.tvDescription);


        final Intent data = getIntent();
        String cityName = data.getStringExtra(Const.CITY_NAME);
        new WeatherService(this, Const.DETAILS_ACTIVITY_CALLER).execute(Connector.CallAPI(cityName));

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wordString = CityListActivity.preferences.getString(Const.KEY, "");
                String deletionTarget = (txtCity.getText() + "!");
                wordString = wordString.replaceAll("(?i)" + deletionTarget, "");
                SharedPreferences.Editor editor = CityListActivity.preferences.edit();
                editor.clear();
                editor.putString(Const.KEY, wordString);
                editor.apply();

                CityListActivity.weatherList.remove(CityListActivity.deletePos);
//                CityListActivity.customAdaptor.notifyDataSetChanged();
                setResult(RESULT_OK, data);
                finish();
            }
        });

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
