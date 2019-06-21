package com.scmsw.kingofactivity.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.GetPersons_ListBean;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class FZRAdpater extends BaseAdapter {

    List<GetPersons_ListBean> listdata = new ArrayList<>();
    Context mContext;
    ListOnclickLister mlister;
    public FZRAdpater(Context mContext, List<GetPersons_ListBean> listdata){
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
            convertView = UIUtils.inflate(mContext, R.layout.item_fenpei);
        }
        ImageView image_text = convertView.findViewById(R.id.image_text);
        image_text.setVisibility(View.GONE);
        TextView text_name = convertView.findViewById(R.id.text_name);
        if(!TextUtils.isEmpty(listdata.get(position).Personname)){
            text_name.setText(listdata.get(position).Personname);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        return convertView;
    }

    public void setListonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
