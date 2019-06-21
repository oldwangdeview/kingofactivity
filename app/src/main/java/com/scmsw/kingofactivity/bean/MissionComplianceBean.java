package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MissionComplianceBean implements Serializable {
    public String Missionid ;
    public List<MissionCompliance_ListdatBean> mcs = new ArrayList<>();

}
