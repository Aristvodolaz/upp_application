package com.example.timetrekerforandroid.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScannerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "ScannerBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast received. Action: " + intent.getAction());
        if ("com.example.myapplication.ACTION".equals(intent.getAction())) {
            String data = intent.getStringExtra("com.symbol.datawedge.data_string");
            if (data != null) {
                Log.d(TAG, "Received data: " + data);
            } else {
                Log.e(TAG, "No barcode data received.");
            }
        } else {
            Log.e(TAG, "Unexpected action: " + intent.getAction());
        }
    }
}
