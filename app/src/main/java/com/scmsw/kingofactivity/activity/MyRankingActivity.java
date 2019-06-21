package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的排名
 */
public class MyRankingActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @Override
    protected void initView() {

        setContentView(R.layout.activity_myranking);

    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("我的排名");
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyRankingActivity.class);
        mContext.startActivity(mIntent);
    }
}
