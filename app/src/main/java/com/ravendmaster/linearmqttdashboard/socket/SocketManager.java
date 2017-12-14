package com.ravendmaster.linearmqttdashboard.socket;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 作为Socket服务器端的管理类
 * Created by ArrayList on 2017/7/17.
 */

public class SocketManager {


    public static int noSpeakCount = 0;

    /**
     * Socket服务器监听的端口号
     */
    public static final int socketPort = 9459;

    /**
     * Socket服务器端对象
     */
    public static ServerSocket serverSocket = null;

    /**
     * Socket流连接对象
     */
    public static Socket socket = null;

    private static final String TAG = "SocketManager";

    /**
     * 创建SocketServer服务器端，并获取连接客户端的Socket对象
     * @return SocketServer服务器端对象
     */
    public static ServerSocket createSocketServer(){
        try {
            serverSocket = new ServerSocket(socketPort);
            return serverSocket;
        } catch (IOException e) {
            Log.e(TAG, "SocketServer服务器端创建异常");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Socket连接对象
     * @return Socket流对象
     */
    public static Socket acceptSocket(){
        try {
            if (serverSocket == null){
                serverSocket = createSocketServer();
            }
            socket = serverSocket.accept();
            return socket;
        } catch (IOException e) {
            Log.d(TAG, "acceptSocket: 连接对象获取异常");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * socket服务器端向客户端发送AMQ状态
     * @param state AMQ状态消息
     */
    public static void sendData(String state){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(state);
        } catch (IOException e) {
            Log.d(TAG, "sendData: 获取输入流错误");
            e.printStackTrace();
        }
    }

}
