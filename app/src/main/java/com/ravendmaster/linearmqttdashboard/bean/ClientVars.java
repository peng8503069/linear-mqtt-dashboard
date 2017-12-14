package com.ravendmaster.linearmqttdashboard.bean;

/**
 * ��������ʱʹ�õ���ȫ�ֱ���
 * @author meng
 * @version 1.0
 */
public final class ClientVars {
	/**
	 * GA467Э��
	 */
	public static final int protocol_69 = 1;
	/**
	 * �Զ���Э�� 96 0A
	 */
	public static final int protocol_0A = 2;

	/**
	 * ���Ӷ����豸��ִ�к�����豸��ɳ�ʼ��������
	 */
	public static final int deviceConnect = 0x70;
	public static final int deviceConnect_OK = 0x70;
	public static final int deviceConnect_ERROR = 0xF0;
	/**
	 * �򿪶�������ִ�к󴴽������̡߳�
	 */
	public static final int openServer = 0x71;
	public static final int openServer_OK = 0x71;
	public static final int openServer_ERROR = 0xF1;
	/**
	 * �رն�������ִ�к�رն����̡߳�
	 */
	public static final int closeServer = 0x72;
	public static final int closeServer_OK = 0x72;
	public static final int closeServer_ERROR = 0xF2;
	/**
	 * �Ͽ������豸���ӡ�ִ�к��ͷŶ����豸��Դ�����硣
	 */
	public static final int deviceDisConnect = 0x73;
	public static final int deviceDisConnect_OK = 0x73;
	public static final int deviceDisConnect_ERROR = 0xF3;
	/**
	 * �����ݷ���
	 */
	public static final int receivefromcard = 0x75;
	/**
	 * �����ݷ��ز������� 0x75׷�Ӳ���
	 */
	public static final int resetSAM = 0x00;//10FF ��λSAM_V
	public static final int checkSAM = 0x01;//11FF SAM_V״̬���
	public static final int readSAMSN= 0x02;//12FF ��SAM_V������Ϣ
	public static final int seachCard= 0x03;//2001  Ѱ�����֤
	public static final int selectCard= 0x04;//2002  ѡȡ���֤
	public static final int readBaseInfo = 0x05;//3001  ��������Ϣ
	public static final int readAllInfo= 0x06;//3010  ��������Ϣ��֤��ָ����Ϣ
	public static final int readAddressAdd = 0x07;//3003  ��׷�ӵ�ַ
	public static final int setBaud= 0x08;//60XX ����UART�ӿ�����
	public static final int setBits= 0x09;//61XX ����SAM_V����Ƶģ��һ֡��������ֽ���
	public static final int unknownError = 0x10;//δ֪����
	/**
	 * ������ʱ
	 */
	public static final int timeout = 10;
	/**
	 * �ͻ�����������
	 */
	public static final int heartbeat = 5;
	public static final String  sendtoserver ="com.sdses.sendtoserver";
	public static final String  receivefromserver ="com.sdses.receivefromserver";
	public static final String  command ="command";
	public static final String  value ="value";
	public static final String  extra ="extra";
	public static final String  data ="data";
	public static final String  ishaveFpData="isHaveFpData";
	

}
