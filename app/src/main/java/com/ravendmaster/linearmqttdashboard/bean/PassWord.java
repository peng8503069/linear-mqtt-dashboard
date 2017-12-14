package com.ravendmaster.linearmqttdashboard.bean;

/**
 * Created by Administrator on 2017/9/22.
 */

public class PassWord {

    private static String pw;

    private static String action;

    private static PassWord passWord;

    private PassWord(){}

    public static PassWord getPassWord(){
        if (passWord == null){
            passWord = new PassWord();
        }
        return passWord;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
