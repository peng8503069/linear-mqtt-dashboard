package com.ravendmaster.linearmqttdashboard.bean;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * ����֤�ı���Ϣ�ࡣ�����������֤��256�ֽ�ԭʼ����,�������Ա����������������ҵ���Ӧ���Ա�������
 * @author meng
 * @version	1.1
 */
public class ID2Txt implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TAG = ID2Txt.class.getSimpleName();
	private String mName;
	private String mGender;
	private String mGenderIndex;
	private String mNational;
	private String mNationalIndex;
	private String mBirthYear;
	private String mBirthMonth;
	private String mBirthDay;
	private String mAddress;
	private String mID2Num;
	private String mIssue;
	private String mBegin;
	private String mEnd;


	@Override
	public String toString() {
		// TODO Auto-generated method stub
//		return super.toString();
		return mName.trim()+"|"+
			   mGender.trim()+"|"+
			   mNational.trim()+"|"+
			   mBirthYear.trim()+"��"+mBirthMonth.trim()+"��"+mBirthDay.trim()+"��"+"|"+
			   mAddress.trim()+"|"+
			   mID2Num.trim()+"|"+
			   mIssue.trim()+"|"+
			   mBegin.trim()+"-"+mEnd.trim();
	}

	public ID2Txt() {
//		Log.i(TAG, "ID2Txt ���캯���޲���");
	}

	/**
	 * ����256�ֽ�ԭʼ����
	 * @param _txt	256�ֽ�ԭʼ����,UTF-16LE����ĺ��֡�
	 */
	public void decode(byte[] _txt) {
		// ����
		try {
			mName = new String(_txt, 0, 30, "UTF-16LE");
			mGenderIndex = new String(_txt, 30, 2, "UTF-16LE");
			mNationalIndex = new String(_txt, 32, 4, "UTF-16LE");
			mBirthYear = new String(_txt, 36, 8, "UTF-16LE");
			mBirthMonth = new String(_txt, 44, 4, "UTF-16LE");
			mBirthDay = new String(_txt, 48, 4, "UTF-16LE");
			mAddress = new String(_txt, 52, 70, "UTF-16LE");
			mID2Num = new String(_txt, 122, 36, "UTF-16LE");
			mIssue = new String(_txt, 158, 30, "UTF-16LE");
			mBegin = new String(_txt, 188, 16, "UTF-16LE");
			mEnd = new String(_txt, 204, 16, "UTF-16LE");
			mGender = GetGenderFromCode(mGenderIndex);
			mNational = GetNationalFromCode(mNationalIndex);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����
	 * @return	String���͵�����ֵ������15λ������ĺ��油�ո�
	 */
	public String getName() {
		return mName;
	}

	/**
	 * ��ȡ�Ա�
	 * @return	String���͵��Ա�ֵ������Ϊ��δ֪���Ա𡱡����С�����Ů������δ˵�����Ա𡱡�
	 */
	public String getGender() {
		return mGender;
	}

	/**
	 * ��ȡ�Ա�����
	 * @return	String���͵��Ա�����������2λ���硰00������01����
	 */
	public String getGenderIndex() {
		return mGenderIndex;
	}

	/**
	 * ��ȡ����
	 * @return	String���͵����壬�硰�����ȡ�
	 */
	public String getNational() {
		return mNational;
	}
	/**
	 * ��ȡ��������
	 * @return	String���͵���������������2λ���硰01������02����
	 */
	public String getNationalIndex() {
		return mNationalIndex;
	}

	/**
	 * ��ȡ��������--��
	 * @return	String���͵�������ڣ��硰1984����
	 */
	public String getBirthYear() {
		return mBirthYear;
	}

	/**
	 * ��ȡ��������--��
	 * @return	String���͵�������ڣ��硰01����
	 */
	public String getBirthMonth() {
		return mBirthMonth;
	}

	/**
	 * ��ȡ��������--��
	 * @return	String���͵�������ڣ��硰01����
	 */
	public String getBirthDay() {
		return mBirthDay;
	}

	/**
	 * ��ȡסַ
	 * @return	String���͵�סַ������35λ������ĺ��油�ո�
	 */
	public String getAddress() {
		return mAddress;
	}

	/**
	 * ��ȡ������ݺ���
	 * @return	String���͵Ĺ�����ݺ��룬����18λ��
	 */
	public String getID2Num() {
		return mID2Num;
	}

	/**
	 * ��ȡǩ������
	 * @return	String���͵�ǩ�����أ�����15λ������ĺ��油�ո�
	 */
	public String getIssue() {
		return mIssue;
	}

	/**
	 * ��ȡ��Ч���޿�ʼʱ��
	 * @return	String���͵���Ч���޿�ʼʱ�䣬����8λ���硰20140511����
	 */
	public String getBegin() {
		return mBegin;
	}

	/**
	 * ��ȡ��Ч���޽���ʱ��
	 * @return	String���͵���Ч���޽���ʱ�䣬����8λ���硰20140511�������ߡ�����      ��������8λ���油�ո�
	 */
	public String getEnd() {
		return mEnd;
	}

	public String GetGenderFromCode(String genderCode) {
		if(genderCode==null || genderCode.length()==0){
			return "";
		}
		switch (Integer.valueOf(genderCode)) {
		case 0:
			return "δ֪���Ա�";
		case 1:
			return "��";
		case 2:
			return "Ů";
		case 9:
			return "δ˵�����Ա�";
		default:
			return "δ������Ա�";
		}
	}

	public String GetNationalFromCode(String nationalCode) {
		if(nationalCode==null || nationalCode.length()==0){
			return "";
		}
		int n = Integer.valueOf(nationalCode);
		switch (n) {
		case 1:
			return "��";
		case 2:
			return "�ɹ�";
		case 3:
			return "��";
		case 4:
			return "��";
		case 5:
			return "ά���";
		case 6:
			return "��";
		case 7:
			return "��";
		case 8:
			return "׳";
		case 9:
			return "����";
		case 10:
			return "����";
		case 11:
			return "��";
		case 12:
			return "��";
		case 13:
			return "��";
		case 14:
			return "��";
		case 15:
			return "����";
		case 16:
			return "����";
		case 17:
			return "������";
		case 18:
			return "��";
		case 19:
			return "��";
		case 20:
			return "����";
		case 21:
			return "��";
		case 22:
			return "�";
		case 23:
			return "��ɽ";
		case 24:
			return "����";
		case 25:
			return "ˮ";
		case 26:
			return "����";
		case 27:
			return "����";
		case 28:
			return "����";
		case 29:
			return "�¶�����";
		case 30:
			return "��";
		case 31:
			return "���Ӷ�";
		case 32:
			return "����";
		case 33:
			return "Ǽ";
		case 34:
			return "����";
		case 35:
			return "����";
		case 36:
			return "ë��";
		case 37:
			return "����";
		case 38:
			return "����";
		case 39:
			return "����";
		case 40:
			return "����";
		case 41:
			return "������";
		case 42:
			return "ŭ";
		case 43:
			return "���α��";
		case 44:
			return "����˹";
		case 45:
			return "���¿�";
		case 46:
			return "����";
		case 47:
			return "����";
		case 48:
			return "ԣ��";
		case 49:
			return "��";
		case 50:
			return "������";
		case 51:
			return "����";
		case 52:
			return "���״�";
		case 53:
			return "����";
		case 54:
			return "�Ű�";
		case 55:
			return "���";
		case 56:
			return "��ŵ";
		case 59:
			return "������";
		case 60:
			return "�ܼ���";
		case 97:
			return "����";
		case 98:
			return "���Ѫͳ";
		default:
			return "����";
		}
	}
}
