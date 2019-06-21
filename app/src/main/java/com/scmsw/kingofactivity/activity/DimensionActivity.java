package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.DimemsionAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.EnclosuresByMissionidBean;
import com.scmsw.kingofactivity.bean.FeedbackListbean;
import com.scmsw.kingofactivity.bean.HKLatitudesBean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.EnclosureMaster;
import com.scmsw.kingofactivity.body.EnclosuresByMissionidBody;
import com.scmsw.kingofactivity.body.HuiKuiBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.DeleteWeiduDataEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
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

public class DimensionActivity  extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;
    private DimemsionAdpater madpater;
    private TaskListData_ListBean choicedata;
    private UserDontGetTaskListDataListBean choicedata2;
    private Dialog mLoadingDialog;
    private int type = -1;
    public List<HKLatitudesListData> listdata = new ArrayList<>();
    @Override
    protected void initView() {
        setContentView(R.layout.activity_dimensionlist);
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mCOntext, TaskListData_ListBean data){
        Intent mIntent = new Intent(mCOntext,DimensionActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,-1);
        mCOntext.startActivity(mIntent);
    }

    public static void startactivity(Context mCOntext, TaskListData_ListBean data,int type){
        Intent mIntent = new Intent(mCOntext,DimensionActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mCOntext.startActivity(mIntent);
    }

    public static void startactivity(Context mCOntext, UserDontGetTaskListDataListBean data, int type){
        Intent mIntent = new Intent(mCOntext,DimensionActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mCOntext.startActivity(mIntent);
    }
    public static void startactivity(Context mCOntext, List<HKLatitudesListData> data){
        Intent mIntent = new Intent(mCOntext,DimensionActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,1);
        mCOntext.startActivity(mIntent);
    }




    @Override
    protected void initData() {
        super.initData();
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);

        EventBus.getDefault().register(this);

        if(type!=1){
            if(type==2||type==-1) {
                choicedata = (TaskListData_ListBean) getIntent().getSerializableExtra(Contans.INTENT_DATA);
            }else if(type==3){
                choicedata2 = (UserDontGetTaskListDataListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);

            }
        }else{
            List<HKLatitudesListData> listdatam = (List<HKLatitudesListData>)getIntent().getSerializableExtra(Contans.INTENT_DATA);
            if(listdatam.size()>0){
                listdata.addAll(listdatam);
            }
            tv_small_title_layout_head.setVisibility(View.VISIBLE);
            tv_small_title_layout_head.setText("新增回馈");
        }



        tv_title_activity_baseperson.setText("维度回馈");

        madpater = new DimemsionAdpater(mcontent,listdata,type);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setAdapter(madpater);
        madpater.setListClicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                switch (v.getId()) {

                    case R.id.button:
                        FeedBackListActivity.startactivity(mcontent, listdata.get(position));
                        break;
                    case R.id.button_xq:
                        DimensionDetailActivity.startactivity(mcontent,listdata.get(position),choicedata!=null?choicedata.Missionid:choicedata2!=null?choicedata2.MissionID:"",choicedata!=null?choicedata.MissionType:choicedata2!=null?choicedata2.MissionType:"",type);
                        break;
                    case R.id.button_sc:
                        showdialog(position);
                        break;
                    case R.id.button_xg:
                        HKLatitudesListData data = listdata.get(position);
                        data.MLid = position+"";
                        AddnewDimensionActivity.startactivity(mcontent,data);
                        break;
                }
            }
        });
    }

    private void getdata(){


//        new LogUntil(mcontent,TAG,new Gson().toJson(new HuiKuiBody(choicedata.Feedbackid)));
        Observable observable =null;
        if(choicedata!=null) {
            observable =
                    ApiUtils.getApi().GetHKLatitudes(new HuiKuiBody(choicedata.Feedbackid));
            observable.compose(RxHelper.getObservaleTransformer())
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

            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<HKLatitudesBean>(mcontent) {
                @Override
                protected void _onNext(StatusCode<HKLatitudesBean> stringStatusCode) {
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                    if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                        if(stringStatusCode.getResult_Data().hkl.size()>0){
                            listdata.clear();
                            listdata.addAll(stringStatusCode.getResult_Data().hkl);
                            if(madpater!=null){
                                madpater.notifyDataSetChanged();
                            }
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

        if(choicedata2!=null){

            observable = ApiUtils.getApi().GetEnclosuresByMissionid(new EnclosuresByMissionidBody(choicedata2.MissionID));
            observable.compose(RxHelper.getObservaleTransformer())
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

            HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<EnclosuresByMissionidBean>(mcontent) {
                @Override
                protected void _onNext(StatusCode<EnclosuresByMissionidBean> stringStatusCode) {
                    LoadingDialogUtils.closeDialog(mLoadingDialog);
                    if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                        if(stringStatusCode.getResult_Data().md.size()>0){
                            listdata.clear();
                            listdata.addAll(stringStatusCode.getResult_Data().md);
                            if(madpater!=null){
                                madpater.notifyDataSetChanged();
                            }
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

    }



    @OnClick(R.id.tv_small_title_layout_head)
    public void Addnewdata(){

        AddnewDimensionActivity.startactivity(mcontent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(choicedata!=null) {
            getdata();
        }
        if(choicedata2!=null){
            getdata();
        }
    }

    private void showdialog(final int mposition){
        AlertView alertView = new AlertView("提示", "是否删除这条维度？", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){

                    EventBus.getDefault().post(new DeleteWeiduDataEvent(mposition));
                    listdata.remove(position);
                    madpater.notifyDataSetChanged();
                }else{


                }

            }
        });
        alertView.show();}



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addlistdata(HKLatitudesListData event){
        madpater.adddata(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
