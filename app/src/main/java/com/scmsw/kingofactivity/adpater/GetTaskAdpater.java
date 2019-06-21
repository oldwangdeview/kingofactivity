package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;

import java.util.List;

public class GetTaskAdpater extends BaseRecycleAdapter<UserDontGetTaskListDataListBean> {
    TextView tutlename;
    TextView startname;
    TextView adduser;
    TextView time;
    TextView mubiaoliang;


    ListOnclickLister lister;
    TextView dabiaoguiz;
    TextView fenpei;
    TextView linqu;
    private int type;

    public GetTaskAdpater(Context context, List<UserDontGetTaskListDataListBean> datas ,int type) {
        super(context, datas, R.layout.item_gettask);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, UserDontGetTaskListDataListBean userDontGetTaskListDataListBean, final int position) {
        tutlename = holder.getItemView(R.id.title_name);
        startname = holder.getItemView(R.id.starttime);
        adduser = holder.getItemView(R.id.adduser);
        time = holder.getItemView(R.id.time);
        dabiaoguiz = holder.getItemView(R.id.dabiaoguiz);
        fenpei = holder.getItemView(R.id.fenpei);
        linqu = holder.getItemView(R.id.linqu);
        mubiaoliang = holder.getItemView(R.id.mubiaoliang);


        if(!TextUtils.isEmpty(userDontGetTaskListDataListBean.MissionTargetQuantized)){
            mubiaoliang.setText(userDontGetTaskListDataListBean.MissionTargetQuantized);
        }

        if(type>0){
            linqu.setVisibility(View.GONE);
            dabiaoguiz.setVisibility(View.VISIBLE);
            fenpei.setVisibility(View.VISIBLE);


        }else{

            dabiaoguiz.setVisibility(View.GONE);
            fenpei.setVisibility(View.GONE);
            linqu.setVisibility(View.VISIBLE);
        }


        if(!TextUtils.isEmpty(userDontGetTaskListDataListBean.MissionName)){
            tutlename.setText(userDontGetTaskListDataListBean.MissionName);
        }
        if(!TextUtils.isEmpty(userDontGetTaskListDataListBean.MissionStartDatetime)){
            startname.setText(userDontGetTaskListDataListBean.MissionStartDatetime);
        }
        if(type>0){
            if(!TextUtils.isEmpty(userDontGetTaskListDataListBean.TargetType)){
                String targettype = userDontGetTaskListDataListBean.TargetType;
                adduser.setText(targettype.equals("1")?"组织机构":targettype.equals("2")?"部门":targettype.equals("3")?"角色":targettype.equals("4")?"岗位":targettype.equals("5")?"末级执行人":"");
            }

        }else
        if(!TextUtils.isEmpty(userDontGetTaskListDataListBean.FBpersonid)){
            adduser.setText(userDontGetTaskListDataListBean.FBpersonid);
        }
        if(!TextUtils.isEmpty(userDontGetTaskListDataListBean.MissionStartDatetime)&&!TextUtils.isEmpty(userDontGetTaskListDataListBean.MissionEndDatetime)){
            time.setText(userDontGetTaskListDataListBean.MissionStartDatetime+"——"+userDontGetTaskListDataListBean.MissionEndDatetime);
        }

        linqu = holder.getItemView(R.id.linqu);
        linqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });
        tutlename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });
        startname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });

        dabiaoguiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });

        fenpei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });


        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });



    }


    public void setonclicllist(ListOnclickLister lister){
        this.lister = lister;
    }
}
