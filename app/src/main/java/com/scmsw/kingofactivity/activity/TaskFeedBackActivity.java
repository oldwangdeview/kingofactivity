package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceChallengTaskAdpater;
import com.scmsw.kingofactivity.adpater.TaskFeedBackAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TaskFeedBackActivity extends BaseActivity {
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    private TaskFeedBackAdpater madpater;
    private Dialog mLoadingDialog;
    List<TaskListData_ListBean> listdata = new ArrayList<>();
    @Override
    protected void initView() {
        setContentView(R.layout.activity_taskfeedback);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(TaskFeedBackActivity.this,true);
    }

    @Override
    protected void initData() {
        super.initData();
        madpater = new TaskFeedBackAdpater(mcontent,listdata);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        recycleview.setAdapter(madpater);
        madpater.setlistonclick(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                FeedBackDetailActivity.startactivity(mcontent,listdata.get(position));
            }
        });
        getdata();
    }

    private void getdata(){
        Observable observable =
                ApiUtils.getApi().tasklistdata(new TasklistdataBody(BaseActivity.getuser().UserId,"可回馈任务"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<TaskListdataBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<TaskListdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().tfb.size()>0){
                        listdata.clear();
                        listdata.addAll(stringStatusCode.getResult_Data().tfb);
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

    public static  void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,TaskFeedBackActivity.class);
        mContext.startActivity(mIntent);
    }
}
