package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.DanWeiAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.HKLatitudesBean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.view.ChoiceChallengTaskDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AddnewDimensionActivity extends BaseActivity {

    @BindView(R.id.weidu_type_name)
    TextView weidu_type_name;


    @BindView(R.id.wedui_task_name)
    TextView wedui_task_name;


    @BindView(R.id.weidu_allcomqty)
    EditText weidu_allcomqty;
    @BindView(R.id.weidu_comqty)
    EditText weidu_comqty;

    @BindView(R.id.weidu_danwei)
    TextView weidu_danwei;

    @BindView(R.id.wedu_jianli)
    EditText wedu_jianli;
    @BindView(R.id.weidu_cf_edi)
    EditText weidu_cf_edi;

    @BindView(R.id.weidu_time)
    EditText weidu_time;
    @BindView(R.id.message)
    EditText message;


    public List<Level_ListdataBean> danweilistdat = new ArrayList<>();
    public List<Level_ListdataBean> weidutype = new ArrayList<>();
    public List<Level_ListdataBean> weiduname = new ArrayList<>();
    private DanWeiAdpater danweiadpater;
    private DanWeiAdpater weidutypeadpater;
    private DanWeiAdpater weidunameadpater;
    private Dialog mLoadingDialog;

    private Dialog choidedanweidialog;
    private Dialog choiceweidutype;
    private Dialog choiceweidunamme;



    private Level_ListdataBean wdtype;
    private Level_ListdataBean wdname;
    private Level_ListdataBean wddw;

    private HKLatitudesListData bean;
    @Override
    protected void initView() {
        setContentView(R.layout.activivty_addnewdimension);
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,AddnewDimensionActivity.class);
        mContext.startActivity(mIntent);
    }



    public static void startactivity(Context mContext,HKLatitudesListData data){
        Intent mIntent = new Intent(mContext,AddnewDimensionActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mContext.startActivity(mIntent);
    }


    @Override
    protected void initData() {
        super.initData();
        bean = (HKLatitudesListData)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(bean!=null){
            setdata(bean);
        }
        danweiadpater = new DanWeiAdpater(mcontent, danweilistdat);
        danweiadpater.setlistonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                wddw = danweilistdat.get(position);

                weidu_danwei.setText(danweilistdat.get(position).name);
            }
        });
        weidutypeadpater = new DanWeiAdpater(mcontent, weidutype);
        weidutypeadpater.setlistonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                wdtype = weidutype.get(position);
                weidu_type_name.setText(weidutype.get(position).name);
            }
        });
        weidunameadpater = new DanWeiAdpater(mcontent, weiduname);
        weidunameadpater.setlistonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                wdname = weiduname.get(position);
                wedui_task_name.setText(weiduname.get(position).name);
            }
        });
        getdanweidata("单位","0");
        getdanweidata("维度类型","0");



    }





    @OnClick(R.id.weidu_type)
    public void showchoiceweidutype(){

        if(weidutype.size()>0&&weidutypeadpater!=null){
            if(choiceweidutype==null) {
                choiceweidutype = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(weidutypeadpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        choiceweidutype.dismiss();
                    }

                }).Builder();
            }
            choiceweidutype.show();
        }
    }

    @OnClick(R.id.weidu_taskname_layout)
    public void weidu_taskname_layout(){
        if(wdtype!=null) {
            getdanweidata("维度名",wdtype.ID);
        }else{
            ToastUtils.makeText("请先选择维度类型");
        }

    }


    @OnClick(R.id.weidu_danwei)
    public void weidu_danwei(){

        if(danweilistdat.size()>0&&danweiadpater!=null){
            if(choidedanweidialog==null) {
                choidedanweidialog = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(danweiadpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        choidedanweidialog.dismiss();
                    }

                }).Builder();
            }
            choidedanweidialog.show();
        }
    }

    private void getdanweidata(final String type,final String id){
        Observable observable =
                ApiUtils.getApi().GetListbyid(new ListbyidBody(type,id))
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

                        if(type.equals("单位")) {
                            danweilistdat.clear();
                            danweilistdat.addAll(stringStatusCode.getResult_Data().kns);
                            if (danweiadpater == null) {
                                danweiadpater = new DanWeiAdpater(mcontent, danweilistdat);
                            } else {
                                danweiadpater.notifyDataSetChanged();
                            }
                        }

                        if(type.equals("维度类型")) {
                            weidutype.clear();
                            weidutype.addAll(stringStatusCode.getResult_Data().kns);
                            if (weidutypeadpater == null) {
                                weidutypeadpater = new DanWeiAdpater(mcontent, weidutype);

                            } else {
                                weidutypeadpater.notifyDataSetChanged();
                            }
                        }


                        if(type.equals("维度名")) {
                            weiduname.clear();
                            weiduname.addAll(stringStatusCode.getResult_Data().kns);
                            if (weidunameadpater == null) {
                                weidunameadpater = new DanWeiAdpater(mcontent, weiduname);
                            } else {
                                weidunameadpater.notifyDataSetChanged();
                            }

                            showdilaog();
                        }
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }


    private void showdilaog(){
        if(weiduname.size()>0&&weidunameadpater!=null){
            if(choiceweidunamme==null) {
                choiceweidunamme = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(weidunameadpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        choiceweidunamme.dismiss();
                    }

                }).Builder();
            }
            choiceweidunamme.show();
        }
    }

    private void setdata(HKLatitudesListData data){

        if(!TextUtils.isEmpty(data.LatitudesTypeName)){
            weidu_type_name.setText(data.LatitudesTypeName);
            if(!TextUtils.isEmpty(data.LatitudesType)){
                wdtype = new Level_ListdataBean();
                wdtype.ID = data.LatitudesType;
                wdtype.name = data.LatitudesTypeName;
            }
        }

        if(!TextUtils.isEmpty(data.LatitudesName)){
            wedui_task_name.setText(data.LatitudesName);
            if(!TextUtils.isEmpty(data.LatitudesID)){
                wdname = new Level_ListdataBean();
                wdname.name = data.LatitudesName;
                wdname.ID = data.LatitudesID;
            }
        }

        if(!TextUtils.isEmpty(data.LatitudeTotalValue)){
            weidu_allcomqty.setText(data.LatitudeTotalValue);
        }
        if(!TextUtils.isEmpty(data.DayUnitValue)){
            weidu_comqty.setText(data.DayUnitValue);
        }

        if(!TextUtils.isEmpty(data.RPunitname)){
            weidu_danwei.setText(data.RPunitname);
            if(!TextUtils.isEmpty(data.RPunit)){
                wddw = new Level_ListdataBean();
                wddw.ID = data.RPunit;
                wddw.name = data.RPunitname;
            }
        }

        if(!TextUtils.isEmpty(data.RewardValue)){
            wedu_jianli.setText(data.RewardValue);
        }


        if(!TextUtils.isEmpty(data.PenaltyValue)){
            weidu_cf_edi.setText(data.PenaltyValue);
        }


        if(!TextUtils.isEmpty(data.IntervalValue)){
            weidu_time.setText(data.IntervalValue);
        }

        if(!TextUtils.isEmpty(data.Memo)){
            message.setText(data.Memo);
        }

    }

    @OnClick(R.id.ojbk_btn)
    public void okdata(){

        String comqty = weidu_comqty.getText().toString();
        String addcomqty = weidu_allcomqty.getText().toString();
        String jianli = wedu_jianli.getText().toString();
        String cefa = weidu_cf_edi.getText().toString();
        String weidu_timedata = weidu_time.getText().toString();
        String messadta = message.getText().toString();


        if(TextUtils.isEmpty(comqty)){
            ToastUtils.makeText("请输入单位目标量");
            return;
        }

        if(TextUtils.isEmpty(addcomqty)){
            ToastUtils.makeText("请输入总目标量");
            return;
        }
        if(TextUtils.isEmpty(jianli)){
            ToastUtils.makeText("请输入奖励值");
            return;
        }

        if(TextUtils.isEmpty(cefa)){
            ToastUtils.makeText("请输入惩罚值");
            return;
        }

        if(TextUtils.isEmpty(weidu_timedata)){
            ToastUtils.makeText("请输入周期");
            return;
        }

        if(wdtype==null){
            ToastUtils.makeText("请选择维度类型");
            return;
        }


        if(wdname==null){
            ToastUtils.makeText("请选择维度名");
            return;
        }


        if(wddw==null){
            ToastUtils.makeText("请选择维度单位");
            return;
        }


        if(bean==null) {
            bean = new HKLatitudesListData();
        }
        bean.LatitudesType = wdtype.ID;
        bean.LatitudesTypeName = wdtype.name;
        bean.LatitudesID = wdname.ID;
        bean.LatitudesName = wdname.name;
        bean.LatitudeTotalValue = addcomqty;
        bean.DayUnitValue = comqty;
        bean.RPunit = wddw.ID;
        bean.RPunitname = wddw.name;
        bean.RewardValue = jianli;
        bean.PenaltyValue = cefa;
        bean.IntervalValue = weidu_timedata;
        bean.Memo = messadta;
        EventBus.getDefault().post(bean);
        finish();
    }
}
