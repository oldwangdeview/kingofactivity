package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class UserdontgettaskListdataBody implements Serializable {

    public String Userid;
    public String MissStatus;

    public String Missionid = "INVALID9999";
    public UserdontgettaskListdataBody(String Userid,String MissStatus){
        this.Userid = Userid;
        this.MissStatus = MissStatus;
    }

    public UserdontgettaskListdataBody(){

    }
}
