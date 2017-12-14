package com.ravendmaster.linearmqttdashboard.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.ravendmaster.linearmqttdashboard.activity.ChatActivity;
import com.ravendmaster.linearmqttdashboard.activity.MainActivityHello;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataAmq;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataAmqPlus;
import com.ravendmaster.linearmqttdashboard.bean.PassWord;
import com.ravendmaster.linearmqttdashboard.utils.MsgSendUtils;
import com.ravendmaster.linearmqttdashboard.utils.MsgType;
import com.ravendmaster.linearmqttdashboard.utils.Utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/9/20.
 */

public class HandMessage {

    private static final String TAG = "sdses_HandMessage";

    /**
     * 解析json文件，包含权限
     * @param message
     */
    public static void parseChatJsonPlus(String message, Context context){

        Log.d(TAG, "parseChatJsonPlus: 开始处理json字符串");

        if (message.equals("0") || message.equals("#55ff55")){
            Log.d(TAG, "parseChatJsonPlus: MQTT 得到0 / #55ff55消息");
            return;
        }

        JsonDataAmqPlus jsonDataAmqPlus = Utils.getGson().fromJson(message, JsonDataAmqPlus.class);
        JsonDataAmqPlus.AuthBean auth = jsonDataAmqPlus.getAuth();
        JsonDataAmqPlus.DataBean dataBean = jsonDataAmqPlus.getData();
        String pop = auth.getPop();

        if (pop.equals("true")){

            //设置密码以及actionID
            PassWord.getPassWord().setPw(auth.getPassword());
            PassWord.getPassWord().setAction(auth.getAction());

            Log.d(TAG, "parseChatJsonPlus: 弹出密码框，进行管理员身份验证");

            Message showDialog = MainActivityHello.handler.obtainMessage();
            showDialog.what = 5;
            MainActivityHello.handler.sendMessage(showDialog);

//            ChatActivity.alertView.show(); // 创建并弹出密码框
//            ChatActivity.dismiss = true;

            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "请输入管理员指令");

            Message dismiss = MainActivityHello.handler.obtainMessage(4);     // Message
            MainActivityHello.handler.sendMessageDelayed(dismiss, 5000);

            return;
        }else {
            if(auth.getAction().contains("$") || auth.getAction().split("#")[0].contains("3")){
                Log.d(TAG, "parseChatJsonPlus: 发送了跳舞指令，不需要密码");
                String[] splites = auth.getAction().split("#");
                //socket发送指令
                int action = Integer.parseInt(splites[2]);
                int index = ChatActivity.actions.indexOf(action);
                String sing = "";
                MsgSendUtils.sendStringMsg(MsgType.NO_ACTIVE, "休眠机器人");
                try {
                    TimeUnit.SECONDS.sleep(1); // 休眠一秒钟
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onItemClick: 出现异常");
                }
                Log.d(TAG, "onItemClick: 发送了跳舞指令");
                if (index != -1) {
                    sing = ChatActivity.sings.get(index);
                    MsgSendUtils.sendStringMsg(MsgType.PLAY_AUDIO, sing);
                }
                MsgSendUtils.sendStringMsg(MsgType.PLAY_ACTION, action + "");
                Log.d(TAG, "parseChatJsonPlus: 发送了跳舞指令: " + action);
                // 回到欢迎界面
                Intent intentMian = new Intent(context, MainActivityHello.class);
                intentMian.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentMian); // 启动欢迎界面
                return;
            }
        }

        if (dataBean.getMsgData().equals("进入人脸识别")){
            /*Message hello = MainActivityHello.handler.obtainMessage();
            hello.what = 9;
            MainActivityHello.handler.sendMessage(hello);*/
            Log.d(TAG, "parseChatJsonPlus: 进入人脸识别");
            /*// 跳转
            Message helloMessage = MainActivityHello.handler.obtainMessage();
            helloMessage.what = 6;
            MainActivityHello.handler.sendMessage(helloMessage);*/
            MsgSendUtils.sendStringMsg(MsgType.NO_ACTIVE, "休眠机器人"); //休眠机器人
            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "您好"); //休眠机器人
            Intent intentCom = new Intent();
            ComponentName componentName = new ComponentName("com.sdses.mqtthead",
                    "com.ravendmaster.linearmqttdashboard.activity.CompareActivity");
            intentCom.setComponent(componentName);
            intentCom.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentCom); // 启动比对界面

            return;
        }

        if (dataBean.getMsgData().equals("返回欢迎界面")){
            /*Message hello = MainActivityHello.handler.obtainMessage();
            hello.what = 9;
            MainActivityHello.handler.sendMessage(hello);*/
            Log.d(TAG, "parseChatJsonPlus: 收到了欢迎界面命令");
            // 跳转
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.sdses.mqtthead", "com.ravendmaster.linearmqttdashboard.activity.MainActivityHello");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return;
        }
        if (dataBean.getMsgData().equals("进入聊天界面")){
            // 跳转
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.sdses.mqtthead", "com.ravendmaster.linearmqttdashboard.activity.ChatActivity");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }

        /*if (dataBean.getMsgData().equals("显示二维码")){
            // 跳转
            Intent intent = new Intent();
            ComponentName cn = new ComponentName("com.sdses.mqtthead", "com.ravendmaster.linearmqttdashboard.activity.ImageActivity");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }*/

        String amqType = jsonDataAmqPlus.getData().getMsgType();
        Log.d(TAG, "json解析的内容类型： " + amqType);
        String amqStr = jsonDataAmqPlus.getData().getMsgData();
        Log.d(TAG, "json解析的内容数据为： " + amqStr);

        Message content = MainActivityHello.handler.obtainMessage();
        content.obj = amqType + "#" + amqStr;
        content.what = 2;
        MainActivityHello.handler.sendMessage(content);

    }



    public static void parseChatJson(String message) {

        if (message.equals("0") || message.equals("#55ff55")){
            return;
        }

        Gson gson = new Gson();
        JsonDataAmq jsonDataAmq = gson.fromJson(message, JsonDataAmq.class);
        String amqStr = jsonDataAmq.getmsgData();
        Log.d(TAG, "json解析的数据内容: " + amqStr);
        int amqType = jsonDataAmq.getMsgType();
        Log.d(TAG, "json解析的数据类型: " + amqType);
//        ChatActivity.msgList.add(jsonDataAmq);

        Message content = MainActivityHello.handler.obtainMessage();
        content.obj = amqType + "#" + amqStr;
        content.what = 2;
        MainActivityHello.handler.sendMessage(content);

        /*ChatActivity.adapter.notifyDataSetChanged();
        ChatActivity.lv.setSelection(ChatActivity.msgList.size());*/
    }
}
