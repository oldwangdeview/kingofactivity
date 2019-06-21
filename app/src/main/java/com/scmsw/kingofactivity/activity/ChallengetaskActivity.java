package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChallengTaskAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.ChallengListData_listBean;
import com.scmsw.kingofactivity.bean.ChallengTaskListDataBean;
import com.scmsw.kingofactivity.bean.EditUserBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.bean.UserdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBody;
import com.scmsw.kingofactivity.body.ChallengTaskListBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.UpdateUserEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.PreferencesUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 挑战任务列表
 */
public class ChallengetaskActivity extends BaseActivity {
    @BindView(R.id.listview)
    YRecycleview mYRecycleview;
    ChallengTaskAdpater madpater;
    int countpage = 1;

    public List<ChallengListData_listBean> listdata = new ArrayList<>();
    @Override
    protected void initView() {
      setContentView(R.layout.activity_challengetask);
    }

    @Override
    protected void initData() {
        super.initData();

        madpater = new ChallengTaskAdpater(this,listdata);
        mYRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mYRecycleview.setItemAnimator(new DefaultItemAnimator());
        mYRecycleview.setAdapter(madpater);
        madpater.setlistclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                ChallengeTaskDetailActivity.startactivity(mcontent,listdata.get(position));
            }
        });
        getdata();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mYRecycleview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                retry(false);
            }

            @Override
            public void onLoadMore() {
                retry(true);
            }
        });
    }

    private void retry(boolean type){
        if(type){
            countpage++;

            mYRecycleview.setloadMoreComplete();
        }else {

            countpage=1;
            mYRecycleview.setReFreshComplete();
        }


    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ChallengetaskActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick({R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    @OnClick(R.id.tv_small_title_layout_head)
    public void gotochalleng(){
        ChallengActivity.startactivity(this);
    }

    private Dialog mLoadingDialog;
    private void getdata(){
        Observable observable =
                ApiUtils.getApi().mychallengtasklistdata(new ChallengTaskListBody(BaseActivity.getuser().UserId,"全部"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ChallengTaskListDataBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<ChallengTaskListDataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    if(stringStatusCode.getResult_Data().pkd.size()>0) {
                        listdata.clear();
                        listdata.addAll(stringStatusCode.getResult_Data().pkd);
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


}
