package com.ravendmaster.linearmqttdashboard.listener;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.AdapterView;

import com.ravendmaster.linearmqttdashboard.activity.WifiSetting;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class WifiClickListener implements AdapterView.OnItemClickListener {

    List<ScanResult> wifiList = null;
    Context mContext;

    public WifiClickListener(Context context, List<ScanResult> list){
        mContext = context;
        wifiList = list;
    }

    public interface TransSSIDData
    {
        void transSSIDData(String wifiName);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ScanResult scanResult = wifiList.get(position);
        TransSSIDData transSSIDData = (TransSSIDData)mContext;
        String ssid = scanResult.SSID;
        transSSIDData.transSSIDData(ssid);
        WifiSetting.etName.setText("");
        WifiSetting.alertView.show();
    }

}
