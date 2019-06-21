package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceLevelAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.ChoiceTask_dataBean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.MissionCompliance_ListdatBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.body.IMFSBody;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.body.MCSUploaddataBody;
import com.scmsw.kingofactivity.body.QianzhiBody;
import com.scmsw.kingofactivity.body.UploadDataBody;
import com.scmsw.kingofactivity.body.UploadmmdBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.DeleteWeiduDataEvent;
import com.scmsw.kingofactivity.eventbus.Event_Data;
import com.scmsw.kingofactivity.eventbus.UpdateTAskListEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.view.ChoiceChallengTaskDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReleaseTaskIngActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.pk_layout)
    LinearLayout pk_layout;
    @BindView(R.id.bzj_layout)
    LinearLayout bzj_layout;
    @BindView(R.id.switch_layout1)
    Switch switech_btn1;
    @BindView(R.id.switch_layout2)
    Switch switech_btn2;
    /**PK金上限**/
    @BindView(R.id.pk_maxmoney)
    EditText pk_max_money;
    /**保证金**/
    @BindView(R.id.bzj_money)
    EditText bzj_money;
    @BindView(R.id.choice_nexlevel)
    TextView choice_nexlevel;
    private Dialog mLoadingDialog;
    List<Level_ListdataBean> levellistdata = new ArrayList<>();
    private List<Level_ListdataBean> clickdata = new ArrayList<>();
    private ChoiceLevelAdpater choicleveladpater;
    //选择任务列表弹窗
    private Dialog choicetaskdialog;
    private RelessTaskOneBody intentedata = null;

    private List<ChoiceTask_dataBean> choicelistdat = new ArrayList<>();
    private List<MissionCompliance_ListdatBean> guizedata = new ArrayList<>();
    private List<HKLatitudesListData>  weidulist = new ArrayList<>();
    @Override
    protected void initView() {

        setContentView(R.layout.activity_releasetasking);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        pk_max_money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        bzj_money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_title_activity_baseperson.setText("发布任务");
        intentedata = (RelessTaskOneBody)getIntent().getSerializableExtra(Contans.INTENT_DATA);



        if(!TextUtils.isEmpty(intentedata.MissionIsPK)){
            if(intentedata.MissionIsPK.equals("0")){
                switech_btn1.setChecked(false);
            }else if(intentedata.MissionIsPK.equals("1")){
                switech_btn1.setChecked(true);
            }
        }

        if(!TextUtils.isEmpty(intentedata.PkAmount)){
            pk_max_money.setText(intentedata.PkAmount);
        }

        if(!TextUtils.isEmpty(intentedata.GuaranteeMoneyState)){
            if(intentedata.GuaranteeMoneyState.equals("0")){
                switech_btn2.setChecked(false);
            }else if(intentedata.GuaranteeMoneyState.equals("1")){
                switech_btn2.setChecked(true);
            }
        }

        if(!TextUtils.isEmpty(intentedata.GuaranteeMoney)){
            bzj_money.setText(intentedata.GuaranteeMoney);
        }


        getlevellistdat();
    }


    /**
     * 选择下级
     */
    @OnClick(R.id.choice_lower_lever_layout)
    public void choice_lower_leverlayout(){
        if(levellistdata.size()>0&&choicleveladpater!=null){
            if(choicetaskdialog==null) {
                choicetaskdialog = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(choicleveladpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickdata = choicleveladpater.getclickdata();
                        choice_nexlevel.setText(getnextlevel(clickdata));
                        choicetaskdialog.dismiss();
                    }

                }).Builder();
            }
            choicetaskdialog.show();
        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        switech_btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pk_layout.setVisibility(View.VISIBLE);
                }else{
                    pk_layout.setVisibility(View.GONE);
                }
            }
        });
        switech_btn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bzj_layout.setVisibility(View.VISIBLE);
                }else{
                    bzj_layout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getlevellistdat(){
        Observable observable =
                ApiUtils.getApi().GetListbyid(new ListbyidBody("过滤","0"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LevelBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<LevelBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    if(stringStatusCode.getResult_Data().kns.size()>0){
                        levellistdata.clear();
                        levellistdata.addAll(stringStatusCode.getResult_Data().kns);
                        choicleveladpater = new ChoiceLevelAdpater(mcontent,levellistdata);
                        choicleveladpater.settasktype(intentedata.tasktype);
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


    public static void startactivity(Context mContext, RelessTaskOneBody data){
        Intent mintent = new Intent(mContext,ReleaseTaskIngActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,(Serializable) data);
        mContext.startActivity(mintent);
    }


    /***
     * 达标规则
     */
    @OnClick(R.id.choice_user_layout)
    public void choiceuser(){

        if(clickdata!=null&&clickdata.size()>0) {
            if(intentedata.tasktype==2) {
                CoiceUserForTaskActivity.startactivity(mcontent, clickdata, intentedata);
            }else{

                CompliancerulesActivity.startactivity(mcontent,clickdata,intentedata,guizedata);
            }
        }else{

            ToastUtils.makeText("请先选择下级类型");
            return ;
        }

    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    private String getnextlevel(List<Level_ListdataBean> listdata){
        String data = "";
        if(listdata.size()>0){
            for(int i=0;i<listdata.size();i++){
                data = data+listdata.get(i).name+" ";
            }
        }

        return data;
    };
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateuser(List<ChoiceTask_dataBean> mchoicelistdat){
        new LogUntil(mcontent,TAG,new Gson().toJson(mchoicelistdat));
        if(intentedata.tasktype==2){
        choicelistdat.clear();;
        choicelistdat.addAll(mchoicelistdat);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void choicefenpeidata(Event_Data data){
        guizedata.clear();
        guizedata.addAll(data.listdata);

    }

    @OnClick(R.id.ojbk_btn)
    public void ojbk_btndata(){
        UploadDataBody uploaddatabody = new UploadDataBody();
        UploadmmdBody mmddata = new UploadmmdBody();
        mmddata.MissionName = intentedata.Task_name;
        mmddata.MissionStatus = "1";
        mmddata.Missionlevel = intentedata.tasklevel==1?"2":"1";
        String time = intentedata.starttime.replace("-","/");
        mmddata.MissionStartDatetime = time.replace("—","/");
        String time2 = intentedata.starttime.replace("-","/");
        mmddata.MissionEndDatetime = time2.replace("—","/");
        mmddata.MissionTargetUnit = intentedata.taskdata.MissionTargetUnit;
        mmddata.MissionTargetQuantized  = intentedata.taskdata.MissionQty;
        mmddata.MissionType = intentedata.tasktype+"";
        mmddata.LimitTargetType = intentedata.tasktype==1?clickdata.get(0).ID:"0";
        mmddata.TargetType = intentedata.taskdatadetail.TargetType;
        mmddata.TargetID = intentedata.taskdatadetail.TargetID;

        mmddata.RecMinQty = intentedata.tasktype==2?intentedata.min_size:"0";
        mmddata.RecMixQty = intentedata.tasktype==2?intentedata.max_size:"0";

        mmddata.RecAmountTotal = intentedata.tasktype==2?intentedata.taskdatadetail.RecAmountTotal:"0";
        mmddata.RecAmountType = intentedata.tasktype==2?intentedata.inputmoney_type+"":"0";
        mmddata.MissionAmountTotal = intentedata.tasktype==1?intentedata.taskdatadetail.MissionAmountTotal:"0";
        mmddata.MissionAmountType = intentedata.tasktype==1?intentedata.choiceintmoey_type1+"":"0";

        mmddata.OverfulfilAmountTotal = intentedata.tasktype==1?intentedata.taskdatadetail.OverfulfilAmountTotal:"0";
        mmddata.OverfulfilAmountType = intentedata.tasktype==1?intentedata.choiceintmonet_type2+"":"0";

        mmddata.MissionIsPK = switech_btn1.isChecked()?"1":"2";
        if( switech_btn1.isChecked()){
            double money = Double.parseDouble(pk_max_money.getText().toString().trim());
            if(money<=0){
                ToastUtils.makeText("Pk金必须大于0");
                return;
            }
        }

        mmddata.ForcePkFlag = "2";
        mmddata.PkAmount = switech_btn1.isChecked()?pk_max_money.getText().toString().trim():"0";
        mmddata.MissionDescribe = intentedata.task_message;

        mmddata.ParentMissionID = intentedata.taskdata.Missionid;

        mmddata.GuaranteeMoneyState = switech_btn2.isChecked()?"1":"2";
        if( switech_btn2.isChecked()){
            double money = Double.parseDouble(bzj_money.getText().toString().trim());
            if(money<=0){
                ToastUtils.makeText("保证金必须大于0");
                return;
            }
        }
        mmddata.GuaranteeMoney = switech_btn2.isChecked()?bzj_money.getText().toString().trim():"0";



        uploaddatabody.mmd = mmddata;


        if(intentedata.tasktype==1&&guizedata.size()>0){
            for(int i =0;i<guizedata.size();i++){
//                QianzhiBody data = new QianzhiBody();
//                data.DistTargetID =choicelistdat.get(i).levellistdat.ID;
//                data.personid = choicelistdat.get(i).levellistdat.ID;
//                data.MissionDistQty = choicelistdat.get(i).clickdata.text1;
//
//                uploaddatabody.imas.add(data);

                MCSUploaddataBody data2 = new MCSUploaddataBody();
                data2.RoleTargetType = guizedata.get(i).RoleTargetType;
                data2.RoleTargetID =  guizedata.get(i).RoleTargetID;
                data2.StandardQty = guizedata.get(i).StandardQty;
                data2.StandardRatio = guizedata.get(i).StandardRatio;
                data2.StandardBonusAmount = guizedata.get(i).StandardBonusAmount;
                data2.OverfulfilQty = guizedata.get(i).OverfulfilQty;
                data2.OverfulfilRatio = guizedata.get(i).OverfulfilRatio;
                data2.OverfulfilBonusAmount = guizedata.get(i).OverfulfilBonusAmount;
                uploaddatabody.mcs.add(data2);

            }

        }

        if(intentedata.tasktype==2&&choicelistdat.size()>0){

            for(int i = 0;i<choicelistdat.size();i++){
                IMFSBody body = new IMFSBody();
                body.FilterTargetType = choicelistdat.get(i).ID;
                body.FilterTargetID = choicelistdat.get(i).levellistdat.ID;
                body.RecStandardQty = choicelistdat.get(i).clickdata.text1;
                body.RecStandardRatio = intentedata.inputmoney_type==2?choicelistdat.get(i).clickdata.text2:"0";
                body.RecStandardAmount = intentedata.inputmoney_type==1?choicelistdat.get(i).clickdata.text2:"0";
                uploaddatabody.imfs.add(body);

            }
        }

        if(weidulist!=null&&weidulist.size()>0){
            uploaddatabody.iml = weidulist;
        }

        uploaddatabody.Userid = BaseActivity.getuser().UserId;

        new LogUntil(mcontent,TAG,"上传data__:"+new Gson().toJson(uploaddatabody));
        Observable observable =
                ApiUtils.getApi().ADDMissionDetails(uploaddatabody)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mcontent) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                EventBus.getDefault().post(new UpdateTAskListEvent());
                finish();
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                ToastUtils.makeText(message);
//                EventBus.getDefault().post(new UpdateTAskListEvent());
//                finish();
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addlistdata(HKLatitudesListData data){
        if(data!=null&&!TextUtils.isEmpty(data.MLid)){
            for(int i =0 ;i<weidulist.size();i++){
                if(data.MLid.equals(i+"")){

                    weidulist.remove(i);
                    weidulist.add(data);
                }
            }


        }else{
            weidulist.add(0,data);

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteweidu(DeleteWeiduDataEvent event){
        if(event!=null){
            weidulist.remove(event.position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick(R.id.choice_weidu_layout)
    public void gotodeimeactivity(){
        DimensionActivity.startactivity(mcontent,weidulist);
    }
}
