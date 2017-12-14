package com.ravendmaster.linearmqttdashboard.activity;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.ravendmaster.linearmqttdashboard.adapter.WifiAdapter;
import com.ravendmaster.linearmqttdashboard.listener.WifiClickListener;
import com.ravendmaster.linearmqttdashboard.utils.MsgSendUtils;
import com.ravendmaster.linearmqttdashboard.utils.MsgType;
import com.sdses.mqtthead.R;

import java.util.List;

import android_serialport_api.ReceiveData;

public class WifiSetting extends AppCompatActivity implements OnItemClickListener, WifiClickListener.TransSSIDData {

    private WifiManager wifiManager;
    List<ScanResult> list;
    ListView listView = null;
    WifiClickListener wifiClickListener = null;
    public static AlertView alertView = null;
    public static EditText etName = null;
    private static final String TAG = "sdses_Main";
    String wifiName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
        hideBottomUIMenu();
        setContentView(R.layout.wifi_list);
        new ReceiveData(this);
        init();
        wifiClickListener = new WifiClickListener(this, list);
        listView.setOnItemClickListener(wifiClickListener);
        alertView = new AlertView("无线密码", "请输入密码", "取消", null, new String[]{"确认"}, this, AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_pass, null);
        etName = (EditText) extView.findViewById(R.id.input_text);
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        /*// 监听焦点
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.d(TAG, "onFocusChange: 密码焦点状态： " + focus);
                ChatActivity.dismiss = !focus;
            }
        });*/
        alertView.addExtView(extView);
    }

    private void init() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        openWifi();
        list = wifiManager.getScanResults();
        listView = (ListView) findViewById(R.id.listView);
        if (list == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        } else {
            listView.setAdapter(new WifiAdapter(this, list));
        }

    }

    /**
     * 打开WIFI
     */
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
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

    @Override
    public void onItemClick(Object o, int position) {
        // 点击了确认按钮
        hideBottomUIMenu();
        if (o == alertView && position != AlertView.CANCELPOSITION) {
            String password = etName.getText().toString();
            if (TextUtils.isEmpty(password)) {
                MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "密码为空");
                alertView.dismiss(); //取消显示
            } else {
                Toast.makeText(this, wifiName + "&&" + password, Toast.LENGTH_SHORT).show();
                MsgSendUtils.sendStringMsg(MsgType.LINK_WIFI, wifiName + "&&" + password); // 连接WiFi
            }
            return;
        }
        alertView.dismiss(); //取消显示
        // 点击了取消按钮
//        Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transSSIDData(String ssid) {
        this.wifiName = ssid;
    }
}
