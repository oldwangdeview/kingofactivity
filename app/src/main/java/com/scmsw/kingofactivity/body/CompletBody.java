package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class CompletBody implements Serializable {

    public String Missionid;
    public String Missiontype;
    public String LatitudesID;


    public CompletBody(String Missionid,String Missiontype,String LatitudesID){
        this.Missionid = Missionid;
        this.Missiontype = Missiontype;
        this.LatitudesID = LatitudesID;
    }

}
