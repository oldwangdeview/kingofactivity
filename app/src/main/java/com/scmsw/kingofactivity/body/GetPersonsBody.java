package com.scmsw.kingofactivity.body;

import java.io.Serializable;

public class GetPersonsBody implements Serializable {
    public String Userid = "0";
    public String Filter;
    public String Filterid;

    public GetPersonsBody(String Filter,String Filterid,String Userid ){
        this.Filter = Filter;
        this.Filterid = Filterid;
        this.Userid = Userid;
    }
}
