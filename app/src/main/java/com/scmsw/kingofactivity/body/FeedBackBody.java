package com.scmsw.kingofactivity.body;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedBackBody implements Serializable {

    public List<FeedBackImageBody> ed = new ArrayList<>();
    public String Userid;
    public String EnclosureType;
    public String Busid;
    public String Comqty;
    public String ComStates;
    public String RPvalue;
    public String EnclosureMasterid;
    public String Memo;
}
