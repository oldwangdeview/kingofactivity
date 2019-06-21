package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class GetAliPaybody implements Serializable {
    public String Userid;
    public String BindingType;
    public GetAliPaybody(String Userid,String BindingType){
        this.Userid = Userid;
        this.BindingType = BindingType;

    }
}
