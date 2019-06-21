package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DanWeiAdpater extends BaseAdapter {



    List<Level_ListdataBean> mlistdat = new ArrayList<>();
    Context mContext;

    ListOnclickLister mlister;
    public DanWeiAdpater(Context context, List<Level_ListdataBean> datas) {
//        super(context, datas, R.layout.item_daiwei);
        this.mlistdat = datas;
        this.mContext = context;
    }



    @Override
    public int getCount() {
        return mlistdat.size();
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
            convertView = UIUtils.inflate(mContext,R.layout.item_daiwei);

        }
        TextView mtextview = convertView.findViewById(R.id.text);
        View mview = convertView.findViewById(R.id.view);
        if(!TextUtils.isEmpty(mlistdat.get(position).name)){
            mtextview.setText(mlistdat.get(position).name);
        }
        if(position==mlistdat.size()-1){
            mview.setVisibility(View.GONE);
        }else{
            mview.setVisibility(View.VISIBLE);
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

    public void setlistonclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
