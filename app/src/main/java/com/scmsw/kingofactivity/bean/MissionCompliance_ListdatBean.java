package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

/**
 *     "RoleTargetType":规则对象类型,
 *                 "RoleTargetID": 规则对象id,
 *   "StandardType":1 固定奖金 2.目标比例
 *                 "StandardQty": 达标量,
 *                 "StandardRatio": 达标分配比例,
 *                 "StandardBonusAmount": 达标奖金,
 *      "OverfulfilType":1 固定奖金 2.目标比例
 *                 "OverfulfilQty": 超额量
 *                 "OverfulfilRatio": 超额分配比例,
 *                 "OverfulfilBonusAmount": 超额奖金
 */

public class MissionCompliance_ListdatBean implements Serializable {

    public String RoleTargetType;
    public String RoleTargetID = "0";
    public String StandardType = "0";
    public String StandardQty = "0";
    public String StandardRatio = "0";
    public String StandardBonusAmount ="0";
    public String OverfulfilType = "0";
    public String OverfulfilQty ="0";
    public String OverfulfilRatio = "0";
    public String OverfulfilBonusAmount = "0";

}
