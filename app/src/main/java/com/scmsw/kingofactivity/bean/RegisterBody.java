package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class RegisterBody implements Serializable {

    private String Account;
    private String Password;
    private String RealName;
    public RegisterBody(){

    }
    public RegisterBody(String Account,String Password,String RealName){
        this.Account = Account;
        this.Password = Password;
        this.RealName = RealName;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
