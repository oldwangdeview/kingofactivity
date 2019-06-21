package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.contans.Contans;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class AddImage_adpater extends BaseAdapter {

    List<String> filepath = new ArrayList<>();
    public static final String ADDIMAGE  = "Add_image";
    public static final int MAX_SIZE = 10;
    ListOnclickLister mlister;
    Context mContext;
    public  AddImage_adpater(Context mContext){
        this.mContext = mContext;
        filepath.add(ADDIMAGE);
    }

    @Override
    public int getCount() {
        return filepath.size();
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


        if(convertView == null){
            convertView = UIUtils.inflate(mContext, R.layout.item_addimage);
        }


        ImageView image1 = convertView.findViewById(R.id.image1);
        ImageView image2 = convertView.findViewById(R.id.image2);

        if(filepath.get(position).equals(ADDIMAGE)){

            image1.setVisibility(View.GONE);
            image2.setVisibility(View.VISIBLE);
        }else{
            image2.setVisibility(View.GONE);
            image1.setVisibility(View.VISIBLE);
            UIUtils.loadImageView(mContext,filepath.get(position),image1);
        }

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        return convertView;
    }


    public void setaddimagelister(ListOnclickLister mlister){
        this.mlister = mlister;
    }


    public void addimage(String mfilepath){
        new LogUntil(mContext,"imagepath",mfilepath);
        if(filepath.size()==MAX_SIZE){
            filepath.remove(ADDIMAGE);
            filepath.add(mfilepath);
            filepath.add(ADDIMAGE);
        }else if(filepath.size()>MAX_SIZE){
            filepath.remove(ADDIMAGE);
            filepath.remove(filepath.size()-1);
            filepath.add(mfilepath);
            filepath.add(ADDIMAGE);
        }else
            {
            filepath.remove(ADDIMAGE);
            filepath.add(mfilepath);
            filepath.add(ADDIMAGE);
        }
        notifyDataSetChanged();
    }


    public List<String> getfilepath(){
        List<String> mpath = filepath;
        mpath.remove(ADDIMAGE);
        return mpath;
    }



    public void setFilepath(List<String> listdata){
        this.filepath.clear();;
        this.filepath.addAll(listdata);
        if(filepath.size()<=MAX_SIZE){
            filepath.add(ADDIMAGE);
        }
        notifyDataSetChanged();
    }


}
