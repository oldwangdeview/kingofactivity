package com.scmsw.kingofactivity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  "md": [
 *             {
 *                 "Userid": "Userid1",
 *                 "UserAccount": "UserAccount1",
 *                 "UserNickName": "UserNickName1",
 *                 "UserHeadIcon": "UserHeadIcon1",
 *                 "Number": "Number1"
 *             },
 *             {
 *                 "Userid": "Userid2",
 *                 "UserAccount": "UserAccount2",
 *                 "UserNickName": "UserNickName2",
 *                 "UserHeadIcon": "UserHeadIcon2",
 *                 "Number": "Number2"
 *             }
 *         ],
 *         "FUserid": "acc25328-5859-4089-9002-5b546a7d27f9",
 *         "Ftitle": "任务达成",
 *         "ScreenID": "1"
 */
public class GetMissionDetailsBean implements Serializable {

    public String FUserid;
    public String Ftitle;
    public String ScreenID;

    public List<GetMissionDetailsListDataBean> md = new ArrayList<>();
    public GetMissionDetailsListDataBean Usermd;

}
