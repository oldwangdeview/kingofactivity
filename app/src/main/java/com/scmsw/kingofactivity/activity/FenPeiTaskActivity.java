package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.FenPeiAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.Task_userBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.AddMissionAllocationBody;
import com.scmsw.kingofactivity.body.AddMissionAllocation_listBody;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.contans.Contans;
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

public class FenPeiTaskActivity extends BaseActivity {
    @BindView(R.id.choice_user_listview)
    ListView choice_user_listview;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    public List<Level_ListdataBean> listdata = new ArrayList<>();
    private UserDontGetTaskListDataListBean choicedata;
    private FenPeiAdpater mfenpeiadpater;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choiceuser);
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        tv_title_activity_baseperson.setText("任务分配");
        choicedata = (UserDontGetTaskListDataListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);

        if(choicedata!=null){
            mfenpeiadpater = new FenPeiAdpater(mcontent,choicedata.LimitTargetType);
            choice_user_listview.setAdapter(mfenpeiadpater);
            mfenpeiadpater.setLisclicklister(new ListOnclickLister() {
                @Override
                public void onclick(View v, int position) {

                    Level_ListdataBean data = mfenpeiadpater.getlisdata().get(position);
                    data.name = choicedata.LimitTargetType;
                    FZRActivity.startactivity(mcontent,data);
                }
            });
            getlevellistdat(choicedata.LimitTargetType);
        }
    }

    public static void startactivity(Context mContext, UserDontGetTaskListDataListBean inputdata){
        Intent mintent = new Intent(mContext,FenPeiTaskActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,(Serializable)inputdata);
        mContext.startActivity(mintent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }



    private Dialog mLoadingDialog;
    private void getlevellistdat(final String id){
        Observable observable =
                ApiUtils.getApi().GetListbyid(new ListbyidBody("过滤",id))
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

                        if(mfenpeiadpater!=null){
                            mfenpeiadpater.setData(stringStatusCode.getResult_Data().kns);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addfenpeiuser(Level_ListdataBean data)
    {
        new LogUntil(mcontent,TAG,"ok"+new Gson().toJson(data));
        mfenpeiadpater.addfzUser(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.ojbk_btn)
    public void ojbkdata(){

        List<Level_ListdataBean> mchoicedata = mfenpeiadpater.getchoicedata();
        AddMissionAllocationBody uploddata = new AddMissionAllocationBody();
        List<AddMissionAllocation_listBody> listdat = new ArrayList<>();
        for(int i =0;i<mchoicedata.size();i++){
            AddMissionAllocation_listBody mdata =  new AddMissionAllocation_listBody();
            mdata.DistTargetID = mchoicedata.get(i).ID;
            mdata.personid = !TextUtils.isEmpty(mchoicedata.get(i).fz_id)?mchoicedata.get(i).fz_id:mchoicedata.get(i).ID;
            mdata.MissionDistQty = mchoicedata.get(i).inpout_text;
            listdat.add(mdata);

        }
        uploddata.Missionid = choicedata.MissionID;
        uploddata.Userid = BaseActivity.getuser().UserId;
        uploddata.imas  = listdat;

        Observable observable =
                ApiUtils.getApi().AddMissionAllocation(uploddata)
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
                ToastUtils.makeText("分配成功");
               finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);


    }



}
