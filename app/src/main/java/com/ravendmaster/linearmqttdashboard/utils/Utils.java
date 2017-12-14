package com.ravendmaster.linearmqttdashboard.utils;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2017/9/22.
 */

public class Utils {

    private static Gson gson;

    private Utils(){
        gson = new Gson();
    }

    public static Gson getGson(){
        if (gson == null){
            gson = new Gson();
        }
        return gson;
    }

}
