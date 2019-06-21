package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class MissionUserDetailsBody implements Serializable {
    public String Userid;
    public String MissStatus;
    public MissionUserDetailsBody(String Userid,String MissStatus ){
        this.Userid = Userid;
        this.MissStatus = MissStatus;
    }
}
