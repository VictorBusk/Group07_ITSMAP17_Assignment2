package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        txtTemp.setText("Temperature\n" + data.getStringExtra(Const.TEMP) + "°C");

        txtHumidity = findViewById(R.id.tvHumidity);
        txtHumidity.setText("Humidity\n" + data.getStringExtra(Const.HUMIDITY) + "%");

        txtDescription = findViewById(R.id.tvDescription);
        txtDescription.setText(data.getStringExtra(Const.DESCRIPTION));

        ImageView img = findViewById(R.id.img);
        Picasso.with(this).load("http://openweathermap.org/img/w/" + data.getStringExtra(Const.WEATHER_IMAGE) + ".png").resize(300,300).into(img);

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
