package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceChallengTaskAdpater;
import com.scmsw.kingofactivity.adpater.GetTaskAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBody;
import com.scmsw.kingofactivity.body.GetTaskBody;
import com.scmsw.kingofactivity.body.MissionUserDetailsBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.InPutTaskSizeClickLister;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.view.GetTask_InputTasksize_Dialog;
import com.scmsw.kingofactivity.view.TaskDetailDialog;
import com.scmsw.kingofactivity.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GetTaskActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.mlisteview)
    YRecycleview mlistview;

    GetTaskAdpater madpater;

    List<UserDontGetTaskListDataListBean> listdata = new ArrayList<>();
    private UserDontGetTaskListDataListBean choicedata;
    private GetTask_InputTasksize_Dialog dialog;
    private int type = -1;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_gettask);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("任务领取");
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        if(type>0){
            tv_title_activity_baseperson.setText("任务分配");
        }
        madpater = new GetTaskAdpater(this,listdata,type);
        mlistview.setLayoutManager(new LinearLayoutManager(this));
        mlistview.setItemAnimator(new DefaultItemAnimator());
        mlistview.setAdapter(madpater);
        madpater.setonclicllist(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                switch (v.getId()){
                    case R.id.linqu:
                        choicedata = listdata.get(position);
                        dialog =  new GetTask_InputTasksize_Dialog.Builder(mcontent).setMinAndMax(listdata.get(position).RecMinQty,listdata.get(position).RecMixQty).setonclicklister(new InPutTaskSizeClickLister() {
                            @Override
                            public void click(View v, int size) {

                                gettask(size+"");
                                 dialog.dismiss();

                            }
                        }).Builder();

                        dialog.show();
                        break;
                    case R.id.dabiaoguiz:
                        CompliancerulesActivity.startactivity(mcontent,listdata.get(position).MissionID);
                        break;
                    case R.id.fenpei:
                        FenPeiTaskActivity.startactivity(mcontent,listdata.get(position));
                        break;
                        default:

                            new TaskDetailDialog.Builder(mcontent).setAdpater(listdata.get(position)).Builder().show();

                            break;
                }
            }
        });


            getchoiechallengtaskdata();

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mlistview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                mlistview.setReFreshComplete();
            }

            @Override
            public void onLoadMore() {

                mlistview.setloadMoreComplete();
            }
        });
    }

    private Dialog mLoadingDialog;

    private void getchoiechallengtaskdata(){
        Observable observable = null;
        if(type<=0) {
            observable = ApiUtils.getApi().UserdontgettaskListdata(new UserdontgettaskListdataBody(BaseActivity.getuser().UserId, "未领取"));
        }else{
            observable = ApiUtils.getApi().GetMissionUserDetails(new MissionUserDetailsBody(BaseActivity.getuser().UserId, "1"));

        }

        observable .compose(RxHelper.getObservaleTransformer())
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserdontgettaskListdataBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<UserdontgettaskListdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().umd.size()>0){

                        listdata.clear();
                        listdata.addAll(stringStatusCode.getResult_Data().umd);
                        madpater.notifyDataSetChanged();
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


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,GetTaskActivity.class);
        mContext.startActivity(mIntent);
    }
    public static void startactivity(Context mContext,int type){
        Intent mIntent = new Intent(mContext,GetTaskActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }


    private void gettask(String size){
        Observable observable =
                ApiUtils.getApi().mychallengtasklistdata(new GetTaskBody(choicedata.MissionID,choicedata.MissionNum,choicedata.MissionName,size,BaseActivity.getuser().UserId))
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


                    ToastUtils.makeText("领用成功");

                    getchoiechallengtaskdata();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }
}
