package com.ravendmaster.linearmqttdashboard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sdses.mqtthead.R;
import com.ravendmaster.linearmqttdashboard.service.AppSettings;

public class ConnectionSettingsActivity extends AppCompatActivity {

    EditText server;
    EditText port;
    EditText username;
    EditText password;
    EditText server_topic;
    EditText push_notifications_subscribe_topic;
    //CheckBox notifications_service;
    CheckBox connection_in_background;
    CheckBox server_mode;
    private static final String TAG = "sdses_SettingsActiv";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_editor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean validateUrl(String adress) {
        if(adress.endsWith(".xyz"))return true;
        return Patterns.DOMAIN_NAME.matcher(adress).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Save:  // 点击了APP Setting设置的保存


                if (!validateUrl(server.getText().toString())) {
                    //Toast.makeText(getApplicationContext(), "Server address is incorrect!", Toast.LENGTH_SHORT).show();
                    //return false;
                }

                AppSettings settings = AppSettings.getInstance();  // 获取AppSettings对象，使用单例设计模式
                // 将页面中的设置内容保存到AppSettings对象中
                settings.server = server.getText().toString();  // MQTT服务器地址
                settings.port = port.getText().toString();    // MQTT服务器端口
                settings.username = username.getText().toString();   // 连接服务器的用户名
                settings.password = password.getText().toString();   // 连接服务器的密码
                settings.server_topic = server_topic.getText().toString();  // TODO server_topic未使用
                settings.push_notifications_subscribe_topic = push_notifications_subscribe_topic.getText().toString(); // 订阅的主题 ---- 至关重要
                settings.connection_in_background = connection_in_background.isChecked();  // 后台是否可接收到订阅消息，并进行通知消息
                settings.server_mode = server_mode.isChecked();

                // APP Setting设置页面保存信息到本地
                settings.saveConnectionSettingsToPrefs(this);

                //MainActivity.presenter.restartService(this);
                if(MainActivity.presenter != null) { // TODO 作用是什么
                    Log.d(TAG, "onOptionsItemSelected: MainActivity.presenter != null");
                    MainActivity.presenter.connectionSettingsChanged();
                }

                finish();

                //MainActivity.connectToMQTTServer(getApplicationContext());
                MainActivity.presenter.resetCurrentSessionTopicList();

                MainActivity.presenter.subscribeToAllTopicsInDashboards(settings);
                break;
        }
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        server = (EditText) findViewById(R.id.editText_server);
        port = (EditText) findViewById(R.id.editText_port);
        username = (EditText) findViewById(R.id.editText_username);
        password = (EditText) findViewById(R.id.editText_password);
        server_topic = (EditText) findViewById(R.id.editText_server_topic);
        push_notifications_subscribe_topic = (EditText) findViewById(R.id.editText_push_notifications_subscribe_topic);
        //notifications_service = (CheckBox) findViewById(R.id.checkBox_start_notifications_service);
        connection_in_background = (CheckBox) findViewById(R.id.checkBox_connection_in_background);
        server_mode = (CheckBox) findViewById(R.id.checkBox_server_mode);

        AppSettings settings = AppSettings.getInstance();
        server.setText(settings.server);
        port.setText(settings.port);
        username.setText(settings.username);
        password.setText(settings.password);
        server_topic.setText(settings.server_topic);
        push_notifications_subscribe_topic.setText(settings.push_notifications_subscribe_topic);
        connection_in_background.setChecked(settings.connection_in_background);
        server_mode.setChecked(settings.server_mode);

    }
}
