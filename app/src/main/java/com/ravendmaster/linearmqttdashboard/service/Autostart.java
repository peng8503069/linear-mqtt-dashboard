package com.ravendmaster.linearmqttdashboard.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ravendmaster.linearmqttdashboard.activity.MainActivity;

/**
 * Created by Андрей on 15.05.2016.
 */
public class Autostart extends BroadcastReceiver
{
    private static final String TAG = "sdses_Autostart";

    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        /*intent.setAction("autostart");
        context.startService(intent);*/
        Log.d(TAG, "收到了系统开机广播   started");
    }
}