package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceChallengTaskAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.ChallengTaskListDataBean;
import com.scmsw.kingofactivity.bean.Choice_user_ListdataBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBody;
import com.scmsw.kingofactivity.body.ChallengTaskListBody;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.body.UpLodePK_listDataBody;
import com.scmsw.kingofactivity.body.UplodPKBody;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.view.ChoiceChallengTaskDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 发起挑战
 */
public class ChallengActivity  extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.task_name)
    TextView task_name;
    @BindView(R.id.inoput_okmoney)
    EditText inout_okmoney;
    @BindView(R.id.pk_message)
    EditText pk_message;
    @BindView(R.id.user_data)
    TextView user_data;
    private List<TaskListData_ListBean> choicechallengatasklistdata = new ArrayList<>();
    private Dialog choicechallengtask;
    private ChoiceChallengTaskAdpater choiceadpater;
    private TaskListData_ListBean choicedata = null;
    private List<Choice_user_ListdataBean> choiceuser = new ArrayList<>();
    private UserDontGetTaskListDataListBean taskdetail;
    @Override
    protected void initView() {
      setContentView(R.layout.activity_challeng);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ChallengActivity.class);
        mContext.startActivity(mIntent);
    }


    @OnClick({R.id.choice_challengtask_layout})
    public void choice_challengtask(){

        if(choicechallengatasklistdata.size()>0&&choiceadpater!=null){
            if(choicechallengtask==null) {
                choicechallengtask = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(choiceadpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choicedata=choiceadpater.getclickdata1();
                        choicechallengtask.dismiss();
                        getTaskDetail(choicedata.Missionid);
                        if(!TextUtils.isEmpty(choicedata.MissionName)){
                            task_name.setText(choicedata.MissionName);
                        }


                    }
                }).Builder();
            }
            choicechallengtask.show();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        inout_okmoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_title_activity_baseperson.setText("发起挑战");
        getchoiechallengtaskdata();
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    @OnClick(R.id.choice_user)
    public void choice_user(){
        if(choicedata==null){
            ToastUtils.makeText("请先选择任务");
            return;
        }
        ChoiceUserActivity.startactivity(mcontent,choicedata);
    }


    private Dialog mLoadingDialog;

    private void getchoiechallengtaskdata(){
        Observable observable =
                ApiUtils.getApi().tasklistdata(new TasklistdataBody(BaseActivity.getuser().UserId,"挑选pk任务"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<TaskListdataBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<TaskListdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    if(stringStatusCode.getResult_Data().tfb.size()>0){
                        choicechallengatasklistdata.clear();
                        choicechallengatasklistdata.addAll(stringStatusCode.getResult_Data().tfb);
                        choiceadpater = new ChoiceChallengTaskAdpater(mcontent,choicechallengatasklistdata);
                    }

                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }


    private void uplodepk(UplodPKBody updlodapk){
        Observable observable =
                ApiUtils.getApi().passpk(updlodapk)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mcontent) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){

                    finish();

                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    @OnClick(R.id.pk_btn)
    public void uplodepk(){
        String pk_money = inout_okmoney.getText().toString();
        String pk_messagedata = pk_message.getText().toString();
        if(taskdetail==null){
            ToastUtils.makeText("请选择PK任务");
            return;
        }
        if(choiceuser.size()==0){
            ToastUtils.makeText("请选择PK人员");
            return;
        }

        if(TextUtils.isEmpty(pk_money)){
            ToastUtils.makeText("请输入PK金");
            return;
        }
        if(TextUtils.isEmpty(pk_messagedata)){
            ToastUtils.makeText("请输入描述");
            return;
        }
        UplodPKBody uploddata = new UplodPKBody();
        uploddata.Userid = BaseActivity.getuser().UserId;
        uploddata.MissionID = choicedata.Missionid;
        uploddata.MissionName = choicedata.MissionName;
        uploddata.Description = pk_messagedata;
        uploddata.PKAmount = pk_money;
        uploddata.MissionNum = choicedata.MissionNum;
        uploddata.FQAcceptID = taskdetail.TargetID;
        uploddata.LaunchType = taskdetail.TargetType;
        List<UpLodePK_listDataBody> choiceuserdata = new ArrayList<>();
        for(int i =0;i<choiceuser.size();i++){
            UpLodePK_listDataBody data = new UpLodePK_listDataBody();
            data.AcceptID = choiceuser.get(i).Typeid;
            data.AcceptType = choiceuser.get(i).Type;
            data.Personid = choiceuser.get(i).Userid;
            choiceuserdata.add(data);
        }
        uploddata.aj = choiceuserdata;
        uplodepk(uploddata);

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
                    taskdetail = stringStatusCode.getResult_Data();
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
    public void updatechoiceuser(List<Choice_user_ListdataBean> choiceuse){
        this.choiceuser.clear();
        this.choiceuser.addAll(choiceuse);
        user_data.setText(getuserdata(choiceuse));
    }

    private String getuserdata(List<Choice_user_ListdataBean> choiceuse){
        String data = "";
        for(int i =0;i<choiceuse.size();i++){
            data = !TextUtils.isEmpty(data+choiceuse.get(i).NickName)?data+choiceuse.get(i).NickName:data+choiceuse.get(i).NickName;
            data = data+" ";
        }
        return  data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
