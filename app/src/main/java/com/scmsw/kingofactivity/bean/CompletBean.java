package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompletBean implements Serializable {
    public String Missionid;
    public String Missiontype;
    public String LatitudesID;
    public List<HKLatitudesListData> md = new ArrayList<>();

}
