package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.GetCopyBody;
import com.scmsw.kingofactivity.bean.GetcopyBean;
import com.scmsw.kingofactivity.bean.HomesBean;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;
import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.wcy.htmltext.HtmlText;

public class NoticeDetialActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;

    private HomesBean intentdata;
    private int type;
    @Override
    protected void initView() {

        setContentView(R.layout.activity_noticedetial);
    }


    @Override
    protected void initData() {
        super.initData();
        intentdata = (HomesBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        switch (type){
            case 1:
                tv_title_activity_baseperson.setText("通知详情");
                if(intentdata!=null){
                    if(!TextUtils.isEmpty(intentdata.Title)){
                        title.setText(intentdata.Title);
                    }

                    if(!TextUtils.isEmpty(intentdata.Content)){
                        setContentText(intentdata.Content);
                    }
                }
                break;

            case 2:
                tv_title_activity_baseperson.setText("使用说明");
                getdata(type);
                break;
            case 3:
                tv_title_activity_baseperson.setText("用户协议");
                getdata(type);
                break;
            case 4:
                tv_title_activity_baseperson.setText("天梯榜规则");
                getdata(type);
                break;
        }


    }



    private void setContentText(String text){
                    HtmlText.from(text)
                    .setImageLoader(new HtmlImageLoader() {
                        @Override
                        public void loadImage(String url, final Callback callback) {
                            // Glide sample, you can also use other image loader
                            SimpleTarget<Bitmap> into = Glide.with(NoticeDetialActivity.this)
                                    .load(url)
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource,
                                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                                            callback.onLoadComplete(resource);
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            callback.onLoadFailed();
                                        }
                                    });
                        }

                        @Override
                        public Drawable getDefaultDrawable() {
                            return ContextCompat.getDrawable(NoticeDetialActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public Drawable getErrorDrawable() {
                            return ContextCompat.getDrawable(NoticeDetialActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public int getMaxWidth() {
                            return content.getMaxWidth();
                        }

                        @Override
                        public boolean fitWidth() {
                            return false;
                        }
                    })
                    .setOnTagClickListener(new OnTagClickListener() {
                        @Override
                        public void onImageClick(Context context, List<String> imageUrlList, int position) {
                            // image click
                        }

                        @Override
                        public void onLinkClick(Context context, String url) {
                            // link click
                        }
                    })
                    .into(content);
    }

    private Dialog mLoadingDialog;
    private void getdata(int type){
        Observable observable =
                ApiUtils.getApi().GetCopy(new GetCopyBody(type==2?"使用帮助":type==3?"用户协议":type==4?"天梯榜规则":""))
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
                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Title)){
                        title.setText(stringStatusCode.getResult_Data().Title);
                    }

                    if(!TextUtils.isEmpty(stringStatusCode.getResult_Data().Content)){
                        setContentText(stringStatusCode.getResult_Data().Content);
                    }

                    if(stringStatusCode.getResult_Data().homes.size()>0&&!TextUtils.isEmpty(stringStatusCode.getResult_Data().homes.get(0).Content)){
                        setContentText(stringStatusCode.getResult_Data().homes.get(0).Content);
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
    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,NoticeDetialActivity.class);
        mContext.startActivity(mIntent);
    }

    public static void startactivity(Context mContext,int type){
        Intent mIntent = new Intent(mContext,NoticeDetialActivity.class);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }

    public static void startactivity(Context mContext, HomesBean bendata,int type){
        Intent mIntent = new Intent(mContext,NoticeDetialActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)bendata);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
