package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.UIUtils;
import com.umeng.socialize.utils.UmengText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FenPeiAdpater extends BaseAdapter {
    List<Level_ListdataBean> listdata = new ArrayList<>();
    Context mContext;
    String typeid;
    ListOnclickLister mlister;
    static HashMap<Integer,Boolean> clickdata = new HashMap<>();
    static HashMap<String ,String> inputdat = new HashMap<>();

    public FenPeiAdpater(Context mContext,String typeid){
        this.mContext = mContext;
        this.typeid = typeid;

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
        TextView text_name = convertView.findViewById(R.id.text_name);
        final   EditText input_editext = convertView.findViewById(R.id.input_editext);
        TextView text_user = convertView.findViewById(R.id.text_user);
        TextView type_image = convertView.findViewById(R.id.type_image);

        if(!TextUtils.isEmpty(inputdat.get(listdata.get(position).ID))){
            input_editext.setText(inputdat.get(listdata.get(position).ID));
        }

        if(clickdata.get(position)) {
            if (!TextUtils.isEmpty(listdata.get(position).inpout_text)) {
                type_image.setVisibility(View.GONE);
            } else {
                type_image.setVisibility(View.GONE);
            }
            image_text.setImageResource(R.mipmap.xuanz_taskuser);
            input_editext.setVisibility(View.VISIBLE);

            if(typeid.equals("5")){
                text_user.setVisibility(View.GONE);
            }else{
                text_user.setVisibility(View.VISIBLE);
            }
        }else{
            image_text.setImageResource(R.mipmap.unselected);
            type_image.setVisibility(View.GONE);
            input_editext.setVisibility(View.GONE);
            text_user.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(listdata.get(position).name)){
            text_name.setText(listdata.get(position).name);
        }

        if(!TextUtils.isEmpty(listdata.get(position).fz_user)){
            text_user.setText(listdata.get(position).fz_user);
        }

        input_editext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 String data = input_editext.getText().toString().trim();
                 if(!TextUtils.isEmpty(data)){
                     inputdat.put(listdata.get(position).ID,data);
                 }else{
                     inputdat.put(listdata.get(position).ID,"");
                 }
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickdata.put(position,!clickdata.get(position));
                notifyDataSetChanged();
            }
        });

        text_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });
        return convertView;
    }


    public void setData(List<Level_ListdataBean> mlistdata){
        listdata.clear();
        listdata.addAll(mlistdata);
        for(int i =0;i<listdata.size();i++){
            clickdata.put(i,false);
            inputdat.put(listdata.get(i).ID,"");
        }

        notifyDataSetChanged();
    }

    public void setLisclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }


    public void addfzUser(Level_ListdataBean choicedata){
        new LogUntil(mContext,"","ok11"+new Gson().toJson(choicedata));
        for(int i =0;i<listdata.size();i++){
            if(listdata.get(i).ID.equals(choicedata.choiceid)){
                Level_ListdataBean mthdata = listdata.get(i);
                mthdata.fz_user = choicedata.name;
                mthdata.fz_id = choicedata.ID;
                listdata.remove(i);
                listdata.add(i,mthdata);
                notifyDataSetChanged();
                return;
            }
        }

    }


    public  List<Level_ListdataBean> getchoicedata(){
        List<Level_ListdataBean> datalist = new ArrayList<>();
        for(int i = 0;i<listdata.size();i++){
            if(clickdata.get(i)&&!TextUtils.isEmpty(inputdat.get(listdata.get(i).ID))){
                Level_ListdataBean data = listdata.get(i);
                data.inpout_text = inputdat.get(data.ID);
                datalist.add(data);
            }
        }
        return datalist;
    }

    public List<Level_ListdataBean> getlisdata(){
        return  listdata;
    }


}
