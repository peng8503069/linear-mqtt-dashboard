package com.ravendmaster.linearmqttdashboard.bean;

/**
 * Created by Administrator on 2017/8/11.
 */

public class JsonDataSerialport {

    /**
     *  不说了  /   自动休眠   时，Socket信息
     * msgData : ok
     * msgType : 9
     */

    /**
     *  你好小神  /  感应器  时，Socket信息
     * msgData : ok
     * msgType : 6 / 11
     */

    private String msgData;
    private int msgType;

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgData) {
        this.msgData = msgData;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "JsonDataSerialport [msgType=" + msgType + ", msgData=" + msgData + "]";
    }


}
