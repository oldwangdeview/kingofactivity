package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoiceLevelAdpater extends BaseAdapter {
    Context mContext;
    List<Level_ListdataBean> listdata;
    int task_type;
    private static HashMap<Integer ,Boolean> click = new HashMap<>();
    public ChoiceLevelAdpater(Context mContext,List<Level_ListdataBean> listdata){
        this.listdata = listdata;
        this.mContext = mContext;
        for(int i =0;i<listdata.size();i++){
            click.put(i,false);
        }
    }
    @Override
    public int getCount() {
        return listdata.size();
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
            convertView = UIUtils.inflate(mContext, R.layout.item_choicechallengtask);
        }
        ImageView titleimage = convertView.findViewById(R.id.titleimage);
        TextView text_missionname = convertView.findViewById(R.id.text_missionname);
        if(!TextUtils.isEmpty(listdata.get(position).name)){
            text_missionname.setText(listdata.get(position).name);
        }

        if(click.get(position)){
            titleimage.setImageResource(R.mipmap.xuanzhong);
        }else{
             titleimage.setImageResource(R.mipmap.unselected);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task_type==1){
                    updateclickdata();
                }
                click.put(position,!click.get(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void settasktype(int type){
        this.task_type = type;
    }
    public  List<Level_ListdataBean> getclickdata(){
        List<Level_ListdataBean> clickdata = new ArrayList<>();
        for(int i=0;i<listdata.size();i++){
            if(click.get(i)){
                clickdata.add(listdata.get(i));
            }
        }
        return clickdata;
    }

    private  void updateclickdata(){
        for(int i =0;i<listdata.size();i++){
            click.put(i,false);
        }
    }
}
