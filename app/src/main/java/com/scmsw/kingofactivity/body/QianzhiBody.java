package com.scmsw.kingofactivity.body;

import java.io.Serializable;

/**
 *     "DistTargetID":强制分配级别id,
 *                 "personid": 负责人名,
 *                 "MissionDistQty": 分配量
 */
public class QianzhiBody implements Serializable {
    public String DistTargetID;
    public String personid;
    public String MissionDistQty;

}
