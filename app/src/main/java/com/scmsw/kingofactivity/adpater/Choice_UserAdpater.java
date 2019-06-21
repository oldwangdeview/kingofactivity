package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.bean.Task_userBean;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.MyGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Choice_UserAdpater  {
    private List<Task_userBean> listdat = new ArrayList<>();
    private Context mContext;

    private static HashMap<Integer ,GrideAdpater> mhashmap = new HashMap<>();
    private LinearLayout mlayout;

    private RelessTaskOneBody tasktype;
    public Choice_UserAdpater(Context mContext, List<Task_userBean> listdat, LinearLayout mlayout,RelessTaskOneBody tasktype){

        this.mContext = mContext;
        this.listdat = listdat;
        this.mlayout = mlayout;
        this.tasktype = tasktype;
        updateview();
    }



   public static HashMap<Integer ,GrideAdpater> hashmap = new HashMap<>();

    private void updateview(){
        for(int i =0;i<listdat.size();i++){
            View mview = UIUtils.inflate(mContext,R.layout.item_choice_taskuser);
            TextView title = mview.findViewById(R.id.title_nae);
            if(!TextUtils.isEmpty(listdat.get(i).title)){
                title.setText(listdat.get(i).title);
            }
            MyGridView mgridview = mview.findViewById(R.id.listview);

            new LogUntil(mContext,"adpater","tapy"+tasktype+"");
            GrideAdpater madpater = new GrideAdpater(mContext, listdat.get(i).dataliste,tasktype,listdat.get(i).id);
            hashmap.put(i,madpater);
            mgridview.setAdapter(hashmap.get(i));
            mlayout.addView(mview);
        }
    }
}
