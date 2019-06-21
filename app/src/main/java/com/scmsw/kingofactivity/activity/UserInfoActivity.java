package com.scmsw.kingofactivity.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.application.BaseApplication;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.bean.EditUserBody;
import com.scmsw.kingofactivity.bean.StatusCode;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBean;
import com.scmsw.kingofactivity.bean.UserBusinessDetailBody;
import com.scmsw.kingofactivity.bean.UserdataBean;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.UpdateUserEvent;
import com.scmsw.kingofactivity.http.HttpUtil;
import com.scmsw.kingofactivity.http.ProgressSubscriber;
import com.scmsw.kingofactivity.http.RxHelper;
import com.scmsw.kingofactivity.rxjava.ApiUtils;
import com.scmsw.kingofactivity.util.FileUtils;
import com.scmsw.kingofactivity.util.LoadingDialogUtils;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.PhotoUtils;
import com.scmsw.kingofactivity.util.PreferencesUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.BottomMenuDialog;
import com.scmsw.kingofactivity.view.ChoiceSexDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UserInfoActivity extends BaseActivity {


    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.heandimage)
    ImageView heandimage;
    @BindView(R.id.sex_text)
    TextView sex_text;
    @BindView(R.id.nicename)
    EditText nickname;
    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;
    private String headimagepath = FileUtils.getSDRoot()+"/kingofactivity/imag/crop_photo.jpg";
    private BottomMenuDialog mBottomMenuDialog;
    private Uri imageUri;
    private Uri cropImageUri;
    private File fileUri = new File(FileUtils.IMAGE_DIR + "/photo.jpg");
    private File fileCropUri = new File(FileUtils.IMAGE_DIR+ "/crop_photo.jpg");
    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;
    private Bitmap imagemap = null;
    private String headimagedata = "";
    private String data = "";
    /**
     * 选择性别
     */
    private ChoiceSexDialog choicesexdiag;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_userinfo);
    }

    @Override
    protected void initData() {
        super.initData();
        title_name.setText("我的资料");
        UpdateUserData();
        tv_small_title_layout_head.setVisibility(View.VISIBLE);
//        getqiniutoken();
    }

    @OnClick(R.id.choice_heandimage)
    public void choiceheandimage(){
        if (mBottomMenuDialog==null) {

            mBottomMenuDialog = new BottomMenuDialog.Builder(UserInfoActivity.this)
                    //.setTitle("更换封面")
                    .addMenu(UserInfoActivity.this.getResources().getString(R.string.tackphoto), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomMenuDialog.dismiss();


/*
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, Contans.CAMERA_REQUEST_CODE);*/

                            imageUri = Uri.fromFile(fileUri);
                            //通过FileProvider创建一个content类型的Uri
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.scmsw.kingofactivity", fileUri);
                            }
                            PhotoUtils.takePicture(UserInfoActivity.this, imageUri, Contans.CAMERA_REQUEST_CODE);
                        }
                    }).addMenu(UserInfoActivity.this.getResources().getString(R.string.imagelist), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomMenuDialog.dismiss();
                          /*  Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, Contans.ALBUM_REQUEST_CODE);*/
                            PhotoUtils.openPic(UserInfoActivity.this, Contans.GALLERY_REQUEST_CODE);
                        }
                    }).create();

        }
        if (!mBottomMenuDialog.isShowing()){

            mBottomMenuDialog.show();
        }
    }

    @OnClick(R.id.tv_small_title_layout_head)
    public void sacve(){
        String nickname_tetx = nickname.getText().toString().trim();
        String mgebdex = sex_text.getText().toString().equals("男")?"1":sex_text.getText().toString().equals("女")?"2":"";
        if(TextUtils.isEmpty(headimagedata)) {
            UplodeUserdata(TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)?"":BaseActivity.getuser().HeadIcon, nickname_tetx, mgebdex);
        }else {
            getqiniutoken();
        }
    }
    @OnClick(R.id.choice_sex)
    public void choicesex(){
        if (choicesexdiag==null) {

            choicesexdiag = new ChoiceSexDialog.Builder(UserInfoActivity.this)
                    //.setTitle("更换封面")
                    .addMenu("男", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicesexdiag.dismiss();
                            sex_text.setText("男");
                        }
                    }).addMenu("女", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choicesexdiag.dismiss();

                            sex_text.setText("女");
                        }
                    }).create();

        }
        if (!choicesexdiag.isShowing()){

            choicesexdiag.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode) {
                case  Contans.CAMERA_REQUEST_CODE:
                    //相机
                    headimagedata = fileUri.getPath();
//                    FileUtils.deleteFile(FileUtils.IMAGE_DIR+ "/crop_photo.jpg");
//                    cropImageUri = Uri.fromFile(fileCropUri);
//                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, Contans.CODE_RESULT_REQUEST);

                    getqiniutoken();
                    break;
                case Contans.GALLERY_REQUEST_CODE:
                    if(!new File(headimagepath).exists()){
                        new File(headimagepath).delete();
                    }
                    headimagedata = PhotoUtils.getPath(mcontent,data.getData());
                    //相册
                    //相机
//                    FileUtils.deleteFile(FileUtils.IMAGE_DIR+ "/crop_photo.jpg");
//                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        newUri = FileProvider.getUriForFile(this, "com.scmsw.kingofactivity", new File(newUri.getPath()));
                    }
//                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, Contans.CODE_RESULT_REQUEST);

                    getqiniutoken();
                    break;


            }
        }
    }


    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,UserInfoActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }




    private void UpdateUserData(){
        if(BaseActivity.getuser()!=null){
            if(!TextUtils.isEmpty(BaseActivity.getuser().HeadIcon)){
                UIUtils.loadImageViewRoud(mcontent,BaseActivity.getuser().HeadIcon,heandimage,UIUtils.dip2px(20),R.mipmap.mrtx);
            }

            if(!TextUtils.isEmpty(BaseActivity.getuser().RealName)){
                nickname.setText(BaseActivity.getuser().RealName);
            }else{
                nickname.setText("");
            }
            if(!TextUtils.isEmpty(BaseActivity.getuser().Gender+"")){
                sex_text.setText(BaseActivity.getuser().Gender.equals("1")?"男":BaseActivity.getuser().Gender.equals("2")?"女":"保密");
            }
        }
    }

    /**
     * 上传修改数据
     */
    private Dialog mLoadingDialog;
    private void UplodeUserdata(String user_heandimage,String nickname,String sex){

        Observable observable =
                ApiUtils.getApi().EditUser(new EditUserBody(
                        BaseActivity.getuser().UserId,TextUtils.isEmpty(nickname)?"INVALID9999":nickname,
                        TextUtils.isEmpty(user_heandimage)?"INVALID9999":user_heandimage,
                        TextUtils.isEmpty(sex)?"INVALID9999":sex))
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<UserdataBean>(mcontent) {
            @Override
            protected void _onNext(StatusCode<UserdataBean> stringStatusCode) {
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getResult_Code()==1&&stringStatusCode.getResult_Data()!=null){
                    BaseActivity.userdata = null;
                    PreferencesUtils.getInstance().putString(Contans.USER_DATA,new Gson().toJson(stringStatusCode.getResult_Data()));
                    EventBus.getDefault().post(new UpdateUserEvent(stringStatusCode.getResult_Data()));
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
                   dismissLoadingDialog();
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
                               updateloimage(uptoken);
                           }

                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }
               break;
               case 102:
                   dismissLoadingDialog();
                   String imagepath = msg.obj+"";
                   String nickname_tetx = nickname.getText().toString().trim();
                   String mgebdex = sex_text.getText().toString().equals("男")?"1":sex_text.getText().toString().equals("女")?"2":"";
                   UplodeUserdata(imagepath, nickname_tetx, mgebdex);
           }
       }
   };

    private void updateloimage(final String tokebn){
        String imagename = UIUtils.getUUidString()+".jpg";
        if(headimagedata.indexOf("file:///")>=0){
            headimagedata = headimagedata.replace("file:///","");
        }
        new LogUntil(mcontent,TAG,"imagepath"+headimagedata);
        BaseApplication.mUploadManager.put( new File(headimagedata), imagename, tokebn,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Log.i("qiniu", "Upload Success");
                            String imagepath = Contans.headPicUrl+info.path;
                            new LogUntil(mcontent,TAG,"图片地址"+imagepath);
                   Message msg = new Message();
                   msg.arg1 = 102;
                   msg.obj = imagepath;
                   handler.sendMessage(msg);
                        } else {
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ", " + info + ", " + res);
                    };
                }, null);

    }

}
