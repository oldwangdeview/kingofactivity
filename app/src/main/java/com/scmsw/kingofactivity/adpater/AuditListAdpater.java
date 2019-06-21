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
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class AuditListAdpater extends BaseRecycleAdapter<TaskListData_ListBean> {
    TextView task_name;
    TextView add_user;
    TextView task_size;
    TextView time;
    TextView shenhe;
    private String type;
    private ListOnclickLister mlister;
    public AuditListAdpater(Context context, List<TaskListData_ListBean> datas,String type) {
        super(context, datas, R.layout.item_audit);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, TaskListData_ListBean s, final int position) {
        task_name = holder.getItemView(R.id.task_name);
        add_user = holder.getItemView(R.id.add_user);
        task_size = holder.getItemView(R.id.task_size);
        time = holder.getItemView(R.id.time);
        shenhe = holder.getItemView(R.id.shenhe);

        if(!TextUtils.isEmpty(s.MissionName)){
            task_name.setText(s.MissionName);
        }

        if(!TextUtils.isEmpty(s.fbperspnid)){
            add_user.setText(s.fbperspnid);
        }

        if(!TextUtils.isEmpty(s.ComQty)){
            task_size.setText(s.ComQty);
        }
        if(!TextUtils.isEmpty(s.MissionStartDatetime)&&!TextUtils.isEmpty(s.MissionEndDatetime)){
            time.setText(UIUtils.gettime(s.MissionStartDatetime)+"-"+UIUtils.gettime(s.MissionEndDatetime));
        }

        if(!TextUtils.isEmpty(type)){
            shenhe.setText(type);
        }
        if(!TextUtils.isEmpty(s.States)){
            if(s.States.equals("1")){
                shenhe.setText("已通过");
            }
            if(s.States.equals("4")){
                shenhe.setText("已否决");
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
        task_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        task_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        shenhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });



    }

    public void setListonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
