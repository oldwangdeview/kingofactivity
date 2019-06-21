package com.scmsw.kingofactivity.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付
 */
public class PayForActivity extends BaseActivity {

    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    private int choicetype = -1;

    @Override
    protected void initView() {
       setContentView(R.layout.activity_payfor);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("收银台");
    }

    @OnClick({R.id.linlayout_1,R.id.linlayout2})
    public void clickview(View v){
        switch (v.getId()){
            case R.id.linlayout_1:

                choicetype = 1;
                image1.setImageResource(R.mipmap.xuanz_taskuser);
                image2.setImageResource(R.mipmap.unselected);
                break;
            case R.id.linlayout2:
                choicetype = 2;


                image2.setImageResource(R.mipmap.xuanz_taskuser);
                image1.setImageResource(R.mipmap.unselected);
                break;
        }
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
    @OnClick(R.id.ojbk_btn)
    public void ojbkbtn(){

    }
}
