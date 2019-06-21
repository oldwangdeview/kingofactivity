package com.scmsw.kingofactivity.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceChallengTaskAdpater;
import com.scmsw.kingofactivity.adpater.DanWeiAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.LevelBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.TaskListdataBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBean;
import com.scmsw.kingofactivity.bean.UserdontgettaskListdataBody;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.body.ListbyidBody;
import com.scmsw.kingofactivity.body.TasklistdataBody;
import com.scmsw.kingofactivity.eventbus.UpdateTAskListEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.interfice.UpdateMoney_DIalog_ClickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.ChoiceChallengTaskDialog;
import com.scmsw.kingofactivity.view.ChoiceSexDialog;
import com.scmsw.kingofactivity.view.Input_Money_Dialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 发布任务
 */

public class ReleaseTaskActivity  extends BaseActivity implements DatePicker.OnDateChangedListener {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.layout_type1)
    LinearLayout layout_type1;
    @BindView(R.id.layout_type2)
    LinearLayout layout_type2;
    @BindView(R.id.task_name)
    TextView task_name;
    @BindView(R.id.task_level)
    TextView task_level;
    @BindView(R.id.task_type)
    TextView task_type;
    @BindView(R.id.starttime_text)
    TextView starttime_text;
    @BindView(R.id.endtime_text)
    TextView endtime_text;
    @BindView(R.id.task_text_allmoeny)
    EditText task_text_allmoeny;
    @BindView(R.id.fp_money_text)
    TextView fp_money_text;
    @BindView(R.id.chaoe_text)
    TextView chaoe_text;
    @BindView(R.id.money_2)
    EditText money_2;
    @BindView(R.id.text_money_2)
    TextView text_money_2;
    /**输入任务详情**/
    @BindView(R.id.input_task_message)
    EditText input_task_message;
    /**输入任务名***/
    @BindView(R.id.taskname_inout)
    EditText input_taskname;

    @BindView(R.id.msize_text)
    TextView msize_text;

    @BindView(R.id.task_danwei)
    TextView task_danwei;
    @BindView(R.id.layout_task)
    LinearLayout layout_task;
    @BindView(R.id.task_name_choicedata)
    TextView task_name_choicedata;

    private Dialog mLoadingDialog;
   //选任任务列表数据
    private List<TaskListData_ListBean> taskListdata = new ArrayList<>();
    //选择任务列表数据适配器
    private ChoiceChallengTaskAdpater choicetaskadpater;
    //选择任务列表弹窗
    private Dialog choicetaskdialog;
    /***被选中的任务***/
    private TaskListData_ListBean taskdata;
    /***被选中的任务详情***/
    private UserDontGetTaskListDataListBean taskdatadetail;
    //选择任务级别
    private ChoiceSexDialog choicetasklevel;
    /****被选择的任务级别（"0" 默认，"1" 末级，"2" 非末级）****/
    private int tasklevel = 0;
    //选择任务类型
    private ChoiceSexDialog choicesexdiag;
    /****被选择的任务类型（"0" 默认，"1" 自由领取，"2" 强制分配）****/
    private int tasktype = 0;

    private int nowyear = 0;
    private int nowmonth = 0;
    private int nowday = 0;
    /**选择的开始时间***/
    private String starttime = "";
    /***选择结束时间***/
    private String endtime = "";
    Calendar calendar = Calendar.getInstance();
    private ChoiceSexDialog setfb_money1;
    /***强制分配奖金分配方式 "0" 默认,"1" 固定金额*,"2" 目标比例**/
    public int choiceintmoey_type1 = 0;

    private ChoiceSexDialog setfb_money2;
    /***超额奖金分配方式 "0" 默认,"1" 固定金额*,"2" 目标比例**/
    private int choiceintmonet_type2 = 0;

    /***自由领取奖金领取分配方式****/
    private Input_Money_Dialog input_money_dialog;
    private int inputmoney_type = 1;
    private String max_size = "";
    private String min_size = "";

    public List<Level_ListdataBean> danweilistdat = new ArrayList<>();
    private DanWeiAdpater danweiadpater;

    private Dialog choidedanweidialog;

    private Level_ListdataBean danweidata;

    @Override
    protected void initView() {
       setContentView(R.layout.activity_releastask);
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        tv_title_activity_baseperson.setText("发布任务");
        getchoiechallengtaskdata();
        danweiadpater = new DanWeiAdpater(mcontent,danweilistdat);
        danweiadpater.setlistonclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                if(choidedanweidialog!=null){
                    choidedanweidialog.dismiss();
                }
                danweidata = danweilistdat.get(position);
                task_danwei.setText(danweidata.name);

            }
        });
        getdanweidata();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,ReleaseTaskActivity.class);
        mContext.startActivity(mIntent);
    }



    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }


    /**
     * 选择任务
     */
    @OnClick(R.id.choice_task_layout)
    public void choicetask(){

        if(taskListdata.size()>0&&choicetaskadpater!=null){
            if(choicetaskdialog==null) {
                choicetaskdialog = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(choicetaskadpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   taskdata = choicetaskadpater.getclickdata1();
                   if(taskdata!=null){
                       task_name.setText("请选择");
                       starttime = taskdata.MissionStartDatetime.replace("0:00:00","");
                       endtime = taskdata.MissionEndDatetime.replace("0:00:00","");
                       starttime_text.setText(taskdata.MissionStartDatetime.replace("0:00:00",""));
                       endtime_text.setText(taskdata.MissionStartDatetime.replace("0:00:00",""));
                       layout_task.setVisibility(View.VISIBLE);
                       task_name_choicedata.setText(taskdata.MissionName);

                       getTaskDetail(taskdata.Missionid);
                    }
                        choicetaskdialog.dismiss();
                    }

                }).Builder();
            }
            choicetaskdialog.show();
        }
    }

    /**
     * 选择任务级别
     */
    @OnClick(R.id.choice_task_level_layout)
    public void choiceTaskleve(){
        if (choicetasklevel==null) {

            choicetasklevel = new ChoiceSexDialog.Builder(ReleaseTaskActivity.this)
                    //.setTitle("更换封面")
                    .addMenu("末级", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicetasklevel.dismiss();
                            tasklevel = 1;
                            task_level.setText("末级");

                        }
                    }).addMenu("非末级", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicetasklevel.dismiss();
                            tasklevel = 2;
                            task_level.setText("非末级");

                        }
                    }).create();

        }
        if (!choicetasklevel.isShowing()){

            choicetasklevel.show();
        }
    }


    /**
     * 选择任务类型
     */
    @OnClick(R.id.choice_task_type_layout)
    public void choicetaskType(){

//        if (choicesexdiag==null) {
//
//            choicesexdiag = new ChoiceSexDialog.Builder(ReleaseTaskActivity.this)
//                    //.setTitle("更换封面")
//                    .addMenu("自由领取", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            choicesexdiag.dismiss();
//                            tasktype = 1;
//                            layout_type2.setVisibility(View.GONE);
//                            layout_type1.setVisibility(View.VISIBLE);
//                            task_type.setText("自由领取");
//
//                        }
//                    }).addMenu("强制分配", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            choicesexdiag.dismiss();
//                            tasktype = 2;
//                            layout_type1.setVisibility(View.GONE);
//                            layout_type2.setVisibility(View.VISIBLE);
//                            task_type.setText("强制分配");
//
//
//                        }
//                    }).create();
//
//        }
//        if (!choicesexdiag.isShowing()){
//
//            choicesexdiag.show();
//        }

        if(input_money_dialog==null){
            input_money_dialog = new Input_Money_Dialog.Builder(mcontent).setMoneyclicklister(new UpdateMoney_DIalog_ClickLister() {
                @Override
                public void click(View v, String data, int choicetype, String data2) {
                    input_money_dialog.dismiss();
                    tasktype = choicetype;
                    max_size = data;
                    min_size = data2;
//                    if(taskdata.MissionType.equals(tasktype)) {
                        task_type.setText(tasktype == 1 ? "分配" : "自由领取：" + max_size + "-" + min_size);
//                    }else{
//                        tasktype = 0;
//                        ToastUtils.makeText("无法选择此类型");
//                        return;
//                    }
                    if(tasktype==1){
                        layout_type1.setVisibility(View.GONE);
                            layout_type2.setVisibility(View.VISIBLE);
                    }else if(tasktype==2){
                                                    layout_type2.setVisibility(View.GONE);
                            layout_type1.setVisibility(View.VISIBLE);
                    }

                }
            }).Builder();
        }
        input_money_dialog.show();
    }


    /**
     * 选择开始时间
     */
    @OnClick(R.id.choice_starttime_layout)
    public void choicestarttrime(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                starttime = nowyear+"—"+nowmonth+"-"+nowday;
                starttime_text.setText(starttime);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = UIUtils.inflate(mcontent,R.layout.dialog_choicedate);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);

        }

    /**
     * 选择结束是时间
     */
    @OnClick(R.id.choice_endtime_layout)
    public void chouceendtime(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                endtime = nowyear+"—"+nowmonth+"-"+nowday;
                endtime_text.setText(endtime);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = UIUtils.inflate(mcontent,R.layout.dialog_choicedate);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle("设置日期");
        dialog.setView(dialogView);
        dialog.show();
        //初始化日期监听事件
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
    }


    /***
     * 输入强制分配——分配方式1
     */
    @OnClick(R.id.choice_qzfp_layout)
    public void choiceqzfp(){
        if (setfb_money1==null) {

            setfb_money1 = new ChoiceSexDialog.Builder(ReleaseTaskActivity.this)
                    //.setTitle("更换封面")
                    .addMenu("固定金额", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setfb_money1.dismiss();
                            choiceintmoey_type1 = 1;
                            fp_money_text.setText("固定金额");

                        }
                    }).addMenu("目标比列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setfb_money1.dismiss();
                            choiceintmoey_type1 = 2;
                            fp_money_text.setText("目标比列");


                        }
                    }).create();

        }
        if (!setfb_money1.isShowing()){

            setfb_money1.show();
        }
    }

    /**
     * 超额完成分配方式
     */
    @OnClick(R.id.chaoe_choice_layout)
    public void choicechaoe(){
        if (setfb_money2==null) {

            setfb_money2 = new ChoiceSexDialog.Builder(ReleaseTaskActivity.this)
                    //.setTitle("更换封面")
                    .addMenu("固定金额", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setfb_money2.dismiss();
                            choiceintmonet_type2 = 1;
                            chaoe_text.setText("固定金额");

                        }
                    }).addMenu("目标比列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setfb_money2.dismiss();
                            choiceintmonet_type2 = 2;
                            chaoe_text.setText("目标比列");


                        }
                    }).create();

        }
        if (!setfb_money2.isShowing()){

            setfb_money2.show();
        }
    }


    @OnClick(R.id.choice_jianjin_layout)
    public void choicejianjin_layout(){
//        if(input_money_dialog==null){
//            input_money_dialog = new Input_Money_Dialog.Builder(mcontent).setMoneyclicklister(new UpdateMoney_DIalog_ClickLister() {
//                @Override
//                public void click(View v, String data, int choicetype, String data2) {
//                    input_money_dialog.dismiss();
//                    inputmoney_type = choicetype;
//                    max_size = data;
//                    min_size = data2;
//                    msize_text.setText(inputmoney_type==1?"强制分配":"自由领取"+max_size+"-"+min_size);
//
//                }
//            }).Builder();
//        }
//        input_money_dialog.show();

        if (choicesexdiag==null) {

            choicesexdiag = new ChoiceSexDialog.Builder(ReleaseTaskActivity.this)
                    //.setTitle("更换封面")
                    .addMenu("固定金额", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicesexdiag.dismiss();
                            inputmoney_type = 1;
                            msize_text.setText("固定金额");

                        }
                    }).addMenu("目标比列", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicesexdiag.dismiss();
                            inputmoney_type = 2;
                            msize_text.setText("目标比列");


                        }
                    }).create();

        }
        if (!choicesexdiag.isShowing()){

            choicesexdiag.show();
        }
    }

    /**
     * 获取任务列表
     */
    private void getchoiechallengtaskdata(){
        Observable observable =
                ApiUtils.getApi().tasklistdata(new TasklistdataBody(BaseActivity.getuser().UserId,"选择发布任务"))
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
                        taskListdata.clear();
                        taskListdata.addAll(stringStatusCode.getResult_Data().tfb);
                        choicetaskadpater = new ChoiceChallengTaskAdpater(mcontent,taskListdata);

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
                    taskdatadetail = stringStatusCode.getResult_Data();
                    updatanowview(taskdatadetail);
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    private void updatanowview(UserDontGetTaskListDataListBean update){

        if(update!=null){

            if(!TextUtils.isEmpty(update.MissionAmountTotal)){
                task_text_allmoeny.setText(update.MissionAmountTotal);
            }

            if(!TextUtils.isEmpty(update.OverfulfilAmountTotal)){
                money_2.setText(update.OverfulfilAmountTotal);
            }

            if(!TextUtils.isEmpty(update.RecAmountTotal)){
                text_money_2.setText(update.RecAmountTotal);
            }

            if(!TextUtils.isEmpty(update.Missionlevel)){
                if(update.Missionlevel.equals("1")){
                    task_level.setText("非末级");
                    tasklevel =2;
                }else if(update.Missionlevel.equals("2")){
                    task_level.setText("末级");
                    tasklevel = 1;
                }
            }

            if(!TextUtils.isEmpty(update.MissionType)){
                if(update.MissionType.equals("1")){
                    task_type.setText("分配");
                    tasktype = 1;
                    layout_type1.setVisibility(View.GONE);
                    layout_type2.setVisibility(View.VISIBLE);
                }else if(update.MissionType.equals("2")){
                    task_type.setText("自由领取");
                    tasktype = 2;
                    layout_type2.setVisibility(View.GONE);
                    layout_type1.setVisibility(View.VISIBLE);

                }
            }
            if(!TextUtils.isEmpty(update.MissionAmountType)){
                if(update.MissionAmountType.equals("1")){
                    choiceintmoey_type1 = 1;
                    fp_money_text.setText("固定金额");
                }else if(update.MissionAmountType.equals("2")){
                    choiceintmoey_type1 = 2;
                    fp_money_text.setText("目标比例");
                }
            }

            if(!TextUtils.isEmpty(update.OverfulfilAmountType)){
                if(update.OverfulfilAmountType.equals("1")){
                    choiceintmonet_type2 = 1;
                    chaoe_text.setText("固定金额");
                }else if(update.OverfulfilAmountType.equals("2")){
                    choiceintmonet_type2 = 2;
                    chaoe_text.setText("目标比例");
                }
            }


            if(!TextUtils.isEmpty(update.RecAmountType)){
                if(update.RecAmountType.equals("1")){
                    inputmoney_type = 1;
                    msize_text.setText("固定金额");
                }else if(update.RecAmountType.equals("2")){
                    inputmoney_type = 2;
                    msize_text.setText("目标比例");
                }
            }

            if(!TextUtils.isEmpty(update.MissionTargetUnitName)&&!TextUtils.isEmpty(update.MissionTargetUnit)){
                danweidata = new Level_ListdataBean();
                danweidata.name = update.MissionTargetUnitName;
                danweidata.ID = update.MissionTargetUnit;
                task_danwei.setText(danweidata.name);
            }

        }
    }

    /**
     * 下一步
     */

    @OnClick(R.id.ojbk_btn)
    public void netweydata(){

        if(taskdatadetail==null){
            ToastUtils.makeText("请选择任务");
            return;
        }
        if(tasktype==0){
            ToastUtils.makeText("请选择任务类型");
            return ;
        }

        if(tasklevel==0){

            ToastUtils.makeText("请选择任务级别");
            return;
        }

        if(TextUtils.isEmpty(starttime)){
            ToastUtils.makeText("请选择开始时间");
            return;
        }
        if(TextUtils.isEmpty(endtime)){
            ToastUtils.makeText("请选择结束时间");
            return;
        }
         switch (tasktype){
             case 2://自由领取
                 if(inputmoney_type==0){
                     ToastUtils.makeText("请选择自由领取任务的奖金分配方式");
                     return ;
                 }
                 break;
             case 1://强制分配

                 if(choiceintmoey_type1==0){
                     ToastUtils.makeText("请选择分配任务的奖金分配方式");
                     return;
                 }
                 if(choiceintmonet_type2==0){
                     ToastUtils.makeText("请选择分配任务的超额奖金分配方式");
                     return;
                 }



                 break;
         }

        RelessTaskOneBody indata = new RelessTaskOneBody();
        indata.choiceintmoey_type1 = choiceintmoey_type1;
        indata.endtime = endtime;



        indata.MissionIsPK = taskdatadetail.MissionIsPK;
        indata.PkAmount = taskdatadetail.PkAmount;
        indata.GuaranteeMoneyState = taskdatadetail.GuaranteeMoneyState;
        indata.GuaranteeMoney = taskdatadetail.GuaranteeMoney;


        indata.starttime = starttime;
        indata.Task_name = !TextUtils.isEmpty(input_taskname.getText().toString())?input_taskname.getText().toString():"";
        indata.task_message = !TextUtils.isEmpty(input_task_message.getText().toString())?input_task_message.getText().toString():"";
        indata.taskdata = taskdata;
        taskdatadetail.MissionAmountTotal = task_text_allmoeny.getText().toString().trim();
        taskdatadetail.OverfulfilAmountTotal = money_2.getText().toString().trim();
        new LogUntil(mcontent,TAG,"chuandishuju"+new Gson().toJson(taskdatadetail));
        indata.taskdatadetail = taskdatadetail;
        indata.tasklevel = tasklevel;
        indata.tasktype = tasktype;
        indata.choiceintmonet_type2 = choiceintmonet_type2;
        indata.inputmoney_type = inputmoney_type;
        indata.max_size = max_size;
        indata.min_size = min_size;

        indata.danwei = danweidata;


         ReleaseTaskIngActivity.startactivity(mcontent,indata);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finfishactivity(UpdateTAskListEvent event){
        finish();
    }

    /**
     * 日期选择期的回调
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        nowyear = year;
        nowmonth = monthOfYear+1;
        nowday = dayOfMonth;
    }



    @OnClick(R.id.choice_task_dawei_layout)
    public void choicetaskdanwei(){


        if(danweilistdat.size()>0&&danweiadpater!=null){
            if(choidedanweidialog==null) {
                choidedanweidialog = new ChoiceChallengTaskDialog.Builder(mcontent).setAdpater(danweiadpater).setOjBkButtonClickLister(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        choidedanweidialog.dismiss();
                    }

                }).Builder();
            }
            choidedanweidialog.show();
        }


    }


    private void getdanweidata(){
        Observable observable =
                ApiUtils.getApi().GetListbyid(new ListbyidBody("单位","0"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<LevelBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<LevelBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    if(stringStatusCode.getResult_Data().kns.size()>0){
                        danweilistdat.clear();
                        danweilistdat.addAll(stringStatusCode.getResult_Data().kns);
                        if(danweiadpater==null){
                            danweiadpater = new DanWeiAdpater(mcontent,danweilistdat);
                        }else{
                            danweiadpater.notifyDataSetChanged();
                        }
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


    @OnClick(R.id.layout_task)
    public void gotoTsskDetail(){
        TaskDetailActivity.startactivity(mcontent,taskdata,"未完成");
    }

}
