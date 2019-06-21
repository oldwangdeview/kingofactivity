package com.scmsw.kingofactivity.eventbus;

import com.scmsw.kingofactivity.bean.UserBusinessDetailBean;
import com.scmsw.kingofactivity.bean.UserdataBean;

public class UpdateUserEvent {

    public UserdataBean userdata;
    public UpdateUserEvent(UserdataBean userdata){
        this.userdata = userdata;
    }
}
