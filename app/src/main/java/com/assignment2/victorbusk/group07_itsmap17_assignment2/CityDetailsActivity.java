package com.assignment2.victorbusk.group07_itsmap17_assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CityDetailsActivity extends AppCompatActivity {

    private Button btnRemove, btnOk;
    private TextView txtCity, txtJsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_details_activity);

        //Refresh button pressed: Reload layout and reload listView (fresh data)
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    protected void removePesistedCity() {

    }
}
