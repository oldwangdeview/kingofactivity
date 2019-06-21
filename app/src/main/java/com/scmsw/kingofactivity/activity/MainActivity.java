package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.MainPagerAdapter;
import com.scmsw.kingofactivity.application.AbsSuperApplication;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.util.FileUtils;
import com.scmsw.kingofactivity.util.ToastUtils;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.ForbidScrollViewpager;

import java.io.File;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.viewpager_activity_main)
    public ForbidScrollViewpager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private MainPagerAdapter mMainPagerAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    mViewPager.setCurrentItem(0,false);
                    UIUtils.showFullScreen(MainActivity.this,false);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1,false);
                    UIUtils.showFullScreen(MainActivity.this,false);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2,false);
                    UIUtils.showFullScreen(MainActivity.this,true);

                    return true;
            }
            return false;
        }
    };



    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        super.initData();
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.setCurrentItem(0);
         navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        creartFilepath();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MainActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL&& event.getAction() == KeyEvent.ACTION_DOWN){
            return true;
        }

        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.makeText(mcontent.getResources().getString(R.string.exite_dialog));
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            showdialogtc();

        }
    }

    private void showdialogtc(){
        AlertView alertView = new AlertView("提示", "确定退出业绩之王～？", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                    finish();
                    System.exit(0);
                }else{
                    isExit = false;
                }

            }
        });
        alertView.show();
    }

    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    private void creartFilepath(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.APP_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/kingofactivity";
        } else {
            FileUtils.APP_DIR = this.getFilesDir().getAbsolutePath() + "/kingofactivity";
        }
        FileUtils.APP_LOG = FileUtils.APP_DIR + "/log";
        FileUtils.APP_CRASH = FileUtils.APP_DIR + "/crash";
        FileUtils.APK_DIR = FileUtils.APP_DIR + "/apks";
        FileUtils.IMAGE_DIR = FileUtils.APP_DIR + "/imag";

        File appDir = new File(FileUtils.APP_DIR);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File logDir = new File(FileUtils.APP_LOG);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File crashDir = new File(FileUtils.APP_CRASH);
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }
        File apkDir = new File(FileUtils.APK_DIR);
        if (!apkDir.exists()) {
            apkDir.mkdirs();
        }
        File imageDir = new File(FileUtils.IMAGE_DIR);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
    }
}
