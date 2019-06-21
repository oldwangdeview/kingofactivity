package com.scmsw.kingofactivity.eventbus;

import com.scmsw.kingofactivity.bean.MissionCompliance_ListdatBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event_Data implements Serializable {
   public  List<MissionCompliance_ListdatBean> listdata = new ArrayList<>();
}
