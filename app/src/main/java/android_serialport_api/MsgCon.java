package android_serialport_api;

public class MsgCon {
        // 协议格式：协议头2+数据长度2 + 数据+ 校验1 + 协议尾2
        // 数据：协议命令1+ 消息类型1 +包ID（2）+ 具体的数据

        // ----------------协议命令---------------------

        /**
         * 发送透传协议的命令（ 胸部 发送给 头部数据透传=0xDB, 头部发送给胸前数据透传=0x13）
         */
        public static byte TransparentSendCmd = 0x13;

        public static byte TransparentSendCmd2 = 0x14;

        /**
         * 接受透传协议的命令 （ 胸部 接受 头部数据透传=0x84, 头部接收胸前数据透传=0xE2）
         */
        public static byte TransparentReceiverCmd = (byte) 0x84;

        public static byte TransparentReceiverCmd2 = -(byte) 0x7b;

        /**
         * 协议头
         */
        public static byte protocol_Head_H = 0x08;

        public static byte protocol_Head_L = 0x06;

        /**
         * 协议尾
         */
        public static final byte protocol_Tail_H = 0x0d;

        public static final byte protocol_Tail_L = 0x0a;

        /**
         * 底盘运动命令
         */
        public static final byte CMD_DIRECTION = 0x01;

        /**
         * 动作库调用命令
         */
        public static final byte CMD_ACTION = 0x0D;

        /**
         * 打断当前动作
         */
        public static final byte CMD_STOP_ACTION = 0x12;

}
