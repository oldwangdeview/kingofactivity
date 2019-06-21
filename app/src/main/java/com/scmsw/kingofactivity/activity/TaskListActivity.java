package com.scmsw.kingofactivity.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.TaskListPagerAdpater;
import com.scmsw.kingofactivity.adpater.TaskPagerAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.eventbus.UpdateTasKLIstFragmentEvent;
import com.scmsw.kingofactivity.eventbus.UpdateTaskAinmEvent;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskListActivity extends BaseActivity {
    @BindView(R.id.mviewpager)
    ViewPager mviewpager;
    TaskListPagerAdpater mpageradpater;
    @BindView(R.id.topview)
    View task_view;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_tasklist);

    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("任务列表");
        mpageradpater = new TaskListPagerAdpater(this.getSupportFragmentManager());
        mviewpager.setOffscreenPageLimit(4);
        mviewpager.setAdapter(mpageradpater);
        mviewpager.setCurrentItem(0);
        tv_small_title_layout_head.setVisibility(View.VISIBLE);
        tv_small_title_layout_head.setText(" 我的发布 ");
        tv_small_title_layout_head.setTextColor(getResources().getColor(R.color.c_ffffff));
        tv_small_title_layout_head.setBackgroundResource(R.drawable.challengettask_shape_button);
  }
    @OnClick({R.id.tasklist_notover,R.id.tasklist_over,R.id.tasklist_weiguos,R.id.tasklist_zuofei})
    public void choicetasklisttype(View v){
        switch (v.getId()){
            case R.id.tasklist_notover:
                mviewpager.setCurrentItem(1);
                break;
            case R.id.tasklist_over:
                mviewpager.setCurrentItem(2);
                break;
            case R.id.tasklist_weiguos:
                mviewpager.setCurrentItem(0);
                break;
                case R.id.tasklist_zuofei:
                    mviewpager.setCurrentItem(3);
                    break;


        }
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                new LogUntil(mcontent,TAG,position+"");

                movetopview(position);
                EventBus.getDefault().post(new UpdateTasKLIstFragmentEvent(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void movetopview(int movetoindex){
        ObjectAnimator animator = ObjectAnimator.ofFloat(task_view,"translationX",movetoindex==0?0:UIUtils.dip2px(75*movetoindex));
        animator.setDuration(300);
        animator.start();
    }

    public static void startactivity(Context mContext){

        Intent mIntent = new Intent(mContext,TaskListActivity.class);
        mContext.startActivity(mIntent);


    }

    @OnClick(R.id.tv_small_title_layout_head)
    public void gotorelesstask(){
        MyRelesstskListdataActivity.startactivity(this);
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
