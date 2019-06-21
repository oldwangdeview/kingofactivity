package com.scmsw.kingofactivity.http;


import android.util.Log;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.application.BaseApplication;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.ExitLoginSuccess;

import org.greenrobot.eventbus.EventBus;



/**
 * Created by oldwang on 2016/10/10 11:52.
 */

public class ApiException extends RuntimeException {
    private static String message;


    public ApiException(int number, StatusCode resultCode) {
        this(getApiExceptionMessage(number, resultCode));

    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @param resultCode
     * @param
     * @return
     */
    private static String getApiExceptionMessage(int code, StatusCode resultCode) {
        Log.e("resultCode",new Gson().toJson(resultCode));
        switch (code) {
            case 0:
                message = resultCode.getResult_Message();
                break;
            default:
                message = resultCode.getResult_Message();
        }
        return message;
    }
}
