 package ToBmp;

import java.io.Serializable;


 /**
  *用于解析图片数据
  * @version V1.0.1
  * @author  wangshuo
  *
  * */
public class Wlt implements Serializable {
    private static final long serialVersionUID = 1L;

    static {
        try {
            System.loadLibrary("SDSES_Wlt_1.2");
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    public Wlt() {
    }

     /**
      * 将var1的原始数据解析成var2
      * @param var1 图像原始数据
      *             @param var2 解析后的图像数据
      *
      * */
    public native int GetBmpByBuffNoLic(byte[] var1, byte[] var2);

    public native int GetBmpByBuff(byte[] var1, String var2, String var3, byte[] var4);
}
