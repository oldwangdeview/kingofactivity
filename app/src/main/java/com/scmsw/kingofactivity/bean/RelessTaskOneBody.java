package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class RelessTaskOneBody implements Serializable {
    /***被选中的任务***/
    public TaskListData_ListBean taskdata;
    /***被选中的任务详情***/
    public UserDontGetTaskListDataListBean taskdatadetail;
    /**任务名称**/
    public String Task_name = "";
    /****被选择的任务类型（"0" 默认，"2" 自由领取，"1" 强制分配）****/
    public int tasktype = 0;
    /****被选择的任务级别（"0" 默认，"1" 末级，"2" 非末级）****/
    public int tasklevel = 0;
    /**选择的开始时间***/
    public String starttime = "";
    /***选择结束时间***/
    public String endtime = "";
    /***任务说明***/
    public String task_message = "0";
    /***强制分配奖金分配方式 "0" 默认,"1" 固定金额*,"2" 目标比例**/
    public int choiceintmoey_type1 = 0;
    /***超额奖金分配方式 "0" 默认,"1" 固定金额*,"2" 目标比例**/
    public int choiceintmonet_type2 = 0;
    public int inputmoney_type = 1;
    public String max_size = "0";
    public String min_size = "0";
    public Level_ListdataBean danwei = null;
    public String MissionIsPK = "";
    public String PkAmount = "";
    public String GuaranteeMoneyState = "";
    public String GuaranteeMoney = "";

}
