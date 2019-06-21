package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelBean implements Serializable {
       public String Titlename;
       public String Selectedid;
       public List<Level_ListdataBean> kns = new ArrayList<>();
}
