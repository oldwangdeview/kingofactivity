package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.Choice_UserAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.ChoiceTask_dataBean;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.Task_userBean;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;

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

public class CoiceUserForTaskActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.choice_user_listview)
    ListView choice_user_listview;
    List<Level_ListdataBean> clickdata ;
    @BindView(R.id.myscroolow)
    ScrollView myscrooview;
    @BindView(R.id.addview_layout)
    LinearLayout mlinyout;
    private List<Task_userBean> mlistdata = new ArrayList<>();
    private Choice_UserAdpater madpater;
    /****被选择的任务类型（"0" 默认，"2" 自由领取，"1" 强制分配）****/
    private int tasktype = 0;
    private RelessTaskOneBody intentedata;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_choiceuser);
    }

    @Override
    protected void initData() {
        super.initData();
        clickdata =(List<Level_ListdataBean>) (getIntent().getSerializableExtra(Contans.INTENT_DATA));
        tv_title_activity_baseperson.setText("选择下级");
        intentedata  = (RelessTaskOneBody)getIntent().getSerializableExtra(Contans.INTENT_TYPE);
        tasktype = intentedata.tasktype;
        new LogUntil(mcontent,TAG,"tapy"+tasktype+"");
        choice_user_listview.setVisibility(View.GONE);
        myscrooview.setVisibility(View.VISIBLE);
        for(int i =0;i<clickdata.size();i++){
            getlevellistdat(clickdata.get(i).name,clickdata.get(i).ID);
        }


    }

    public static void startactivity(Context mContext, List<Level_ListdataBean> clickdata ,RelessTaskOneBody tasktaype){
        Intent mIntent = new Intent(mContext,CoiceUserForTaskActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)clickdata);
        mIntent.putExtra(Contans.INTENT_TYPE,(Serializable)tasktaype);
        mContext.startActivity(mIntent);
    }



    private Dialog mLoadingDialog;
    private void getlevellistdat(final String type,final String id){
        Observable observable =
                ApiUtils.getApi().GetListbyid(new ListbyidBody("过滤",id))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LevelBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<LevelBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    if(stringStatusCode.getResult_Data().kns.size()>0){
                        Task_userBean takuser = new Task_userBean();
                        takuser.title = type;
                        takuser.id = id;
                        takuser.dataliste = stringStatusCode.getResult_Data().kns;
                        Message msg = new Message();
                        msg.obj = takuser;
                        mhander.sendMessage(msg);
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

    Handler mhander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Task_userBean data = (Task_userBean)msg.obj;
            mlistdata.add(data);
            if(mlistdata.size()==clickdata.size()){
//                madpater.notifyDataSetChanged();
                new LogUntil(mcontent,TAG,"tapy"+tasktype+"");
                madpater = new Choice_UserAdpater(mcontent,mlistdata,mlinyout,intentedata);
            }


        }
    };


    @OnClick({R.id.ojbk_btn})
    public void ohbtm(){

        List<ChoiceTask_dataBean> choicelistdat = new ArrayList<>();
        for(int i=0;i<mlistdata.size();i++){
            choicelistdat.addAll(madpater.hashmap.get(i).getchoicedata());
        }
        EventBus.getDefault().post(choicelistdat);
        finish();

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
