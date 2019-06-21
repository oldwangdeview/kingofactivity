package com.scmsw.kingofactivity.body;

import com.scmsw.kingofactivity.bean.HKLatitudesListData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UploadDataBody implements Serializable {

    public UploadmmdBody mmd;
//    public List<QianzhiBody> imas = new ArrayList<>();
    public List<MCSUploaddataBody> mcs = new ArrayList<>();
    public List<IMFSBody> imfs = new ArrayList<>();
    public List<HKLatitudesListData> iml = new ArrayList<>();
    public String Userid;
}
