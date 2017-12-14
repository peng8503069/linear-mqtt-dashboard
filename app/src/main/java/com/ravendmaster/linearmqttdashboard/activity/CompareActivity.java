package com.ravendmaster.linearmqttdashboard.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ravendmaster.linearmqttdashboard.bean.FaceCompareJson;
import com.ravendmaster.linearmqttdashboard.bean.Person;
import com.ravendmaster.linearmqttdashboard.utils.MsgSendUtils;
import com.ravendmaster.linearmqttdashboard.utils.MsgType;
import com.ravendmaster.linearmqttdashboard.utils.Utils;
import com.sdses.mqtthead.R;

import java.util.List;

import android_serialport_api.ReceiveData;

public class CompareActivity extends AppCompatActivity {

    private static final String TAG = "sdses_Compare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideBottomUIMenu();
        setContentView(R.layout.layout_compare);
        Log.d(TAG, "onCreate: 启动了比对页面");
        // 注册广播接收器
        IntentFilter intentFilter = new IntentFilter("com.sdses.face");
        registerReceiver(receiver,intentFilter);
        // 启动人脸检测/比对程序
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.example.administrator.myapplication",
                "com.example.administrator.myapplication.camera.MainActivity");
        intent.setComponent(componentName);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideBottomUIMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * ACTION: com.sdses.face
     *      data : ""
     *      flag : int 1:成功  0:失败
     * */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("com.sdses.face")) {
                Log.d(TAG, "onReceive: ");
                if(intent.getIntExtra("flag",0) == 1) { //比对成功
                    String jsonData = intent.getStringExtra("data");
                    parseJsonFromFace(jsonData); // 人脸信息
                    CompareActivity.this.finish();
                    Message helloMessage2 = MainActivityHello.handler.obtainMessage();
                    helloMessage2.what = 10;
//                    MainActivityHello.handler.sendMessageDelayed(helloMessage2, 7000);
//                    MainActivityHello.handler.sendMessageDelayed(helloMessage2, 4000);
                    MainActivityHello.handler.sendMessageDelayed(helloMessage2, 6000);
                    //成功
                    Toast.makeText(context, "人脸匹配成功", Toast.LENGTH_SHORT).show();
                } else { // 比对失败
                    CompareActivity.this.finish();
                    MsgSendUtils.sendStringMsg(MsgType.ACTIVE, "你好"); //唤醒机器人
//                    MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "您好");
                    //失败
                    Toast.makeText(context, "人脸匹配失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

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

    /**
     * 解析人脸比对的json字符串
     * @param data
     */
    public void parseJsonFromFace(String data){
        FaceCompareJson faceCompareJson = Utils.getGson().fromJson(data, FaceCompareJson.class);
        List<FaceCompareJson.ResultsBean> resultsBeenList = faceCompareJson.getResults();
        Log.d(TAG, "parseJsonFromFace: " + resultsBeenList.toString());
        if (resultsBeenList == null || resultsBeenList.isEmpty()){
            return;
        }
        if (resultsBeenList.size() >= 1){ //人脸比对成功，已注册人脸
            int gender = resultsBeenList.get(0).getGender();
            String name = resultsBeenList.get(0).getName();
            String surname = "";
            surname = name.substring(0, 1);
            String person_id = resultsBeenList.get(0).getPerson_id();
            Person person = Person.getPersonInstance();
            person.setGender(gender);
            person.setName(name);
            person.setPersonId(person_id);
            String sex = "";
            if (gender == 1){
                sex = "先生";
            }else {
                sex = "女士";
            }
//            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, surname + sex + "您好，下面让小神来为您介绍烟台智慧警务实验室");
//            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, surname + sex + "您好，请问有什么需要帮助的？");
            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, surname + sex + "您好，您可以去VIP贵宾室办理业务");

        }else { //人脸比对失败，未注册人脸
//            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "您好");
        }
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
