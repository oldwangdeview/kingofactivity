package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class TaskListAdpater extends BaseRecycleAdapter<TaskListData_ListBean> {
    TextView task_list_title;
    TextView task_list_titlename;

    TextView task_list_comqty;
    TextView task_list_missiontargetquantized;
    TextView text_list_starttime;
    TextView text_list_overtime;
    TextView task_tape;
    ProgressBar progressBar;
    Button ojbk_btn;

    private String typeString;
    private ListOnclickLister mLister;
    public TaskListAdpater(Context context, List<TaskListData_ListBean> datas,String typeString) {
        super(context, datas, R.layout.item_tasklist);
        this.typeString = typeString;
    }

    @Override
    protected void setData(RecycleViewHolder holder, TaskListData_ListBean s, final int position) {

        task_list_title = holder.getItemView(R.id.task_list_title);
        task_list_titlename = holder.getItemView(R.id.task_list_titlename);
        task_list_comqty = holder.getItemView(R.id.task_list_comqty);
        task_list_missiontargetquantized = holder.getItemView(R.id.task_list_missiontargetquantized);
        text_list_starttime = holder.getItemView(R.id.text_list_starttime);
        text_list_overtime = holder.getItemView(R.id.text_list_overtime);
        ojbk_btn = holder.getItemView(R.id.ojbk_btn);
        task_tape = holder.getItemView(R.id.task_tape);

        progressBar = holder.getItemView(R.id.myprogressbar);


        if(!TextUtils.isEmpty(s.MissionName)){
            task_list_title.setText(s.MissionName);
        }else{
            task_list_title.setText("");
        }


        if(!TextUtils.isEmpty(s.MissionStartDatetime)){
            task_list_titlename.setText(UIUtils.gettime(s.MissionStartDatetime));
        }else{
            task_list_titlename.setText("");
        }

        if(!TextUtils.isEmpty(s.ComQty)){
            task_list_comqty.setText(s.ComQty);
        }else{
            task_list_comqty.setText("");
        }


        if(!TextUtils.isEmpty(s.MissionTargetQuantized)){
            task_list_missiontargetquantized.setText(UIUtils.gettime(s.MissionStartDatetime));
        }else{
            task_list_missiontargetquantized.setText("");
        }

        if(!TextUtils.isEmpty(s.MissionStartDatetime)){
            text_list_starttime.setText(UIUtils.gettime(s.MissionStartDatetime));
        }else{
            text_list_starttime.setText("");
        }
        if(!TextUtils.isEmpty(s.MissionEndDatetime)){
            text_list_overtime.setText(UIUtils.gettime(s.MissionEndDatetime));
        }else{
            text_list_overtime.setText("");
        }

        if(!TextUtils.isEmpty(typeString)){
            ojbk_btn.setText(typeString);
            if(typeString.equals("未完成")){
                task_tape.setVisibility(View.VISIBLE);

                try {

                    int data = Integer.parseInt(s.SurplusTimeDay);
                        if(data>=0){
                            task_tape.setText("剩余 "+data+"天");
                        }else{
                            task_tape.setText("逾期"+Math.abs(data)+"天");
                        }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{
                task_tape.setVisibility(View.GONE);
            }
        }


        if(!TextUtils.isEmpty(s.ComQty)&&!TextUtils.isEmpty(s.MissionQty)){
            try {

                Double text1 = Double.parseDouble(s.ComQty);
                Double text2 = Double.parseDouble(s.MissionQty);
                double index = (text1/text2)*100;
                progressBar.setProgress((int)index);


            }catch (Exception e){
                e.printStackTrace();
            }
        }


        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLister!=null){
                    mLister.onclick(v,position);
                }
            }
        });
    }

    public void setOnItemListCilicLister(ListOnclickLister mLister){
        this.mLister = mLister;
    }
}
