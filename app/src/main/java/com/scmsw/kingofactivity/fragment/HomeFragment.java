package com.scmsw.kingofactivity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.activity.ChallengetaskActivity;
import com.scmsw.kingofactivity.activity.GetTaskActivity;
import com.scmsw.kingofactivity.activity.Loginactivity;
import com.scmsw.kingofactivity.activity.MainActivity;
import com.scmsw.kingofactivity.activity.MyRelesstskListdataActivity;
import com.scmsw.kingofactivity.activity.NoticeActivity;
import com.scmsw.kingofactivity.activity.ReleaseTaskActivity;
import com.scmsw.kingofactivity.activity.TaskListActivity;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.BaseFragment;
import com.scmsw.kingofactivity.bean.GetMoenyBean;
import com.scmsw.kingofactivity.bean.GetMoenyBody;
import com.scmsw.kingofactivity.bean.LoginBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBean;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBody;
import com.scmsw.kingofactivity.bean.UserdataBean;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.PreferencesUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.yifa_money)
    TextView yifa_money;
    @BindView(R.id.zhixin_money)
    TextView zhixin_money;
    @BindView(R.id.tiaozhan_money)
    TextView tiaozhan_money;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_home);
    }

    /**
     * 我的任务
     */
    @OnClick(R.id.home_layout_mytasklist)
    public void gotomyTaskListActivity(){
        TaskListActivity.startactivity(mContext);
    }
    /**
     * 挑战任务
     */
    @OnClick(R.id.challenget_layout)
    public void gotoChallengetActivity(){
        ChallengetaskActivity.startactivity(mContext);
    }


    /**
     * 领取任务
     */
    @OnClick(R.id.gettask_layout)
    public void gotogettaskactiuvity(){
        GetTaskActivity.startactivity(mContext);
    }
    @Override
    protected void initData() {
        super.initData();
        getdata();
    }

    private Dialog mLoadingDialog;
    private void getdata(){
        Observable observable =
                ApiUtils.getApi().UserBusinessDetail(new UserBusinessDetailBody(BaseActivity.getuser().UserId))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserBusinessDetailBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<UserBusinessDetailBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){


                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().TotalBonusPaid)){
                        yifa_money.setText(stringStatusCode.getResult_Data().TotalBonusPaid);
                    }

                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().ExecutionBonus)){
                        zhixin_money.setText(stringStatusCode.getResult_Data().ExecutionBonus);
                    }
                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().ChallengeBonus)){
                        tiaozhan_money.setText(stringStatusCode.getResult_Data().ChallengeBonus);
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
    @OnClick(R.id.layout_click_1)
    public void setonclick(){

        NoticeActivity.startactivity(mContext);

    }

    /***
     * 创建任务
     */
    @OnClick(R.id.addtask_layout)
    public  void gotoaddtaskactivity(){
        ReleaseTaskActivity.startactivity(mContext);
    }

    /**
     * 分配任务
     */
    @OnClick(R.id.taskdata_layout)
    public void gettofenpeirenwu(){
        GetTaskActivity.startactivity(mContext,2);
    }
}
