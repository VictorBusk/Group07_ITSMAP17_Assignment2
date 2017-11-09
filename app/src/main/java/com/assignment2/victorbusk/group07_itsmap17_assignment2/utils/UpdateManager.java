package com.assignment2.victorbusk.group07_itsmap17_assignment2.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by victo on 09-11-2017.
 */

public class UpdateManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

    }
}
