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
import com.sdses.mqtthead.R;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataAmq;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<JsonDataAmq> {

	private static final String TAG = "sdses_MsgAdapter";

	private int resourceId;

	public MsgAdapter(Context context, int textViewResourceId, List<JsonDataAmq> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		JsonDataAmq msg = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);

			viewHolder = new ViewHolder();
			viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
			viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);

			viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
			viewHolder.leftImage = (ImageView) view.findViewById(R.id.left_image);
			viewHolder.ivPic = (ImageView) view.findViewById(R.id.iv_pic);

			viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
			viewHolder.rightImage = (ImageView) view.findViewById(R.id.right_image);

			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.d(TAG, "position: " + position + "  msgData: " + msg.getmsgData());
		if (msg.getMsgType() == JsonDataAmq.TYPE_ROBOT) {
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			if (msg.getmsgData().contains("二维码")){
				viewHolder.leftMsg.setVisibility(View.GONE);
				viewHolder.ivPic.setImageResource(R.mipmap.erwei);
			}else {
				viewHolder.ivPic.setVisibility(View.GONE);
				viewHolder.leftMsg.setText(msg.getmsgData());
			}
            viewHolder.leftImage.setImageResource(R.mipmap.chat_robot);
		} else if(msg.getMsgType() == JsonDataAmq.TYPE_SPEAK) {
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			viewHolder.rightMsg.setText(msg.getmsgData());
            viewHolder.rightImage.setImageResource(R.mipmap.chat_man);
		}
		return view;
	}

	class ViewHolder {
		
		LinearLayout leftLayout;
		
		LinearLayout rightLayout;

		ImageView rightImage;

		ImageView leftImage;

		ImageView ivPic;

		View leftView ;

		View rightView;
		
		TextView leftMsg;
		
		TextView rightMsg;
		
	}

}
