package com.ravendmaster.linearmqttdashboard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ravendmaster.linearmqttdashboard.Log;
import com.ravendmaster.linearmqttdashboard.music.Player;
import com.sdses.mqtthead.R;
import com.ravendmaster.linearmqttdashboard.Utilites;
import com.ravendmaster.linearmqttdashboard.customview.MyColors;
import com.ravendmaster.linearmqttdashboard.customview.MyTabsController;
import com.ravendmaster.linearmqttdashboard.customview.RGBLEDView;
import com.ravendmaster.linearmqttdashboard.service.AppSettings;
import com.ravendmaster.linearmqttdashboard.service.MQTTService;
import com.ravendmaster.linearmqttdashboard.service.Presenter;
import com.ravendmaster.linearmqttdashboard.service.WidgetData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class MainActivity extends AppCompatActivity implements Presenter.IView{

    public static Player player = new Player();

    public MainActivity() {
        super();
        instance = this;
        android.util.Log.d(TAG, "MainActivity: 创建了Presenter对象");
        presenter = new Presenter(this);
    }

    private static final String TAG = "sdses_MainActivity";

    public static Presenter presenter;

    public static MainActivity instance;

    public static DisplayMetrics mDisplayMetrics;

    MyTabsController tabsController;

    public final int ConnectionSettings_CHANGE = 0;

    public final int Tabs = 4;

    MenuItem menuItem_add_new_widget;
    MenuItem menuItem_clear_dashboard;

    RGBLEDView mqttBrokerStatusRGBLEDView;
    RGBLEDView connectionStatusRGBLEDView;

    Menu optionsMenu;

    public enum DASHBOARD_VIEW_MODE {
        SIMPLE,
        COMPACT
    }

    DASHBOARD_VIEW_MODE mDashboardViewMode;

    public DASHBOARD_VIEW_MODE getDashboardViewMode() {
        if (mDashboardViewMode == null) {

            SharedPreferences sprefs = getPreferences(MODE_PRIVATE);
            String dashboard_view_mode_text = sprefs.getString("dashboard_view_mode", "");
            switch (dashboard_view_mode_text) {
                case "simple":
                    mDashboardViewMode = DASHBOARD_VIEW_MODE.SIMPLE;
                    break;
                case "compact":
                    mDashboardViewMode = DASHBOARD_VIEW_MODE.COMPACT;
                    break;
            }

        }
        return mDashboardViewMode;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        menuItem_add_new_widget = menu.findItem(R.id.Add_new_widget);
        menuItem_clear_dashboard = menu.findItem(R.id.Clean_dashboard);


        updatePlayPauseMenuItem();

        AppSettings appSettings = AppSettings.getInstance();
        if (appSettings.server == null) return true;
        if (appSettings.server.equals("ravend.asuscomm.com")) {
            optionsMenu.findItem(R.id.request_prices).setVisible(true);
            optionsMenu.findItem(R.id.action_board).setVisible(true);
            optionsMenu.findItem(R.id.action_lists).setVisible(true);
        }

        getDashboardViewMode();

        return super.onCreateOptionsMenu(menu);
    }


    void updatePlayPauseMenuItem() {

        MenuItem menuItemPlayPause = optionsMenu.findItem(R.id.Edit_play_mode);

        MenuItem menuItemAutoCreateWidgets = optionsMenu.findItem(R.id.auto_create_widgets);

        if (presenter.isEditMode()) {
            menuItemPlayPause.setIcon(R.drawable.ic_play);
            menuItemAutoCreateWidgets.setVisible(presenter.getUnusedTopics().length > 0);

        } else {
            menuItemPlayPause.setIcon(R.drawable.ic_pause);
            menuItemAutoCreateWidgets.setVisible(false);
        }

        menuItem_add_new_widget.setVisible(presenter.isEditMode());
        menuItem_clear_dashboard.setVisible(presenter.isEditMode());

    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        presenter.onMainMenuItemSelected();

        switch (item.getItemId()) {

           case R.id.connection_settings:
                Intent intent = new Intent(this, ConnectionSettingsActivity.class);
                startActivityForResult(intent, ConnectionSettings_CHANGE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void refreshTabState() {
        tabsController.refreshState(this);
    }


    void shareSettings() {
        File file = createExternalStoragePublicDownloads();
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        this.startActivity(Intent.createChooser(emailIntent, "Share settings..."));
    }

    File createExternalStoragePublicDownloads() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, "config.linear");

        try {
            path.mkdirs();
            OutputStream os_ = new FileOutputStream(file);
            ZipOutputStream os = new ZipOutputStream(new BufferedOutputStream(os_));

            String allSettings = AppSettings.getInstance().getSettingsAsString();
            os.putNextEntry(new ZipEntry("settings"));
            buff = Utilites.stringToBytesUTFCustom(allSettings);
            os.flush();
            os.write(buff);
            os.close();


        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Tabs) {
            refreshTabState();
        }

        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    void refreshDashboard(boolean hard_mode) {
        if (presenter.isEditMode() && !hard_mode) return;
    }

    @Override
    public void onRefreshDashboard() {
        refreshDashboard(true);
    }

    @Override
    public void notifyPayloadOfWidgetChanged(int screenTabIndex, int widgetIndex) {
    }

    @Override
    public void setBrokerStatus(Presenter.CONNECTION_STATUS status) {
        mqttBrokerStatusRGBLEDView.setColorLight(getRGBLEDColorOfConnectionStatus(status));
    }

    @Override
    public void setNetworkStatus(Presenter.CONNECTION_STATUS status) {
        connectionStatusRGBLEDView.setColorLight(getRGBLEDColorOfConnectionStatus(status));
    }

    @Override
    public void onOpenValueSendMessageDialog(WidgetData widgetData) {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.new_value_send_dialog, null);
        TextView nameView = (TextView) promptsView.findViewById(R.id.textView_name);
        nameView.setText(widgetData.getName(0));


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set new_value_send_dialogue_send_dialog.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setText(presenter.getMQTTCurrentValue(widgetData.getSubTopic(0)).replace("*", ""));

        if (widgetData.type == WidgetData.WidgetTypes.VALUE && widgetData.mode == 1) {
            userInput.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL | EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
        }


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                presenter.sendMessageNewValue(userInput.getText().toString());
                                refreshDashboard(true);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onTabSelected() {

        refreshDashboard(true);

    }

    @Override
    public void showPopUpMessage(String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(title);
        ad.setMessage(text);
        ad.show();
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    int getRGBLEDColorOfConnectionStatus(Presenter.CONNECTION_STATUS status) {
        switch (status) {
            case DISCONNECTED:
                return MyColors.getRed();
            case IN_PROGRESS:
                return MyColors.getYellow();
            case CONNECTED:
                return MyColors.getGreen();
            default:
                return 0;
        }
    }


    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");

        /*presenter.onPause();*/
        super.onPause();
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        presenter.onResume(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d(TAG, "MainActivity 调用 onCreate()");
        // V7包中AppCompatActivity原因，使用requestWindowFeature(Window.FEATURE_NO_TITLE)代码无效
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideBottomUIMenu();
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            inputFileAlreadyPrecessed = false;
        }

        // 创建了 Presenter 对象
        presenter.onCreate(this);

        if (presenter.getDashboards() == null) {

            android.util.Log.d(TAG, "onCreate presenter.getDashboards() == null : 创建了启动MQTTService的意图并启动MQTTService");
            Intent service_intent = new Intent(this, MQTTService.class);
            service_intent.setAction("autostart");
            startService(service_intent);

            android.util.Log.d(TAG, "onCreate presenter.getDashboards() == null : MainActivity中调用了finish()方法");
            finish();
            android.util.Log.d(TAG, "onCreate presenter.getDashboards() == null : MainActivity销毁被重新创建");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return;
        }

        android.util.Log.d(TAG, "onCreate presenter.getDashboards()!= null : MainActivity类获取屏幕尺寸");
        mDisplayMetrics = getResources().getDisplayMetrics();

        mqttBrokerStatusRGBLEDView = (RGBLEDView) findViewById(R.id.mqtt_broker_status_RGBLed);
        connectionStatusRGBLEDView = (RGBLEDView) findViewById(R.id.connection_status_RGBLed);

        //import settings
        checkInputFileAndProcess();

        try {
            // 返回到桌面
            Thread.sleep(1000);
            Intent hello = new Intent(this, MainActivityHello.class);
            startActivity(hello);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    static byte buff[];
    static String result;
    static boolean inputFileAlreadyPrecessed;

    void checkInputFileAndProcess() {
        if (inputFileAlreadyPrecessed) return;
        //inputFileAlreadyPrecessed=true;
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null && action.equals("android.intent.action.VIEW")) {
            Uri data = intent.getData();
            try {
                InputStream is_ = getContentResolver().openInputStream(data);
                ZipInputStream is = new ZipInputStream(new BufferedInputStream(is_));

                ZipEntry entry;
                while ((entry = is.getNextEntry()) != null) {

                    ByteArrayOutputStream os = new ByteArrayOutputStream();


                    byte[] buff = new byte[1024];
                    int count;
                    while ((count = is.read(buff, 0, 1024)) != -1) {
                        os.write(buff, 0, count);
                    }
                    os.flush();
                    os.close();

                    result = Utilites.bytesToStringUTFCustom(os.toByteArray(), os.toByteArray().length);


                    AlertDialog.Builder ad = new AlertDialog.Builder(this);
                    ad.setTitle("Import settings");  // заголовок
                    ad.setMessage("All settings (except password) will be replaced. Continue?"); // сообщение
                    ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            inputFileAlreadyPrecessed = true;

                            AppSettings settings = AppSettings.getInstance();
                            settings.setSettingsFromString(result);

                            settings.saveTabsSettingsToPrefs(MainActivity.instance);
                            settings.saveConnectionSettingsToPrefs(MainActivity.instance);

                            presenter.createDashboardsBySettings();
                            MainActivity.presenter.saveAllDashboards(getApplicationContext());

                            MainActivity.presenter.connectionSettingsChanged();

                            presenter.onTabPressed(0);
                            refreshTabState();
                        }
                    });
                    ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            inputFileAlreadyPrecessed = true;
                        }
                    });
                    ad.show();
                }
                is.close();
                is_.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeAccepted) {
                    Log.d(TAG, "perm OK");
                    shareSettings();
                } else {
                    Log.d(TAG, "perm forbidden");
                }
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        presenter.onDestroy(this);
        saveDashboardViewMode();

    }

    void saveDashboardViewMode() {
        SharedPreferences sprefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sprefs.edit();
        ed.putString("dashboard_view_mode", mDashboardViewMode == DASHBOARD_VIEW_MODE.SIMPLE ? "simple" : "compact");
        if (!ed.commit()) {
            Log.d(TAG, "dashboard_view_mode commit failure!!!");
        }
    }

}
