package android_serialport_api;

/**
 * Created by Administrator on 2017/5/11.
 */

import com.ravendmaster.linearmqttdashboard.utils.DataUtils;
import com.ravendmaster.linearmqttdashboard.utils.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送消息的线程。。。
 */
public class SendMsgThread extends Thread {

        private static final String TAG = "SendMsgThread";

        private volatile boolean flag = true;

        private FileOutputStream mFileOutputStream;

        private List<byte[]> sendPools = new ArrayList<>(); // 发送数据池

        public SendMsgThread(FileOutputStream mFileOutputStream) {
                this.mFileOutputStream = mFileOutputStream;
        }

        @Override
        public void run() {
                while (flag) {
                        synchronized (sendPools) {
                                if (sendPools.size() > 0) {
                                        byte[] data = sendPools.remove(0);
                                        write(data);
                                        try {
                                                sleep(80);
                                        }
                                        catch (InterruptedException e) {
                                                Logger.d(TAG, "InterruptedException00====" + e.toString());
                                                e.printStackTrace();
                                        }
                                }
                                else {
                                        try {
                                                sendPools.wait();
                                        }
                                        catch (InterruptedException e) {
                                                Logger.d(TAG, "InterruptedException====" + e.toString());
                                                e.printStackTrace();
                                        }
                                }
                        }
                }
        }

        /**
         * 停止线程
         */
        public void stopThread() {
                this.flag = false;
        }

        /**
         * 将数据添加到发送数据池
         *
         * @param data
         */
        public void addToSendPool(byte[] data) {
                synchronized (sendPools) {
                        sendPools.add(data);
                        sendPools.notify();
                }
        }

        /**
         * 发送消息
         */
        public void write(byte[] data) {
                if (mFileOutputStream != null) {
                        try {
                                mFileOutputStream.write(data);
                                mFileOutputStream.flush();
                                Logger.d(TAG, "write  success===" + DataUtils.bytesToHexString(data));
                        }
                        catch (IOException e) {
                                e.printStackTrace();
                                Logger.d(TAG, "write IOException " + e.getLocalizedMessage());
                        }
                }
                else {
                        Logger.e(TAG, "write  mFileOutputStream is  null");
                }
        }

}
