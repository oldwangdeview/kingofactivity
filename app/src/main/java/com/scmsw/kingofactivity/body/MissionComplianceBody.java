package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class MissionComplianceBody implements Serializable {
    public String Missionid;
    public String Userid;
    public MissionComplianceBody(String Missionid,String Userid){
        this.Missionid = Missionid;
        this.Userid = Userid;
    }
}
