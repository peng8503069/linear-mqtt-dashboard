package com.ravendmaster.linearmqttdashboard.bean;

/**
 * Created by Administrator on 2017/6/13.
 */

public class JsonDataAmq {

    private int msgType;

    public static final int TYPE_ROBOT = 0;

    public static final int TYPE_SPEAK = 1;

    public String msgData;

    public JsonDataAmq(int msgType, String msgData) {
        this.msgType = msgType;
        this.msgData = msgData;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getmsgData() {
        return msgData;
    }

    public void setmsgData(String msgData) {
        this.msgData = msgData;
    }
}
