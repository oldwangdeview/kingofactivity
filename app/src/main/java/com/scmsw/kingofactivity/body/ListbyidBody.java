package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class ListbyidBody implements Serializable {

    private String Titlename;
    private String Selectedid;
    public ListbyidBody(String Titlename,String Selectedid){
        this.Titlename = Titlename;
        this.Selectedid = Selectedid;
    }
    public ListbyidBody(){

    }
}
