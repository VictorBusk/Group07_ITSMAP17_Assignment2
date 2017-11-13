package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.squareup.picasso.Picasso;

public class CityDetailsActivity extends AppCompatActivity {

    public TextView txtCity, txtTemp, txtHumidity, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_activity);

        final Intent data = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        txtCity = findViewById(R.id.tvCityName);
        txtCity.setText(data.getStringExtra(Const.CITY_NAME));

        txtTemp = findViewById(R.id.tvTemp);
        String tempPrefix = getResources().getString(R.string.txt_temp);
        String tempPostfix = getResources().getString(R.string.temp_symbol);
        String tempString = tempPrefix + "\n" + data.getStringExtra(Const.TEMP) + tempPostfix;
        txtTemp.setText(tempString);

        txtHumidity = findViewById(R.id.tvHumidity);
        String humPrefix = getResources().getString(R.string.txt_hum);
        String humPostfix = getResources().getString(R.string.humidity_symbol);
        String humString = humPrefix + "\n" + data.getStringExtra(Const.HUMIDITY) + humPostfix;
        txtHumidity.setText(humString);

        txtDescription = findViewById(R.id.tvDescription);
        txtDescription.setText(data.getStringExtra(Const.DESCRIPTION));

        ImageView img = findViewById(R.id.img);
        Picasso.with(this).load(Connector.iconURL(data.getStringExtra(Const.WEATHER_IMAGE))).resize(300, 300).into(img);

        final String cityName = data.getStringExtra(Const.CITY_NAME);

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        Button btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newData = new Intent();
                newData.putExtra(Const.CITY_NAME, cityName);
                setResult(Const.RESULT_REMOVED, newData);
                finish();
            }
        });

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
