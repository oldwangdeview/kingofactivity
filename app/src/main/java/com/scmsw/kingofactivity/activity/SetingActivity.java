package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.util.DataCleanManager;
import com.scmsw.kingofactivity.util.PreferencesUtils;
import com.scmsw.kingofactivity.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SetingActivity extends BaseActivity {

    @BindView(R.id.nowviseion)
    TextView nowviseion;
    @BindView(R.id.hc)
    TextView hc;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView tv_title_activity_baseperson;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_seting);
    }

    @Override
    protected void initData() {
        super.initData();
        tv_title_activity_baseperson.setText("设置");
        try {
            hc.setText(DataCleanManager.getTotalCacheSize(SetingActivity.this));
        }catch (Exception e){

        }
        nowviseion.setText("V"+UIUtils.getAppVersionName(SetingActivity.this));
    }
    @OnClick({R.id.clear_layout,R.id.loginout_layout})
    public void click(View v){
        switch (v.getId()){
            case R.id.clear_layout:
                AlertView alertView = new AlertView("", "确定清除缓存～", null, null, new String[]{"取消", "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position==1){
                            DataCleanManager.clearAllCache(SetingActivity.this);
                            try {
                                hc.setText(DataCleanManager.getTotalCacheSize(SetingActivity.this));
                            } catch (Exception e) {

                                hc.setText("0k");
                            }
                        }

                    }
                });
                alertView.show();
                break;
            case R.id.loginout_layout:
                loginout();
                break;
        }
    }

    private void loginout(){
        AlertView alertView = new AlertView(getResources().getString(R.string.help), getResources().getString(R.string.dialog_message), null, null, new String[]{getResources().getString(R.string.translate_text_quxiao), "确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position==1){
                   BaseActivity.userdata= null;
                    PreferencesUtils.getInstance().putString(Contans.USER_DATA,"");
                   Loginactivity.startactivity(SetingActivity.this);
                   finish();
                }else{
                    return;
                }

            }
        });
        alertView.show();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,SetingActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick(R.id.iv_back_activity_basepersoninfo)
    public void finishactivity(){
        finish();
    }
}
