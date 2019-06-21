package com.scmsw.kingofactivity.bean;

public class StatusCode2<T> {
    private int number;// 状态码  状态码状态 0成功true 1失败false
    private boolean flag = false;//
    private String msg;// 状态码值
    private String Result_Message;// 状态码详细值
    private T data;


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult_Message() {
        return Result_Message;
    }

    public void setResult_Message(String result_Message) {
        Result_Message = result_Message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "number='" + number + '\'' +
                ", flag=" + flag +
                ", msg='" + msg + '\'' +
                ", detailMsg='" + Result_Message + '\'' +
                ", data=" + data +
                '}';
    }
}
