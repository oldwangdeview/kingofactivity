package com.scmsw.kingofactivity.body;

import java.io.Serializable;

/**
 * 任务列表传递数据
 */
public class TasklistdataBody  implements Serializable {
    public String UserID;
    public String Status;

    public String Missionid = "INVALID9999";
    public TasklistdataBody(String userID,String status){
        this.UserID = userID;
        this.Status = status;
    }

    public TasklistdataBody(String userID,String status,String missionid){
        this.UserID = userID;
        this.Status = status;
        this.Missionid = missionid;
    }
    public TasklistdataBody(){

    }
}
