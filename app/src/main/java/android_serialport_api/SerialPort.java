/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package android_serialport_api;

import com.ravendmaster.linearmqttdashboard.utils.Logger;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public abstract class SerialPort extends Thread {
	private static final String TAG = "SerialPort";
	private FileDescriptor mFd;
	private volatile boolean mStopThread = false;
	private FileInputStream mFileInputStream;
	protected FileOutputStream mFileOutputStream;
	private DataInputStream mDataInputStream;
	private SendMsgThread mSendMsgThread = null ;

	/**
	 * 鑾峰彇涓插彛杩囨潵鐨勬暟鎹�
	 */
	public abstract void getTouChuanData(DataInputStream in);

	public SerialPort(File device, int baudrate, int flags) {
		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
					Logger.e(TAG, "not root,please check ");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.e(TAG, "not root,please check ");
			}
		}
		mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (mFd == null) {
			Logger.e(TAG, "native open returns null");
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	@Override
	public void run() {
		if (mFileInputStream != null) {
			mDataInputStream = new DataInputStream(mFileInputStream);
			mSendMsgThread = new SendMsgThread(mFileOutputStream);
			mSendMsgThread.start();
			while (!mStopThread) {
				getTouChuanData(mDataInputStream);
			}
		} else {
			Logger.e(TAG, "mFileInputStream is null");
			return;
		}
	}

	/**
	 * 鍋滄绾跨▼
	 */
	public void stopThread() {
		mStopThread = true;
		if (mSendMsgThread != null){
			mSendMsgThread.stopThread();
		}
	}

	/**
	 * 鍐欐暟鎹�
	 * 
	 * @param data
	 */
	public void write(byte[] data) {
		if (mSendMsgThread != null){
				mSendMsgThread.addToSendPool(data);
		}else{
			Logger.e(TAG,"mSendMsgThread is null");
		}
//		if (mFileOutputStream != null) {
//			try {
//				mFileOutputStream.write(data);
//				mFileOutputStream.flush();
//				Logger.d(TAG, "write  success===" + Arrays.toString(data));
//			} catch (IOException e) {
//				e.printStackTrace();
//				Logger.d(TAG, "write IOException " + e.getLocalizedMessage());
//			}
//		} else {
//			Logger.e(TAG, "write  mFileOutputStream is  null");
//		}


	}

	/**
	 * 鑾峰彇妫�楠屽拰
	 * 
	 * @param data
	 * @return
	 */
	protected byte getCheckSum(byte[] data) {
		byte sum = 0;
		if (data != null) {
			for (int i = 0, len = data.length; i < len; i++) {
				sum += data[i];
			}
		}
		return sum;
	}

	/*
	 * 鎵撳紑涓插彛
	 */
	public native static FileDescriptor open(String path, int baudrate, int flags);

	/*
	 * 鍏抽棴涓插彛
	 */

	/*
	 * 鍔犺浇涓插彛so
	 */
	static {
		System.loadLibrary("serial_port");
	}
}
