package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.LoginBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserdataBean;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.DeviceUuidFactory;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.PreferencesUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class Loginactivity extends BaseActivity {


    @BindView(R.id.edit_uname)
    EditText edi_uname;
    @BindView(R.id.edit_password)
    EditText edi_password;

    private Dialog mLoadingDialog;

    @Override
    protected void initView() {
        if(!TextUtils.isEmpty(PreferencesUtils.getInstance().getString(Contans.USER_DATA,""))){
            MainActivity.startactivity(Loginactivity.this);
            finish();
        }
        setContentView(R.layout.activity_login);
    }


    @Override
    protected void initData() {
        super.initData();
        updateactionbar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(Loginactivity.this,true);

    }

    @OnClick(R.id.btn_login)
    public void login(){

        String username = edi_uname.getText().toString().trim();
        String password = edi_password.getText().toString().trim();


        if(TextUtils.isEmpty(username)){
            ToastUtils.makeText("请输入账号");

            return ;
        }


        if(TextUtils.isEmpty(password)){
            ToastUtils.makeText("请输入密码");
            return;
        }
        login(username,password);

    }


    @OnClick(R.id.text_register)
    public void gotoregister(){
        RegisterActivity.startactivity(this);
//payforali("charset=utf-8&biz_content=%7B%22timeout_express%22%3A%225m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%221.00%22%2C%22subject%22%3A%22%E8%AE%A2%E5%8D%95%E6%94%AF%E4%BB%98%22%2C%22body%22%3A%22null%22%2C%22out_trade_no%22%3A%22201904170003%22%7D&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F116.62.117.249%3A60039%2FPayForAlipayCallBack%2FPayForCallBack&app_id=2017122101029308&sign_type=RSA2&version=1.0&timestamp=2019-04-17+21%3A21%3A39&sign=QjrnH6wnE7NtW2nm5bu82Gf5UsGl7xV8zYdaPan9qdMjuLEmoJUed89tXsf4+X%2BBOx3H4zGZCsMTIql%2BHQebaOzbrOO094PHi7OIg9ayY3Pzmy7ZepQteWTAa+Jw2%2BKq72IohVGFXVU8yOxdTjV5nDunpbnIwBrMRkXnRFFaM5aia7g1AmF7jU+Hsi57oQg2rv%2BB64sZbUOFTRnCt2s8UWRMv0M%2BJChyWVI0adzbZp1eINm750D+AEXUw8PJzuuAtMsT8knlLC85H4KnZJMO742RcZgQAMz%2F4RME7R4%2BHFRgK0zV+hbi8GN7w3xUpRP1rZKaZkrG%2FBoINlP8DEqDNbbLjdQ%3D%3D");
    }

    @OnClick(R.id.text_forgetpassword)
    public void gotoforgetpassowed(){

    }


    public static void startactivity(Context mCOntext){
        Intent mIntent = new Intent(mCOntext,Loginactivity.class);
        mCOntext.startActivity(mIntent);
    }


    private void login(String phone,String password){

        Observable observable =
                ApiUtils.getApi().login(new LoginBody(phone,UIUtils.md5(password)))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(Loginactivity.this, "");
                                }
//                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserdataBean>(Loginactivity.this) {
            @Override
            protected void _onNext(StatusCode<UserdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    PreferencesUtils.getInstance().putString(Contans.USER_DATA,new Gson().toJson(stringStatusCode.getResult_Data()));
                    MainActivity.startactivity(Loginactivity.this);
                    finish();

                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText("用户名或者密码错误");
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    @OnClick(R.id.text_forgetpassword)
    public  void forgetpasword(){
        ForGetPasswordActivity.startactivty(Loginactivity.this);
    }


}
