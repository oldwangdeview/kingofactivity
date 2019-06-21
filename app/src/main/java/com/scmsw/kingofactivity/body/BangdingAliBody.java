package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class BangdingAliBody implements Serializable {
    public String Userid;
    public String BindingType;
    public String RealName;
    public String BingNumber;
    public BangdingAliBody(String userid,String BindingType,String RealName,String BingNumber){
        this.Userid = userid;
        this.BindingType = BindingType;
        this.RealName = RealName;
        this.BingNumber = BingNumber;
    }

    public BangdingAliBody(){

    }
}
