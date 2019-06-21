package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetPersonsBean implements Serializable {


    public List<GetPersons_ListBean> per = new ArrayList<>();
    public String Userid;
    public String Filter;
    public String Filterid;

}
