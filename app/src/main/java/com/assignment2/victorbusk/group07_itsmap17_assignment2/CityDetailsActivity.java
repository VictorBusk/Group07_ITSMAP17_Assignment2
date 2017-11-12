package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.model.WeatherItemModel;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Connector;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.Listener;
import com.assignment2.victorbusk.group07_itsmap17_assignment2.utils.WeatherService;
import com.squareup.picasso.Picasso;

public class CityDetailsActivity extends AppCompatActivity implements Listener {

    private Button btnRemove, btnOk;
    public TextView txtCity, txtTemp, txtHumidity, txtDescription;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        txtCity = findViewById(R.id.tvCityName);
        txtTemp = findViewById(R.id.tvTemp);
        txtHumidity = findViewById(R.id.tvHumidity);
        txtDescription = findViewById(R.id.tvDescription);
        img = findViewById(R.id.img);

        final Intent data = getIntent();
        final String cityName = data.getStringExtra(Const.CITY_NAME);
        new WeatherService(this).execute(Connector.CallAPI(cityName));

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnRemove = findViewById(R.id.btnRemove);
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
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRemoteCallComplete(WeatherItemModel weather) {
        txtCity.setText(weather.getName());
        txtTemp.setText("Temperature:\n" + weather.getTemperature() + "°C");
        txtHumidity.setText("Humidity:\n" + weather.getHumidity() + "%");
        txtDescription.setText(weather.getDescription());
        Picasso.with(this).load("http://openweathermap.org/img/w/" + weather.getImage() + ".png").resize(300,300).into(img);
    }
}
