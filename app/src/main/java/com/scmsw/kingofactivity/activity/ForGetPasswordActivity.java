package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.GetcodeBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.UpdatePasswordBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;


import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by oldwang on 2019/1/21 0021.
 */

public class ForGetPasswordActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.phone_num)
    EditText phone_num;
    @BindView(R.id.code_num)
    EditText code_num;
    @BindView(R.id.view_1)
    View view;
    @BindView(R.id.view2)
    View view1;
    @BindView(R.id.ts)
    TextView ts;
    @BindView(R.id.login_btn)
    Button loginbtn;

    @BindView(R.id.getcode)
    Button mgetcode;

    @BindView(R.id.newpass)
    EditText newpass;
    @BindView(R.id.view_3)
    View view_3;

    public boolean islogin = false;
    private Dialog mLoadingDialog;


    private boolean startthread = true;//控制倒计时线程是否执行，防止因结束页面的线程崩溃
    private int MaxMinte = 30;
    private boolean getcode = true;
    @Override
    protected void initView() {
       setContentView(R.layout.activity_forgetpassword);
    }

    @Override
    protected void initData() {
        super.initData();
//        EventBus.getDefault().register(this);
        title_name.setText("忘记密码");
        phone_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ts.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phonenum = phone_num.getText().toString().trim();
                String code = code_num.getText().toString().trim();
                String password = newpass.getText().toString().trim();
                if(!TextUtils.isEmpty(phonenum)){
                    view.setBackgroundResource(R.color.c_168AE1);
                }else{
                    view.setBackgroundResource(R.color.c_eeeeee);
                }


                    if(!TextUtils.isEmpty(phonenum)&&!TextUtils.isEmpty(code)&&!TextUtils.isEmpty(password)){
                        loginbtn.setBackgroundResource(R.drawable.shpe_loginbtn1);
                        islogin = true;
                    }else {
                        loginbtn.setBackgroundResource(R.drawable.shpe_loginbtn);
                        islogin = false;
                    }

            }
        });

        code_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ts.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = code_num.getText().toString().trim();
                String phonenum = phone_num.getText().toString().trim();
                String password = newpass.getText().toString().trim();
                if(!TextUtils.isEmpty(code)){
                    view1.setBackgroundResource(R.color.c_168AE1);
                }else{
                    view1.setBackgroundResource(R.color.c_eeeeee);
                }
                if(!TextUtils.isEmpty(phonenum)&&!TextUtils.isEmpty(code)&&!TextUtils.isEmpty(password)){
                    loginbtn.setBackgroundResource(R.drawable.shpe_loginbtn1);
                    islogin = true;
                }else {
                    loginbtn.setBackgroundResource(R.drawable.shpe_loginbtn);
                    islogin = false;
                }
            }
        });

        newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String code = code_num.getText().toString().trim();
                String phonenum = phone_num.getText().toString().trim();
                String password = newpass.getText().toString().trim();
                if(!TextUtils.isEmpty(password)){
                    view_3.setBackgroundResource(R.color.c_168AE1);
                }else{
                    view_3.setBackgroundResource(R.color.c_eeeeee);
                }
                if(!TextUtils.isEmpty(phonenum)&&!TextUtils.isEmpty(code)&&!TextUtils.isEmpty(password)){
                    loginbtn.setBackgroundResource(R.drawable.shpe_loginbtn1);
                    islogin = true;
                }else {
                    loginbtn.setBackgroundResource(R.drawable.shpe_loginbtn);
                    islogin = false;
                }
            }
        });
    }

    /**
     * 按钮点击下一步
     */
    @OnClick(R.id.login_btn)
    public void netdata(){


        if(!islogin){
            return;
        }
        String phone_numdata = phone_num.getText().toString().trim();
        String codedeta = code_num.getText().toString().trim();
        String password = newpass.getText().toString().trim();
        ts.setVisibility(View.VISIBLE);
        if(!UIUtils.isPhoneNumber(phone_numdata)){
            //请输入正确的手机号
            ts.setText("请输入正确的手机号");
            return ;
        }

        if(TextUtils.isEmpty(codedeta)){
            //请输入验证码
            ts.setText("请输入验证码");
            return;
        }


        if(TextUtils.isEmpty(password)){
            ts.setText("请输入新的密码");
            return;
        }

        if( BaseActivity.mcode!=Integer.parseInt(codedeta)){
            ts.setText("验证吗错误");
            return;
        }
        uodatepassword(phone_numdata,password);

    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    public static void startactivty(Context mCOntext){

        Intent mintent = new Intent(mCOntext,ForGetPasswordActivity.class);

        mCOntext.startActivity(mintent);

    }
    @OnClick(R.id.getcode)
    public void gecode(){
        String phone_numdata = phone_num.getText().toString().trim();
        ts.setVisibility(View.VISIBLE);
        if(!UIUtils.isPhoneNumber(phone_numdata)){
            //请输入正确的手机号
            ts.setText("请输入正确的手机号");
            return ;
        }
        getcode(phone_numdata);
    }


    /**
     * 获取验证码
     * @param phone
     */
    private void getcode(String phone){


        final int code = UIUtils.getcode();
        Observable observable =
                ApiUtils.getApi().getcode(new GetcodeBody(code+"",phone))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(ForGetPasswordActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(ForGetPasswordActivity.this) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                new LogUntil(ForGetPasswordActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                BaseActivity.mcode = code;
                startthreadtime();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);


    }

    /**
     * 倒计时验证码
     */

    private void startthreadtime(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    if(startthread) {

                        for(;MaxMinte>0;MaxMinte--) {
                            getcode = false;
                            Message msg = new Message();
                            msg.arg1 = MaxMinte;
                            mhander.sendMessage(msg);
                            Thread.sleep(1000);

                        }
                    }


                }catch (Exception e){
                    e.printStackTrace();


                }
            }
        }.start();
    }


    Handler mhander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);  new LogUntil(ForGetPasswordActivity.this,TAG,msg.arg1+"");
            if(mgetcode!=null) {
                if (msg.arg1 > 1) {
                    mgetcode.setBackgroundResource(R.drawable.shape_getcode_btn_h);
                    mgetcode.setTextColor(getResources().getColor(R.color.c_999999));
                    mgetcode.setText(msg.arg1 + "s 后重发");
                } else {
                    getcode = true;
                    MaxMinte = 30;
                    mgetcode.setBackgroundResource(R.drawable.shpe_getcode_btn);
                    mgetcode.setTextColor(getResources().getColor(R.color.c_168AE1));
                    mgetcode.setText("获取验证码");
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        startthread = false;
    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void finishthisactivity(FinishLoginAndRejest_Event event){
//        finish();
//    }



    public void uodatepassword(String name,String newpassword){
        Observable observable =
                ApiUtils.getApi().UpdatePassword(new UpdatePasswordBody(name,newpassword))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(ForGetPasswordActivity.this, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<String>(ForGetPasswordActivity.this) {
            @Override
            protected void _onNext(StatusCode<String> stringStatusCode) {
                new LogUntil(ForGetPasswordActivity.this,TAG+"loginreturn",new Gson().toJson(stringStatusCode));
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
