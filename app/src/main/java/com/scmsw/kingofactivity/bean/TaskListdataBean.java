package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskListdataBean implements Serializable {
    public String UserID;
    public String Status;
    public List<TaskListData_ListBean> tfb = new ArrayList<>();
}
