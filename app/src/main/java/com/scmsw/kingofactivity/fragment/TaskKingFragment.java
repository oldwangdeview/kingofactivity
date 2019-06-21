package com.scmsw.kingofactivity.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.activity.ReleaseTaskActivity;
import com.scmsw.kingofactivity.adpater.TaskPagerAdpater;
import com.scmsw.kingofactivity.base.BaseFragment;
import com.scmsw.kingofactivity.eventbus.ChoiceDialogEvent;
import com.scmsw.kingofactivity.eventbus.UpdateTaskAinmEvent;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.ChoiceSexDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskKingFragment extends BaseFragment {


    @BindView(R.id.task_top_text1)
    TextView task_top_text1;
    @BindView(R.id.task_top_text2)
    TextView task_top_text2;
    @BindView(R.id.task_top_text3)
    TextView task_top_text3;
    @BindView(R.id.task_view)
    View task_view;
    @BindView(R.id.mviewpager)
    ViewPager mviewpager;
    TaskPagerAdpater mviewpageradpater;
    @BindView(R.id.task_saixuan)
    TextView task_saixuan;

    private ChoiceSexDialog choicedialog;
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_taskking);
    }

    @Override
    protected void initData() {
        super.initData();
        mviewpageradpater = new TaskPagerAdpater(getChildFragmentManager());
        mviewpager.setOffscreenPageLimit(3);
        mviewpager.setAdapter(mviewpageradpater);
        mviewpager.setCurrentItem(0);

    }

    @OnClick({R.id.task_top_text1,R.id.task_top_text2,R.id.task_top_text3})
    public void choicetopclick(View v){

        new LogUntil(mContext,TAG,task_view.getScaleX()+"");
        new LogUntil(mContext,TAG,task_view.getScaleY()+"");
        switch (v.getId()){
            case R.id.task_top_text1:
                mviewpager.setCurrentItem(0);
                break;
            case R.id.task_top_text2:
                mviewpager.setCurrentItem(1);
                break;
            case R.id.task_top_text3:
                mviewpager.setCurrentItem(2);
                break;
        }

    }
    private int position = 0;

    @Override
    protected void initEvent() {
        super.initEvent();
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                moveview(position);
                EventBus.getDefault().post(new UpdateTaskAinmEvent(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.task_saixuan)
    public  void chpicedata(){
        if (choicedialog==null) {

            choicedialog = new ChoiceSexDialog.Builder(mContext)
                    //.setTitle("更换封面")
                    .addMenu("组织机构", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicedialog.dismiss();
                            task_saixuan.setText("组织机构");
                            EventBus.getDefault().post(new ChoiceDialogEvent(position,1));

                        }
                    }).addMenu(" 部门", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicedialog.dismiss();
                            task_saixuan.setText("部门");
                            EventBus.getDefault().post(new ChoiceDialogEvent(position,2));

                        }
                    }).addMenu(" 岗位", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicedialog.dismiss();
                            task_saixuan.setText("岗位");
                            EventBus.getDefault().post(new ChoiceDialogEvent(position,3));


                        }
                    })
                    .addMenu(" 角色", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicedialog.dismiss();
                            task_saixuan.setText("角色");
                            EventBus.getDefault().post(new ChoiceDialogEvent(position,4));

                        }
                    }).create();

        }
        if (!choicedialog.isShowing()){

            choicedialog.show();
        }
    }

    private void moveview(int movetoindex){
        ObjectAnimator animator = ObjectAnimator.ofFloat(task_view,"translationX",movetoindex==0?0:UIUtils.dip2px(100*movetoindex));
        animator.setDuration(300);
        animator.start();
    }


}
