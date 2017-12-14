package com.ravendmaster.linearmqttdashboard.bean;

/**
 * Created by Administrator on 2017/9/22.
 */

public class JsonDataAmqPlus {


    /**
     * data : {"msgType":"类型","msgData":"内容"}
     * auth : {"password":"密码","pop":"true/false 是否弹出密码框","action":"动作ID"}
     */

    private DataBean data;
    private AuthBean auth;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public AuthBean getAuth() {
        return auth;
    }

    public void setAuth(AuthBean auth) {
        this.auth = auth;
    }

    public static class DataBean {
        /**
         * msgType : 类型
         * msgData : 内容
         */

        private String msgType;
        private String msgData;

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getMsgData() {
            return msgData;
        }

        public void setMsgData(String msgData) {
            this.msgData = msgData;
        }
    }

    public static class AuthBean {
        /**
         * password : 密码
         * pop : true/false 是否弹出密码框
         * action : 动作ID
         */

        private String password;
        private String pop;
        private String action;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }
}
