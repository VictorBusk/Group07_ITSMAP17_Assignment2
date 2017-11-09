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

import java.util.ArrayList;

public class CityDetailsActivity extends AppCompatActivity {

    private Button btnRemove, btnOk;
    private TextView txtCity, txtTemp, txtHumidity, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_activity);

        final Intent data = getIntent();

        txtCity = findViewById(R.id.tvCityName);
        txtCity.setText(data.getStringExtra(Const.CITY_NAME));

        txtTemp = findViewById(R.id.tvTemp);
        txtTemp.setText(data.getStringExtra(Const.TEMP));

        txtHumidity = findViewById(R.id.tvHumidity);
        txtHumidity.setText(data.getStringExtra(Const.HUMIDITY));

        txtDescription = findViewById(R.id.tvDescription);
        txtDescription.setText(data.getStringExtra(Const.DESCRIPTION));

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnRemove = (Button) findViewById(R.id.btnRemove);
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
                CityListActivity.customAdaptor.notifyDataSetChanged();
                setResult(RESULT_OK, data);
                finish();
            }
        });

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
