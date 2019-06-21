package com.scmsw.kingofactivity.body;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UplodPKBody implements Serializable {

    public String Userid;
    public String LaunchType;
    public String FQAcceptID;
    public String MissionID;
    public String MissionNum;
    public String MissionName;
    public String PKAmount;
    public String Description;
    public List<UpLodePK_listDataBody> aj = new ArrayList<>();

}
