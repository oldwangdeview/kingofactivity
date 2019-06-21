package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoiceChallengTaskAdpater extends BaseAdapter {
    Context mCOntext;
    List<TaskListData_ListBean> listdata;
    ListOnclickLister mlister;
    public static HashMap<Integer ,Boolean> clickdata = new HashMap<>();
    public ChoiceChallengTaskAdpater(Context mCOntext, List<TaskListData_ListBean> listdata){
        this.mCOntext = mCOntext;
        this.listdata = listdata;
        for(int i =0;i<listdata.size();i++){
            clickdata.put(i,false);
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
            convertView = UIUtils.inflate(mCOntext, R.layout.item_choicechallengtask);
        }
        ImageView titleimage = convertView.findViewById(R.id.titleimage);
        TextView text_missionname = convertView.findViewById(R.id.text_missionname);


        if(!TextUtils.isEmpty(listdata.get(position).MissionName)){
            text_missionname.setText(listdata.get(position).MissionName);
        }

        if(clickdata.get(position)){
            titleimage.setImageResource(R.mipmap.xuanzhong);
        }else{
            titleimage.setImageResource(R.mipmap.unselected);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setalldatafalse();
              clickdata.put(position,!clickdata.get(position));
              if(mlister!=null){
                  mlister.onclick(v,position);
              }
              notifyDataSetChanged();

            }
        });

        return convertView;
    }

    public  List<TaskListData_ListBean> getclickdata(){

        List<TaskListData_ListBean> lisclickdata = new ArrayList<>();
        for(int i =0;i<listdata.size();i++){
            if(clickdata.get(i)){
                lisclickdata.add(listdata.get(i));
            }
        }
        return lisclickdata;

    }

    public void setalldatafalse(){
        for(int i =0;i<listdata.size();i++){
            clickdata.put(i,false);
        }
    }

    public TaskListData_ListBean getclickdata1(){


        for(int i =0;i<listdata.size();i++){
            if(clickdata.get(i)){
               return listdata.get(i);
            }
        }
        return null;

    }

    public void setonclicklister(ListOnclickLister lister){
        this.mlister = lister;
    }
}
