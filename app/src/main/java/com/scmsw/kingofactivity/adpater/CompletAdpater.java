package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.CompletListBean;
import com.scmsw.kingofactivity.bean.HKLatitudesListData;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class CompletAdpater extends BaseAdapter {

    private Context mContext;
    private List<HKLatitudesListData> mlistdata = new ArrayList<>();
    private ListOnclickLister mlister;
    public CompletAdpater(Context mContext, List<HKLatitudesListData> mlisterdata){

        this.mContext = mContext;
        this.mlistdata = mlisterdata;
    }
    @Override
    public int getCount() {
        return mlistdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
         convertView=UIUtils.inflate(mContext, R.layout.item_taskdetail_item);
        }

        convertView.findViewById(R.id.payfor_type).setVisibility(View.GONE);
        TextView name = convertView.findViewById(R.id.name);
        TextView title_text = convertView.findViewById(R.id.title_text);
        TextView wanchenglv = convertView.findViewById(R.id.wanchenglv);
        ProgressBar  progressbar = convertView.findViewById(R.id.progressbar);

        if(!TextUtils.isEmpty(mlistdata.get(position).Name)){
            name.setText(mlistdata.get(position).Name);
        }
        if(!TextUtils.isEmpty(mlistdata.get(position).ComValue)&&!TextUtils.isEmpty(mlistdata.get(position).LatitudeTotalValue)){
            String text1 = mlistdata.get(position).ComValue;
            String text2 = mlistdata.get(position).LatitudeTotalValue;

            title_text.setText(text1+"/"+text2);


            try{
                Double data1 = Double.parseDouble(text1);
                Double data2 = Double.parseDouble(text2);

                double bili = data1/data2*100;
                if(bili>100){
                    progressbar.setProgress(100);
                }else{
                    progressbar.setProgress((int)bili);
                }
                wanchenglv.setText(String.format("%.2f",bili)+"%");

            }catch (Exception e){

            }

        }



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });


        return convertView;
    }


    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
