package com.ravendmaster.linearmqttdashboard.utils;

/**
 * Created by Administrator on 2017/7/1.
 */

/**
 * 数据格式转换类
 */
public class DataUtils {

        public static String bytesToHexString(byte[] src) {
                StringBuilder stringBuilder = new StringBuilder("");
                if (src == null || src.length <= 0) {
                        return null;
                }
                for (int i = 0; i < src.length; i++) {
                        int v = src[i] & 0xFF;
                        String hv = Integer.toHexString(v);
                        stringBuilder.append("0x");
                        if (hv.length() < 2) {
                                stringBuilder.append(0);
                        }
                        stringBuilder.append(hv);
                        stringBuilder.append(", ");
                }
                return stringBuilder.toString();
        }
}
