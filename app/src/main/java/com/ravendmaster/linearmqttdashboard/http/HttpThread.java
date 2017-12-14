package com.ravendmaster.linearmqttdashboard.http;

/**
 * Created by Administrator on 2017/1/18.
 */

public class HttpThread extends Thread {

    /**
     * msgData 参数中的ID
     */
    private String httpParam;

    /**
     * msgType  参数中的类型
     */
    private String type;

    /**
     * 发送HTTP请求的线程
     * @param httpParam 参数中的ID号
     * @param type 参数类型
     */
    public HttpThread(String httpParam, String type) {
        this.httpParam = httpParam;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            new HttpUtil().sendHttpRequest(httpParam, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
