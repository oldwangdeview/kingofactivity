package com.scmsw.kingofactivity.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.adpater.MyImageAdapter;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.eventbus.UpdateImagePathEvent;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.view.PhotoViewPager;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LookImageActivity extends BaseActivity {
    @BindView(R.id.mviewpager)
    PhotoViewPager mviewpager;

    @BindView(R.id.index)
    TextView index;
    @BindView(R.id.delete_image)
    ImageView delete_image;
    private MyImageAdapter madpater;
    private List<String> listdat = new ArrayList<>();
    private int nowposition = 0;
    private int type = -1;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_lookimage);
    }


    @Override
    protected void initData() {

        listdat = (List<String>)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        type = getIntent().getIntExtra(Contans.INTENT_TYPE,-1);
        nowposition = getIntent().getIntExtra("position",-1);
        madpater = new MyImageAdapter(listdat,this);
        mviewpager.setAdapter(madpater);
        mviewpager.setCurrentItem(nowposition);
        index.setText(nowposition+1+"/"+listdat.size());
        madpater.clickimage(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                if(type>0){
                    EventBus.getDefault().post(new UpdateImagePathEvent(listdat));
                }
                finish();
            }
        });
        if(type>0){
            delete_image.setVisibility(View.VISIBLE);
        }
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                nowposition = position;
                index.setText(nowposition+1+"/"+listdat.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    @OnClick(R.id.delete_image)
    public void deleteimage(){
        listdat.remove(nowposition);
        madpater.notifyDataSetChanged();
        index.setText(nowposition+"/"+listdat.size());

    }

    public static void startactivity(Context mContext,List<String> imagepath,int position,int type){
        Intent mIntent = new Intent(mContext,LookImageActivity.class);
        mIntent.putExtra(Contans.INTENT_DATA,(Serializable)imagepath);
        mIntent.putExtra("position",position);
        mIntent.putExtra(Contans.INTENT_TYPE,type);
        mContext.startActivity(mIntent);
    }



}
