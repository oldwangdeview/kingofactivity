package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;

import java.util.List;

public class MyRelesslistdataAdpater extends BaseRecycleAdapter<UserDontGetTaskListDataListBean> {

    TextView task_list_title;
    TextView task_list_titlename;
    TextView task_list_comqty;
    TextView task_list_missiontargetquantized;
    ProgressBar myprogressbar;
    TextView text_list_starttime;
    TextView text_list_overtime;
    Button ojbk_btn;

    ListOnclickLister mlister;

    public MyRelesslistdataAdpater(Context context, List<UserDontGetTaskListDataListBean> datas) {
        super(context, datas, R.layout.item_tasklist);
    }

    @Override
    protected void setData(RecycleViewHolder holder, UserDontGetTaskListDataListBean s, final int position) {
        task_list_title = holder.getItemView(R.id.task_list_title);
        task_list_titlename = holder.getItemView(R.id.task_list_titlename);
        task_list_comqty = holder.getItemView(R.id.task_list_comqty);
        task_list_missiontargetquantized = holder.getItemView(R.id.task_list_missiontargetquantized);
        myprogressbar = holder.getItemView(R.id.myprogressbar);
        text_list_starttime = holder.getItemView(R.id.text_list_starttime);
        text_list_overtime = holder.getItemView(R.id.text_list_overtime);
        ojbk_btn = holder.getItemView(R.id.ojbk_btn);
        if(!TextUtils.isEmpty(s.MissionName)){
            task_list_title.setText(s.MissionName);
        }else{
            task_list_comqty.setText("");
        }
        if(!TextUtils.isEmpty(s.MissionStartDatetime)){
            task_list_titlename.setText(s.MissionStartDatetime);
        }
        if(!TextUtils.isEmpty(s.MissionTargetQuantized)){
            task_list_comqty.setText("任务总量："+s.MissionTargetQuantized);
        }

        if(!TextUtils.isEmpty(s.MissionType)){
            if(s.MissionType.equals("1")){
                if(!TextUtils.isEmpty(s.MissionAmountTotal)){
                    task_list_missiontargetquantized.setText("任务奖金："+s.MissionAmountTotal);
                }

            }
            if(s.MissionType.equals("2")){
                if(!TextUtils.isEmpty(s.RecAmountTotal)){
                    task_list_missiontargetquantized.setText("任务奖金："+s.RecAmountTotal);
                }
            }
        }

        myprogressbar.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(s.MissionStartDatetime)){
            text_list_starttime.setText(s.MissionStartDatetime);
        }
        if(!TextUtils.isEmpty(s.MissionEndDatetime)){
            text_list_overtime.setText(s.MissionEndDatetime);
        }

        ojbk_btn.setBackgroundResource(R.drawable.challengettask_shape_button);
        ojbk_btn.setText("详情");
        ojbk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }


    public void setonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
