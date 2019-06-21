package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.eventbus.UserAgreementEvent;
import com.scmsw.kingofactivity.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class UserAgreementActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_useragreement);
    }

    @Override
    protected void initData() {
        super.initData();
        updateactionbar();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.showFullScreen(UserAgreementActivity.this,true);
    }
    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,UserAgreementActivity.class);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.ok_btn)
    public void okbtn(){
        EventBus.getDefault().post(new UserAgreementEvent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
