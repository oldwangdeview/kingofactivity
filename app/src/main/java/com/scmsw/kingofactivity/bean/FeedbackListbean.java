package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedbackListbean implements Serializable {

    public List<EnclosureMasterBean> elm = new ArrayList<>();
    public String EnclosureType;
    public String Busid;
    public String Status;
    public String ComStates;

}
