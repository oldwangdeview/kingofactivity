package com.scmsw.kingofactivity.body;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddMissionAllocationBody implements Serializable {

    public List<AddMissionAllocation_listBody> imas = new ArrayList<>();
    public String Userid;
    public String Missionid;

}
