package com.ravendmaster.linearmqttdashboard.bean;

/**
 * Created by Administrator on 2017/9/26.
 */

public class Person {

    /**
     * name : xx
     * gender : xx
     * id : xx
     */

    private String name;
    private int gender;
    private String person_id;
    private static Person person;

    private Person(){}

    public static Person getPersonInstance(){
        if (person == null){
            person = new Person();
        }
        return person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPersonId() {
        return person_id;
    }

    public void setPersonId(String person_id) {
        this.person_id = person_id;
    }

}
