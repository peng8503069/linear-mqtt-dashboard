package android_serialport_api;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.ravendmaster.linearmqttdashboard.activity.ChatActivity;
import com.ravendmaster.linearmqttdashboard.activity.MainActivityHello;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataSerialport;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataSocket;
import com.ravendmaster.linearmqttdashboard.utils.Logger;


/**
 * 处理头部3288通过串口发来的消息
 */

public class ReceiveData {

    private static final String TAG = "sdses_ReceiveData";

    /**
     * 等于1，深度休眠
     * 等于0，浅休眠
     */
    public static int robotState = 0;

    public static int noSpeakCount = 0;

    public Context mContext;

    private MyReceiveDataInterface listener;

    // 是否连续收到了心跳消息，连续三次
    public static int heartbeatCount = 0;

    // 机器人是否在说话
    public static boolean speak = false;

    /**
     * 处理所有通过串口发送过来的消息
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String msgData = (String) msg.obj;
            Logger.d(TAG, "msg.what " + msg.what + " msg.obj: " + msg.obj);
        }
    };

    public ReceiveData(Context context) {
        this.mContext = context;
        listener = new MyReceiveDataInterface();
        SerialPortMgr.getInstance().init(listener);
    }

    /**
     * 接收处理头部通过串口发来的消息
     */
    public class MyReceiveDataInterface implements ReceiveDataInterface {

        /**
         * 接收串口消息
         * @param data
         */
        @Override
        public void onReceive(String data) {
            if (data != null) {

                Log.d(TAG, "串口收到的原始消息： " + data);

                Gson gson = new Gson();
                JsonDataSerialport serialportData = gson.fromJson(data, JsonDataSerialport.class);
                if (serialportData != null) {
                    Logger.d(TAG, serialportData.toString());
                    // 处理通过串口发送过来的消息
                    handSerialportMessage(data);
                }
            }
        }

        /**
         * 处理通过串口发送过来的消息
         * @param serialportData
         */
        private void handSerialportMessage(String serialportData){
            Gson gson = new Gson();
            JsonDataSocket socketData = gson.fromJson(serialportData, JsonDataSocket.class);
            switch (socketData.getMsgType()){
                case 1:   // 识别到了语音，转化成了文字信息
                    Intent intentChat2 = new Intent(mContext, ChatActivity.class);
                    intentChat2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentChat2); // 启动聊天界面
                    ReceiveData.noSpeakCount = 0;
                    ReceiveData.heartbeatCount = 0;
                    ReceiveData.speak = true;
                    break;
                case 11: // AIUI收不到此消息
                    ReceiveData.noSpeakCount ++;
                    ReceiveData.speak = false;
                    Log.d(TAG, "收到了开始识别的串口消息，handSerialportMessage: noSpeakCount" + ReceiveData.noSpeakCount);
                    if (ReceiveData.noSpeakCount == 6){
                        Intent intentMian = new Intent(mContext, MainActivityHello.class);
                        intentMian.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intentMian); // 启动欢迎界面
                        robotState = 0;
                    }
                    break;
                case 6:   //唤醒成功时发送的消息
                    ReceiveData.speak = true;
                    MainActivityHello.currentTime = System.currentTimeMillis();
                    Intent intentChat = new Intent(mContext, ChatActivity.class);
                    intentChat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentChat); // 启动聊天界面
                    ReceiveData.heartbeatCount = 0;
                      // 开启人脸检测
                    /*Message helloMessage = MainActivityHello.handler.obtainMessage();
                    helloMessage.what = 6;
                    MainActivityHello.handler.sendMessage(helloMessage);*/
                    break;
                case 12: // 11
                    if (robotState == 1){
                        Log.d(TAG, "深度休眠状态，不进入聊天界面");
                        break;
                    }
                    Intent intentChat3 = new Intent(mContext, ChatActivity.class);
                    intentChat3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentChat3); // 启动聊天界面
                    break;
                case 9:
                    robotState = 1;
                    Log.d(TAG, "handSerialportMessage: 收到不说了信号，进行欢迎界面");
                    Intent intentMian = new Intent(mContext, MainActivityHello.class);
                    intentMian.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentMian); // 启动欢迎界面
                    ReceiveData.speak = false;
                    ReceiveData.heartbeatCount = 0;
                    break;
                case 255: //打招呼
                    /*Message helloMessage2 = MainActivityHello.handler.obtainMessage();
                    helloMessage2.what = 8;
                    MainActivityHello.handler.sendMessage(helloMessage2);*/
                case 254: //拜拜
                    /*Message byeMessage = MainActivityHello.handler.obtainMessage();
                    byeMessage.what = 7;
                    MainActivityHello.handler.sendMessage(byeMessage);*/

                /*case 39: //收到心跳
                    ReceiveData.heartbeatCount++;
                    if (!ReceiveData.speak && ReceiveData.heartbeatCount >= 3){
                        Log.d(TAG, "handSerialportMessage: 收到开机心跳，进行欢迎界面");
                        Intent intentMian2 = new Intent(mContext, MainActivityHello.class);
                        intentMian2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intentMian2); // 启动欢迎界面
                    }*/
                    break;
                case 9000:
                    Log.d(TAG, "handSerialportMessage: 收到了头部的串口数据");
                    break;
                default:
                    ReceiveData.heartbeatCount = 0;
                    break;
            }
        }

    }
}
