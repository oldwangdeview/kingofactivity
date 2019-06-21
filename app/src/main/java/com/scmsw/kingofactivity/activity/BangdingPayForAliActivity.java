package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.GetCopyBody;
import com.scmsw.kingofactivity.bean.GetcopyBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.BangdingAliBody;
import com.scmsw.kingofactivity.body.GetAliPaybody;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BangdingPayForAliActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.editext_1)
    EditText editext_1;
    @BindView(R.id.editext_2)
    EditText editext_2;

    @Override
    protected void initView() {
       setContentView(R.layout.activity_bangdingzhifubao);
    }


    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("绑定支付宝");
        getalipaytype();

    }

    @OnClick(R.id.ojbk_btn)
    public void bangdingbtn(){
        String text1data = editext_1.getText().toString().trim();
        String text2data = editext_2.getText().toString().trim();
        if(TextUtils.isEmpty(text1data)){
            ToastUtils.makeText("请输入姓名");
            return ;
        }
        if(TextUtils.isEmpty(text2data)){
            ToastUtils.makeText("请输入支付宝登录账号");
            return;
        }
        bangdingdata(text1data,text2data);
    }
    private Dialog mLoadingDialog;
    private void bangdingdata(String name,String numdata){
        Observable observable =
                ApiUtils.getApi().UserBingding(new BangdingAliBody(BaseActivity.getuser().UserId,"支付宝",name,numdata))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mcontent) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                 finish();
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    public static  void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,BangdingPayForAliActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


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
                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().RealName)){
                        editext_1.setText(stringStatusCode.getResult_Data().RealName);
                    }
                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().BingNumber)){
                        editext_2.setText(stringStatusCode.getResult_Data().BingNumber);
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
