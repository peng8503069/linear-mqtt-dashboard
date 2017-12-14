package android_serialport_api;

import java.io.File;

/**
 * Created by X230 on 2017/4/18.
 */

public class SerialPortMgr {

    private static SerialPortMgr instance ;

    /**
     * 串口对象
     */
    private FpgeThread fpga;

    private SerialPortMgr(){
         //新机器用ttyS3
         //旧机器用ttyS1
         fpga= new FpgeThread(new File("/dev/ttyS3"),115200,0); //串口号以及波特率
         fpga.start();
    }

    public static SerialPortMgr getInstance(){
        if (instance==null){
            synchronized (SerialPortMgr.class){
                if (instance == null){
                    instance = new SerialPortMgr();
                }
            }
        }
        return instance;
    }
    public void init(ReceiveDataInterface listener){
       fpga.init(listener);
    }

    public void sendTouChuanData(int msgType,String msgData){

        fpga.sendTouChuanData(msgType,msgData);
    }

    public void sendHeadCmd(short verAngle, short verTime, short horAngle,
                            short horTime){
        fpga.sendHeadCmd(verAngle,verTime,horAngle,horTime);

    }
    public void sendActionCmd(int actionID){
        fpga.sendActionCmd(actionID,(byte) 0,(byte)1);
    }

    public void sendMsg(int msgType,String data){
        fpga.sendTouChuan(msgType,data);
    }


    /**
     * SLAM建图开关指令  0x00 开启地图    0x01 关闭地图
     * @param state
     */
    private void sendMapCmd(byte state){
        if (fpga != null ){
            fpga.sendMapCmd(state);
        }
    }

    /**
     * 停止当前动作
     */
    public void sendStopActionCmd(){
        if (fpga != null ){
            fpga.sendStopActionCmd();
        }
    }

    public void sendDirectionCmd(short angle, short moveSpeed, short rotateSpeed, short moveDistance){
        if (fpga != null ){
            fpga.sendDirectionCmd(angle,moveSpeed,rotateSpeed,moveDistance);
        }
    }


    /**
     * 打开地图
     */
    public void openMapCmd(){
        sendMapCmd((byte) 0x00);
    }
    /**
     * 关闭地图
     */
    public void closeMapCmd(){
        sendMapCmd((byte) 0x01);
    }

}
