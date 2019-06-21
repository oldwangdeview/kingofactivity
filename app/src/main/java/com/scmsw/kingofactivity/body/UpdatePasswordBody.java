package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class UpdatePasswordBody implements Serializable {

    public String Account;
    public String Password;
    public UpdatePasswordBody(String Account,String Password){

        this.Account = Account;
        this.Password = Password;
    }
}
