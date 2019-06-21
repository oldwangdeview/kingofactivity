package com.scmsw.kingofactivity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.activity.AuditTaskActivity;
import com.scmsw.kingofactivity.activity.BalanceActivity;
import com.scmsw.kingofactivity.activity.BangdingPayForAliActivity;
import com.scmsw.kingofactivity.activity.MoneyActivity;
import com.scmsw.kingofactivity.activity.MyProblemActivity;
import com.scmsw.kingofactivity.activity.MyRankingActivity;
import com.scmsw.kingofactivity.activity.NoticeDetialActivity;
import com.scmsw.kingofactivity.activity.SetingActivity;
import com.scmsw.kingofactivity.activity.TaskFeedBackActivity;
import com.scmsw.kingofactivity.activity.UserInfoActivity;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.BaseFragment;
import com.scmsw.kingofactivity.bean.GetMoenyBean;
import com.scmsw.kingofactivity.bean.GetMoenyBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBean;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBody;
import com.scmsw.kingofactivity.bean.UserdataBean;
import com.scmsw.kingofactivity.eventbus.UpdateUserEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserFragmnet extends BaseFragment {

    @BindView(R.id.heandimage)
    ImageView heandimage;

    @BindView(R.id.myfragment_text_usermoney)
    TextView myfragment_text_usermoney;
    @BindView(R.id.myfragment_text_taskmoney)
    TextView myfragment_text_taskmoney;
    @BindView(R.id.myfragment_text_challengemoney)
    TextView myfragment_text_challengemoney;
    @BindView(R.id.myfragment_text_kymoney)
    TextView myfragment_text_kymoney;
    @BindView(R.id.myfragmnet_text_nickname)
    TextView myfragmnet_text_nickname;
    @BindView(R.id.myfragment_text_id)
    TextView myfragment_text_id;
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_user);
    }


    @OnClick({R.id.userdata_linlayout,R.id.heandimage})
    public void gotouserinfo(){
        UserInfoActivity.startactivity(mContext);
    }

    /**
     * 天梯榜规则
     */
    @OnClick(R.id.ttb_linerlayout)
    public void gotottb(){
        NoticeDetialActivity.startactivity(mContext,4);
    }
    /**
     * 使用帮助
     */
    @OnClick(R.id.help_linlayout)
    public void gotohelpactivity(){
        NoticeDetialActivity.startactivity(mContext,2);
    }

    /**
     * 常见问题
     */
    @OnClick(R.id.question_linearlyout)
    public void gotoquestion(){
        MyProblemActivity.startactivity(mContext);
    }

    /**
     * 设置
     */

    @OnClick(R.id.seting_linlayout)
    public void gotoseting(){
        SetingActivity.startactivity(mContext);
    }

    /**
     * 我的排名
     */
    @OnClick(R.id.ranking_layout)
    public void gotoranking(){
        MyRankingActivity.startactivity(mContext);
    }

    /***
     * 任务审核
     */
    @OnClick(R.id.shenhe_linlayout)
    public void gotoadudit(){
        AuditTaskActivity.startactivity(mContext);
    }

    /**
     * 收支账单
     */
    @OnClick(R.id.money_layout)
    public void shouzhizhandan(){
        MoneyActivity.startactivity(mContext);
    }

    /**
     * 绑定支付宝
     */
    @OnClick(R.id.bangding_ali)
    public void bangdingzhifub(){
        BangdingPayForAliActivity.startactivity(mContext);
    }

    /***
     * 余额
     */
    @OnClick(R.id.yue_layout)
    public void gotoyue(){
        String data = myfragment_text_usermoney.getText().toString();
        BalanceActivity.startactivity(mContext,data);
    }
    @OnClick(R.id.huikui_linlayout)
    public void gotoFeedBack(){
        TaskFeedBackActivity.startactivity(mContext);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        uopdateUserMessage();
        getdata();
    }

    private Dialog mLoadingDialog;
    private void getdata(){
        Observable observable =
                ApiUtils.getApi().GetMony(new GetMoenyBody(BaseActivity.getuser().UserId))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GetMoenyBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<GetMoenyBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){


                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().TotalSum)){
                        myfragment_text_usermoney.setText(stringStatusCode.getResult_Data().TotalSum);
                    }

                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().TaskGold)){
                        myfragment_text_taskmoney.setText(stringStatusCode.getResult_Data().TaskGold);
                    }
                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().ChallengeGold)){
                        myfragment_text_challengemoney.setText(stringStatusCode.getResult_Data().ChallengeGold);
                    }
                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().AvailableGold)){
                        myfragment_text_kymoney.setText(stringStatusCode.getResult_Data().AvailableGold);
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



    public void uopdateUserMessage(){
        if(BaseActivity.getuser()!=null){

            if(!TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)){
                UIUtils.loadImageViewRoud(mContext,BaseActivity.getuser().HeadIcon,heandimage,UIUtils.dip2px(30),R.mipmap.mrtx);
            }

            if(!TextUtils.isEmpty(BaseActivity.getuser().RealName)){
                myfragmnet_text_nickname.setText(BaseActivity.getuser().RealName);
            }else
            if(!TextUtils.isEmpty(BaseActivity.getuser().NickName)){
                myfragmnet_text_nickname.setText(BaseActivity.getuser().NickName);
            }

            if(!TextUtils.isEmpty(BaseActivity.getuser().Account)){
                myfragment_text_id.setText(BaseActivity.getuser().Account);
            }else
            if(!TextUtils.isEmpty(BaseActivity.getuser().UserId)){
                myfragment_text_id.setText(BaseActivity.getuser().UserId);
            }else{
                myfragment_text_id.setText("");
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateUserEvent(UpdateUserEvent event){

        if(event.userdata!=null){

            if(!TextUtils.isEmpty(event.userdata.HeadIcon)){
                UIUtils.loadImageViewRoud(mContext,event.userdata.HeadIcon,heandimage,UIUtils.dip2px(30),R.mipmap.mrtx);
            }
            if(!TextUtils.isEmpty(event.userdata.RealName)){
                myfragmnet_text_nickname.setText(event.userdata.RealName);
            }else

            if(!TextUtils.isEmpty(event.userdata.NickName)){
                myfragmnet_text_nickname.setText(event.userdata.NickName);
            }
            if(!TextUtils.isEmpty(event.userdata.Account)){
                myfragment_text_id.setText(event.userdata.Account);
            }else
            if(!TextUtils.isEmpty(event.userdata.UserId)){
                myfragment_text_id.setText(event.userdata.UserId);
            }else{
                myfragment_text_id.setText("");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().unregister(this);
    }
}
