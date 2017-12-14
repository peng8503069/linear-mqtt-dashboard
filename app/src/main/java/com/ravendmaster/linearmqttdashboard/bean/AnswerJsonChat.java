package com.ravendmaster.linearmqttdashboard.bean;

/**
 * Created by Administrator on 2017/7/4.
 */

public class AnswerJsonChat {


    /**
     * quesAnsw : {"question":"需要什么证件","answer":"本人领卡持有效身份证到归属行领取；代办需两人有效身份证件。增加关联折不可以代办。"}
     */

    private QuesAnswBean quesAnsw;

    public QuesAnswBean getQuesAnsw() {
        return quesAnsw;
    }

    public void setQuesAnsw(QuesAnswBean quesAnsw) {
        this.quesAnsw = quesAnsw;
    }

    public static class QuesAnswBean {
        /**
         * question : 需要什么证件
         * answer : 本人领卡持有效身份证到归属行领取；代办需两人有效身份证件。增加关联折不可以代办。
         */

        private String question;
        private String answer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
