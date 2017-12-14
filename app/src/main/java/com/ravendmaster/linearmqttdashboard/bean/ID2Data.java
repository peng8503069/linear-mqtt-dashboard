package com.ravendmaster.linearmqttdashboard.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import android.util.Log;

/**
 * ����֤��Ϣ��װ��
 * @author meng
 * @version	1.0
 */
public class ID2Data extends ID2DataRAW implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TAG = ID2Data.class.getSimpleName();
	private ID2Txt mID2Txt;
	private ID2PicNoLic mID2Pic;
	private ID2FP mID2FP;
	private String mID2NewAddress;

	private boolean mHaveFP = false;


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mID2Txt.toString();
	}


	public ID2Data() {
		Log.i(TAG, "ID2Data ���캯��");
		mID2Txt = new ID2Txt();
		mID2Pic = new ID2PicNoLic();
		mID2FP = new ID2FP();
	}


	/**
	 * ������֤ԭʼ��������װ��
	 * @return	1װ���ɹ�
	 * 			0װ��ʧ��
	 */
	public int rePackage() {
		mID2Txt.decode(this.getID2TxtRAW());
		mID2Pic.decodeNohLic(this.getID2PicRAW());
		mHaveFP = mID2FP.initFP(this.getID2FPRAW());
		try {
			mID2NewAddress = this.getID2AddRAW() == null ? null : new String(
					this.getID2AddRAW(), 3, 70, "UTF-16LE");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * ��ȡ׷�ӵ�ַ
	 * @return	�����׷�ӵ�ַ�򷵻�String���͵�׷�ӵ�ַ������15λ��û��׷�ӵ�ַ�򷵻�null��
	 */
	public String getID2NewAddress() {
		return mID2NewAddress;
	}

	/**
	 * ��ȡһ������֤�ı��ķ�װ��
	 * @return	ID2Txt���͵Ķ���
	 */
	public ID2Txt getID2Txt() {
		return mID2Txt;
	}

	/**
	 * ��ȡһ������֤ͷ��ķ�װ��
	 * @return	ID2Pic���͵Ķ���
	 */
	public ID2PicNoLic getID2Pic() {
		return mID2Pic;
	}

	/**
	 * ��ȡָ�Ʒ�װ����
	 * @return	ID2FP���͵Ķ���,���û��ָ���򷵻�null��
	 */
	public ID2FP getID2FP() {
		if (mHaveFP)
			return mID2FP;
		else
			return null;
	}

}
