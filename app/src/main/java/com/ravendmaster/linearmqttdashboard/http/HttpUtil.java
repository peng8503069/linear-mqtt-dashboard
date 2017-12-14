package com.ravendmaster.linearmqttdashboard.http;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/15.
 */

public class HttpUtil {
    private final static String baseUrl = "http://127.0.0.1:7755/handleMsg?";
    public final static String VOICE = "8401";
    public final static String VOLUME = "8407";
    public final static String MOTION = "8420";
    public final static String EYE = "8422";
    public final static String ACTIVE = "8403";
    public final static String N_ACTIVE = "9";

    public int sendHttpRequest(String param, String type) {
        String urlNameString = baseUrl + "msgType=" + type + "&msgData=" + param;
        HttpURLConnection connection = null;
        try {
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestMethod("POST");
            Log.d("main", "sendHttpRequest: " + urlNameString);
            connection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection.getInputStream() != null)
                    connection.getInputStream().close();
                if (connection.getOutputStream() != null)
                    connection.getOutputStream().close();
                // int code = connection.getResponseCode();
            } catch (Exception e) {

            }
            connection.disconnect();
        }

        return 0;

    }
}
