package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseActivity;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.ChallengListData_listBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class ChallengTaskAdpater extends BaseRecycleAdapter<ChallengListData_listBean> {

    TextView title_text;
    TextView addtime_text;
    TextView fab_username_text;
    TextView size_text;
    TextView time;
    Button  type;
    public ListOnclickLister mlister;
    public ChallengTaskAdpater(Context context, List<ChallengListData_listBean> datas) {
        super(context, datas, R.layout.item_challengtask);
    }

    @Override
    protected void setData(RecycleViewHolder holder, ChallengListData_listBean s, final int position) {
        title_text  = holder.getItemView(R.id.title_text);
        addtime_text  = holder.getItemView(R.id.addtime_text);
        fab_username_text  = holder.getItemView(R.id.fab_username_text);
        size_text  = holder.getItemView(R.id.size_text);
        time  = holder.getItemView(R.id.time);
        type  = holder.getItemView(R.id.type);



        if(!TextUtils.isEmpty(s.Missionname)){
            title_text.setText(s.Missionname);
        }else{
            title_text.setText("");
        }
        if(!TextUtils.isEmpty(s.PKcreateDatetime)){
            addtime_text.setText(s.PKcreateDatetime);
        }else{
            addtime_text.setText("");
        }
        if(!TextUtils.isEmpty(s.pktype)&&s.pktype.equals("2")) {
            if (!TextUtils.isEmpty(s.pkFBpersonid)) {

                fab_username_text.setText(s.pkFBpersonid);
            } else {
                fab_username_text.setText("");
            }
        }else if(!TextUtils.isEmpty(s.pktype)&&s.pktype.equals("1")){
            if(!TextUtils.isEmpty(s.pkJSpersonid)){
                fab_username_text.setText(s.pkJSpersonid);
            }else{
                fab_username_text.setText(s.pkJSpersonid);
            }
        }

        if(!TextUtils.isEmpty(s.Amount)){
            size_text.setText(s.Amount);
        }else{
            size_text.setText("");
        }
        if(!TextUtils.isEmpty(s.MissionStartDatetime)&&!TextUtils.isEmpty(s.MissionEndDatetime)){
            time.setText(UIUtils.gettime(s.MissionStartDatetime)+"-"+UIUtils.gettime(s.MissionEndDatetime));
        }

        if(!TextUtils.isEmpty(s.PKStatus)){
           if(s.PKStatus.equals("1")){
               type.setText("已接受");
           }
           if(s.PKStatus.equals("2")){
               type.setText("拒绝");
           }
           if(s.PKStatus.equals("0")){
               type.setText("未接受");
           }
            if(s.PKStatus.equals("3")){
                type.setText("已接受");
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

    public void setlistclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
