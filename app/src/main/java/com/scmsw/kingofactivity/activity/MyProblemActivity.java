package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.ProblemAdpater;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.GetCopyBody;
import com.scmsw.kingofactivity.bean.GetcopyBean;
import com.scmsw.kingofactivity.bean.HomesBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
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

public class MyProblemActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.listview)
    RecyclerView listview;
    public List<HomesBean> homes = new ArrayList<>();
    private ProblemAdpater madpater;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_noticelistdata);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("常见问题");
        madpater = new ProblemAdpater(mcontent,homes);
        listview.setLayoutManager(new LinearLayoutManager(mcontent));
        listview.setItemAnimator(new DefaultItemAnimator());
        listview.setAdapter(madpater);
        madpater.setListonCLickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                ProBlemDetatilActivity.startactivity(mcontent,homes.get(position));
            }
        });
        getdata();
    }

    private Dialog mLoadingDialog;
    private void getdata(){
        Observable observable =
                ApiUtils.getApi().GetCopy(new GetCopyBody("常见问题"))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<GetcopyBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<GetcopyBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    homes.clear();
                    homes.addAll(stringStatusCode.getResult_Data().homes);
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

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyProblemActivity.class);
        mContext.startActivity(mIntent);
    }



    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
