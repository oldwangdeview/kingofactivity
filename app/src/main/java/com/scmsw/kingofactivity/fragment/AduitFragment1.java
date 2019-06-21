package com.scmsw.kingofactivity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.AuditListAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.BaseFragment;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.AuditBody;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.AuditTaskDetailDialog;
import com.scmsw.kingofactivity.view.YRecycleview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AduitFragment1  extends BaseFragment {
    @BindView(R.id.myrecyleview)
    YRecycleview myrecyleview;
    private AuditListAdpater madpater;
    private List<TaskListData_ListBean> mListdata = new ArrayList<>();
    private UserDontGetTaskListDataListBean taskdetail;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_tasklist);
    }


    @Override
    protected void initData() {
        super.initData();
        madpater = new AuditListAdpater(mContext,mListdata,"审核");
        myrecyleview.setLayoutManager(new LinearLayoutManager(mContext));
        myrecyleview.setItemAnimator(new DefaultItemAnimator());
        myrecyleview.setAdapter(madpater);
        madpater.setListonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                getTaskDetail(mListdata.get(position));
            }
        });
        getdata();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        myrecyleview.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                myrecyleview.setReFreshComplete();;
            }

            @Override
            public void onLoadMore() {
                myrecyleview.setloadMoreComplete();
            }
        });
    }

    private Dialog mLoadingDialog;
    private void getdata(){

        Observable observable =
                ApiUtils.getApi().tasklistdata(new TasklistdataBody(BaseActivity.getuser().UserId,"我发布审核"))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<TaskListdataBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<TaskListdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().tfb!=null&&stringStatusCode.getResult_Data().tfb.size()>0){
                        mListdata.clear();
                        mListdata.addAll(stringStatusCode.getResult_Data().tfb);
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

    /**
     * 获取任务详情
     */
    private void getTaskDetail(final  TaskListData_ListBean data){
        Observable observable =
                ApiUtils.getApi().getTaskdetail(new GetTaskdetailBody(data.Missionid))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserDontGetTaskListDataListBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<UserDontGetTaskListDataListBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    taskdetail = stringStatusCode.getResult_Data();
                    new AuditTaskDetailDialog.Builder(mContext).setDetailData(taskdetail,data).setbuttonclicklister(lster).Builder().show();
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }
    ListOnclickLister lster = new ListOnclickLister() {
        @Override
        public void onclick(View v, int position) {

            updateAudit(position);

        }
    };

    private void updateAudit(int index){
        Observable observable =
                ApiUtils.getApi().aduditdata(new AuditBody(BaseActivity.getuser().UserId,taskdetail.MissionID,index==1?"1":index==2?"4":"4"))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                getdata();
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }
}
