package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class LoginBody implements Serializable {
    private String Account;
    private String Password;
    public LoginBody(){

    }
    public LoginBody(String Account,String Password){
        this.Account = Account;
        this.Password = Password;
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
