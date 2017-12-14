package com.ravendmaster.linearmqttdashboard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ravendmaster.linearmqttdashboard.utils.MsgSendUtils;
import com.ravendmaster.linearmqttdashboard.utils.MsgType;

/**
 * 人社厅展示，接收第三方广播，并做处理
 */
public class InsuranceReceiver extends BroadcastReceiver {

    private static final String TAG = "sdses_Insurance";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action.equals("com.sdses.insurance.speaktts")){
            MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, intent.getStringExtra("tts"));
            Log.d(TAG, "onReceive: 收到的语音内容： " + intent.getStringExtra("tts"));
        }

    }
}
