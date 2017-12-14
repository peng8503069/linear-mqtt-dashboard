package com.ravendmaster.linearmqttdashboard.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ToBmp.Wlt;

/**
 * 该实体类包含了身份证的基本信息
 *
 * @author  wangshuo
 * @date 2017/9/20
 *
 */
public class CardInfo implements Serializable {

    private String mName;
    private int mGenderIndex;
    private int mNationalIndex;
    private String mBirthYear;
    private String mBirthMonth;
    private String mBirthDay;
    private String mAddress;
    private String mID2Num;
    private String mIssue;
    private String mBegin;
    private String mEnd;

    private String mFPInfo1;
    private String mFPInfo2;

    private byte[] mHead;
    private byte[] mFPDatasOne;
    private byte[] mFPDatasTwo;

    //0:不带指纹读卡
    //1:带指纹读卡
    private int readMode;


    /**
     * 获取姓名
     * @since  V1.0.1
     *@return 姓名
     *
     * */
    public String getmName() {
        return mName;
    }


    public void setmName(String mName) {
        this.mName = mName;
    }


    /**
     * 得到性别编号
     * @since  V1.0.1
     *@return   性别编号  编号代码参考
     * @see {CardInfo#GetGenderFromCode}
     *
     * */
    public int getmGenderIndex() {
        return mGenderIndex;
    }



    public void setmGenderIndex(int mGenderIndex) {
        this.mGenderIndex = mGenderIndex;
    }
    /**
     * 得到民族编号
     * @since  V1.0.1
     *@return   民族编号
     *
     *
     * */
    public int getmNationalIndex() {
        return mNationalIndex;
    }

    public void setmNationalIndex(int mNationalIndex) {
        this.mNationalIndex = mNationalIndex;
    }


    /**
     * 获取出生年份
     * @since V1.0.1
     *@return  出生年份
     *
     * */
    public String getmBirthYear() {
        return mBirthYear;
    }



    public void setmBirthYear(String mBirthYear) {
        this.mBirthYear = mBirthYear;
    }
    /**
     * 获取出生月份
     * @since V1.0.1
     *@return  出生月份
     *
     * */
    public String getmBirthMonth() {
        return mBirthMonth;
    }

    public void setmBirthMonth(String mBirthMonth) {
        this.mBirthMonth = mBirthMonth;
    }
    /**
     * 获取出生日
     * @since V1.0.1
     *@return  出生日
     *
     * */
    public String getmBirthDay() {
        return mBirthDay;
    }

    public void setmBirthDay(String mBirthDay) {
        this.mBirthDay = mBirthDay;
    }
    /**
     *  获取家庭住址
     * @since V1.0.1
     *@return  家庭住址
     *
     * */
    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
    /**
     * 获取身份证号
     * @since V1.0.1
     *@return  身份证号
     *
     * */
    public String getmID2Num() {
        return mID2Num;
    }

    public void setmID2Num(String mID2Num) {
        this.mID2Num = mID2Num;
    }
    /**
     * 获取签发机关
     * @since V1.0.1
     *@return  签发机关
     *
     * */
    public String getmIssue() {
        return mIssue;
    }

    public void setmIssue(String mIssue) {
        this.mIssue = mIssue;
    }
    /**
     * 获得身份证有效期的开始时间
     * @since V1.0.1
     *@return  身份证有效期的开始时间 格式为yyyyMMdd 如20170912
     *
     * */
    public String getmBegin() {
        return mBegin;
    }

    public void setmBegin(String mBegin) {
        this.mBegin = mBegin;
    }
    /**
     * 获得身份证有效期的结束时间
     * @since V1.0.1
     *@return  身份证有效期的结束时间 格式为yyyyMMdd 如20170912
     *
     * */
    public String getmEnd() {
        return mEnd;
    }

    public void setmEnd(String mEnd) {
        this.mEnd = mEnd;
    }


    /**
     * 获得身第一个指纹信息
     * @since V1.0.1
     *@return  指纹信息
     *
     * */
    public String getmFPInfo1() {
        if(getReadMode() == 0) {
            throw new RuntimeException("If you want to get FP infomation,please call SdsesUSBDevice#readCardWithFP");
        }
        return mFPInfo1;
    }

    public void setmFPInfo1(String mFPInfo1) {
        this.mFPInfo1 = mFPInfo1;
    }
    /**
     * 获得身第二个指纹信息
     * @since V1.0.1
     *@return  指纹信息
     *
     * */
    public String getmFPInfo2() {
        if(getReadMode() == 0) {
            throw new RuntimeException("If you want to get FP infomation,please call SdsesUSBDevice#readCardWithFP");
        }
        return mFPInfo2;
    }

    public void setmFPInfo2(String mFPInfo2) {
        this.mFPInfo2 = mFPInfo2;
    }


    /**
     * 获取头像原始数据
     * @return 头像原始数据
     * */
    public byte[] getmHead() {
        return mHead;
    }

    public void setmHead(byte[] mHead) {
        this.mHead = mHead;
    }
    /**
     * 得到第一个指纹的特征值
     * @return 第一个指纹的特征值
     * */
    public byte[] getmFPDatasOne() {
        if(getReadMode() == 0) {
            throw new RuntimeException("If you want to get FP infomation,please call SdsesUSBDevice#readCardWithFP");
        }
        return mFPDatasOne;
    }

    public void setmFPDatasOne(byte[] mFPDatasOne) {
        this.mFPDatasOne = mFPDatasOne;
    }

    /**
     * 得到第二个指纹的特征值
     * @return 第二个指纹的特征值
     * */
    public byte[] getmFPDatasTwo() {
        if(getReadMode() == 0) {
            throw new RuntimeException("If you want to get FP infomation,please call SdsesUSBDevice#readCardWithFP");
        }
        return mFPDatasTwo;
    }

    public void setmFPDatasTwo(byte[] mFPDatasTwo) {
        this.mFPDatasTwo = mFPDatasTwo;
    }

/**
 * 获得出生年月日
 * @return 出生年月日 用“-”分割，比如2010-12-22
 * */
    public String getBirthInfo() {
        return getmBirthYear() + "-" + getmBirthMonth() + "-" + getmBirthDay();
    }

    /**
     * 获得出生年月日
     * @return 出生年月日
     * @param format 日期格式化
     *               @see  SimpleDateFormat#format(Date)
     * */
    public String getBirthInfo(String format) {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(getmBirthYear()),Integer.parseInt(getmBirthMonth()),Integer.parseInt(getmBirthDay()));
            calendar.getTime();

            Date date =  calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            return getmBirthDay();
        }
    }

/**
 * 得到解码后的图片
 * @return 解码后的图片
 *
 * */
    public Bitmap getHeadBmp() {
        Wlt wlt = new Wlt();
        byte[] ss = new byte[38862];
        wlt.GetBmpByBuffNoLic(getmHead(), ss);
        return BitmapFactory.decodeByteArray(ss, 0, ss.length);
    }

/**
 * 获取性别字符串
 * */
    public String GetGenderFromCode() {

        switch (getmGenderIndex()) {
            case 0:
                return "未知的性别";
            case 1:
                return "男";
            case 2:
                return "女";
            case 9:
                return "未说明的性别";
            default:
                return "未定义的性别";
        }
    }

    /**
     * 获取民族名称
     * @return 民族字符串
     */
    public String GetNationalFromCode() {

        int n = Integer.valueOf(getmNationalIndex());
        switch (n) {
            case 1:
                return "汉";
            case 2:
                return "蒙古";
            case 3:
                return "回";
            case 4:
                return "藏";
            case 5:
                return "维吾尔";
            case 6:
                return "苗";
            case 7:
                return "彝";
            case 8:
                return "壮";
            case 9:
                return "布依";
            case 10:
                return "朝鲜";
            case 11:
                return "满";
            case 12:
                return "侗";
            case 13:
                return "瑶";
            case 14:
                return "白";
            case 15:
                return "土家";
            case 16:
                return "哈尼";
            case 17:
                return "哈萨克";
            case 18:
                return "傣";
            case 19:
                return "黎";
            case 20:
                return "傈僳";
            case 21:
                return "佤";
            case 22:
                return "畲";
            case 23:
                return "高山";
            case 24:
                return "拉祜";
            case 25:
                return "水";
            case 26:
                return "东乡";
            case 27:
                return "纳西";
            case 28:
                return "景颇";
            case 29:
                return "柯尔克孜";
            case 30:
                return "土";
            case 31:
                return "达斡尔";
            case 32:
                return "仫佬";
            case 33:
                return "羌";
            case 34:
                return "布朗";
            case 35:
                return "撒拉";
            case 36:
                return "毛难";
            case 37:
                return "仡佬";
            case 38:
                return "锡伯";
            case 39:
                return "阿昌";
            case 40:
                return "普米";
            case 41:
                return "塔吉克";
            case 42:
                return "怒";
            case 43:
                return "乌孜别克";
            case 44:
                return "俄罗斯";
            case 45:
                return "鄂温克";
            case 46:
                return "崩龙";
            case 47:
                return "保安";
            case 48:
                return "裕固";
            case 49:
                return "京";
            case 50:
                return "塔塔尔";
            case 51:
                return "独龙";
            case 52:
                return "鄂伦春";
            case 53:
                return "赫哲";
            case 54:
                return "门巴";
            case 55:
                return "珞巴";
            case 56:
                return "基诺";
            case 59:
                return "穿青人";
            case 60:
                return "家人";
            case 97:
                return "其他";
            case 98:
                return "外国血统";
            default:
                return "其他";
        }
    }


    /**
     * 读卡方式（带指纹和不带指纹）
     * */
    public int getReadMode() {
        return readMode;
    }

    public void setReadMode(int readMode) {
        this.readMode = readMode;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "mName='" + mName + '\'' +
                ", mGenderIndex='" + mGenderIndex + '\'' +
                ", mNationalIndex='" + mNationalIndex + '\'' +
                ", mBirthYear='" + mBirthYear + '\'' +
                ", mBirthMonth='" + mBirthMonth + '\'' +
                ", mBirthDay='" + mBirthDay + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mID2Num='" + mID2Num + '\'' +
                ", mIssue='" + mIssue + '\'' +
                ", mBegin='" + mBegin + '\'' +
                ", mEnd='" + mEnd + '\'' +
                ", mFPInfo1='" + mFPInfo1 + '\'' +
                ", mFPInfo2='" + mFPInfo2 + '\'' +
                ", mHead=" + Arrays.toString(mHead) +
                ", mFPDatasOne=" + Arrays.toString(mFPDatasOne) +
                ", mFPDatasTwo=" + Arrays.toString(mFPDatasTwo) +
                '}';
    }
}
