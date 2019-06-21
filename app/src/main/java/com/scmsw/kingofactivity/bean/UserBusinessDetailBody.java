package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class UserBusinessDetailBody implements Serializable {
    public String Userid;
    public UserBusinessDetailBody(String Userid){
        this.Userid = Userid;

    }
    public UserBusinessDetailBody(){

    }
}
