package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.BangdingAliBody;
import com.scmsw.kingofactivity.body.GetAliPaybody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 余额提现
 */
public class BalanceActivity extends BaseActivity {


    private BangdingAliBody data;
    @BindView(R.id.money)
    TextView money;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_balance);
    }


    @Override
    protected void initData() {
        super.initData();
        String mdata = getIntent().getStringExtra(Contans.INTENT_DATA);
        if(!TextUtils.isEmpty(mdata)){
            money.setText(mdata);
        }
        getalipaytype();
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    @OnClick(R.id.tv_small_title_layout_head)
    public void gotoyue(){
        MoneyActivity.startactivity(mcontent);
    }


    public static void startactivity(Context mContext,String data){
        Intent mIntent = new Intent(mContext,BalanceActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,data);
        mContext.startActivity(mIntent);

    }


    private Dialog mLoadingDialog;
    private void getalipaytype(){
        Observable observable =
                ApiUtils.getApi().UserBindByUserid(new GetAliPaybody(BaseActivity.getuser().UserId,"支付宝"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<BangdingAliBody>(mcontent) {
            @Override
            protected void _onNext(StatusCode<BangdingAliBody> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Data()!=null){
                    data = stringStatusCode.getResult_Data();
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    @OnClick(R.id.ojbk_btn)
    public void tixianbutton(){

    }


}
