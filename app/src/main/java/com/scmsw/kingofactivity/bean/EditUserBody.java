package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class EditUserBody implements Serializable {

    public String UserId;
    public String NickName;
    public String HeadIcon;
    public String Gender;


    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadIcon() {
        return HeadIcon;
    }

    public void setHeadIcon(String headIcon) {
        HeadIcon = headIcon;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
    public EditUserBody(){

    }

    public EditUserBody(String UserID, String Nickname, String HeadIco, String Gender){
        this.UserId = UserID;
        this.NickName = Nickname;
        this.HeadIcon = HeadIco;
        this.Gender = Gender;
    }






}
