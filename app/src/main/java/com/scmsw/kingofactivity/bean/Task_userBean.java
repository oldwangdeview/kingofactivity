package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task_userBean implements Serializable {
    public String title;
    public String id;
    public List<Level_ListdataBean> dataliste = new ArrayList<>();
}
