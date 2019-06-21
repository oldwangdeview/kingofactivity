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
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.AliPayBean;
import com.scmsw.kingofactivity.bean.ChallengListData_listBean;
import com.scmsw.kingofactivity.bean.ChallengTaskListDataBean;
import com.scmsw.kingofactivity.bean.ChallengTaskdetailBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.ChallengTaskListBody;
import com.scmsw.kingofactivity.body.GetChallengTaskDetailBody;
import com.scmsw.kingofactivity.body.PayForAlibody;
import com.scmsw.kingofactivity.body.UpdatePkMoneyBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.UpdateMoney_DIalog_ClickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.UpdateMoneyDialog;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChallengeTaskDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView titlename;
    /**pk平定人**/
    @BindView(R.id.pingding_layout)
    LinearLayout pingding_layout;
    /***pk评定时间***/
    @BindView(R.id.pingding_time)
    LinearLayout pingding_time;
    /**确认人**/
    @BindView(R.id.data_queren_user_layout)
    LinearLayout layout_querenuser;
    /**确认时间***/
    @BindView(R.id.queren_time_layout)
    LinearLayout queren_time_layout;
    /***修改PK金***/
    @BindView(R.id.challengetaskdetail_button_updatemoney)
    Button updatePk_money;
    /**支付按钮***/
    @BindView(R.id.challengetaskdetail_button_pay)
    Button challengetaskdetail_button_pay;



    @BindView(R.id.task_name)
    TextView task_name;
    @BindView(R.id.time_text)
    TextView time_text;
    @BindView(R.id.task_message)
    TextView task_message;
    @BindView(R.id.gettask_type)
    TextView gettask_type;
    @BindView(R.id.fb_usernickname_layout)
    TextView fb_usernickname_layout;
    @BindView(R.id.pk_pingding_text)
    TextView pk_pingding_text;
    @BindView(R.id.pk_pingding_time)
    TextView pk_pingding_time;
    @BindView(R.id.jieguo_userdata)
    TextView jieguo_userdata;
    @BindView(R.id.queren_time_text)
    TextView queren_time_text;
    @BindView(R.id.pk_money)
    TextView pk_money;


    @BindView(R.id.faqizhif_layout)
    LinearLayout faqizhif_layout;
    @BindView(R.id.jiesour_zhifu_layout)
    LinearLayout jiesour_zhifu_layout;

    @BindView(R.id.faqi_money_type)
    TextView faqi_money_type;
    @BindView(R.id.jiesou_money_type)
    TextView jiesou_money_type;
    @BindView(R.id.image_type)
    ImageView image_type;


    private UpdateMoneyDialog mdialog;
    private ChallengListData_listBean clickdata;
    private ChallengTaskdetailBean nowdata;

    @Override
    protected void initView() {
      setContentView(R.layout.activity_challengetaskdetail);
    }

    @Override
    protected void initData() {
        super.initData();
        titlename.setText("任务详情");
        clickdata = (ChallengListData_listBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(clickdata!=null){
            getdata(clickdata.Detailid);
        }
    }

    /**
     * 修改PK金
     */
    @OnClick(R.id.challengetaskdetail_button_updatemoney)
    public void updatemoney(){

        if(mdialog==null){
            mdialog = new UpdateMoneyDialog.Builder(this).setcliklistrer(new UpdateMoney_DIalog_ClickLister() {
                @Override
                public void click(View v, String data,int choicetype,String data2) {
                    mdialog.dismiss();
                    UpdatePkMoney(data);
                }
            }).build();
        }else{
            mdialog.show();
        }
    }

    /**
     * 支付
     */

    @OnClick(R.id.challengetaskdetail_button_pay)
    public void payfoe(){
        payForali();
    }

    @OnClick({R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mContext, ChallengListData_listBean clicldata){
        Intent mIntent = new Intent(mContext,ChallengeTaskDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)clicldata);
        mContext.startActivity(mIntent);
    }

    private Dialog mLoadingDialog;
    private void getdata(String id){
        Observable observable =
                ApiUtils.getApi().GetChallengtasgDetail(new GetChallengTaskDetailBody(id))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ChallengTaskdetailBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<ChallengTaskdetailBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    update(stringStatusCode.getResult_Data());
                    nowdata = stringStatusCode.getResult_Data();
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    public void update(ChallengTaskdetailBean data){

        if(data!=null){
            if(!TextUtils.isEmpty(data.PKStatus)){

                if(data.PKStatus.equals("0")){
                    layout_querenuser.setVisibility(View.GONE);
                    queren_time_layout.setVisibility(View.GONE);
                    pingding_layout.setVisibility(View.GONE);
                    pingding_time.setVisibility(View.GONE);
                    if(clickdata.pktype.equals("1")) {
                        updatePk_money.setVisibility(View.VISIBLE);
                    }else{
                        updatePk_money.setVisibility(View.GONE);
                    }
                    faqizhif_layout.setVisibility(View.GONE);
                    jiesour_zhifu_layout.setVisibility(View.GONE);
                    image_type.setVisibility(View.GONE);
                }

                if(data.PKStatus.equals("1")){
                    layout_querenuser.setVisibility(View.GONE);
                    queren_time_layout.setVisibility(View.GONE);
                    pingding_layout.setVisibility(View.GONE);
                    pingding_time.setVisibility(View.GONE);
                    updatePk_money.setVisibility(View.GONE);
                    if(!TextUtils.isEmpty(data.FQpersonid)&&data.FQpersonid.equals(BaseActivity.getuser().UserId)&&!data.FBPersonMoneyState.equals("1")) {
                        challengetaskdetail_button_pay.setVisibility(View.VISIBLE);
                    }if(!data.JSPersonMoneyState.equals("1")){
                        challengetaskdetail_button_pay.setVisibility(View.VISIBLE);

                    }else{
                        challengetaskdetail_button_pay.setVisibility(View.GONE);

                    }
                    faqizhif_layout.setVisibility(View.VISIBLE);
                    jiesour_zhifu_layout.setVisibility(View.VISIBLE);
                    image_type.setVisibility(View.GONE);
                }
                if(data.PKStatus.equals("2")){
                    layout_querenuser.setVisibility(View.GONE);
                    queren_time_layout.setVisibility(View.GONE);
                    pingding_layout.setVisibility(View.GONE);
                    pingding_time.setVisibility(View.GONE);
                    updatePk_money.setVisibility(View.GONE);
                    challengetaskdetail_button_pay.setVisibility(View.GONE);
//                    challengetaskdetail_button_pay.setVisibility(View.GONE);
                    faqizhif_layout.setVisibility(View.GONE);
                    jiesour_zhifu_layout.setVisibility(View.GONE);
                    image_type.setVisibility(View.GONE);
                }
                if(data.PKStatus.equals("3")||data.PKStatus.equals("4")){
                    layout_querenuser.setVisibility(View.VISIBLE);
                    queren_time_layout.setVisibility(View.VISIBLE);
                    pingding_layout.setVisibility(View.VISIBLE);
                    pingding_time.setVisibility(View.VISIBLE);
                    updatePk_money.setVisibility(View.GONE);
                    challengetaskdetail_button_pay.setVisibility(View.GONE);
                    faqizhif_layout.setVisibility(View.VISIBLE);
                    jiesour_zhifu_layout.setVisibility(View.VISIBLE);
                    image_type.setVisibility(View.VISIBLE);
                }

            }


            if(!TextUtils.isEmpty(data.MissionName)){
                task_name.setText(data.MissionName);
            }else if(!TextUtils.isEmpty(clickdata.Missionname)){
                task_name.setText(clickdata.Missionname);
            }else{
                task_name.setText("");
            }

            if(!TextUtils.isEmpty(data.MissionStartDatetime)&&!TextUtils.isEmpty(data.MissionEndDatetime)){
                time_text.setText(UIUtils.gettime(data.MissionStartDatetime)+"-"+UIUtils.gettime(data.MissionEndDatetime));
            }else if(!TextUtils.isEmpty(clickdata.MissionStartDatetime)&&!TextUtils.isEmpty(clickdata.MissionEndDatetime)){
                time_text.setText(UIUtils.gettime(clickdata.MissionStartDatetime)+"-"+UIUtils.gettime(clickdata.MissionEndDatetime));
            }else{
                time_text.setText("");
            }

            if(!TextUtils.isEmpty(data.Description)){
                task_message.setText(data.Description);
            }else{
                task_message.setText("未添加描述");
            }

            if(!TextUtils.isEmpty(data.PKStatus)){
                gettask_type.setText(data.PKStatus.equals("0")?"未接受":data.PKStatus.equals("1")?"已接受":data.PKStatus.equals("2")?"已拒绝":data.PKStatus.equals("3")?"已接受":"");
            }else
            if(!TextUtils.isEmpty(clickdata.PKStatus)){
                gettask_type.setText(clickdata.PKStatus.equals("0")?"未接受":clickdata.PKStatus.equals("1")?"已接受":clickdata.PKStatus.equals("2")?"已拒绝":clickdata.PKStatus.equals("3")?"已接受":"");

            }else{
                gettask_type.setText("");
            }
            if(!TextUtils.isEmpty(data.FQpersonName)){
                fb_usernickname_layout.setText(data.FQpersonName);
            }else
            if(!TextUtils.isEmpty(data.FQpersonid)){
                fb_usernickname_layout.setText(data.FQpersonid);
            }else{
                fb_usernickname_layout.setText("");
            }

            if(!TextUtils.isEmpty(data.ResultAcceptPersonName)){
                pk_pingding_text.setText(data.ResultAcceptPersonName);
            }else{
                pk_pingding_text.setText("还未评定");
            }

            if(!TextUtils.isEmpty(data.ResultAcceptDate)) {
                pk_pingding_time.setText(data.ResultAcceptDate);
            }else{
                pk_pingding_time.setText("暂无评定时间");
            }
            if(!TextUtils.isEmpty(data.AcceptPersonName)){
                jieguo_userdata.setText(data.AcceptPersonName);
            }else{
                jieguo_userdata.setText("还未确认");
            }
            if(!TextUtils.isEmpty(data.AcceptDate)){
                queren_time_text.setText(data.AcceptDate);
            }else{
                queren_time_text.setText("暂无确认时间");
            }
            if(!TextUtils.isEmpty(data.PKAmount)){
                pk_money.setText(data.PKAmount);
            }else if(!TextUtils.isEmpty(clickdata.Amount)){
                pk_money.setText(clickdata.Amount);
            }else{
                pk_money.setText("");
            }


            if(!TextUtils.isEmpty(data.FBPersonMoneyState)&&data.FBPersonMoneyState.equals("1")){
                faqi_money_type.setText("已支付");
            }else{
                faqi_money_type.setText("未支付");
            }
            if(!TextUtils.isEmpty(data.JSPersonMoneyState)&&data.JSPersonMoneyState.equals("1")){
                jiesou_money_type.setText("已支付");

            }else{
                jiesou_money_type.setText("未支付");
            }

            if(!TextUtils.isEmpty(data.PKResult)){
                if(data.PKResult.equals("0")){
                    image_type.setImageResource(R.mipmap.jinxingz);
                }
                if(data.PKResult.equals("1")){
                    image_type.setImageResource(R.mipmap.shenli);
                }
                if(data.PKResult.equals("2")){
                    image_type.setImageResource(R.mipmap.shibai);
                }

            }

        }
    }




    private void UpdatePkMoney(String data){

        Observable observable =
                ApiUtils.getApi().UpdatePkMoney(new UpdatePkMoneyBody(BaseActivity.getuser().UserId,clickdata.PKid,data))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ChallengTaskdetailBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<ChallengTaskdetailBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    update(stringStatusCode.getResult_Data());
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);

    }

    private void payForali(){
        PayForAlibody payforali = new PayForAlibody();

        if(clickdata.pktype.equals("1")){
            payforali.type = "3";
            payforali.Numbers.add(clickdata.Masterid);
        }
        if(clickdata.pktype.equals("2")){
            payforali.type = "4";
            payforali.Numbers.add(clickdata.Detailid);
        }
        payforali.totalFee= !TextUtils.isEmpty(nowdata.PKAmount)?nowdata.PKAmount:!TextUtils.isEmpty(clickdata.Amount)?clickdata.Amount:clickdata.Amount;
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
                PayTask alipay = new PayTask(ChallengeTaskDetailActivity.this);
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
                                getdata(clickdata.Detailid);
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
}
