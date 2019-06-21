package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

public class GetCopyBody implements Serializable {
    private String Title;

    public GetCopyBody(String title){
        this.Title = title;
    }
    public GetCopyBody(){

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
