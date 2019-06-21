package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class EnclosuresByMissionidBody implements Serializable {
    public String missionid;
    public EnclosuresByMissionidBody(String missionid){
        this.missionid = missionid;
    }
}
