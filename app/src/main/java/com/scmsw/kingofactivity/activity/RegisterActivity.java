package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.GetcodeBody;
import com.scmsw.kingofactivity.bean.RegisterBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.UserAgreementEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.register_edi_phone)
    EditText edi_phonenumber;
    @BindView(R.id.register_edi_code)
    EditText edi_code;
    @BindView(R.id.register_edi_password)
    EditText edi_password;
    @BindView(R.id.register_text_getcode)
    TextView text_getcode;

    @BindView(R.id.register_image_choice)
    ImageView register_image_choice;
    @BindView(R.id.register_edi_username)
    EditText register_edi_username;


    private boolean startthread = true;
    private boolean getcode = true;
    private boolean choiceagreement = false;
    private Dialog mLoadingDialog;
    @Override
    protected void initView() {
       setContentView(R.layout.activity_register);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);

        updateactionbar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(RegisterActivity.this,true);

    }


    @OnClick(R.id.register_text_getcode)
    public void getcode(){
        if(getcode){
            String phone = edi_phonenumber.getText().toString().trim();
            if(TextUtils.isEmpty(phone)){
                ToastUtils.makeText("请先输入手机号");
                return;
            }
            if(!UIUtils.isPhoneNumber(phone)){
                ToastUtils.makeText("请输入正确的手机号");
                return;
            }
            getcode(phone);
//            getcodetimer();
        }
    }

    private void getcodetimer(){


        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    if (startthread) {
                        getcode = false;
                        for (int i = Contans.MAXGETCODETIMEOUT; i >= 0; i--) {
                            Thread.sleep(1000);
                            Message msg = new Message();
                            msg.arg1 = i;
                            mhander.sendMessage(msg);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }

    android.os.Handler mhander = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1>0){

                text_getcode.setText("验证码（"+msg.arg1+"S)");
            }else{
                getcode = true;
                text_getcode.setText("重新获取");
            }
        }
    };



    @OnClick(R.id.register_botton_register)
    public void register(){

        String username = register_edi_username.getText().toString();
        if(TextUtils.isEmpty(username)){
            ToastUtils.makeText("请输入用户真实姓名");
            return;
        }

        if(!choiceagreement){
            ToastUtils.makeText("请同意使用条款及隐私协议");
            return;
        }
        String phone = edi_phonenumber.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.makeText("请先输入手机号");
            return;
        }
        if(!UIUtils.isPhoneNumber(phone)){
            ToastUtils.makeText("请输入正确的手机号");
           return;
        }
        String code = edi_code.getText().toString();
        if(TextUtils.isEmpty(code)){
            ToastUtils.makeText("请输入验证码");
            return;
        }

        if(!code.equals(BaseActivity.mcode+"")){
            ToastUtils.makeText("验证码错误");
            return;
        }

        String password = edi_password.getText().toString();
        if(TextUtils.isEmpty(password)){
            ToastUtils.makeText("请输入密码");
            return;
        }
        if(password.length()<8||password.length()>20){
            ToastUtils.makeText("密码不符合规则");
            return;
        }

        register(phone,password,username);
//        finish();
    }

    @OnClick(R.id.register_image_choice)
    public void choiceuseragreement(){
        if(choiceagreement){
            register_image_choice.setImageResource(R.mipmap.register_image_nochoice);
        }else{
            register_image_choice.setImageResource(R.mipmap.register_image_choice);
        }
        choiceagreement = !choiceagreement;
    }

    @OnClick(R.id.useragreement)
    public void gotouseragreemwnt(){

//        UserAgreementActivity.startactivity(this);
         NoticeDetialActivity.startactivity(mcontent,3);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAgreement(UserAgreementEvent event){
        choiceagreement = true;
        register_image_choice.setImageResource(R.mipmap.register_image_choice);

    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent( mContext,RegisterActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startthread = false;
        EventBus.getDefault().unregister(this);
    }


    private void register(String phone,String password,String username){

        Observable observable =
                ApiUtils.getApi().Register(new RegisterBody(phone,UIUtils.md5(password),username))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(RegisterActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<String>(RegisterActivity.this) {
            @Override
            protected void _onNext(StatusCode<String> stringStatusCode) {
                new LogUntil(RegisterActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);

    }

    private void getcode(String phone){

        final int code = UIUtils.getcode();
        Observable observable =
                ApiUtils.getApi().getcode(new GetcodeBody(code+"",phone))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(RegisterActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(RegisterActivity.this) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                new LogUntil(RegisterActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                BaseActivity.mcode = code;

                getcodetimer();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);


    }
}
