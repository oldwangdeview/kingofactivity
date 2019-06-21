package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.FeedBacListAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.EnclosureMasterBean;
import com.scmsw.kingofactivity.bean.FeedbackListbean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.EnclosureMaster;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FeedBackListActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    private TaskListData_ListBean choicedata;
    private HKLatitudesListData weidudata;
    private Dialog mLoadingDialog;
    private int type = -1;
    public List<EnclosureMasterBean> listdata = new ArrayList<>();
    private FeedBacListAdpater madpater;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_feedbacklist);

    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("回馈记录");
        tv_small_title_layout_head.setVisibility(View.VISIBLE);
        tv_small_title_layout_head.setText("新增回馈");

        type  = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);

        if(type==1){
            choicedata = (TaskListData_ListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        }
        if(type==2){
            weidudata = (HKLatitudesListData)getIntent().getSerializableExtra(Contans.INTENT_DATA);
            if(TextUtils.isEmpty(weidudata.HKState)||weidudata.HKState.equals("2")){
                tv_small_title_layout_head.setVisibility(View.GONE);
            }
        }

        madpater = new FeedBacListAdpater(mcontent,listdata);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setAdapter(madpater);
        madpater.setListonLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                EnclosureMasterBean data = listdata.get(position);
                if(weidudata!=null){
                    data.LatitudeTotalValue = weidudata.LatitudeTotalValue;
                    data.PenaltyValue = weidudata.PenaltyValue;
                    data.RewardValue = weidudata.RewardValue;
                }
                AddFeedBackActivity.startactivity(mcontent,data,type);
            }
        });

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mCOntext, TaskListData_ListBean data){
        Intent mIntent = new Intent(mCOntext,FeedBackListActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,1);
        mCOntext.startActivity(mIntent);
    }

    public static void startactivity(Context mCOntext, HKLatitudesListData data){
        Intent mIntent = new Intent(mCOntext,FeedBackListActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,2);
        mCOntext.startActivity(mIntent);
    }


    private void getdata(){

        EnclosureMaster data = new EnclosureMaster();
        data.EnclosureType = type+"";
        data.Busid = type==1?choicedata.Feedbackid:type==2?weidudata.MLid:"";
        data.Status = 3+"";
        data.ComStates = 3+"";

        Observable observable =
                ApiUtils.getApi().GetEnclosureMaster(data)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<FeedbackListbean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<FeedbackListbean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().elm.size()>0){
                        listdata.clear();
                        listdata.addAll(stringStatusCode.getResult_Data().elm);
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

    /**
     * 新增回馈记录
     */

    @OnClick(R.id.tv_small_title_layout_head)
    public void addnewfeedback(){

        if(type==1) {
            AddFeedBackActivity.startactivity(this, choicedata);
        }else if(type==2){
            AddFeedBackActivity.startactivity(this, weidudata);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }
}
