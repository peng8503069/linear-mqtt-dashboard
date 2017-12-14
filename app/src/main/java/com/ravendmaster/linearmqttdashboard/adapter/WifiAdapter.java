package com.ravendmaster.linearmqttdashboard.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdses.mqtthead.R;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 */

public class WifiAdapter extends BaseAdapter {


    LayoutInflater inflater;
    List<ScanResult> list;
    Context mContext = null;

    public WifiAdapter(Context context, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        view = inflater.inflate(R.layout.item_wifi_list, null);
        ScanResult scanResult = list.get(position);
        TextView textView = (TextView) view.findViewById(R.id.tv_name);
        textView.setText(scanResult.SSID);
        TextView signalStrenth = (TextView) view.findViewById(R.id.tv_state);
        signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
        ImageView imageView = (ImageView) view.findViewById(R.id.signal_strenth);
        //判断信号强度，显示对应的指示图标
        if (Math.abs(scanResult.level) > 100) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ap_lock_weak));
        } else if (Math.abs(scanResult.level) > 80) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ap_lock_weak));
        } else if (Math.abs(scanResult.level) > 70) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ap_lock_weak));
        } else if (Math.abs(scanResult.level) > 60) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ap_lock_ok));
        } else if (Math.abs(scanResult.level) > 50) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ap_lock_ok));
        } else {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ap_lock_max));
        }
        return view;
    }

}
