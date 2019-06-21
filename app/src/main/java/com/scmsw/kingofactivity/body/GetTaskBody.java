package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class GetTaskBody implements Serializable {
    public String MissionID;
    public String MissionNum;
    public String MissionName;
    public String MissionRecQty;
    public String RecPersonID;

    public GetTaskBody(String MissionID,String MissionNum,String MissionName,String MissionRecQty,String RecPersonID){
        this.MissionID = MissionID;
        this.MissionNum = MissionNum;
        this.MissionName = MissionName;
        this.MissionRecQty = MissionRecQty;
        this.RecPersonID = RecPersonID;
    }
    public GetTaskBody(){

    };
}
