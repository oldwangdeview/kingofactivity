package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.HomesBean;
import com.scmsw.kingofactivity.contans.Contans;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

public class ProBlemDetatilActivity  extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.text_name)
    TextView text_name;
    @BindView(R.id.content)
    TextView contet;
    private HomesBean mdata;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_problemdetail);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("常见问题");
        mdata = (HomesBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(mdata!=null){

            if(!TextUtils.isEmpty(mdata.Title)){
                text_name.setText(mdata.Title);
            }
            if(!TextUtils.isEmpty(mdata.Content)){
                contet.setText(mdata.Content);
            }
        }

    }




    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    public static void startactivity(Context mContext, HomesBean data){
        Intent mIntent = new Intent(mContext,ProBlemDetatilActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mContext.startActivity(mIntent);
    }
}
