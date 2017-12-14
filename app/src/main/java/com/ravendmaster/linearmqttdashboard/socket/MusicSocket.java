package com.ravendmaster.linearmqttdashboard.socket;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.ravendmaster.linearmqttdashboard.activity.MainActivity;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataSocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/7/18.
 */

public class MusicSocket implements Runnable{

    private static final String TAG = "sdses_MusicSocket";

    /**
     * 等于1，深度休眠
     * 等于0，浅休眠
     */
    public static int robotState = 0;

    public static int noSpeakCount = 0;

    public Context context;

    public static Socket socket;

    public InputStream is;

    public DataInputStream dis;

    public MusicSocket (Context context){
        this.context = context;
    }

    public static void conSocket() {
        try {
            socket = new Socket("127.0.0.1", 19255);
            socket.setKeepAlive(true);
            if (socket.isConnected()){
                Log.d(TAG, "conSocket: Socket连接成功");
            }else {
                Log.d(TAG, "conSocket: Socket连接失败");
            }
        } catch (IOException e) {
            Log.d(TAG, "conSocket: Socket连接异常，请检查网络连接");
            e.printStackTrace();
        }
    }

    public void receiveData(){
        Log.d(TAG, "receiveData: 开始获取内容");
        try {
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            while (true){
                if (is == null){
                    Log.d(TAG, "receiveData: 未获取到了输入流");
                }
                String text = dis.readUTF(); //获取到内容
                Log.d(TAG, "receiveData: " + text);
//                toastText(text);
                handSocketMessage(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handSocketMessage(String msg){
        Gson gson = new Gson();
        JsonDataSocket socketData = gson.fromJson(msg, JsonDataSocket.class);
        switch (socketData.getMsgType()){
            case 6:   // 唤醒机器人
                Log.d(TAG, "handSocketMessage: 收到了唤醒机器人的socket信息，暂停音乐");
                MainActivity.player.pause(); // 暂停音乐
                break;
            case 12: // 感应器消息
                Log.d(TAG, "handSocketMessage: " + socketData.getMsgData());
                if ((socketData.getMsgData() + "").equals("153")){  // 人体感应器消息
                    Log.d(TAG, "handSocketMessage: 收到了人体感应器消息，对音乐不影响");
                    break;
                }
                Log.d(TAG, "handSocketMessage: 收到了感应器消息，暂停音乐");
                MainActivity.player.pause(); // 暂停音乐
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        conSocket();
        receiveData();
    }

}
