package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;

import java.util.Date;
import java.util.List;

public class TaskFeedBackAdpater extends BaseRecycleAdapter<TaskListData_ListBean> {

    TextView task_name;
    TextView task_time;
    TextView adduser;
    TextView jindu;
    ListOnclickLister mlister;
    public TaskFeedBackAdpater(Context context, List<TaskListData_ListBean> datas) {
        super(context, datas, R.layout.item_feedback);
    }

    @Override
    protected void setData(RecycleViewHolder holder, TaskListData_ListBean s, final int position) {

        jindu = holder.getItemView(R.id.jindu);
        task_name  = holder.getItemView(R.id.task_name);
        task_time = holder.getItemView(R.id.task_time);
        adduser = holder.getItemView(R.id.addname);


        if(!TextUtils.isEmpty(s.MissionName)){
            task_name.setText(s.MissionName);
        }
        if(!TextUtils.isEmpty(s.fbpersonname)){
            adduser.setText(s.fbpersonname);
        }

        if(!TextUtils.isEmpty(s.MissionStartDatetime)&&!TextUtils.isEmpty(s.MissionEndDatetime)){
            task_time.setText(s.MissionStartDatetime+"-"+s.MissionEndDatetime);
        }

        if(!TextUtils.isEmpty(s.ComQty)&&!TextUtils.isEmpty(s.MissionQty)){
            try {
                double data1 = Double.parseDouble(s.ComQty);
                double data2 = Double.parseDouble(s.MissionQty);

                double fl = data1/data2;
                if(fl>0){
                    jindu.setText(fl+"%");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
    }



    public void setlistonclick(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
