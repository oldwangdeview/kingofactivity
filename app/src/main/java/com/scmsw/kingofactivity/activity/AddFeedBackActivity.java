package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.AddImage_adpater;
import com.scmsw.kingofactivity.application.BaseApplication;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.EnclosureMasterBean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.body.FeedBackBody;
import com.scmsw.kingofactivity.body.FeedBackImageBody;
import com.scmsw.kingofactivity.body.GetTaskdetailBody;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.UpdateImagePathEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.FileUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.PhotoUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.BottomMenuDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AddFeedBackActivity extends BaseActivity {

    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;

    @BindView(R.id.mygridview)
    GridView mygridview;
    @BindView(R.id.comqty)
    EditText comqty;
    @BindView(R.id.message)
    EditText message;
    private AddImage_adpater madpater;


    private BottomMenuDialog mBottomMenuDialog;
    private Uri imageUri;
    private  File fileUri;

    private TaskListData_ListBean choicedata;
    private HKLatitudesListData weidudata;
    private EnclosureMasterBean feeddata;
    private int type = -1;

    private List<String> httpimagelist = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_addnewfeedback);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        tv_title_activity_baseperson.setText("新增回馈");

        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        Object obj = getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(obj!=null) {

            if(obj instanceof EnclosureMasterBean) {
                feeddata = (EnclosureMasterBean) obj;
            }

            if(obj instanceof TaskListData_ListBean) {
                choicedata = (TaskListData_ListBean) obj;
            }

            if(obj instanceof HKLatitudesListData) {
                weidudata = (HKLatitudesListData) obj;
            }
        }
        madpater = new AddImage_adpater(mcontent);
        madpater.setaddimagelister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                switch (v.getId()) {

                    case R.id.image2:
                        if (mBottomMenuDialog == null) {

                            mBottomMenuDialog = new BottomMenuDialog.Builder(AddFeedBackActivity.this)
                                    //.setTitle("更换封面")
                                    .addMenu(AddFeedBackActivity.this.getResources().getString(R.string.tackphoto), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBottomMenuDialog.dismiss();


/*
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, Contans.CAMERA_REQUEST_CODE);*/
                                            fileUri = new File(FileUtils.IMAGE_DIR + "/img_" + new Date().getTime() + "m.jpg");
                                            imageUri = Uri.fromFile(fileUri);
                                            //通过FileProvider创建一个content类型的Uri
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                imageUri = FileProvider.getUriForFile(AddFeedBackActivity.this, "com.scmsw.kingofactivity", fileUri);
                                            }
                                            PhotoUtils.takePicture(AddFeedBackActivity.this, imageUri, Contans.CAMERA_REQUEST_CODE);
                                        }
                                    }).addMenu(AddFeedBackActivity.this.getResources().getString(R.string.imagelist), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBottomMenuDialog.dismiss();
                          /*  Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, Contans.ALBUM_REQUEST_CODE);*/
                                            PhotoUtils.openPic(AddFeedBackActivity.this, Contans.GALLERY_REQUEST_CODE);
                                        }
                                    }).create();

                        }
                        if (!mBottomMenuDialog.isShowing()) {

                            mBottomMenuDialog.show();
                        }
                        break;
                    case R.id.image1:
                        LookImageActivity.startactivity(mcontent,madpater.getfilepath(),position,2);
                        break;
                }
            }

        });
        mygridview.setAdapter(madpater);


        if(feeddata!=null){
            if(!TextUtils.isEmpty(feeddata.Comqty)){
                comqty.setText(feeddata.Comqty);
            }
            if(!TextUtils.isEmpty(feeddata.Memo)){
                message.setText(feeddata.Memo);
            }
            if(feeddata.ed.size()>0){
                List<String> filepath = new ArrayList<>();
                for(int i =0;i<feeddata.ed.size();i++) {
                    filepath.add(feeddata.ed.get(i).Srcurl);
                }
                if(madpater!=null){
                    madpater.setFilepath(filepath);
                }
            }
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case  Contans.CAMERA_REQUEST_CODE:
                    //相机
                    madpater.addimage(fileUri.getPath());

                    break;
                case Contans.GALLERY_REQUEST_CODE:
                    madpater.addimage(PhotoUtils.getPath(mcontent,data.getData()));
                    break;

            }
        }
    }

    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mCOntext, TaskListData_ListBean data){
        Intent mIntent = new Intent(mCOntext,AddFeedBackActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,1);
        mCOntext.startActivity(mIntent);
    }

    public static void startactivity(Context mCOntext, HKLatitudesListData data){
        Intent mIntent = new Intent(mCOntext,AddFeedBackActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,2);
        mCOntext.startActivity(mIntent);
    }
    public static void startactivity(Context mContext, EnclosureMasterBean data,int type){
        Intent mIntent = new Intent(mContext,AddFeedBackActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)data);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }

    private Dialog mLoadingDialog;
    @OnClick(R.id.add_btm)
    public void addfeedback(){
        if(madpater.getfilepath().size()>0) {
            getqiniutoken();
        }else{
            uploddata();
        }
    }


    private  void uploddata(){
        Observable observable = null;
        String comqtydata = comqty.getText().toString();
        String messagedata = message.getText().toString();


        if(TextUtils.isEmpty(comqtydata)){
            ToastUtils.makeText("请输入目标量");
            return;
        }
        if(!TextUtils.isEmpty(messagedata)){
            ToastUtils.makeText("请输入描述");
            return;
        }

        FeedBackBody data = new FeedBackBody();




        if(feeddata==null) {
            data.Busid = type == 1 ? choicedata.Feedbackid : type == 2 ? weidudata.MLid : "";
        }
        data.Userid = BaseActivity.getuser().UserId;
        data.EnclosureType = type+"";
        data.Comqty = comqtydata;
        if(feeddata!=null){
            data.EnclosureMasterid = feeddata.EnclosureMasterid;
        }
        data.ComStates = type==1?"2":type==2?getComStates(comqtydata):"";
        data.RPvalue = type==1?"0":type==2?getRPvalue(comqtydata):"";
        data.Memo = messagedata;

        for(int i =0;i<httpimagelist.size();i++){
            FeedBackImageBody image = new FeedBackImageBody();
            image.Srcurl = httpimagelist.get(i);
            data.ed.add(image);
        }




        if(feeddata==null) {
            observable = ApiUtils.getApi().InsertEnclosureMaster(data);
        }else{
            observable = ApiUtils.getApi().getUpdateEnclosureMaster(data);
        }
        observable .compose(RxHelper.getObservaleTransformer())
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

                finish();
            }

            @Override
            protected void _onError(String message) {
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
            }
        }, "", lifecycleSubject, false, true);
    }


    private String getComStates(String comqty){
        try {
            if(weidudata!=null){
                double data1 = Double.parseDouble(weidudata.DayUnitValue);
                double data2 = Double.parseDouble(comqty);

                if(data2>=data1){
                    return "1";
                }else{
                    return "2";
                }
            }

            if(feeddata!=null){
                double data1 = Double.parseDouble(feeddata.LatitudeTotalValue);
                double data2 = Double.parseDouble(comqty);

                if(data2>=data1){
                    return "1";
                }else{
                    return "2";
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    private String getRPvalue(String comqty){
        try {
            if(weidudata!=null){
                double data1 = Double.parseDouble(weidudata.DayUnitValue);
                double data2 = Double.parseDouble(comqty);

                if(data2>=data1){
                    return weidudata.RewardValue;
                }else{
                    return "-"+weidudata.PenaltyValue;
                }
            }

            if(feeddata!=null){
                double data1 = Double.parseDouble(feeddata.LatitudeTotalValue);
                double data2 = Double.parseDouble(comqty);

                if(data2>=data1){
                    return feeddata.RewardValue;
                }else{
                    return "-"+feeddata.PenaltyValue;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    private void getqiniutoken(){
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    URL url = new URL("http://120.79.136.125/shushe/qiniu/getsecrettoken?pa55w0rd=fuckoff");
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setRequestProperty("accept", "*/*");
                    httpconn.setDoInput(true);
                    httpconn.setDoOutput(true);
                    httpconn.setConnectTimeout(5000);
                    httpconn.connect();
                    //int stat = httpconn.getResponseCode();
                    int stat = 200;
                    String ss = httpconn.getRequestMethod();

                    String msg = "";
                    if (stat == 200) {
                        br = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
                        msg = br.readLine();
                        Log.i("Tag", "msg" + msg);
                        Bundle b = new Bundle();
                        b.putString("msg", msg);
                        Message m = new Message();
                        m.obj = msg;
                        m.arg1 = 101;
                        handler.sendMessage(m);
                    } else {
                        msg = "请求失败";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 101:

                    if (msg.obj != null) {
                        String message = msg.obj + "";
                        new LogUntil(mcontent, TAG, "图片token" + message);
                        try {
                            JSONObject json = new JSONObject(message);
                            if (json.has("data")) {
                                JSONObject data = json.getJSONObject("data");
                                if (data.has("uptoken")) {
                                    String uptoken = data.getString("uptoken");
                                    String a = uptoken.replace("\\s", "");
                                    String token = a.replace(" ", "");
//                                    updateloimage(uptoken);
                                    if(madpater.getfilepath().size()>0){
                                        List<String> filpath = madpater.getfilepath();
                                        httpimagelist.clear();
                                        for(int i =0;i<filpath.size();i++) {
                                            String imagefilepatj = filpath.get(i);
                                            if(imagefilepatj.indexOf("file:///")>=0){
                                                imagefilepatj = imagefilepatj.replace("file:///","");
                                            }

                                            updateloimage(token, imagefilepatj);
                                        }
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case 102:
//                    dismissLoadingDialog();
                    String imagepath = msg.obj+"";
                    httpimagelist.add(imagepath);
                    if(httpimagelist.size()==madpater.getfilepath().size()){
                        dismissLoadingDialog();
                        uploddata();
                    }
//                    String nickname_tetx = nickname.getText().toString().trim();
//                    String mgebdex = sex_text.getText().toString().equals("男")?"1":sex_text.getText().toString().equals("女")?"2":"";
//                    UplodeUserdata(imagepath, nickname_tetx, mgebdex);



            }
        }
    };

    private void updateloimage(final String tokebn, final String imagepath){


        new Thread(){
            @Override
            public void run() {
                super.run();
                final String imagename = UIUtils.getUUidString()+".jpg";
                if(imagepath.indexOf("http")>=0){
                    Message msg = new Message();
                    msg.arg1 = 102;
                    msg.obj = imagepath;
                    handler.sendMessage(msg);
                    return;
                }else {
                    new LogUntil(mcontent,TAG,"qiniufile_"+imagepath);
                    BaseApplication.mUploadManager.put(FileUtils.bitmapToByte(FileUtils.compress(imagepath,2)), imagename, tokebn,
                            new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject res) {
                                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                                    if (info.isOK()) {
                                        Log.i("qiniu", "Upload Success");
                                        String mimagepath = Contans.headPicUrl + imagename;
                                        new LogUntil(mcontent, TAG, "图片地址" + imagepath);
                                        Message msg = new Message();
                                        msg.arg1 = 102;
                                        msg.obj = mimagepath;
                                        handler.sendMessage(msg);
                                    } else {
                                        Log.i("qiniu", "Upload Fail");
                                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                                    }
                                    Log.i("qiniu", key + ", " + info + ", " + res);
                                }

                                ;
                            }, null);
                }
            }
        }.start();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAddimage(UpdateImagePathEvent event){
        if(event.filepath.size()>0){
            madpater.setFilepath(event.filepath);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
