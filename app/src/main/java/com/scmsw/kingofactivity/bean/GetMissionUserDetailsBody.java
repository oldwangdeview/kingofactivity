package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class GetMissionUserDetailsBody implements Serializable {
    public String Userid ;//用户id （来宾：d00527d8-c4df-4e60-89ea-de4b227285c9   公司：acc25328-5859-4089-9002-5b546a7d27f9）
    public String MissStatus;//任务状态：（没有数据：已完成，未完成   现在没数据）

    public GetMissionUserDetailsBody(String Userid,String MissStatus){
        this.Userid = Userid;
        this.MissStatus = MissStatus;
    }
    public GetMissionUserDetailsBody(){

    }
}