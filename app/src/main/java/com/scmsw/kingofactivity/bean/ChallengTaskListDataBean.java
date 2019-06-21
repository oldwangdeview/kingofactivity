package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChallengTaskListDataBean implements Serializable {
           public String Userid;
           public String Status;
           public List<ChallengListData_listBean> pkd = new ArrayList<>();
}
