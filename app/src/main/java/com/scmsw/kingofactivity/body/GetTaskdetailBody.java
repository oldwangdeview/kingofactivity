package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class GetTaskdetailBody implements Serializable {
    public String MissionID;
    public GetTaskdetailBody(String MissionID){
        this.MissionID = MissionID;
    }
}
