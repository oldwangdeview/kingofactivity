package com.scmsw.kingofactivity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.activity.TaskDetailActivity;
import com.scmsw.kingofactivity.adpater.TaskListAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.BaseFragment;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.eventbus.UpdateTasKLIstFragmentEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.YRecycleview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class TaskListFragment2 extends BaseFragment {
    @BindView(R.id.myrecyleview)
    YRecycleview mYRecycleview;
    LayoutAnimationController controller;
    TaskListAdpater madpater;
    private List<TaskListData_ListBean> mListdata = new ArrayList<>();
    @Override
    public View initView(Context context) {
        EventBus.getDefault().register(this);
        return UIUtils.inflate(mContext, R.layout.fragment_tasklist);
    }

    @Override
    protected void initData() {
        super.initData();
        controller = new LayoutAnimationController(AnimationUtils.loadAnimation(mContext, R.anim.listview_inputanim));
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        madpater = new TaskListAdpater(mContext,mListdata,"已完成");
        mYRecycleview.setLayoutManager(new LinearLayoutManager(mContext));
        mYRecycleview.setItemAnimator(new DefaultItemAnimator());
        mYRecycleview.setAdapter(madpater);
        madpater.setOnItemListCilicLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                TaskDetailActivity.startactivity(mContext,mListdata.get(position),"已完成");
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

                mYRecycleview.setReFreshComplete();
            }

            @Override
            public void onLoadMore() {

                mYRecycleview.setloadMoreComplete();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void StartAnim(UpdateTasKLIstFragmentEvent event){
        new LogUntil(mContext,TAG,event.fragmentindex+"");
        if(event.fragmentindex==1){
//            mYRecycleview.setLayoutAnimation(controller);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private Dialog mLoadingDialog;
    private void getdata(){

        Observable observable =
                ApiUtils.getApi().tasklistdata(new TasklistdataBody(BaseActivity.getuser().UserId,"已完成"))
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
}
