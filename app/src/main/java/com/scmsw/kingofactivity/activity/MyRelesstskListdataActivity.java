package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceChallengTaskAdpater;
import com.scmsw.kingofactivity.adpater.MyRelesslistdataAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBody;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.eventbus.UpdateTAskListEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyRelesstskListdataActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;
    @BindView(R.id.listview)
    RecyclerView listview;

    private List<UserDontGetTaskListDataListBean> listdata = new ArrayList<>();
    private Dialog mLoadingDialog;
    private MyRelesslistdataAdpater madpater;

    @Override
    protected void initView() {
      setContentView(R.layout.activity_myrelesstasklist);
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        tv_title_activity_baseperson.setText("分配任务");
        tv_small_title_layout_head.setText("  维度列表  ");
        tv_small_title_layout_head.setVisibility(View.GONE);
        tv_small_title_layout_head.setTextColor(getResources().getColor(R.color.c_ffffff));
        tv_small_title_layout_head.setBackgroundResource(R.drawable.challengettask_shape_button);
        madpater = new MyRelesslistdataAdpater(mcontent,listdata);
        madpater.setonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                TaskDetailActivity.startactivity(mcontent ,listdata.get(position),"未过审");

            }
        });
        listview.setLayoutManager(new LinearLayoutManager(this));
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(madpater);
        getlistdata();


    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    private void getlistdata(){
        Observable observable =
                ApiUtils.getApi().UserdontgettaskListdata(new UserdontgettaskListdataBody(BaseActivity.getuser().UserId,"我发布"))
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
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    public static void startactivity(Context mContext){
        Intent mIntentr = new Intent(mContext,MyRelesstskListdataActivity.class);
        mContext.startActivity(mIntentr);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finfishactivity(UpdateTAskListEvent event){
        getlistdata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//    }
//
//    @OnClick(R.id.tv_small_title_layout_head)
//    public void gotorelesstask(){
//
//        DimensionActivity.startactivity(mcontent,);
//    }
    }

}
