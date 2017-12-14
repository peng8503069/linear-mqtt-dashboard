package com.ravendmaster.linearmqttdashboard.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/10/18.
 */

public class SettingTouchListener implements View.OnTouchListener {

    float mPosX;
    float mPosY;
    float mCurPosX;
    float mCurPosY;
    Context mContext;

    public SettingTouchListener(Context context){
        this.mContext = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurPosX = event.getX();
                mCurPosY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                if (mCurPosY - mPosY > 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向下滑動

                } else if (mCurPosY - mPosY < 0
                        && (Math.abs(mCurPosY - mPosY) > 25)) {
                    //向上滑动
                }

                break;
        }
        return false;
    }



}
