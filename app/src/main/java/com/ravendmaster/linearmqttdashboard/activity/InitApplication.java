package com.ravendmaster.linearmqttdashboard.activity;

import android.app.Application;
import android.content.Context;


/**
 * 应用初始化
 * Created by ArrayList on 2017/6/16.
 */

public class InitApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
