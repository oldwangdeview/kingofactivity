package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserdontgettaskListdataBean implements Serializable {

    public String Userid;
    public String MissStatus;
    public List<UserDontGetTaskListDataListBean> umd = new ArrayList<>();

}
