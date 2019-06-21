package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class GetMoenyBody implements Serializable {
    public String UserId;
    public GetMoenyBody(String UserId){
        this.UserId = UserId;
    }
    public GetMoenyBody(){

    }
}
