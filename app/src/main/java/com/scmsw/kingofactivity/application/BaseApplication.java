package com.scmsw.kingofactivity.application;

import android.content.Context;
import android.os.Handler;


import com.orhanobut.hawk.Hawk;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
//import com.qiniu.android.common.FixedZone;
//import com.qiniu.android.storage.Configuration;
//import com.qiniu.android.storage.UploadManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Author wangshifu
 * Time  2018/12/25 14:21
 * Dest  ${TODO}
 */
public class BaseApplication extends AbsSuperApplication {

    public static Context mContext;
    public static Handler mHandler;
    public static long		mMainThreadId;
    public static Thread mMainThread;
    public static  boolean isLoginSuccess=false;//登录是否成功
//    public static UserInfoBean sUserInfoBean;  //登录个人信息
    public static String sLocationCity="";  //城市定位
    public   static UploadManager mUploadManager;  //七牛上传管理器
//    private RefWatcher mRefWatcher;
    public static BaseApplication mapp;
    public static boolean isShowLog = true ;//是否显示log



//    public static RefWatcher getRefWatcher(Context context) {
//        BaseApplication application = (BaseApplication) context.getApplicationContext();
//        return application.mRefWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        mapp = this;
        // 内存泄漏检测
     /*   if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        mRefWatcher = LeakCanary.install(this);*/
        closeAndroidPDialog();
        // 1.上下文
//        com.darsom.aar.App.Companion.setInstance(this);
        mContext = getApplicationContext();
//        UMConfigure.setLogEnabled(true);
//        UMConfigure.init(this," 5b860801f29d98114f000096","umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
//        PlatformConfig.setWeixin("wxcf83e4f906c7ecf8", "1b21dbcd56704a4d509eaa88582a921c");
//        PlatformConfig.setQQZone("101553730", "f8b8319b7a7727398787a7d77c6d56fe");
//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
        // 2.创建一个handler
        mHandler = new Handler();
        // 3.得到一个主线程id
        mMainThreadId = android.os.Process.myTid();
        // 4.得到主线程
        mMainThread = Thread.currentThread();
//        ListenClipboardService.start(this );
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        Hawk.init(this).build();
//
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                // .recorder(recorder)           // recorder分片上传时，已上传片记录器。默认null
                //.recorder(recorder, keyGen)   // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                .zone(FixedZone.zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        mUploadManager = new UploadManager(config);


//        PushAgent

    }

    public static BaseApplication getInstance(){
        return mapp;
    }


    @Override
    protected String getAppNameFromSub() {
        return null;
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    /**
//     * 获取toke
//     * @return
//     */
//    public static String getToken(){
//        return isLoginSuccess?sUserInfoBean.token:"scmsw";
//}
//
//    /**
//     * 获取userId
//     * @return
//     */
//    public static String getUserId(){
//
//        return sUserInfoBean!=null?sUserInfoBean.getUserId():"";
//    }
//
//    /**
//     * 获取userId
//     * @return
//     */
//    public static String getNewUserId(){
//
//        return sUserInfoBean!=null?sUserInfoBean.getUserId():null;
//    }
}
