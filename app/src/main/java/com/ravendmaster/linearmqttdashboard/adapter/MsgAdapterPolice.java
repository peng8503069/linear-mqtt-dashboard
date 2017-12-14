package com.ravendmaster.linearmqttdashboard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ravendmaster.linearmqttdashboard.bean.JsonDataAmq;
import com.sdses.mqtthead.R;

import java.util.List;

public class MsgAdapterPolice extends ArrayAdapter<JsonDataAmq> {

    private static final String TAG = "sdses_MsgAdapter";

    private int resourceId;

    LinearLayout leftLayout;

    LinearLayout rightLayout;

    ImageView rightImage;

    ImageView leftImage;

    ImageView ivPic;

    View leftView;

    View rightView;

    TextView leftMsg;

    TextView rightMsg;

    public MsgAdapterPolice(Context context, int textViewResourceId, List<JsonDataAmq> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JsonDataAmq msg = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);

        leftMsg = (TextView) view.findViewById(R.id.left_msg);
        leftImage = (ImageView) view.findViewById(R.id.left_image);
        ivPic = (ImageView) view.findViewById(R.id.iv_pic);

        rightMsg = (TextView) view.findViewById(R.id.right_msg);
        rightImage = (ImageView) view.findViewById(R.id.right_image);

        Log.d(TAG, "position: " + position + "  msgData: " + msg.getmsgData());
        if (msg.getMsgType() == JsonDataAmq.TYPE_ROBOT) {
            leftLayout.setVisibility(View.VISIBLE);
            rightLayout.setVisibility(View.GONE);
            if (msg.getmsgData().contains("二维码")) {
                leftMsg.setVisibility(View.GONE);
                ivPic.setImageResource(R.mipmap.erwei);
            } else {
                ivPic.setVisibility(View.GONE);
                leftMsg.setText(msg.getmsgData());
            }
            leftImage.setImageResource(R.mipmap.chat_robot);
        } else if (msg.getMsgType() == JsonDataAmq.TYPE_SPEAK) {
            rightLayout.setVisibility(View.VISIBLE);
            leftLayout.setVisibility(View.GONE);
            rightMsg.setText(msg.getmsgData());
            rightImage.setImageResource(R.mipmap.chat_man);
        }
        return view;
    }

}
