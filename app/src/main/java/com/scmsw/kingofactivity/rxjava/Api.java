package com.scmsw.kingofactivity.rxjava;


import com.scmsw.kingofactivity.bean.AliPayBean;
import com.scmsw.kingofactivity.bean.ChallengTaskListDataBean;
import com.scmsw.kingofactivity.bean.ChallengTaskdetailBean;
import com.scmsw.kingofactivity.bean.ChoiceuserBean;
import com.scmsw.kingofactivity.bean.CompletBean;
import com.scmsw.kingofactivity.bean.EditUserBody;
import com.scmsw.kingofactivity.bean.EnclosuresByMissionidBean;
import com.scmsw.kingofactivity.bean.FeedbackListbean;
import com.scmsw.kingofactivity.bean.GetCopyBody;
import com.scmsw.kingofactivity.bean.GetMissionDetailsBean;
import com.scmsw.kingofactivity.bean.GetMissionDetailsBody;
import com.scmsw.kingofactivity.bean.GetMissionUserDetailsBean;
import com.scmsw.kingofactivity.bean.GetMissionUserDetailsBody;
import com.scmsw.kingofactivity.bean.GetMoenyBean;
import com.scmsw.kingofactivity.bean.GetMoenyBody;
import com.scmsw.kingofactivity.bean.GetPersonsBean;
import com.scmsw.kingofactivity.bean.GetcodeBody;
import com.scmsw.kingofactivity.bean.GetcopyBean;
import com.scmsw.kingofactivity.bean.HKLatitudesBean;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.LoginBody;
import com.scmsw.kingofactivity.bean.MissionComplianceBean;
import com.scmsw.kingofactivity.bean.MoneyBean;
import com.scmsw.kingofactivity.bean.RegisterBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBean;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBody;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.bean.UserdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBody;
import com.scmsw.kingofactivity.body.AddMissionAllocationBody;
import com.scmsw.kingofactivity.body.AuditBody;
import com.scmsw.kingofactivity.body.BangdingAliBody;
import com.scmsw.kingofactivity.body.ChallengTaskListBody;
import com.scmsw.kingofactivity.body.CompletBody;
import com.scmsw.kingofactivity.body.EnclosureMaster;
import com.scmsw.kingofactivity.body.EnclosuresByMissionidBody;
import com.scmsw.kingofactivity.body.FeedBackBody;
import com.scmsw.kingofactivity.body.GetAliPaybody;
import com.scmsw.kingofactivity.body.GetChallengTaskDetailBody;
import com.scmsw.kingofactivity.body.GetPersonsBody;
import com.scmsw.kingofactivity.body.GetTaskBody;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.body.GetUserBody;
import com.scmsw.kingofactivity.body.HuiKuiBody;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.body.MissionComplianceBody;
import com.scmsw.kingofactivity.body.MissionUserDetailsBody;
import com.scmsw.kingofactivity.body.MoneyBody;
import com.scmsw.kingofactivity.body.PayForAlibody;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.body.UpdatePasswordBody;
import com.scmsw.kingofactivity.body.UpdatePkMoneyBody;
import com.scmsw.kingofactivity.body.UploadDataBody;
import com.scmsw.kingofactivity.body.UplodPKBody;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 *
 */
public interface Api {
    boolean isRelease=false;
    /**发布地址*/
    String baseUrl="http://120.79.136.125/";
    /**测试地址*/
     String testBaseUrl="http://116.62.117.249:60039/";


     @POST("api/Login/CheckLogin")
     Observable<StatusCode<UserdataBean>> login(
             @Body LoginBody data
     );

     @POST("api/Login/Register")
     Observable<StatusCode<Object>> Register(
             @Body RegisterBody data);

     @POST("/api/Login/GetSecurityCode")
     Observable<StatusCode<Object>> getcode(
             @Body GetcodeBody data
     );

    /**
     * 首页文案
     * @param data
     * @return
     */
     @POST("/api/Homes/GetCopy")
     Observable<StatusCode<GetcopyBean>> GetCopy(
             @Body GetCopyBody data
     );

    /**
     * 首页金额
     * @param data
     * @return
     */
     @POST("/api/Homes/GetMony")
     Observable<StatusCode<GetMoenyBean>> GetMony(
             @Body GetMoenyBody data
     );

    /**
     * 修改接口
     * @param data
     * @return
     */
     @POST("/api/Login/Edit")
     Observable<StatusCode<UserdataBean>> EditUser(
             @Body EditUserBody data
     );

    /**
     * 7.榜单
     * @param data
     * @return
     */
     @POST("/api/Mission/GetMissionDetails")
     Observable<StatusCode<GetMissionDetailsBean>> GetMissionDetails(
             @Body GetMissionDetailsBody data
     );

    /**
     * 个人任务
     * @param data
     * @return
     */

     @POST("/api/Mission/GetMissionUserDetails")
     Observable<StatusCode<GetMissionUserDetailsBean>> GetMissionUserDetails(
             @Body GetMissionUserDetailsBody data
     );

    /**
     * 任务首页汇总金额
     * @param data
     * @return
     */

     @POST("/api/Mission/UserBusinessDetail")
     Observable<StatusCode<UserBusinessDetailBean>> UserBusinessDetail(
             @Body UserBusinessDetailBody data
     );

    /**
     * 用户未领取，挑战任务数据
     * @param data
     * @return
     */
     @POST("/api/MissionOriented/GetMissionUserDetails")
     Observable<StatusCode<UserdontgettaskListdataBean>> UserdontgettaskListdata(
             @Body UserdontgettaskListdataBody data
     );
    /**
     * 任务列表
     */
    @POST("/api/TaskFeedback/UserBusinessDetail")
    Observable<StatusCode<TaskListdataBean>> tasklistdata(
            @Body TasklistdataBody data
    );


    @POST("/api/PlayerKillingDetail/SelectPKDetailsByUserID")
    Observable<StatusCode<ChallengTaskListDataBean>> mychallengtasklistdata(
            @Body ChallengTaskListBody data
    );

    /**
     * 领取任务
     * @param data
     * @return
     */
    @POST("/api/MissionOriented/InsertMissionOreneted")
    Observable<StatusCode<Object>> mychallengtasklistdata(
            @Body GetTaskBody data
            );
    /**
     * 获取任务详情
     */
    @POST("/api/Mission/GetMissionByMissionid")
    Observable<StatusCode<UserDontGetTaskListDataListBean>> getTaskdetail(
            @Body GetTaskdetailBody data
    );

    /**
     * 下级部门
     */
    @POST("/api/keys/GetListbyid")
    Observable<StatusCode<LevelBean>> GetListbyid(
            @Body ListbyidBody data
    );

    /**
     * 获取pk详情
     * @param data
     * @return
     */
    @POST("/api/PlayerKillingDetail/GETPKDetailByid")
    Observable<StatusCode<ChallengTaskdetailBean>> GetChallengtasgDetail(
            @Body GetChallengTaskDetailBody data
    );

    @POST("/api/PlayerKillingDetail/UpdatePKDetail")
    Observable<StatusCode<Object>> UpdatePkMoney(
            @Body UpdatePkMoneyBody data
    );

    /***
     * 选择PK人
     */
    @POST("/api/Person/GetTypeDetail")
    Observable<StatusCode<ChoiceuserBean>> getchoiceuserlistdat(
            @Body GetUserBody data
    );

    /***
     * 发布PK
     */
    @POST("/api/PlayerKillingDetail/GetPKDetailsByUserID")
    Observable<StatusCode<Object>> passpk(
            @Body UplodPKBody data
    );

    @POST("/api/MissionOriented/updateMissionOrenetedState")
    Observable<StatusCode<Object>> aduditdata(
            @Body AuditBody data
    );

    @POST("/api/PayForAlipay/pay")
    Observable<StatusCode<AliPayBean>> payforali(
            @Body PayForAlibody data
    );

    @POST("/api/PayForAlipay/GetCapitalOrderRelationtextByUserid")
    Observable<StatusCode<MoneyBean>> GetCapitalOrderRelationtextByUserid(
            @Body MoneyBody data
    );

    @POST("/api/Person/UserBingding")
    Observable<StatusCode<Object>> UserBingding(
            @Body BangdingAliBody data
    );

    /***
     * /api/Mission/ADDMissionDetails
     */
    @POST("/api/Mission/ADDMissionDetails")
    Observable<StatusCode<Object>> ADDMissionDetails(
            @Body UploadDataBody data
    );


    @POST("/api/Person/UserBindByUserid")
    Observable<StatusCode<BangdingAliBody>> UserBindByUserid(
            @Body GetAliPaybody data
    );

    @POST("/api/Login/UpdatePassword")
    Observable<StatusCode<Object>> UpdatePassword(
            @Body UpdatePasswordBody data
    );


    @POST("/api/Mission/GetMissionComplianceByMissionid")
    Observable<StatusCode<MissionComplianceBean>> GetMissionComplianceByMissionid(
            @Body MissionComplianceBody data
    );

    @POST("/api/Mission/GetMissionUserDetails")
    Observable<StatusCode<UserdontgettaskListdataBean>> GetMissionUserDetails(
            @Body MissionUserDetailsBody data
    );

    @POST("/api/Person/GetPersons")
    Observable<StatusCode<GetPersonsBean>> GetPersons(
            @Body GetPersonsBody data
    );

    @POST("/api/Mission/AddMissionAllocation")
    Observable<StatusCode<Object>> AddMissionAllocation(
            @Body AddMissionAllocationBody data
    );


    @POST("/api/MissionLatitude/InsertEnclosureMaster")
    Observable<StatusCode<Object>> InsertEnclosureMaster(
            @Body FeedBackBody data
    );

    @POST("/api/MissionLatitude/GetEnclosureMaster")
    Observable<StatusCode<FeedbackListbean>> GetEnclosureMaster(
            @Body EnclosureMaster data
    );

    @POST("/api/MissionLatitude/GetHKLatitudes")
    Observable<StatusCode<HKLatitudesBean>> GetHKLatitudes(
            @Body HuiKuiBody data
    );
    @POST("/api/Mission/GetEnclosuresByMissionid")
    Observable<StatusCode<EnclosuresByMissionidBean>> GetEnclosuresByMissionid(
            @Body EnclosuresByMissionidBody data
    );

    @POST("/api/MissionLatitude/GetMissionLatitudeComplet")
    Observable<StatusCode<CompletBean>> GetMissionLatitudeComplet(
            @Body CompletBody data
    );
    @POST("/api/MissionLatitude/UpdateEnclosureMaster")
    Observable<StatusCode<Object>> getUpdateEnclosureMaster(
            @Body FeedBackBody data
    );
}


