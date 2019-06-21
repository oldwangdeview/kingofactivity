package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.activity.LookImageActivity;
import com.scmsw.kingofactivity.bean.EnclosureMasterBean;
import com.scmsw.kingofactivity.bean.EnclosureMaster_benlist;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;
import com.scmsw.kingofactivity.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class FeedDetailAdpater extends BaseAdapter {
    Context mContext;
    List<EnclosureMasterBean> listdata;
    ListOnclickLister mlister;

    public FeedDetailAdpater(Context mContext,List<EnclosureMasterBean> listdata){
        this.mContext = mContext;
        this.listdata = listdata;
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
            convertView = UIUtils.inflate(mContext, R.layout.item_feenbackitem);
        }
        TextView feedback_time = convertView.findViewById(R.id.feedback_time);
        TextView feedback_type = convertView.findViewById(R.id.feedback_type);
        TextView feedback_comqty = convertView.findViewById(R.id.feedback_comqty);
        TextView feedback_message = convertView.findViewById(R.id.feedback_message);
        MyGridView mygridview = convertView.findViewById(R.id.mygridview);

        LinearLayout layout_update = convertView.findViewById(R.id.layout_update);
        Button btn_up = convertView.findViewById(R.id.btn_up);


        EnclosureMasterBean s = listdata.get(position);
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

        btn_up.setVisibility(View.GONE);
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
        return convertView;
    }
}
