package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.CompletAdpater;
import com.scmsw.kingofactivity.adpater.FeedBacListAdpater;
import com.scmsw.kingofactivity.adpater.FeedDetailAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.CompletBean;
import com.scmsw.kingofactivity.bean.CompletListBean;
import com.scmsw.kingofactivity.bean.EnclosureMasterBean;
import com.scmsw.kingofactivity.bean.FeedbackListbean;
import com.scmsw.kingofactivity.bean.HKLatitudesBean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.body.CompletBody;
import com.scmsw.kingofactivity.body.EnclosureMaster;
import com.scmsw.kingofactivity.body.HuiKuiBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DimensionDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.task_type)
    TextView task_type;

    @BindView(R.id.task_name)
    TextView task_name;

    @BindView(R.id.yejimubiao_text)
    TextView yejimubiao_text;

    @BindView(R.id.wanchengyeji_text)
    TextView wanchengyeji_text;

    @BindView(R.id.baozhen_money)
    TextView baozhen_money;
    @BindView(R.id.task_time)
    TextView task_time;

    @BindView(R.id.heandimage)
    ImageView heandimage;
    @BindView(R.id.nickname)
    TextView nickname;

    @BindView(R.id.linquliang_text)
    TextView linquliang_text;


    @BindView(R.id.mygridview)
    MyGridView mygridview;



    private CompletAdpater madpater;


    private String missionid;
    private String missiontype;
    private HKLatitudesListData choicedata;
    private Dialog mLoadingDialog;
    public List<HKLatitudesListData> listdata = new ArrayList<>();
    public List<EnclosureMasterBean> weidulistdata = new ArrayList<>();
    private FeedDetailAdpater weiduadptaer;
    private int type;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_dimension);
    }


    @Override
    protected void initData() {
        super.initData();
        choicedata = (HKLatitudesListData)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        tv_title_activity_baseperson.setText("维度回馈详情");
        missionid = getIntent().getStringExtra("missid");
        missiontype = getIntent().getStringExtra("misstype");
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        if(choicedata!=null){
            indata(choicedata);

            if(type==3) {

                if (!TextUtils.isEmpty(missionid) && !TextUtils.isEmpty(missiontype)) {
                    madpater = new CompletAdpater(mcontent,listdata);
                    mygridview.setAdapter(madpater);
                    madpater.setlistonclicklister(new ListOnclickLister() {
                        @Override
                        public void onclick(View v, int position) {
                            FeedBackListActivity.startactivity(mcontent,listdata.get(position));
                        }
                    });
                    getdata();
                }
            }
            if(type==2){
                weiduadptaer = new FeedDetailAdpater(mcontent,weidulistdata);
                mygridview.setAdapter(weiduadptaer);
//                weiduadptaer.setlistonclicklister(new ListOnclickLister() {
//                    @Override
//                    public void onclick(View v, int position) {
//                        FeedBackListActivity.startactivity(mcontent,listdata.get(position));
//                    }
//                });
                getweidudata();
            }
        }

    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mCOntext, HKLatitudesListData data,String missionid,String missiontype,int type){
        Intent mIntent = new Intent(mCOntext,DimensionDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra("missid",missionid);
        mIntent.putExtra("misstype",missiontype);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mCOntext.startActivity(mIntent);
    }


    private void indata(HKLatitudesListData data){



        if(!TextUtils.isEmpty(data.LatitudesTypeName)){
            task_type.setText(data.LatitudesTypeName);
        }else{
            task_type.setText("未知");
        }


        if(!TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)){
            UIUtils.loadImageViewRoud(mcontent,BaseActivity.getuser().HeadIcon,heandimage,UIUtils.dip2px(35),R.mipmap.mrtx);
        }

        if(!TextUtils.isEmpty(BaseActivity.getuser().RealName)){
            nickname.setText(BaseActivity.getuser().RealName);
        }
        if(!TextUtils.isEmpty(data.LatitudesName)){
            task_name.setText(data.LatitudesName);
        }else{
            task_name.setText("");
        }

        if(!TextUtils.isEmpty(data.LatitudeTotalValue)){
            yejimubiao_text.setText(data.LatitudeTotalValue);
        }else{

            yejimubiao_text.setText("");
        }

        if(!TextUtils.isEmpty(data.DayUnitValue)){
            wanchengyeji_text.setText(data.DayUnitValue);
        }else{
            wanchengyeji_text.setText("");
        }

        if(!TextUtils.isEmpty(data.RPvalue)){
            linquliang_text.setText(data.RPvalue);
        }

        if(!TextUtils.isEmpty(data.RPunitname)){
            baozhen_money.setText(data.RPunitname);
        }else{
            baozhen_money.setText("");
        }

        if(!TextUtils.isEmpty(data.IntervalValue)){
            task_time.setText(data.IntervalValue);
        }else{
            task_time.setText("");
        }
    }



    private void getdata(){



        Observable observable =
                ApiUtils.getApi().GetMissionLatitudeComplet(new CompletBody(missionid,missiontype,choicedata.LatitudesID))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<CompletBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<CompletBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){


                    if(stringStatusCode.getResult_Data().md.size()>0){
                        listdata.clear();;
                        listdata.addAll(stringStatusCode.getResult_Data().md);
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




    private void getweidudata(){

        EnclosureMaster data = new EnclosureMaster();
        data.EnclosureType = 2+"";
        data.Busid = choicedata.MLid;
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
                        weidulistdata.clear();
                        weidulistdata.addAll(stringStatusCode.getResult_Data().elm);
                        if(weiduadptaer!=null){
                            weiduadptaer.notifyDataSetChanged();
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
