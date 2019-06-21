package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.AduitPagerAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AuditTaskActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.viewpage)
    ViewPager viewpage;


    private AduitPagerAdpater mpageradpater;

    @Override
    protected void initView() {
          setContentView(R.layout.activity_addittask);
    }



    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("任务审核");
        mpageradpater = new AduitPagerAdpater(this.getSupportFragmentManager());
        viewpage.setOffscreenPageLimit(2);
        viewpage.setAdapter(mpageradpater);
        viewpage.setCurrentItem(0);
        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateviewpagerindex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @OnClick({R.id.layout_1,R.id.layout_2})
    public void choicetask(View v){
        switch (v.getId()){
            case R.id.layout_1:
                viewpage.setCurrentItem(0);
                break;
            case R.id.layout_2:
                viewpage.setCurrentItem(1);
                break;
        }
    }

    private void updateviewpagerindex(int index){
        switch (index){
            case 0:
                text1.setTextColor(getResources().getColor(R.color.yellow));
                text2.setTextColor(getResources().getColor(R.color.c_000000));
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                text2.setTextColor(getResources().getColor(R.color.yellow));
                text1.setTextColor(getResources().getColor(R.color.c_000000));
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.INVISIBLE);
                break;
        }
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,AuditTaskActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
