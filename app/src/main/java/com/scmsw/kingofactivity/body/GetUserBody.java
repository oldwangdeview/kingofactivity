package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class GetUserBody implements Serializable {
    public String Userid;
    public String Missionid;
    public String MissionType;
    public GetUserBody(String Userid,String Missionid,String MissionType){
        this.Userid = Userid;
        this.Missionid = Missionid;
        this.MissionType = MissionType;

    }
}
