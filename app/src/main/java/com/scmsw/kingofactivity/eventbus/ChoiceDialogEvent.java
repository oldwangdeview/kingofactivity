package com.scmsw.kingofactivity.eventbus;

import java.io.Serializable;

public class ChoiceDialogEvent implements Serializable {
    public int index;
    public int type;
    public  ChoiceDialogEvent(int index,int type){
        this.index = index;
        this.type = type;
    }
}
