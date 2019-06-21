package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChoiceuserBean implements Serializable {
    public String Missionid;
    public String Userid;
    public String MissionType;
    public List<Choice_user_ListdataBean> tds = new ArrayList<>();
}
