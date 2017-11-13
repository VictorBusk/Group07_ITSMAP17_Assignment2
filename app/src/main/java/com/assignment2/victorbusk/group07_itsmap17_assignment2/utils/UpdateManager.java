package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.assignment2.victorbusk.group07_itsmap17_assignment2.CityListActivity;


public class UpdateManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CityListActivity cityListActivity = new CityListActivity();
        try {
            cityListActivity.callAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
