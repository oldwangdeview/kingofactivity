package com.scmsw.kingofactivity.bean;

import java.io.Serializable;

/**
 *   "Title": 资金头,
 *                 "Amount": 金额,
 *                 "Number":流水号
 *                 "PaymentMethod":支付方式,
 *                 "Remarks": 支付事件,
 *                 "Datatime": 支付时间
 */
public class Money_listdataBean implements Serializable {
    public String Title;
    public String Amount;
    public String Number;
    public String PaymentMethod;
    public String Remarks;
    public String Datatime;
}
