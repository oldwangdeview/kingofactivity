package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.GuizeAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.ChoiceTask_dataBean;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.MissionComplianceBean;
import com.scmsw.kingofactivity.bean.MissionCompliance_ListdatBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.Task_userBean;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.body.MissionComplianceBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.Event_Data;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;

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

public class CompliancerulesActivity extends BaseActivity {
    @BindView(R.id.listview)
    ListView mlistview;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;
    @BindView(R.id.queding_btn)
    Button queding_btn;
    private GuizeAdpater madpater;
    private List<MissionCompliance_ListdatBean> mlistdat = new ArrayList<>();
    private RelessTaskOneBody tasktype;
    private List<Level_ListdataBean> clickdata;
    private int missiontypeindex = -1;
    private String missionid = "";


    @Override
    protected void initView() {
        setContentView(R.layout.activity_compliancerules);

    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        missiontypeindex = getIntent().getIntExtra("missiontypeindex",-1);
        tv_title_activity_baseperson.setText("达标规则");
        tv_small_title_layout_head.setText("新增达标规则");
        tv_small_title_layout_head.setBackgroundResource(R.drawable.challengettask_shape_button);
        madpater = new GuizeAdpater(mcontent,mlistdat);
        mlistview.setAdapter(madpater);
        tasktype = (RelessTaskOneBody)getIntent().getSerializableExtra(Contans.INTENT_TYPE);
        clickdata = (List<Level_ListdataBean>)getIntent().getSerializableExtra(Contans.INTENT_DATA);

        if(missiontypeindex<=0) {
            tv_small_title_layout_head.setVisibility(View.VISIBLE);
            tv_small_title_layout_head.setTextColor(getResources().getColor(R.color.c_ffffff));
            queding_btn.setVisibility(View.VISIBLE);

        }else{
            missionid = getIntent().getStringExtra("missionid");
            getdata(missionid);
            queding_btn.setVisibility(View.GONE);
        }


        if(tasktype!=null&&!TextUtils.isEmpty(tasktype.taskdata.Missionid)){
            List<MissionCompliance_ListdatBean> listdata  = (List<MissionCompliance_ListdatBean>)getIntent().getSerializableExtra("adpaterlistdata");
            if(listdata.size()>0){
                madpater.setdat(listdata);
            }else{
                getdata(tasktype.taskdata.Missionid);
            }

        }




    }

    public static void startactivity(Context mContext,List<Level_ListdataBean> clickdata , RelessTaskOneBody tasktaype, List<MissionCompliance_ListdatBean> listdata ){
        Intent mIntent = new Intent(mContext,CompliancerulesActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)clickdata);
        mIntent.putExtra(Contans.INTENT_TYPE,(Serializable)tasktaype);
        mIntent.putExtra("adpaterlistdata",(Serializable)listdata);
        mContext.startActivity(mIntent);
    }


    public static void startactivity(Context mContext,String missionid){
        Intent mIntent = new Intent(mContext,CompliancerulesActivity.class);
        mIntent.putExtra("missionid",missionid);
        mIntent.putExtra("missiontypeindex",2);
        mContext.startActivity(mIntent);
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        if(missiontypeindex<=0) {
            Event_Data data = new Event_Data();
            data.listdata = madpater.getcilckdata();
            EventBus.getDefault().post(data);
            finish();
        }
        finish();
    }




    private Dialog mLoadingDialog;
    private void getdata(final String missionid){
        Observable observable =
                ApiUtils.getApi().GetMissionComplianceByMissionid(new MissionComplianceBody(missionid,BaseActivity.getuser().UserId))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mcontent, "");
                                }
//                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<MissionComplianceBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<MissionComplianceBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().mcs.size()>0){
                        madpater.setdat(stringStatusCode.getResult_Data().mcs);
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


    @OnClick(R.id.queding_btn)
    public void mquediong_btn(){
        Event_Data data = new Event_Data();
        data.listdata = madpater.getcilckdata();
        EventBus.getDefault().post(data);
        finish();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateuser(List<ChoiceTask_dataBean> mchoicelistdat){

        for(int i =0;i<mchoicelistdat.size();i++){
            MissionCompliance_ListdatBean data = new MissionCompliance_ListdatBean();
            data.RoleTargetType = mchoicelistdat.get(i).levellistdat.name;
            data.StandardQty = mchoicelistdat.get(i).clickdata.text2;
            data.StandardType = tasktype.choiceintmoey_type1+"";
            data.RoleTargetID = mchoicelistdat.get(i).levellistdat.ID;
            data.RoleTargetType = clickdata.get(0).ID;
            if(tasktype.choiceintmoey_type1==1){
                data.StandardBonusAmount = mchoicelistdat.get(i).clickdata.text3;
            }else if(tasktype.choiceintmoey_type1==2){
                data.StandardRatio = mchoicelistdat.get(i).clickdata.text3;
            }

            data.OverfulfilQty = mchoicelistdat.get(i).clickdata.text4;
            data.OverfulfilType = tasktype.choiceintmonet_type2+"";
            if(tasktype.choiceintmonet_type2==1){
                data.OverfulfilBonusAmount = mchoicelistdat.get(i).clickdata.text5;
            }else if(tasktype.choiceintmonet_type2==2){
                data.OverfulfilRatio = mchoicelistdat.get(i).clickdata.text5;
            }

            madpater.Adddata(data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.tv_small_title_layout_head)
    public void gotochoiceguiz(){
        CoiceUserForTaskActivity.startactivity(mcontent, clickdata, tasktype);

    }
}
