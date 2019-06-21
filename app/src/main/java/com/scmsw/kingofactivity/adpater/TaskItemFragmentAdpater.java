package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.GetMissionDetailsListDataBean;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class TaskItemFragmentAdpater extends BaseAdapter {
    Context mContext;
    List<GetMissionDetailsListDataBean> listdata;

    public TaskItemFragmentAdpater(Context mContext,List<GetMissionDetailsListDataBean> listdat){
        this.mContext = mContext;
        this.listdata = listdat;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.item_taskfragmentitem);
        }
        TextView index_text = convertView.findViewById(R.id.index_text);
        index_text.setText(position+1+"");
        ImageView heandimage = convertView.findViewById(R.id.head_image);
        if(!TextUtils.isEmpty(listdata.get(position).UserHeadIcon)){
            UIUtils.loadImageViewRoud(mContext,listdata.get(position).UserHeadIcon,heandimage,UIUtils.dip2px(40),R.mipmap.mrtx);
        }
        TextView nickname = convertView.findViewById(R.id.nickname);
        if(!TextUtils.isEmpty(listdata.get(position).UserNickName)){
            nickname.setText(listdata.get(position).UserNickName);
        }else if(!TextUtils.isEmpty(listdata.get(position).UserAccount)){
            nickname.setText(listdata.get(position).UserAccount);
        }
        TextView userid = convertView.findViewById(R.id.userid);
        if(!TextUtils.isEmpty(listdata.get(position).UserAccount)){
            userid.setText("部门:"+listdata.get(position).UserAccount);
        }else{
            userid.setText("");
        }
        TextView size = convertView.findViewById(R.id.size);
        if(!TextUtils.isEmpty(listdata.get(position).Number)){
            size.setText(listdata.get(position).Number);
        }else{
            size.setText("");
        }
        TextView type_index = convertView.findViewById(R.id.type_index);
        if(!TextUtils.isEmpty(indextype)){
            type_index.setText(indextype);
        }

        return convertView;
    }

    private String indextype;
    public void setdattype(String indextype){
        this.indextype = indextype;
    }
}
