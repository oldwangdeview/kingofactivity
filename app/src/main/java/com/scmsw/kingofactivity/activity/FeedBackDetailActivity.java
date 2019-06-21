package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FeedBackDetailActivity extends BaseActivity {


    @BindView(R.id.task_name)
    TextView task_name;
    @BindView(R.id.task_adduser)
    TextView tesk_adduser;
    @BindView(R.id.task_comqty)
    TextView task_comqty;
    @BindView(R.id.task_missqty)
    TextView task_missqty;
    @BindView(R.id.task_time)
    TextView task_time;
    @BindView(R.id.task_message)
    TextView task_message;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.layout_weidu)
    LinearLayout layout_weidu;

    @BindView(R.id.hean_image)
    ImageView hean_image;
    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    private TaskListData_ListBean choicedata;
    private Dialog mLoadingDialog;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_feedbackdetail);
    }


    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("任务回馈");
        choicedata = (TaskListData_ListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(choicedata!=null){
            getTaskDetail(choicedata.Missionid);
        }


        if(!TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)){
            UIUtils.loadImageViewRoud(this,BaseActivity.getuser().HeadIcon,hean_image,UIUtils.dip2px(30));
        }
        if(!TextUtils.isEmpty(BaseActivity.getuser().RealName)){
            name.setText(BaseActivity.getuser().RealName);
        }



    }


    /**
     * 获取任务详情
     * @param missionID
     */
    private void getTaskDetail(String missionID){
        Observable observable =
                ApiUtils.getApi().getTaskdetail(new GetTaskdetailBody(missionID))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (mLoadingDialog == null) {
                                    mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mcontent, "");
                                }
                                LoadingDialogUtils.show(mLoadingDialog);
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserDontGetTaskListDataListBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<UserDontGetTaskListDataListBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    updatedata(stringStatusCode.getResult_Data());
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }




    private void updatedata(UserDontGetTaskListDataListBean s){

        if(!TextUtils.isEmpty(s.MissionName)){
            task_name.setText(s.MissionName);
        }

        if(!TextUtils.isEmpty(s.FBpersonName)){
            tesk_adduser.setText(s.FBpersonName);
        }

        if(!TextUtils.isEmpty(s.MissionTargetQuantized)){
            task_comqty.setText(s.MissionTargetQuantized);
        }
        if(!TextUtils.isEmpty(choicedata.ComQty)){
            task_missqty.setText(choicedata.ComQty);
        }

        if(!TextUtils.isEmpty(s.MissionStartDatetime)&&!TextUtils.isEmpty(s.MissionEndDatetime)){
            task_time.setText(s.MissionStartDatetime+"-"+s.MissionEndDatetime);
        }
        if(!TextUtils.isEmpty(s.MissionDescribe)){
            task_message.setText(s.MissionDescribe);
        }else if(!TextUtils.isEmpty(choicedata.MissionDescribe)){
            task_message.setText(choicedata.MissionDescribe);
        }

        if(!TextUtils.isEmpty(choicedata.MissionLatitude)){
            if(choicedata.MissionLatitude.equals("1")){
                layout_weidu.setVisibility(View.VISIBLE);
            }else if(choicedata.MissionLatitude.equals("2")){

                layout_weidu.setVisibility(View.GONE);
            }
        }


        if(!TextUtils.isEmpty(choicedata.ComQty)&&!TextUtils.isEmpty(choicedata.MissionQty)){

            try{
                Double data1 = Double.parseDouble(choicedata.ComQty);
                Double data2 = Double.parseDouble(choicedata.MissionQty);

                double bili = data1/data2*100;
                if(bili>100){
                    progressbar.setProgress(100);
                }else{
                    progressbar.setProgress((int)bili);
                }


            }catch (Exception e){

            }

        }

    }


    @OnClick(R.id.button_taskfeedback)
    public void gitoFeedbacklist(){
        FeedBackListActivity.startactivity(mcontent,choicedata);
        finish();
    }


    @OnClick(R.id.weidu_layout)
    public void gotoDimensionActivity(){
        DimensionActivity.startactivity(mcontent,choicedata);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mCOntext, TaskListData_ListBean data){
        Intent mIntent = new Intent(mCOntext,FeedBackDetailActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mCOntext.startActivity(mIntent);
    }
}
