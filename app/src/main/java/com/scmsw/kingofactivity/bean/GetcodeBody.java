package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class GetcodeBody implements Serializable {

    public String ValidCode;
    public String Phon;

    public GetcodeBody(){}
    public GetcodeBody(String ValidCode,String phon){
        this.ValidCode = ValidCode;
        this.Phon = phon;
    }
}
