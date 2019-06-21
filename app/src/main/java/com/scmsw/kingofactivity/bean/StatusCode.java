package com.scmsw.kingofactivity.bean;

/**
 * Created by oldwang on 2019/1/2 0002.
 */


public class StatusCode<T> {
    private int Result_Code;// 状态码  状态码状态 0成功true 1失败false
    private boolean flag = false;//
    private String msg;// 状态码值
    private String Result_Message;// 状态码详细值
    private T Result_Data;




    public int getResult_Code() {
        return Result_Code;
    }

    public void setResult_Code(int result_Code) {
        Result_Code = result_Code;
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

    public T getResult_Data() {
        return Result_Data;
    }

    public void setResult_Data(T result_Data) {
        Result_Data = result_Data;
    }

    @Override
    public String toString() {
        return "StatusCode{" +
                "number='" + Result_Code + '\'' +
                ", flag=" + flag +
                ", msg='" + msg + '\'' +
                ", detailMsg='" + Result_Message + '\'' +
                ", data=" + Result_Data +
                '}';
    }
}
