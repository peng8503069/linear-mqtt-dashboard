package com.ravendmaster.linearmqttdashboard.utils;

/**
 * 和平板通信的消息类型
 *
 * @author xiaowei
 */
public class MsgType {
        // 平板接受rk3288的数据格式： 80 。。。数据(第一个字节是0x80，数据位是从第9个字节开始)

        // 平板发送给rk3288的数据格式：00 80 60 60 数据长度 (給mcu) 数据(数据位是从第9个字节开始)
        // 数据=消息类型+数据类型(byte or String)+数据长度(给3288) + 内容


        /**
         * 数据类型
         */

        // 平板接受的消息编号---- 1--- 8399
        // 平板发送的消息编号---- 8400-65535

        /**
         * 消息类型---播放TTS
         */
        public static final int PLAY_TTS = 8401;

        public static final int PLAY_ACTION = 8420;

        public static final int PLAY_AUDIO = 8400;

        public static final int NO_ACTIVE = 8414;

        public static final int ACTIVE = 8403;

        public static final int LINK_WIFI = 8405;
}
