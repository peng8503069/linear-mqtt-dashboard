package android_serialport_api;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ravendmaster.linearmqttdashboard.bean.StringMsgBean;
import com.ravendmaster.linearmqttdashboard.utils.Logger;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 具体的串口接受器
 *
 * @author xiaowei
 */
public class FpgeThread extends SerialPort {

        private static final String TAG = "FpgaThread";

        private ReceiveDataInterface mInterface;

        public FpgeThread(File device, int baudrate, int flags) {
                super(device, baudrate, flags);
        }

        public void init(ReceiveDataInterface listener) {
                this.mInterface = listener;
        }

        @Override
        public void getTouChuanData(DataInputStream is) {
                try {
                        byte DH_Head = is.readByte();
                        Logger.d(TAG, "DH_Head====" + DH_Head);
                        if (DH_Head == MsgCon.protocol_Head_H) {
                                byte DL_Head = is.readByte();
//				Logger.e(TAG,"DL_Head===="+DL_Head);
                                if (MsgCon.protocol_Head_L == DL_Head) {
                                        byte d = is.readByte();
                                        byte c = is.readByte();
                                        int len = ((d << 8) | c) + 3;
                                        Logger.d(TAG, "len====" + len);
                                        if (len > 0) { // 有时候读取到的数据为负数
                                                byte[] data = new byte[len];
                                                is.readFully(data);
                                                Logger.d(TAG, "data====" + Arrays.toString(data));
                                                if (checkData(data)) {
                                                        byte cmd = data[0];
                                                        Logger.d(TAG, "cmd  ====" + cmd);

                                                        if (cmd == MsgCon.TransparentReceiverCmd) {
                                                                decodeTouChuanData(len, data);
                                                        }
                                                        if (cmd == MsgCon.TransparentReceiverCmd2) {
                                                                decodeTouChuanData2(len, data);
                                                        }
                                                }
                                        }
                                }
                        }

                }
                catch (IOException e) {
                        e.printStackTrace();
                        Logger.d(TAG, "IOException=" + e.toString());
                }
        }

        private void decodeTouChuanData(int len, byte[] data) {
//		byte[] content = new byte[len-4];
//		System.arraycopy(data,1,content,0,len-4);
//		String temp = new String(content);
//		Logger.e(TAG,"收到串口数据 ====  "+temp);

                byte[] content = new byte[len - 6]; //读取到的数据包括 校验和 和 协议尾，命令，，msgType
                int msgType = (data[1] * 256) + (data[2] & 0xff);
                Logger.d(TAG, "胸口msgType" + msgType);
                System.arraycopy(data, 3, content, 0, len - 6);
                String temp = new String(content);
                Logger.d(TAG, "receiverData====" + temp + "    =msgType=" + msgType);
                StringMsgBean stringMsgBean = new StringMsgBean(msgType, temp);
                mInterface.onReceive(JSON.toJSONString(stringMsgBean));
        }


        private void decodeTouChuanData2(int len, byte[] data) {
//		byte[] content = new byte[len-4];
//		System.arraycopy(data,1,content,0,len-4);
//		String temp = new String(content);
//		Logger.e(TAG,"收到串口数据 ====  "+temp);

                byte[] content = new byte[len - 6]; //读取到的数据包括 校验和 和 协议尾，命令，，msgType
                int msgType = (data[2] * 256) + (data[1] & 0xff);
                //int msgType = data[2]<<8|data[1];
                Logger.d(TAG, "胸口msgType" + msgType);
                System.arraycopy(data, 3, content, 0, len - 6);
                String temp = new String(content);
                Logger.d(TAG, "receiverData====" + temp + "    =msgType=" + msgType);
                StringMsgBean stringMsgBean = new StringMsgBean(msgType, temp);
                mInterface.onReceive(JSON.toJSONString(stringMsgBean));

                //data====[-123, 30, 34, 49, 50, 52, 53, -111, 13, 10]  8734  7714
        }

        private boolean checkData(byte[] data) {
                int length = data.length;
                if (data[length - 2] == MsgCon.protocol_Tail_H && data[length - 1] == MsgCon.protocol_Tail_L) {
                        int datalen = length - 3;
                        byte sum = 0;
                        for (int i = 0; i < datalen; i++) {
                                sum += data[i];
                        }
                        if (sum == data[length - 3]) {
                                return true;
                        }
                }
                return true;
        }

        /**
         * 发送透传数据
         *
         * @param data
         */
        public void sendTouChuanData(int msgtype, String data) {
                if (!TextUtils.isEmpty(data)) {
                        endCodeTouChuanData(msgtype, data);
                }
        }

        public void sendHeadCmd(short verAngle, short verTime, short horAngle,
                                short horTime) {
                byte[] data = encodeHeadData(verAngle, verTime, horAngle, horTime);
                endCodeGeneralPackageData((byte) 0x03, data);
        }

        public void sendMapCmd(byte state) {
                byte[] data = encodeMapData(state);
                endCodeGeneralPackageData((byte) 0x17, data);
        }

        public void sendStopActionCmd() {
                endCodeGeneralPackageData(MsgCon.CMD_STOP_ACTION);
        }


        private byte[] encodeMapData(byte state) {
                byte[] data = new byte[1];
                int index = 0;
                data[index++] = state;
                return data;
        }

        private byte[] encodeHeadData(short verAngle, short verTime, short horAngle,
                                      short horTime) {
                byte[] data = new byte[10];
                int index = 0;
                data[index++] = 0x00;
                data[index++] = 0x01;
                data[index++] = (byte) (verAngle >> 8);
                data[index++] = (byte) verAngle;
                data[index++] = (byte) (verTime >> 8);
                data[index++] = (byte) verTime;
                data[index++] = (byte) (horAngle >> 8);
                data[index++] = (byte) horAngle;
                data[index++] = (byte) (horTime >> 8);
                data[index++] = (byte) horTime;
                return data;
        }

        private void endCodeTouChuanData(int msgType, String data) {
                byte[] str = data.getBytes();
                int len = str.length + 1 + 2;
                byte buffer[] = new byte[len + 7];
                int index = 0;
                buffer[index++] = MsgCon.protocol_Head_H;
                buffer[index++] = MsgCon.protocol_Head_L;
                buffer[index++] = (byte) ((len >> 8) & 0xff);
                buffer[index++] = (byte) (len & 0xff);


                buffer[index++] = MsgCon.TransparentSendCmd;
                buffer[index++] = (byte) (msgType >> 8);
                buffer[index++] = (byte) (msgType & 0xff);

                byte sum = (byte) (MsgCon.TransparentSendCmd + (byte) (msgType >> 8) + (byte) (msgType & 0xff));
                for (int i = 0; i < (len - 3); i++) {
                        buffer[index++] = str[i];
                        sum += str[i];
                }
                buffer[index++] = sum;

                buffer[index++] = 0x0d;
                buffer[index++] = 0x0a;
                Logger.d(TAG, "touchuan====" + Arrays.toString(buffer));
                write(buffer);
        }

        /**
         * 向平板发送透传数据(通过WiFi协议)
         *
         * @return
         */
        public void sendTouChuan(int msgType, String data) {
                byte[] str = data.getBytes();
                int len = str.length + 1 + 2;
                byte[] buf = new byte[7 + len];
                int index = 0;

                buf[index++] = MsgCon.protocol_Head_H;
                buf[index++] = MsgCon.protocol_Head_L;
                buf[index++] = (byte) ((len >> 8) & 0xff);
                buf[index++] = (byte) (len & 0xff);


                buf[index++] = MsgCon.TransparentSendCmd2;

                buf[index++] = (byte) (msgType >> 8);
                buf[index++] = (byte) (msgType & 0xff);

                byte sum = (byte) (MsgCon.TransparentSendCmd2 + (byte) (msgType >> 8) + (byte) (msgType & 0xff));
                for (int i = 0; i < (len - 3); i++) {
                        buf[index++] = str[i];
                        sum += str[i];
                }
                buf[index++] = sum;

                buf[index++] = 0x0d;
                buf[index++] = 0x0a;

                write(buf);

        }

        /**
         * 计算校验和
         *
         * @param data
         * @return
         */
        private static byte check(byte[] data) {
                byte check = 0;
                for (int i = 8, len = data.length - 1; i < len; i++) {
                        check ^= data[i];
                }
                return check;
        }

        private void endCodeTouChuanData(String data) {
                byte[] str = data.getBytes();
                int len = str.length + 1;
                byte buffer[] = new byte[len + 7];
                int index = 0;
                buffer[index++] = MsgCon.protocol_Head_H;
                buffer[index++] = MsgCon.protocol_Head_L;
                buffer[index++] = (byte) ((len >> 8) & 0xff);
                buffer[index++] = (byte) (len & 0xff);
                buffer[index++] = MsgCon.TransparentSendCmd;
                byte sum = MsgCon.TransparentSendCmd;
                for (int i = 0; i < (len - 1); i++) {
                        buffer[index++] = str[i];
                        sum += str[i];
                }
                buffer[index++] = sum;

                buffer[index++] = 0x0d;
                buffer[index++] = 0x0a;
                write(buffer);
        }


        private void endCodeGeneralPackageData(byte ordor, byte[] data) {
                int len = data.length + 1;
                byte buffer[] = new byte[len + 7];
                int index = 0;
                buffer[index++] = MsgCon.protocol_Head_H;
                buffer[index++] = MsgCon.protocol_Head_L;
                buffer[index++] = (byte) ((len >> 8) & 0xff);
                buffer[index++] = (byte) (len & 0xff);
                buffer[index++] = ordor;

                byte sum = ordor;
                for (int i = 0; i < (len - 1); i++) {
                        buffer[index++] = data[i];
                        sum += data[i];
                }
                buffer[index++] = sum;

                buffer[index++] = 0x0d;
                buffer[index++] = 0x0a;
                write(buffer);
        }

        private void endCodeGeneralPackageData(byte ordor) {
                int len = 1;
                byte buffer[] = new byte[len + 7];
                int index = 0;
                buffer[index++] = MsgCon.protocol_Head_H;
                buffer[index++] = MsgCon.protocol_Head_L;
                buffer[index++] = (byte) ((len >> 8) & 0xff);
                buffer[index++] = (byte) (len & 0xff);
                buffer[index++] = ordor;

                byte sum = ordor;
                buffer[index++] = sum;

                buffer[index++] = 0x0d;
                buffer[index++] = 0x0a;
                write(buffer);
        }


        /**
         * 底盘运动控制
         *
         * @param angle
         * @param moveSpeed
         * @param rotateSpeed
         * @param moveDistance
         * @return
         */
        private byte[] enCodeDirectionData(short angle, short moveSpeed, short rotateSpeed, short moveDistance) {
                byte[] buffer = new byte[8];
                int index = 0;
                buffer[index++] = (byte) ((angle >> 8) & 0xff);
                buffer[index++] = (byte) (angle & 0xff);

                buffer[index++] = (byte) ((moveSpeed >> 8) & 0xff);
                buffer[index++] = (byte) (moveSpeed & 0xff);

                buffer[index++] = (byte) ((rotateSpeed >> 8) & 0xff);
                buffer[index++] = (byte) (rotateSpeed & 0xff);

                buffer[index++] = (byte) ((moveDistance >> 8) & 0xff);
                buffer[index++] = (byte) (moveDistance & 0xff);

                return buffer;
        }

        /**
         * 调用动作库执行动作
         *
         * @param actionID
         * @param needHead
         * @param libID
         * @return
         */
        private byte[] enCodeActionData(int actionID, byte needHead, byte libID) {
                byte[] buffer = new byte[6];
                int index = 0;
                buffer[index++] = (byte) ((actionID >> 24) & 0xFF);
                buffer[index++] = (byte) ((actionID >> 16) & 0xFF);
                buffer[index++] = (byte) ((actionID >> 8) & 0xFF);
                buffer[index++] = (byte) (actionID & 0xFF);

                buffer[index++] = needHead;
                buffer[index++] = libID;

                return buffer;
        }

        /**
         * 打断档当前的动作
         *
         * @return
         */
        private byte[] enCodeStopActionData() {
                byte[] buffer = new byte[1];
                int index = 0;
                buffer[index++] = MsgCon.CMD_STOP_ACTION;
                return buffer;
        }


        /**
         * 控制动作
         *
         * @param actionID
         * @param needHead
         * @param libID
         */
        public void sendActionCmd(int actionID, byte needHead, byte libID) {
                byte[] content = enCodeActionData(actionID, needHead, libID);
                endCodeGeneralPackageData(MsgCon.CMD_ACTION, content);
        }

        /**
         * 控制底盘
         *
         * @param angle
         * @param moveSpeed
         * @param rotateSpeed
         * @param moveDistance
         */
        public void sendDirectionCmd(short angle, short moveSpeed, short rotateSpeed, short moveDistance) {
                byte[] content = enCodeDirectionData(angle, moveSpeed, rotateSpeed, moveDistance);
                endCodeGeneralPackageData(MsgCon.CMD_DIRECTION, content);
        }


}
