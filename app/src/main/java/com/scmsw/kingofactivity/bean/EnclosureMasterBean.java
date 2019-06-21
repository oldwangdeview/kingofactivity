package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnclosureMasterBean implements Serializable {


    public List<EnclosureMaster_benlist> ed = new ArrayList<>();
    public String EnclosureMasterid;
    public String EnclosureType;
    public String Busid;
    public String Status;
    public String Comqty;
    public String ComStates;
    public String RPvalue;
    public String VerifyMemo;
    public String Memo;
    public String CreateDate;
    public String CreateUserId;
    public String CreateUserName;
    public String VerifyDate;
    public String LatitudeTotalValue;
    public String VerifyUserId;
    public String VerifyUserName;
    public String RewardValue;
    public String PenaltyValue;
}
