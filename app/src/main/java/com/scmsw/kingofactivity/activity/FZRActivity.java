package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.FZRAdpater;
import com.scmsw.kingofactivity.bean.GetPersonsBean;
import com.scmsw.kingofactivity.bean.GetPersons_ListBean;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.GetPersonsBody;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FZRActivity extends BaseActivity {
    @BindView(R.id.ojbk_btn)
    Button ojbk_btn;
    @BindView(R.id.choice_user_listview)
    ListView choice_user_listview;
    Level_ListdataBean choicedata;
    private FZRAdpater madpater;
    public List<GetPersons_ListBean> listdata = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choiceuser);
    }

    @Override
    protected void initData() {
        super.initData();
        ojbk_btn.setVisibility(View.GONE);
        choicedata = (Level_ListdataBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        madpater = new FZRAdpater(mcontent,listdata);
        madpater.setListonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {

                if(choicedata!=null){
                    choicedata.choiceid = choicedata.ID;
                    choicedata.ID = listdata.get(position).Personid;
                    choicedata.name = listdata.get(position).Personname;

                    EventBus.getDefault().post(choicedata);
                    finish();
                }

            }
        });
        choice_user_listview.setAdapter(madpater);
        if(choicedata!=null){
            getlevellistdat();
        }

    }

    private Dialog mLoadingDialog;
    private void getlevellistdat(){
        Observable observable =
                ApiUtils.getApi().GetPersons(new GetPersonsBody(choicedata.name,choicedata.ID,BaseActivity.getuser().UserId))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GetPersonsBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<GetPersonsBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    listdata.clear();
                    listdata.addAll(stringStatusCode.getResult_Data().per);
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mContext, Level_ListdataBean data){
        Intent mintet = new Intent(mContext,FZRActivity.class);
        mintet.putExtra(Contans.INTENT_DATA,data);
        mContext.startActivity(mintet);
    }
}
