package com.scmsw.kingofactivity.rxjava;

import com.scmsw.kingofactivity.bean.LoginBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserdataBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageLoadApi {
   String baseurl = "http://120.79.136.125/";

   @POST("/shushe/qiniu/getsecrettoken")
   Observable<StatusCode<UserdataBean>> login(
           @Body LoginBody data
   );
}
