package com.ravendmaster.linearmqttdashboard.bean;

import java.io.Serializable;
import java.util.Arrays;

import android.util.Log;
/**
 * ����֤ԭʼ����	256�ֽ����֡�1024�ֽ�wlt��1024ָ�ơ�70�ֽ�׷�ӵ�ַ
 * @author meng
 * @version	1.0
 */
public class ID2DataRAW implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String TAG = ID2DataRAW.class.getSimpleName();
	private byte[] mID2TxtRAW = new byte[256];	//����ԭʼ����
	private byte[] mID2PicRAW = new byte[1024];	//ͷ��ԭʼ����
	private byte[] mID2FPRAW = new byte[1024];	//ָ��ԭʼ����
	private byte[] mID2AddRAW = new byte[3+70];	//׷�ӵ�ַԭʼ���� +00 00 90
	public ID2DataRAW() {
		super();
		Log.i(TAG,"ID2DataRAW ���캯��");
	}

	/**
	 * ��ʼ������ԭʼ����
	 * @param _raw	�����ӿڵĻ�����,��6+256+1024+1024+1024�ֽ�
	 */
	public void decode(byte[] _raw){
		clearID2DataRAW();
		//��������
		if(_raw[0]==(byte)0x01&&_raw[1]==(byte)0x00){
			System.arraycopy(_raw, 6, mID2TxtRAW, 0, 256);
//			Log.i(TAG,"��������");
		}
		//��Ƭ����
		if(_raw[2]==(byte)0x04&&_raw[3]==(byte)0x00){
			System.arraycopy(_raw, 6+256, mID2PicRAW, 0, 1024);
//			Log.i(TAG,"��Ƭ����");
		}
		//ָ������
		if(_raw[4]==(byte)0x04&&_raw[5]==(byte)0x00&&_raw[6+256+1024]=='C'){
			System.arraycopy(_raw, 6+256+1024, mID2FPRAW, 0, 1024);
//			Log.i(TAG,"ָ������");
		}
//		//׷�ӵ�ַ����
//		if(_raw[6+256+1024+1024+0]==(byte)0x00
//				&&_raw[6+256+1024+1024+1]==(byte)0x00
//				&&_raw[6+256+1024+1024+2]==(byte)0x90){
//			System.arraycopy(_raw, 6+256+1024+1024, mID2AddRAW, 0, 73);
////			Log.i(TAG,"׷�ӵ�ַ����");
//		}
	}

	/**
	 * ��ʼ��1280�ֽڶ���֤����,����ʱʹ�á�
	 * @param _raw	256+1024�ֽڵ�������Ϣ��wlt����
	 */
	public void decode_debug(byte[] _raw) {
		// ��������
		System.arraycopy(_raw, 0, mID2TxtRAW, 0, 256);
		// ��Ƭ����
		System.arraycopy(_raw, 0 + 256, mID2PicRAW, 0, 1024);
	}

	/**
	 * ��ʼ������ԭʼ����
	 */
	private void clearID2DataRAW(){
		Arrays.fill(mID2TxtRAW, (byte)0x00);
		Arrays.fill(mID2PicRAW, (byte)0x00);
		Arrays.fill(mID2FPRAW, (byte)0x00);
		Arrays.fill(mID2AddRAW, (byte)0x00);
	}

	/**
	 * ��ȡָ������
	 * @return	�����ָ���򷵻�1024�ֽ�ָ�����ݣ����򷵻�null��
	 */
	public byte[] getID2FPRAW() {
		if(mID2FPRAW[0]=='C'){
//			Log.i(TAG,"��ָ������");
			return mID2FPRAW;
		}
		else{
//			Log.i(TAG,"δ����ָ������");
			return null;
		}
	}
	/**
	 * ��ȡ׷�ӵ�ַ
	 * @return	�����׷�ӵ�ַ�򷵻�׷�ӵ�ַ�����򷵻�null��
	 */
	public byte[] getID2AddRAW(){
		if((mID2AddRAW[0]==(byte)0x00)
				&&(mID2AddRAW[1]==(byte)0x00)
				&&(mID2AddRAW[2]==(byte)0x90))
		{
//			Log.i(TAG,"׷�ӵ�ַУ��ͨ��");
			return mID2AddRAW;
		}
		else{
//			Log.i(TAG,"");
			return null;
		}
	}


	/**
	 * ��ȡ������Ϣ
	 * @return	��ȡ256�ֽ����ֵ�ԭʼ����
	 */
	public byte[] getID2TxtRAW() {
		return mID2TxtRAW;

	}

	/**
	 * ��ȡ����֤wlt����
	 * @return	��ȡ1024�ֽڵ�wltԭʼ����
	 */
	public byte[] getID2PicRAW() {
		return mID2PicRAW;
	}
}