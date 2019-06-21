package com.scmsw.kingofactivity.eventbus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateImagePathEvent implements Serializable {
    public List<String> filepath = new ArrayList<>();
    public UpdateImagePathEvent(List<String> imagepath){
        filepath.clear();
        filepath.addAll(imagepath);
    }
}
