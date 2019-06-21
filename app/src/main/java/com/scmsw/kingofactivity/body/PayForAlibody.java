package com.scmsw.kingofactivity.body;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PayForAlibody implements Serializable {
    public String type;
    public List<String> Numbers = new ArrayList<>();
    public String totalFee;
    public String UserID;
    public String body;
    public String Paytype = 1+"";


}
