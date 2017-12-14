package com.ravendmaster.linearmqttdashboard.utils;

import android_serialport_api.SerialPortMgr;

/**
 * 平板发送消息给3288
 *
 * @author xiaowei
 */
public class MsgSendUtils {

        public static final String TAG = "MsgSendUtils";

        /**
         * 发送String类型的数据
         *
         * @param msgType
         * @param msgData
         */
        public static void sendStringMsg(int msgType, String msgData) {
                Logger.i(TAG, "msgData --" + msgData);
                SerialPortMgr.getInstance().sendTouChuanData(msgType, msgData);
        }

        /**
         * 通过串口转WiFi，发送String到手持平板
         *
         * @param msgType
         * @param msgData
         */
        public static void sendMsgToPad(int msgType, String msgData) {
                Logger.i(TAG, "msgData --" + msgData);
                SerialPortMgr.getInstance().sendMsg(msgType, msgData);
        }


}
