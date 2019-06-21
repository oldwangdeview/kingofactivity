package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;

import java.util.List;

public class DimemsionAdpater extends BaseRecycleAdapter<HKLatitudesListData> {

    TextView task_name;
    TextView qty;
    TextView task_zq;
    Button button;
    ListOnclickLister mlister;
    Button button_xq;
    ProgressBar progressbar;
    LinearLayout jindu_layout;
    TextView weidu_typedata;
    LinearLayout weidu_type;
    LinearLayout layout_shanchu;
    Button button_xg;
    Button button_sc;
    LinearLayout layout_mom;
    TextView text_mom;


    int type;

    public DimemsionAdpater(Context context, List<HKLatitudesListData> datas,int type) {
        super(context, datas, R.layout.item_dimension);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, HKLatitudesListData s, final int position) {

        task_name = holder.getItemView(R.id.task_name);
        qty = holder.getItemView(R.id.qty);
        task_zq = holder.getItemView(R.id.task_zq);
        button = holder.getItemView(R.id.button);
        button_xq  = holder.getItemView(R.id.button_xq);
        progressbar = holder.getItemView(R.id.progressbar);
        jindu_layout  = holder.getItemView(R.id.jindu_layout);
        weidu_typedata = holder.getItemView(R.id.weidu_typedata);
        weidu_type = holder.getItemView(R.id.weidu_type);
        layout_shanchu = holder.getItemView(R.id.layout_shanchu);
        button_xg = holder.getItemView(R.id.button_xg);
        button_sc = holder.getItemView(R.id.button_sc);
        layout_mom = holder.getItemView(R.id.layout_mom);
        text_mom = holder.getItemView(R.id.text_mom);


        if(!TextUtils.isEmpty(s.Memo)){
            text_mom.setText(s.Memo);
        }else{
            text_mom.setText("未添加备注");
        }
        if(!TextUtils.isEmpty(s.LatitudeTotalValue)){
            weidu_typedata.setText(s.LatitudeTotalValue);
        }

        if(!TextUtils.isEmpty(s.LatitudesName)){
            task_name.setText(s.LatitudesName);
        }

        if(!TextUtils.isEmpty(s.DayUnitValue)){
            qty.setText(s.DayUnitValue);
        }

        if(!TextUtils.isEmpty(s.IntervalValue)){
            task_zq.setText(s.IntervalValue+"小时");
        }
        if(!TextUtils.isEmpty(s.HKState)){
            if(s.HKState.equals("1")){
                button.setVisibility(View.VISIBLE);
            }
            if(s.HKState.equals("2")){
                button.setVisibility(View.VISIBLE);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        button_xg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        button_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        if(type ==2||type==3){
            button.setVisibility(View.GONE);
            weidu_type.setVisibility(View.GONE);
            layout_shanchu.setVisibility(View.GONE);
            button_xq.setVisibility(View.VISIBLE);
            jindu_layout.setVisibility(View.VISIBLE);
            if(type==3){
                layout_mom.setVisibility(View.GONE);
            }
        }else if(type==1){
            button_xq.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            jindu_layout.setVisibility(View.GONE);
            weidu_type.setVisibility(View.VISIBLE);
            layout_shanchu.setVisibility(View.VISIBLE);

        }else{
            layout_shanchu.setVisibility(View.GONE);
            weidu_type.setVisibility(View.GONE);
            button_xq.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            jindu_layout.setVisibility(View.VISIBLE);
        }
        button_xq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        if(!TextUtils.isEmpty(s.ComValue)&&!TextUtils.isEmpty(s.LatitudeTotalValue)){

            try{
                Double data1 = Double.parseDouble(s.ComValue);
                Double data2 = Double.parseDouble(s.LatitudeTotalValue);

                double bili = data1/data2*100;
                if(bili>100){
                    progressbar.setProgress(100);
                }else{
                    progressbar.setProgress((int)bili);
                }


            }catch (Exception e){

            }

        }
    }


    public void setListClicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }


    public void adddata(HKLatitudesListData data){
        if(data!=null&&!TextUtils.isEmpty(data.MLid)){
            for(int i =0 ;i<mDatas.size();i++){
                if(data.MLid.equals(i+"")){

                    mDatas.remove(i);
                    mDatas.add(data);
                }
            }


        }else{
            mDatas.add(0,data);

        }
        notifyDataSetChanged();
    }
}
