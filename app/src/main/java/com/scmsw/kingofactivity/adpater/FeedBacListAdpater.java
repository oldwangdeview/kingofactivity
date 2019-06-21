package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.activity.LookImageActivity;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.EnclosureMasterBean;
import com.scmsw.kingofactivity.bean.EnclosureMaster_benlist;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class FeedBacListAdpater extends BaseRecycleAdapter<EnclosureMasterBean> {


    TextView feedback_time;
    TextView feedback_type;
    TextView feedback_comqty;
    TextView feedback_message;
    MyGridView mygridview;

    LinearLayout layout_update;
    Button btn_up;

    ListOnclickLister mlister;

    public FeedBacListAdpater(Context context, List<EnclosureMasterBean> datas) {
        super(context, datas, R.layout.item_feenbackitem);
    }

    @Override
    protected void setData(RecycleViewHolder holder, EnclosureMasterBean s, final int position) {

        feedback_time = holder.getItemView(R.id.feedback_time);
        feedback_type = holder.getItemView(R.id.feedback_type);
        feedback_comqty = holder.getItemView(R.id.feedback_comqty);
        feedback_message = holder.getItemView(R.id.feedback_message);
        mygridview = holder.getItemView(R.id.mygridview);
        layout_update = holder.getItemView(R.id.layout_update);
        btn_up = holder.getItemView(R.id.btn_up);


        if(!TextUtils.isEmpty(s.CreateDate)){
            feedback_time.setText(s.CreateDate);
        }

        if(!TextUtils.isEmpty(s.Status)){
            layout_update.setVisibility(View.GONE);
            if(s.Status.equals("0")){
                feedback_type.setText("未审核");
            }
            if(s.Status.equals("1")||s.Status.equals("2")||s.Status.equals("3")){
                feedback_type.setText("已审核");
            }

            if(s.Status.equals("4")){
                feedback_type.setText("未通过");
                layout_update.setVisibility(View.VISIBLE);
            }

        }

        if(!TextUtils.isEmpty(s.Comqty)){
            feedback_comqty.setText("回馈任务量："+s.Comqty);
        }


        if(!TextUtils.isEmpty(s.Memo)){
            feedback_message.setText(s.Memo);
        }

        if(s.ed.size()>0){
            final List<EnclosureMaster_benlist> mlistdata = s.ed;
            FeedbackimageAdpater imageadpater = new FeedbackimageAdpater(mContext,s.ed);
            mygridview.setAdapter(imageadpater);
            mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<String> filepath = new ArrayList<>();
                    for(int i = 0;i<mlistdata.size();i++){
                        filepath.add(mlistdata.get(i).Srcurl);
                    }
                    LookImageActivity.startactivity(mContext,filepath,position,-1);
                }
            });
        }

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }


    public void setListonLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
