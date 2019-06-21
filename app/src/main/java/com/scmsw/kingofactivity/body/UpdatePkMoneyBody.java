package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class UpdatePkMoneyBody implements Serializable {


    public String Userid;
    public String pkmastid;
    public String PKAmount;

    public UpdatePkMoneyBody(String userid,String pkmastid,String PKAmount){
        this.Userid  = userid;
        this.pkmastid = pkmastid;
        this.PKAmount = PKAmount;
    }
    public UpdatePkMoneyBody(){

    }
}
