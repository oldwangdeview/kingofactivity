package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class GetChallengTaskDetailBody implements Serializable {
    public String MissionPKSubID;
    public GetChallengTaskDetailBody(String MissionPKSubID ){
        this.MissionPKSubID = MissionPKSubID;
    }
    public GetChallengTaskDetailBody(){

    }
}
