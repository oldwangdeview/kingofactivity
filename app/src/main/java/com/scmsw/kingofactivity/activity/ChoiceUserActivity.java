package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ChoiceUserAdptaer;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.ChallengTaskListDataBean;
import com.scmsw.kingofactivity.bean.Choice_user_ListdataBean;
import com.scmsw.kingofactivity.bean.ChoiceuserBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.body.ChallengTaskListBody;
import com.scmsw.kingofactivity.body.GetUserBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ChoiceUserActivity extends BaseActivity {
    @BindView(R.id.choice_user_listview)
    ListView mlistview;
    @BindView(R.id.ojbk_btn)
    Button ojbk_btn;
    @BindView(R.id.find_iput)
    EditText findinouttext;
    TaskListData_ListBean choicedata;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    private Dialog mLoadingDialog;
    private List<Choice_user_ListdataBean> listdat = new ArrayList<>();
    private ChoiceUserAdptaer madpater;

    @Override
    protected void initView() {
      setContentView(R.layout.activity_choiceuser);
    }


    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("选择PK人员");
        choicedata = (TaskListData_ListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        madpater = new ChoiceUserAdptaer(mcontent,listdat);
        mlistview.setAdapter(madpater);
        if(choicedata!=null){
            getchpiceuserdata(choicedata.Missionid,choicedata.MissionType);
        }

        findinouttext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  String data = findinouttext.getText().toString();
                  if(!TextUtils.isEmpty(data)){
                      List<Choice_user_ListdataBean> finddata = new ArrayList<>();
                      for(int i = 0;i<listdat.size();i++){
                          if(!TextUtils.isEmpty(listdat.get(i).NickName)&&listdat.get(i).NickName.indexOf(data)>=0){
                                finddata.add(listdat.get(i));
                          }else
                          if(!TextUtils.isEmpty(listdat.get(i).Account)&&listdat.get(i).Account.indexOf(data)>=0){
                              finddata.add(listdat.get(i));
                          }
                      }
                      madpater.setdata(finddata);

                  }else{

                      madpater.setdata(listdat);
                  }
            }
        });

    }







    public static void startactivity(Context mContext, TaskListData_ListBean chicedata){
        Intent mIntent = new Intent(mContext,ChoiceUserActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable) chicedata);
        mContext.startActivity(mIntent);
    }


    public void getchpiceuserdata(String missionid,String MissionType){

        Observable observable =
                ApiUtils.getApi().getchoiceuserlistdat(new GetUserBody(BaseActivity.getuser().UserId,missionid,MissionType))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<ChoiceuserBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<ChoiceuserBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){


                    if(stringStatusCode.getResult_Data().tds.size()>0){
                        listdat.clear();
                        listdat.addAll(stringStatusCode.getResult_Data().tds);
                        madpater.setdata(listdat);
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


    @OnClick(R.id.ojbk_btn)
    public void okbtn(){
        List<Choice_user_ListdataBean> clickdata = madpater.getChoiceUserdata();
        EventBus.getDefault().post(clickdata);
        finish();
    }


    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
