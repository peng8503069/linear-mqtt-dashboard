package com.ravendmaster.linearmqttdashboard.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 该类包含了指纹的基本信息
 *
 * @author  wangshuo
 * @date 2017/9/20
 *
 */
public class ID2FP implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TAG = ID2FP.class.getSimpleName();
	private byte[] mID2FPOne = new byte[512];
	private byte[] mID2FPTwo = new byte[512];

	public ID2FP() {

	}

	/**
	 * 初始化指纹原始数据
	 *
	 * @param _fingerprint	1024字节指纹原始数据
	 * @return	true	初始化成功
	 * 			false	初始化失败
	 */
	public boolean initFP(byte[] _fingerprint) {
		if (_fingerprint != null && _fingerprint.length >= 1024) {
//			Log.i(TAG, "复制指纹数据");
			System.arraycopy(_fingerprint, 0, mID2FPOne, 0, 512);
			System.arraycopy(_fingerprint, 512, mID2FPTwo, 0, 512);
			return true;
		} else {
//			Log.i(TAG, "未发现指纹数据");
			Arrays.fill(mID2FPOne, (byte) 0x00);
			Arrays.fill(mID2FPTwo, (byte) 0x00);
			return false;
		}
	}

	/**
	 * 获取第一个指纹的原始数据
	 * @return	512字节的原始数据
	 */
	public byte[] getmID2FPOne() {
		return mID2FPOne;
	}

	/**
	 * 获取第二个指纹的原始数据
	 * @return	512字节的原始数据
	 */
	public byte[] getmID2FPTwo() {
		return mID2FPTwo;
	}

	/**
	 * 获取指位情况
	 * @param _num	第几个手指
	 * @return	指位情况信息
	 */
	public String getFingerPosition(int _num) {
		if (_num == 1) {
			if (mID2FPOne[0] == 'C')
				return _getFingerPosition(mID2FPOne[5]);
		} else if (_num == 2) {
			if (mID2FPTwo[0] == 'C')
				return _getFingerPosition(mID2FPTwo[5]);
		}
		return null;
	}

	private String _getFingerPosition(byte _key) {
		String back = null;
		switch (_key) {
			case (byte) 0x0B:
				back = "右手拇指";
				break;
			case (byte) 0x0C:
				back = "右手食指";
				break;
			case (byte) 0x0D:
				back = "右手中指";
				break;
			case (byte) 0x0E:
				back = "右手环指";
				break;
			case (byte) 0x0F:
				back = "右手小指";
				break;
			case (byte) 0x10:
				back = "左手拇指";
				break;
			case (byte) 0x11:
				back = "左手食指";
				break;
			case (byte) 0x12:
				back = "左手中指";
				break;
			case (byte) 0x13:
				back = "左手环指";
				break;
			case (byte) 0x14:
				back = "左手小指";
				break;
			case (byte) 0x61:
				back = "右手不确定指位";
				break;
			case (byte) 0x62:
				back = "左手不确定指位";
				break;
			case (byte) 0x63:
				back = "其他不确定指位";
				break;
			default:
				back = null;
				break;

		}
		return back;
	}
}
