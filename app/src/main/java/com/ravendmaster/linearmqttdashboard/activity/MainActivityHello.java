package com.ravendmaster.linearmqttdashboard.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ravendmaster.linearmqttdashboard.bean.JsonDataAmq;
import com.ravendmaster.linearmqttdashboard.utils.MsgSendUtils;
import com.ravendmaster.linearmqttdashboard.utils.MsgType;
import com.sdses.mqtthead.R;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.ReceiveData;
import pl.droidsonroids.gif.GifImageView;

public class MainActivityHello extends AppCompatActivity {

    private static final String TAG = "sdses_MainHello";
    public static List<JsonDataAmq> msgList = new ArrayList<JsonDataAmq>();
    ImageView imageView;
    //    GifView gifView = null;
    GifImageView givHello = null;
    static Context mContext;
    public static long currentTime;

    public static final int TOAST_TEXT = 0;
    float x1;
    float x2;
    float y1;
    float y2;

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: // 发送输入框内请求
                    String content = (String) msg.obj;
                    String[] contents = content.split("#");
                    JsonDataAmq msg1 = new JsonDataAmq(JsonDataAmq.TYPE_SPEAK, contents[0]);
                    MainActivityHello.msgList.add(msg1);
                    if (contents.length == 2) {
                        JsonDataAmq msg2 = new JsonDataAmq(JsonDataAmq.TYPE_ROBOT, contents[1]);
                        MainActivityHello.msgList.add(msg2);
                    } else {
                        JsonDataAmq msg2 = new JsonDataAmq(JsonDataAmq.TYPE_ROBOT, "");
                        MainActivityHello.msgList.add(msg2);
                    }
                    ChatActivity.adapter.notifyDataSetChanged();
                    ChatActivity.lv.setSelection(MainActivityHello.msgList.size());
//                    ChatActivity.adapter.notifyDataSetChanged();
                    break;
                case 2:
                    String chat = (String) msg.obj;
                    String[] chats = chat.split("#");
                    String answer = "";
                    if (chats.length == 2) {
                        answer = answer + chats[1];
                    }
                    JsonDataAmq msg3 = new JsonDataAmq(Integer.parseInt(chats[0]), answer);
                    if(MainActivityHello.msgList == null && ChatActivity.adapter == null && ChatActivity.lv == null){
                        Log.d(TAG, "handleMessage: 空指针");
                    }else {
                        MainActivityHello.msgList.add(msg3);
                        ChatActivity.adapter.notifyDataSetChanged();
                        ReceiveData.speak = true; // 机器人正在说话
                        currentTime = System.currentTimeMillis();
                        ChatActivity.lv.setSelection(MainActivityHello.msgList.size());
                    }
                    break;
                case 4:
                    if (ChatActivity.dismiss) {
                        ChatActivity.alertView.dismiss();
                        Log.d(TAG, "handleMessage: 密码框取消");
                    }
                    break;
                case 5:
                    ChatActivity.etName.setText("");
                    ChatActivity.alertView.show(); // 创建并弹出密码框
                    ChatActivity.dismiss = true;
                    break;
                case 6:
                    showCompare(); //比对界面
                    ReceiveData.speak = true;
//                    activeRobot(); // 唤醒机器人
                case 7:
//                    ChatActivity.cleanPersonData(); // 清楚用户信息
                    MsgSendUtils.sendStringMsg(MsgType.NO_ACTIVE, "休眠机器人"); // 休眠机器人
                    Intent intentMian = new Intent(mContext, MainActivityHello.class);
                    intentMian.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentMian); // 启动欢迎界面
                    break;
                case 8:
                    MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "您好，有什么需要帮助您的？"); //唤醒机器人
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MsgSendUtils.sendStringMsg(MsgType.ACTIVE, "唤醒机器人"); //唤醒机器人
                    break;
                case 9:
                    long updateTime = System.currentTimeMillis();
                    long time = (updateTime - currentTime) / 1000; // 得到秒数
                    Log.d(TAG, "handleMessage: " + time);
                    if (time > 30){
                        // 启动欢迎界面
                        showHello();
                        ReceiveData.speak = false;
                    }
                    break;
                case 10:
                    MsgSendUtils.sendStringMsg(MsgType.ACTIVE, "唤醒机器人"); //唤醒机器人
                    break;
                default:
                    Log.d("main", "what错误");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // V7包中AppCompatActivity原因，使用requestWindowFeature(Window.FEATURE_NO_TITLE)代码无效
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideBottomUIMenu();
        setContentView(R.layout.activity_main_hello);
        mContext = this;

        // 串口接收数据
        new ReceiveData(mContext);

        givHello = (GifImageView) findViewById(R.id.giv_hello);

        givHello.setImageResource(R.drawable.hello_seri_big_size);  // 请对我说：你好小神
//        givHello.setImageResource(R.drawable.hello_seri_police_update);
        currentTime = System.currentTimeMillis();
        Log.d(TAG, "onCreate: 当前时间为 " + currentTime);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (y1 - y2 > 50) {
                Toast.makeText(MainActivityHello.this, "向上滑", Toast.LENGTH_SHORT).show();
            } else if (y2 - y1 > 50) {
                Toast.makeText(MainActivityHello.this, "向下滑", Toast.LENGTH_SHORT).show();
            } else if (x1 - x2 > 50) {
                ReceiveData.speak = true;
                startActivity(new Intent(this, WifiSetting.class));
                Toast.makeText(MainActivityHello.this, "向左滑", Toast.LENGTH_SHORT).show();
            } else if (x2 - x1 > 50) {
                Toast.makeText(MainActivityHello.this, "向右滑", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideBottomUIMenu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideBottomUIMenu();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ReceiveData.speak = false;
        hideBottomUIMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReceiveData.speak = false;
        hideBottomUIMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void showHello(){
        Log.d(TAG, "showHello: 收到定时任务，未说话超过30s，进入休眠状态");
//        MsgSendUtils.sendStringMsg(MsgType.NO_ACTIVE, "休眠机器人"); //休眠机器人
        Intent intentCom = new Intent(mContext, MainActivityHello.class);
        intentCom.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intentCom); // 欢迎界面
        return;
    }

    public static void showCompare() {
        MsgSendUtils.sendStringMsg(MsgType.NO_ACTIVE, "休眠机器人"); //休眠机器人
        MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "您好"); //休眠机器人
        Log.d(TAG, "showCompare: 检测到需要启动人脸识别，进入比对页面");
        Intent intentCom = new Intent();
        ComponentName componentName = new ComponentName("com.sdses.mqtthead",
                "com.ravendmaster.linearmqttdashboard.activity.CompareActivity");
        intentCom.setComponent(componentName);
//        intentCom.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intentCom); // 启动比对界面
        return;
    }

    public static void activeRobot() {
        MsgSendUtils.sendStringMsg(MsgType.ACTIVE, "唤醒机器人"); //休眠机器人
    }


    public void chatDemo(View view) {
        startActivity(new Intent(this, ChatActivity.class));
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
