package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class GetMissionDetailsBody implements Serializable {
    public String FUserid;//用户id （来宾：d00527d8-c4df-4e60-89ea-de4b227285c9   公司：acc25328-5859-4089-9002-5b546a7d27f9）
    public String ScreenID;//筛选条件的id （现在没数据：1）
    public String Ftitle;//页面类别（任务达成，任务目标，任务发布）

    public GetMissionDetailsBody(String FUserid,String ScreenID,String Ftitle){
        this.FUserid = FUserid;
        this.ScreenID = ScreenID;
        this.Ftitle = Ftitle;
    }
    public GetMissionDetailsBody(){

    }

}
