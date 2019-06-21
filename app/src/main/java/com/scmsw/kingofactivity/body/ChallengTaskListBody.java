package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class ChallengTaskListBody implements Serializable {
    public String Userid;
    public String Status;
    public ChallengTaskListBody(){

    }
    public ChallengTaskListBody(String userid,String status){
        this.Userid = userid;
        this.Status = status;
    }
}
