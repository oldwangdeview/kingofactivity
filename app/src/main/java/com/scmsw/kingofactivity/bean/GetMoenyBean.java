package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

/**
 *    "UserId": "ce001",
 *         "TotalSum": "总金额",
 *         "TaskGold": "任务金",
 *         "ChallengeGold": "挑战金",
 *         "AvailableGold": "可用金",
 *         "TaskScaling": "任务达标量",
 *         "TaskTargetQuantity": "任务目标量",
 *         "TaskReleaseVolume": "任务发布量"
 */
public class GetMoenyBean implements Serializable {
    public String UserId;
    public String TotalSum;
    public String TaskGold;
    public String ChallengeGold;
    public String AvailableGold;
    public String TaskScaling;
    public String TaskTargetQuantity;
    public String TaskReleaseVolume;
}
