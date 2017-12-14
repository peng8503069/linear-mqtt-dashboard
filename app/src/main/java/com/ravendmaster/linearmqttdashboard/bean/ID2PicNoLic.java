package com.ravendmaster.linearmqttdashboard.bean;

import java.io.Serializable;
import java.util.Arrays;

import ToBmp.Wlt;

public class ID2PicNoLic implements Serializable {
	private static final long serialVersionUID = 1L;
	private Wlt w = new Wlt();
	private byte[] mHeadFromCard = new byte[38862];
	
	public ID2PicNoLic() {
	}

	/**
	 * ʹ�ø�����SAMģ��ż���Ȩ�ļ���������֤ͷ��
	 * @param _picBuff	1024�ֽ�ͷ��ԭʼ����
	 * @return	-5:��Ȩ�ļ���SAMģ��Ų�ƥ��
	 * 			38862:ͷ�����ɹ�
	 */
	public int decodeNohLic(byte[] _picBuff) {
		Arrays.fill(mHeadFromCard, (byte) 0x00);
		// ͼƬ
		return w.GetBmpByBuffNoLic(_picBuff, mHeadFromCard);
	}

	/**
	 * ��ȡ����֤�������ͷ������
	 * @return	38862�ֽ�bmp����
	 */
	public byte[] getHeadFromCard() {
		return mHeadFromCard;
	}
}
