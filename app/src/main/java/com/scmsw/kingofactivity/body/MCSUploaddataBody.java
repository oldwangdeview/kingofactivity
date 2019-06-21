package com.scmsw.kingofactivity.body;

import java.io.Serializable;

/**
 *     "RoleTargetType": 达标规则类型
 *                 "RoleTargetID":规则id
 *                 "StandardQty": 达标量,      达标目标量
 *                 "StandardRatio": 比例达标分配数值
 *                 "StandardBonusAmount": 固定达标分配数值,
 *                 "OverfulfilQty": 超额量,
 *                 "OverfulfilRatio": 超额分配数值,
 *                  "OverfulfilBonusAmount": 固定分配数值
 */

public class MCSUploaddataBody implements Serializable {
    public String RoleTargetType;
    public String  RoleTargetID;
    public String StandardQty;
    public String StandardRatio;
    public String StandardBonusAmount;
    public String OverfulfilQty;
    public String OverfulfilRatio;
    public String OverfulfilBonusAmount;

}
