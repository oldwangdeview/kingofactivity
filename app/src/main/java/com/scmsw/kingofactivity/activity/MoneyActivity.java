package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.MoneyAdpater;
import com.scmsw.kingofactivity.adpater.ProblemAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.GetCopyBody;
import com.scmsw.kingofactivity.bean.GetcopyBean;
import com.scmsw.kingofactivity.bean.MoneyBean;
import com.scmsw.kingofactivity.bean.Money_listdataBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.body.MoneyBody;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 资金流水
 */
public class MoneyActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    private MoneyAdpater madpater;
    @BindView(R.id.mlisteview)
    RecyclerView listview;
    public List<Money_listdataBean> mlistdata = new ArrayList<>();
    @Override
    protected void initView() {
        setContentView(R.layout.activity_moneydata);
    }


    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("资金流水");
        madpater = new MoneyAdpater(mcontent,mlistdata);
        listview.setLayoutManager(new LinearLayoutManager(mcontent));
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(madpater);
        getdata();
    }



    private Dialog mLoadingDialog;
    private void getdata(){
        Observable observable =
                ApiUtils.getApi().GetCapitalOrderRelationtextByUserid(new MoneyBody(BaseActivity.getuser().UserId))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<MoneyBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<MoneyBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    mlistdata.clear();
                    mlistdata.addAll(stringStatusCode.getResult_Data().cort);
                    madpater.notifyDataSetChanged();
                }
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MoneyActivity.class);
        mContext.startActivity(mIntent);
    }
}
