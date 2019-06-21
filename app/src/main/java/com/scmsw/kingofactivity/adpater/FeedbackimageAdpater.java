package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.application.BaseApplication;
import com.scmsw.kingofactivity.bean.EnclosureMaster_benlist;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class FeedbackimageAdpater extends BaseAdapter {

    Context mContext;
    List<EnclosureMaster_benlist> listdata = new ArrayList<>();

    public FeedbackimageAdpater(Context mContext, List<EnclosureMaster_benlist> listdata){
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.item_addimage);
        }
        ImageView image1 = convertView.findViewById(R.id.image1);
        ImageView image2 = convertView.findViewById(R.id.image2);
        image2.setVisibility(View.GONE);
        image1.setVisibility(View.VISIBLE);

        UIUtils.loadImageView(mContext,listdata.get(position).Srcurl,image1);
        return convertView;
    }
}
