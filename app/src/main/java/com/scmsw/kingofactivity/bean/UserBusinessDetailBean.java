package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

/**
 *     "UserId": "ce001",
 *         "TotalBonusPaid": "已发送总奖金",
 *         "ExecutionBonus": "执行中奖金",
 *         "ChallengeBonus": "挑战中奖金"
 */

public class UserBusinessDetailBean implements Serializable {
    public String UserId;
    public String TotalBonusPaid;
    public String ExecutionBonus;
    public String ChallengeBonus;

}
