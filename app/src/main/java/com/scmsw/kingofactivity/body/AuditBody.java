package com.scmsw.kingofactivity.body;

import com.scmsw.kingofactivity.activity.AuditTaskActivity;

import java.io.Serializable;

public class AuditBody implements Serializable {
    public String Userid;
    public String MissionRecID;
    public String MissinState;
    public AuditBody(String Userid,String MissionRecID,String MissinState){
        this.Userid = Userid;
        this.MissionRecID = MissionRecID;
        this.MissinState = MissinState;
    }
    public AuditBody(){

    }
}
