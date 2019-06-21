package com.scmsw.kingofactivity.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.TaskItemFragmentAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.BaseFragment;
import com.scmsw.kingofactivity.bean.GetMissionDetailsBean;
import com.scmsw.kingofactivity.bean.GetMissionDetailsBody;
import com.scmsw.kingofactivity.bean.GetMissionDetailsListDataBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBean;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBody;
import com.scmsw.kingofactivity.eventbus.ChoiceDialogEvent;
import com.scmsw.kingofactivity.eventbus.UpdateTaskAinmEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TaskItemFragment1 extends BaseFragment {
    @BindView(R.id.task_layout_1)
    LinearLayout task_layout1;
    @BindView(R.id.task_layout_2)
    LinearLayout task_layout2;
    @BindView(R.id.task_layout_3)
    LinearLayout task_layout3;

    @BindView(R.id.user_layout)
    LinearLayout user_layout;
    @BindView(R.id.user_num)
    TextView wrap_content;
    @BindView(R.id.head_image)
    ImageView head_image;
    @BindView(R.id.usernam)
    TextView usernam;
    @BindView(R.id.user_id)
    TextView user_id;
    @BindView(R.id.user_size)
    TextView user_size;


    @BindView(R.id.gj_image)
    ImageView gj_image;
    @BindView(R.id.gj_nickname)
    TextView gj_nickname;

    @BindView(R.id.yj_image)
    ImageView yj_image;
    @BindView(R.id.yj_nickname)
    TextView yj_nuckname;

    @BindView(R.id.jj_image)
    ImageView jj_image;
    @BindView(R.id.jj_nickname)
    TextView jj_nickname;

    @BindView(R.id.listview)
    ListView mlistview;
    @BindView(R.id.fb_text)
            TextView fb_text;

    TaskItemFragmentAdpater madpater;
    List<String> tastdata = new ArrayList<>();
    private int mtype = 1;
    LayoutAnimationController controller;
    public List<GetMissionDetailsListDataBean> mlistdata = new ArrayList<>();
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_taskitem);
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        madpater = new TaskItemFragmentAdpater(mContext,mlistdata);
        controller = new LayoutAnimationController(AnimationUtils.loadAnimation(mContext, R.anim.listview_inputanim));
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mlistview.setAdapter(madpater);
        tastdata.addAll(BaseActivity.gettestdata());
        madpater.notifyDataSetChanged();

        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("translationY", -UIUtils.dip2px(95));
        PropertyValuesHolder colorHolder = PropertyValuesHolder.ofFloat("alpha", 255f);
        PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("ScaleX", 2f);
        PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("ScaleY", 2f);
        PropertyValuesHolder rotationHolderleft = PropertyValuesHolder.ofFloat("translationX", -UIUtils.dip2px(70));
        PropertyValuesHolder rotationHoldertop = PropertyValuesHolder.ofFloat("translationY", -UIUtils.dip2px(20));
        PropertyValuesHolder rotationHolderright = PropertyValuesHolder.ofFloat("translationX", UIUtils.dip2px(70));
        task_layout1.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(task_layout1, rotationHolder, colorHolder, scaleXHolder, scaleYHolder);
        animator.setDuration(500);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();

        task_layout2.setVisibility(View.VISIBLE);
        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(task_layout2, rotationHolderleft, rotationHoldertop,colorHolder, scaleXHolder, scaleYHolder);
        animator1.setDuration(500);
        animator1.setInterpolator(new AccelerateInterpolator());
        animator1.start();
        task_layout3.setVisibility(View.VISIBLE);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(task_layout3, rotationHolderright, rotationHoldertop,colorHolder, scaleXHolder, scaleYHolder);
        animator2.setDuration(500);
        animator2.setInterpolator(new AccelerateInterpolator());
        animator2.start();
        mlistview.setLayoutAnimation(controller);
        tastdata.addAll(BaseActivity.gettestdata());
        madpater.notifyDataSetChanged();
        madpater.setdattype("任务完成量");
        fb_text.setText("任务完成量");
        getdata(false,mtype);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startanim(UpdateTaskAinmEvent event){
        if(event.fragmentindex==0){

            PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("translationY", -UIUtils.dip2px(95));
            PropertyValuesHolder colorHolder = PropertyValuesHolder.ofFloat("alpha", 255f);
            PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("ScaleX", 2f);
            PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("ScaleY", 2f);
            PropertyValuesHolder rotationHolderleft = PropertyValuesHolder.ofFloat("translationX", -UIUtils.dip2px(70));
            PropertyValuesHolder rotationHoldertop = PropertyValuesHolder.ofFloat("translationY", -UIUtils.dip2px(20));
            PropertyValuesHolder rotationHolderright = PropertyValuesHolder.ofFloat("translationX", UIUtils.dip2px(70));
            task_layout1.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(task_layout1, rotationHolder, colorHolder, scaleXHolder, scaleYHolder);
            animator.setDuration(500);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();

            task_layout2.setVisibility(View.VISIBLE);
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(task_layout2, rotationHolderleft, rotationHoldertop,colorHolder, scaleXHolder, scaleYHolder);
            animator1.setDuration(500);
            animator1.setInterpolator(new AccelerateInterpolator());
            animator1.start();
            task_layout3.setVisibility(View.VISIBLE);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(task_layout3, rotationHolderright, rotationHoldertop,colorHolder, scaleXHolder, scaleYHolder);
            animator2.setDuration(500);
            animator2.setInterpolator(new AccelerateInterpolator());
            animator2.start();
            mlistview.setLayoutAnimation(controller);
            tastdata.addAll(BaseActivity.gettestdata());
            madpater.notifyDataSetChanged();

        }else{
            task_layout1.setVisibility(View.GONE);
            task_layout2.setVisibility(View.GONE);
            task_layout3.setVisibility(View.GONE);

            PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("translationY", 0);
            PropertyValuesHolder colorHolder = PropertyValuesHolder.ofFloat("alpha", 0f);
            PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("ScaleX", 1f);
            PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("ScaleY", 1f);
            PropertyValuesHolder rotationHolderleft = PropertyValuesHolder.ofFloat("translationX", 0);
            PropertyValuesHolder rotationHoldertop = PropertyValuesHolder.ofFloat("translationY", 0);
            PropertyValuesHolder rotationHolderright = PropertyValuesHolder.ofFloat("translationX",0);
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(task_layout1, rotationHolder, colorHolder, scaleXHolder, scaleYHolder);
            animator.setDuration(500);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(task_layout2, rotationHolderleft, rotationHoldertop,colorHolder, scaleXHolder, scaleYHolder);
            animator1.setDuration(500);
            animator1.setInterpolator(new AccelerateInterpolator());
            animator1.start();
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(task_layout3, rotationHolderright, rotationHoldertop,colorHolder, scaleXHolder, scaleYHolder);
            animator2.setDuration(500);
            animator2.setInterpolator(new AccelerateInterpolator());
            animator2.start();

        }
    }

    private Dialog mLoadingDialog;
    private void getdata(final boolean type,int mtype){
        Observable observable =
                ApiUtils.getApi().GetMissionDetails(new GetMissionDetailsBody(BaseActivity.getuser().UserId,mtype+"","1"))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GetMissionDetailsBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<GetMissionDetailsBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    mlistdata.clear();
                    mlistdata.addAll(stringStatusCode.getResult_Data().md);
                    madpater.notifyDataSetChanged();
                    if(stringStatusCode.getResult_Data().Usermd!=null){
                        user_layout.setVisibility(View.VISIBLE);

                        wrap_content.setText(stringStatusCode.getResult_Data().Usermd.sort);
                        if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Usermd.UserHeadIcon)){
                            UIUtils.loadImageViewRoud(mContext,stringStatusCode.getResult_Data().Usermd.UserHeadIcon,head_image,UIUtils.dip2px(30),R.mipmap.mrtx);
                        }

                        if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Usermd.UserNickName)){
                            usernam.setText(stringStatusCode.getResult_Data().Usermd.UserNickName);
                        }else if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Usermd.UserAccount)){
                            usernam.setText(stringStatusCode.getResult_Data().Usermd.UserAccount);
                        }

                        if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Usermd.UserAccount)){
                            user_id.setText("部门:"+stringStatusCode.getResult_Data().Usermd.UserAccount);
                        }

                        if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Usermd.Number)){
                            user_size.setText(stringStatusCode.getResult_Data().Usermd.Number);
                        }

                    }else{
                        user_layout.setVisibility(View.GONE);
                    }


                    if(mlistdata.size()>=1){
                        if(!TextUtils.isEmpty(mlistdata.get(0).UserHeadIcon)){
                            UIUtils.loadImageViewRoud(mContext,mlistdata.get(0).UserHeadIcon,gj_image,UIUtils.dip2px(30),R.mipmap.mrtx);
                        }
                        if(!TextUtils.isEmpty(mlistdata.get(0).UserNickName)){
                            gj_nickname.setText(mlistdata.get(0).UserNickName);
                        }else if(!TextUtils.isEmpty(mlistdata.get(0).UserAccount)){
                            gj_nickname.setText(mlistdata.get(0).UserAccount);
                        }
                    }else{
                        task_layout1.setVisibility(View.GONE);
                    }

                    if(mlistdata.size()>=2){
                        if(!TextUtils.isEmpty(mlistdata.get(1).UserHeadIcon)){
                            UIUtils.loadImageViewRoud(mContext,mlistdata.get(1).UserHeadIcon,yj_image,UIUtils.dip2px(30),R.mipmap.mrtx);
                        }
                        if(!TextUtils.isEmpty(mlistdata.get(1).UserNickName)){
                            yj_nuckname.setText(mlistdata.get(1).UserNickName);
                        }else if(!TextUtils.isEmpty(mlistdata.get(1).UserAccount)){
                            yj_nuckname.setText(mlistdata.get(1).UserAccount);
                        }
                    }else{
                        task_layout2.setVisibility(View.GONE);
                    }
                    if(mlistdata.size()>=3){
                        if(!TextUtils.isEmpty(mlistdata.get(2).UserHeadIcon)){
                            UIUtils.loadImageViewRoud(mContext,mlistdata.get(2).UserHeadIcon,jj_image,UIUtils.dip2px(30),R.mipmap.mrtx);
                        }

                        if(!TextUtils.isEmpty(mlistdata.get(2).UserNickName)){
                            jj_nickname.setText(mlistdata.get(2).UserNickName);
                        }else if(!TextUtils.isEmpty(mlistdata.get(2).UserAccount)){
                            jj_nickname.setText(mlistdata.get(2).UserAccount);
                        }
                    }else{
                        task_layout3.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatedata(ChoiceDialogEvent event){

        if(event.index==0){
            getdata(false,event.type);
        }
    }



}
