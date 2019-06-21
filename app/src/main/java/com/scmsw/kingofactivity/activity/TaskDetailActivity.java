package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.TaskDetailAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.AliPayBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.body.PayForAlibody;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TaskDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.heandimage)
    ImageView heandimage;

    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.task_type)
    TextView task_type;
    @BindView(R.id.yejimubiao_text)
    TextView yejimubiao_text;
    @BindView(R.id.linquliang_text)
    TextView linquliang_text;

    /**业绩目标***/
    @BindView(R.id.yejimubiao_layout)
    LinearLayout yejimubiao_layout;
    /**完成业绩额***/
    @BindView(R.id.wanchengyejie_layout)
    LinearLayout wanchengyejie_layout;
    /**领取量****/
    @BindView(R.id.linquliang_layout)
    LinearLayout linquliang_layout;

    @BindView(R.id.pk_money_layout)
    LinearLayout pk_money_layout;
    /**任务奖金**/
    @BindView(R.id.task_money)
    LinearLayout task_money;
    /**超额奖金**/
    @BindView(R.id.chaoe_money)
    LinearLayout chaoe_money;

    @BindView(R.id.task_name)
    TextView task_name;
    @BindView(R.id.wanchengyeji_text)
    TextView wanchengyeji_text;
    @BindView(R.id.pkmoney_text)
    TextView pkmoney_text;
    @BindView(R.id.baozhen_money)
    TextView baozhen_money;
    @BindView(R.id.time_text)
    TextView time_text;
    @BindView(R.id.adduser)
    TextView adduser;
    @BindView(R.id.task_money1)
    TextView task_money1;
    @BindView(R.id.choae_money)
    TextView choae_money;
    @BindView(R.id.task_message)
    TextView task_message;

    @BindView(R.id.mygridview)
    MyGridView mygridview;

    @BindView(R.id.ojbk_btn)
    Button ojbk_btn;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.progress_data)
    TextView progress_data;

    @BindView(R.id.zhutask_name)
    LinearLayout zhutask_name;
    @BindView(R.id.zhutask_name_text)
    TextView zhutask_name_text;

    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;



    private UserDontGetTaskListDataListBean taskdatadetail;
    private Dialog mLoadingDialog;
    private TaskListData_ListBean clickdata;
    private UserDontGetTaskListDataListBean clickdata2;
    private String index_type;
    private int inddex_type = -1;
    private TaskDetailAdpater madpater;
    private List<TaskListData_ListBean> griddata = new ArrayList<>();
    @Override
    protected void initView() {
       setContentView(R.layout.activity_taskdetails);
    }

    public static void startactivity(Context mCOntext , TaskListData_ListBean clicldata,String type){
        Intent mIntent = new Intent(mCOntext,TaskDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)clicldata);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mCOntext.startActivity(mIntent);
    }

    public static void startactivity(Context mcontext,UserDontGetTaskListDataListBean choicedata ,String type){
        Intent mIntente = new Intent(mcontext,TaskDetailActivity.class);
        mIntente.putExtra(Contans.USER_DATA,(Serializable)choicedata);
        mIntente.putExtra(Contans.INTENT_TYPE,type);
        mIntente.putExtra("mtype",3);

        mcontext.startActivity(mIntente);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("任务详情");

        tv_small_title_layout_head.setText("维度回馈");
        clickdata =(TaskListData_ListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(clickdata!=null){
            tv_small_title_layout_head.setVisibility(View.VISIBLE);
        }
        index_type = getIntent().getStringExtra(Contans.INTENT_TYPE);
        inddex_type = getIntent().getIntExtra("mtype",-1);
        madpater = new TaskDetailAdpater(mcontent,griddata);
        mygridview.setAdapter(madpater);
        if(clickdata!=null){
            getTaskDetail(clickdata.Missionid);
        }
        if(inddex_type>0){
            clickdata2 = (UserDontGetTaskListDataListBean)getIntent().getSerializableExtra(Contans.USER_DATA);
            tv_small_title_layout_head.setVisibility(View.VISIBLE);
        }

        if(clickdata2!=null){
            updateview2(clickdata2);
        }
//        if()
    }

    /**
     * 获取任务详情
     * @param missionID
     */
    private void getTaskDetail(String missionID){
        Observable observable =
                ApiUtils.getApi().getTaskdetail(new GetTaskdetailBody(missionID))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mcontent, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserDontGetTaskListDataListBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<UserDontGetTaskListDataListBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    taskdatadetail = stringStatusCode.getResult_Data();
                    updayedata(taskdatadetail);
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    /**
     * 获取任务详情
     * @param missionID
     */
    private void getTaskDetail(String missionID,boolean type){
        Observable observable =
                ApiUtils.getApi().getTaskdetail(new GetTaskdetailBody(missionID))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mcontent, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserDontGetTaskListDataListBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<UserDontGetTaskListDataListBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    zhutask_name.setVisibility(View.VISIBLE);
                    taskdatadetail = stringStatusCode.getResult_Data();
//                    updayedata(taskdatadetail);
                    if(!TextUtils.isEmpty(taskdatadetail.MissionName)){
                        zhutask_name_text.setText(taskdatadetail.MissionName);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }



    public void updayedata(UserDontGetTaskListDataListBean data){
        if(data!=null){

            if(!TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)){
                UIUtils.loadImageViewRoud(mcontent,BaseActivity.getuser().HeadIcon,heandimage,UIUtils.dip2px(30),R.mipmap.mrtx);
            }
            if(!TextUtils.isEmpty(BaseActivity.getuser().RealName)){
                nickname.setText(BaseActivity.getuser().RealName);
            }else
            if(!TextUtils.isEmpty(BaseActivity.getuser().NickName)){
                nickname.setText(BaseActivity.getuser().NickName);
            }else if(!TextUtils.isEmpty(BaseActivity.getuser().Account)){
                nickname.setText(BaseActivity.getuser().Account);
            }else{
                nickname.setText("");
            }
            zhutask_name.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(index_type)){
                task_type.setText(index_type);

                if(index_type.equals("未完成")){
                    linquliang_layout.setVisibility(View.GONE);
                    yejimubiao_layout.setVisibility(View.VISIBLE);
                    wanchengyejie_layout.setVisibility(View.VISIBLE);
                    task_money.setVisibility(View.VISIBLE);
                    chaoe_money.setVisibility(View.VISIBLE);
                    if(!TextUtils.isEmpty(clickdata.GuaranteeMoneyDetailState)&&clickdata.GuaranteeMoneyDetailState.equals("1")){
                        ojbk_btn.setVisibility(View.VISIBLE);
                    }

                }
                if(index_type.equals("未过审")){
                    yejimubiao_layout.setVisibility(View.GONE);
                    wanchengyejie_layout.setVisibility(View.GONE);
                    chaoe_money.setVisibility(View.GONE);
                    linquliang_layout.setVisibility(View.VISIBLE);
                    task_money.setVisibility(View.VISIBLE);


                }

                if(index_type.equals("已完成")){
                    linquliang_layout.setVisibility(View.GONE);
                    yejimubiao_layout.setVisibility(View.VISIBLE);
                    wanchengyejie_layout.setVisibility(View.VISIBLE);
                    task_money.setVisibility(View.VISIBLE);
                    chaoe_money.setVisibility(View.VISIBLE);
                }

                if(index_type.equals("已作废")){
                    yejimubiao_layout.setVisibility(View.GONE);
                    wanchengyejie_layout.setVisibility(View.GONE);
                    chaoe_money.setVisibility(View.GONE);
                    linquliang_layout.setVisibility(View.VISIBLE);
                    task_money.setVisibility(View.GONE);
                }

            }


            if(!TextUtils.isEmpty(data.MissionName)){
                task_name.setText(data.MissionName);
            }

            if(!TextUtils.isEmpty(clickdata.MissionQty)){
                yejimubiao_text.setText(clickdata.MissionQty);
            }

            if(!TextUtils.isEmpty(clickdata.ComQty)){
                wanchengyeji_text.setText(clickdata.ComQty);
            }else{
                wanchengyeji_text.setText("");
            }

            if(!TextUtils.isEmpty(clickdata.MissionQty)){
                linquliang_text.setText(clickdata.MissionQty);
            }else{
                linquliang_text.setText("");
            }


            if(!TextUtils.isEmpty(data.PkAmount)){
                pkmoney_text.setText(data.PkAmount);
            }else{
                pkmoney_text.setText("");
            }

            if(!TextUtils.isEmpty(data.GuaranteeMoney)){
                baozhen_money.setText(data.GuaranteeMoney);
            }else{
                baozhen_money.setText("");
            }

            if(!TextUtils.isEmpty(data.MissionStartDatetime)&&!TextUtils.isEmpty(data.MissionEndDatetime)){
                time_text.setText(UIUtils.gettime(data.MissionStartDatetime)+"-"+UIUtils.gettime(data.MissionEndDatetime));
            }else if(!TextUtils.isEmpty(clickdata.MissionStartDatetime)&&!TextUtils.isEmpty(clickdata.MissionEndDatetime)){
                time_text.setText(UIUtils.gettime(clickdata.MissionStartDatetime)+"-"+UIUtils.gettime(clickdata.MissionEndDatetime));

            }else{
                time_text.setText("");
            }

            if(!TextUtils.isEmpty(clickdata.fbpersonname)){
                adduser.setText(clickdata.fbpersonname);
            }else
            if(!TextUtils.isEmpty(data.FBpersonid)){
                adduser.setText(data.FBpersonid);
            }else{
                adduser.setText("");
            }

            if(!TextUtils.isEmpty(data.RecAmountTotal)){
                task_money1.setText(data.RecAmountTotal);
            }else{
                task_money1.setText("");
            }
            if(!TextUtils.isEmpty(data.OverfulfilAmountTotal)){
                choae_money.setText(data.OverfulfilAmountTotal);
            }else{
                choae_money.setText("");
            }
           if(!TextUtils.isEmpty(data.MissionDescribe)){
               task_message.setText(data.MissionDescribe);
           }else{
               task_message.setText("");
           }

           if(!TextUtils.isEmpty(clickdata.ComQty)&&!TextUtils.isEmpty(clickdata.MissionQty)){
                try{
                    Double text1 = Double.parseDouble(clickdata.ComQty);
                    Double text2 = Double.parseDouble(clickdata.MissionQty);

                    double index = (text1/text2)*100;
                    progressbar.setProgress((int)index);
                    progress_data.setVisibility(View.VISIBLE);
                    progress_data.setText(String.format("%.2f", index)+"%");

                }catch (Exception e){
                    e.printStackTrace();
                }
           }
        }

    }

    public void updateview2(UserDontGetTaskListDataListBean data){
        if(!TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)){
            UIUtils.loadImageViewRoud(mcontent,BaseActivity.getuser().HeadIcon,heandimage,UIUtils.dip2px(30),R.mipmap.mrtx);
        }
        if(!TextUtils.isEmpty(BaseActivity.getuser().NickName)){
            nickname.setText(BaseActivity.getuser().NickName);
        }else if(!TextUtils.isEmpty(BaseActivity.getuser().Account)){
            nickname.setText(BaseActivity.getuser().Account);
        }else{
            nickname.setText("");
        }
        wanchengyejie_layout.setVisibility(View.GONE);
        linquliang_layout.setVisibility(View.GONE);
        pk_money_layout.setVisibility(View.GONE);
        task_money.setVisibility(View.GONE);
        chaoe_money.setVisibility(View.GONE);
        if(data!=null) {
            if (!TextUtils.isEmpty(data.MissionName)) {
                task_name.setText(data.MissionName);
            }


            if (!TextUtils.isEmpty(data.MissionStartDatetime) && !TextUtils.isEmpty(data.MissionEndDatetime)) {
                time_text.setText(UIUtils.gettime(data.MissionStartDatetime) + "-" + UIUtils.gettime(data.MissionEndDatetime));
            }
            if (!TextUtils.isEmpty(data.MissionDescribe)) {
                task_message.setText(data.MissionDescribe);
            } else {
                task_message.setText("");
            }

            if(!TextUtils.isEmpty(data.FBpersonName)){
                adduser.setText(data.FBpersonName);
            }else
            if (!TextUtils.isEmpty(data.FBpersonid)) {
                adduser.setText(data.FBpersonid);
            } else {
                adduser.setText("");
            }
            if (!TextUtils.isEmpty(data.MissionTargetQuantized)) {
                yejimubiao_text.setText(data.MissionTargetQuantized);
            } else {
                yejimubiao_text.setText("");
            }

            if(!TextUtils.isEmpty(data.MissionType)){
                task_type.setText(data.MissionType.equals("1")?"分配":data.MissionType.equals("2")?"自由领取":"");
            }
            mygridview.setVisibility(View.VISIBLE);

            if(!TextUtils.isEmpty(data.ParentMissionID)) {
                gettasklistdata(data.ParentMissionID);
                getTaskDetail(data.ParentMissionID,false);
            }


        }

    }



    private void gettasklistdata(String missoonid){

        Observable observable =
                ApiUtils.getApi().tasklistdata(new TasklistdataBody(BaseActivity.getuser().UserId,"我发布审核",missoonid))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mcontent, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<TaskListdataBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<TaskListdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().tfb!=null&&stringStatusCode.getResult_Data().tfb.size()>0){

                        griddata.clear();;
                        griddata.addAll(stringStatusCode.getResult_Data().tfb);
                        madpater.notifyDataSetChanged();
                    }


                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    @OnClick(R.id.ojbk_btn)
    public void payforali(){
        payForali();
    }

    private void payForali(){
        PayForAlibody payforali = new PayForAlibody();

        if(taskdatadetail.MissionType.equals("1")){
            payforali.type = "1";

        }
        if(taskdatadetail.MissionType.equals("2")){
            payforali.type = "2";

        }
        payforali.Numbers.add(clickdata.Feedbackid);
        payforali.totalFee= !TextUtils.isEmpty(clickdata.GuaranteeMoney)?clickdata.GuaranteeMoney:"";
        payforali.UserID = BaseActivity.getuser().UserId;
        payforali.body = "adsada";
//        payforali.totalFee = "0.01";

        Observable observable =
                ApiUtils.getApi().payforali(payforali)
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mcontent, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<AliPayBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<AliPayBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().codeStr)){
                    payforali(stringStatusCode.getResult_Data().codeStr);
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }


    private void payforali(final String orderinfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(TaskDetailActivity.this);
                Map<String, String> result = alipay.payV2(orderinfo, true);

                Message msg = new Message();
                msg.what = 101;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Map<String, String> result = ((Map<String, String>) msg.obj);
            new LogUntil(mcontent, TAG, "result_" + new Gson().toJson(result));
            String requestcode = result.get("resultStatus");
            if (!TextUtils.isEmpty(requestcode)) {
                try {

                    switch (Integer.parseInt(requestcode)) {
                        case 9000:

                            ToastUtils.makeText("支付宝支付成功");
                            if(clickdata!=null){
                             finish();
                            }
//                            PayforSesscesActivity.startactivity(OrderDetailActivity.this,mdata.month);
                            break;
                        default:
                            ToastUtils.makeText("支付失败");
                            break;
                    }

                } catch (Exception e) {

                }
            }
        }

        ;
    };

    @OnClick(R.id.zhutask_name)
    public void onclickdata(){
        TaskDetailActivity.startactivity(this,this.taskdatadetail,"未完成");
    }


    @OnClick(R.id.tv_small_title_layout_head)
    public void gitioweiduactivity(){
        if(clickdata!=null) {
            DimensionActivity.startactivity(mcontent, clickdata, 2);
        }
        if(clickdata2!=null){
            DimensionActivity.startactivity(mcontent, clickdata2, 3);
        }
    }
}
