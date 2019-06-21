package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.ChoiceTask_dataBean;
import com.scmsw.kingofactivity.bean.GrideClickBean;
import com.scmsw.kingofactivity.bean.Level_ListdataBean;
import com.scmsw.kingofactivity.bean.RelessTaskOneBody;
import com.scmsw.kingofactivity.util.LogUntil;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrideAdpater extends BaseAdapter {
    private Context mContext;
    private List<Level_ListdataBean> listdat;
    private int index = 1;

    private RelessTaskOneBody task_type = null;
    private static HashMap<Integer,GrideClickBean> clickdata = new HashMap<>();
    private String id;

    public GrideAdpater(Context mContext, List<Level_ListdataBean> listdat, RelessTaskOneBody task_type , String id){
        this.mContext = mContext;
        this.listdat = listdat;
        this.id = id;

        this.task_type = task_type;
        for(int i =0;i<listdat.size();i++){
            clickdata.put(i,new GrideClickBean());
        }

    }
    @Override
    public int getCount() {
        return listdat.size();
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
            convertView = UIUtils.inflate(mContext, R.layout.item_grideview);
        }

        ImageView selete_image = convertView.findViewById(R.id.selete_image);
        ImageView heand_image = convertView.findViewById(R.id.heand_image);
        TextView title_name = convertView.findViewById(R.id.titlename);
        LinearLayout shouwbottom_layout = convertView.findViewById(R.id.shouwbottom_layout);
         ImageView mimageview = convertView.findViewById(R.id.image_jiantou);
        TextView mtextview = convertView.findViewById(R.id.image_type);
         LinearLayout bottom_layout = convertView.findViewById(R.id.bottom_layout);
        final EditText edittext1 = convertView.findViewById(R.id.editext_1);
        final EditText edittext2 = convertView.findViewById(R.id.editext_2);
        final EditText edittext3 = convertView.findViewById(R.id.editext_3);
        final EditText edittext4 = convertView.findViewById(R.id.editext_4);
        final EditText edittext5 = convertView.findViewById(R.id.editext_5);
        if(clickdover(task_type.tasktype,clickdata.get(position))&&clickdata.get(position).clickd){
            mtextview.setVisibility(View.VISIBLE);
        }else {
            mtextview.setVisibility(View.GONE);
        }
        if(task_type.tasktype==2){
            edittext1.setVisibility(View.VISIBLE);
            edittext3.setVisibility(View.GONE);
            edittext4.setVisibility(View.GONE);
            edittext5.setVisibility(View.GONE);
            edittext1.setHint("请输入单位完成量");
            if(task_type.inputmoney_type==2){
                edittext2.setHint("请输入单位完成奖金比例");
            }else {
                edittext2.setHint("请输入单位完成固定奖金");
            }

        }else{
            edittext1.setVisibility(View.GONE);
            edittext3.setVisibility(View.VISIBLE);
            edittext4.setVisibility(View.VISIBLE);
            edittext5.setVisibility(View.VISIBLE);
            edittext1.setHint("请输入目标量");
            edittext2.setHint("请输入达标量");
            edittext3.setHint("请输入单位超额量");
            if(task_type.choiceintmoey_type1==1){
                edittext4.setHint("请输入达标固定金额");
            }else {
                edittext4.setHint("请输入达标奖金比例");
            }
            if(task_type.choiceintmonet_type2==1){
                edittext5.setHint("请输入单位超额固定金额");
            }else {
                edittext5.setHint("请输入单位超额奖金比例");
            }
        }




         if(!TextUtils.isEmpty(listdat.get(position).name)){
             title_name.setText(listdat.get(position).name);
         }

         GrideClickBean mclick = clickdata.get(position);
        if(mclick.clickd){
            selete_image.setImageResource(R.mipmap.xuanz_taskuser);
        }else{
            selete_image.setImageResource(R.mipmap.unselected);
        }

        if(mclick.type){
            bottom_layout.setVisibility(View.VISIBLE);
            mimageview.setImageResource(R.mipmap.jiant_3);
        }else{
            bottom_layout.setVisibility(View.GONE);
            mimageview.setImageResource(R.mipmap.jiantou2);
        }


        selete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.clickd = !mclick.clickd;
                if(mclick.clickd){
                    mclick.type = true;

                }else{
                    mclick.type = false;
                }
                clickdata.put(position,mclick);
                notifyDataSetChanged();

            }
        });
        heand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.clickd = !mclick.clickd;
                if(mclick.clickd){
                    mclick.type = true;
                }else{
                    mclick.type = false;
                }
                clickdata.put(position,mclick);
                notifyDataSetChanged();

            }
        });
        title_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.clickd = !mclick.clickd;
                if(mclick.clickd){
                    mclick.type = true;
                }else{
                    mclick.type = false;
                }
                clickdata.put(position,mclick);
                notifyDataSetChanged();
            }
        });

        shouwbottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GrideClickBean mclick = clickdata.get(position);
                if(mclick.clickd) {
                mclick.type = !mclick.type;
                clickdata.put(position,mclick);
                notifyDataSetChanged();
                }
            }
        });



        edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.text1 = edittext1.getText().toString().trim();
                clickdata.put(position,mclick);
                notifyDataSetChanged();
            }
        });

        edittext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.text2 = edittext2.getText().toString().trim();
                clickdata.put(position,mclick);
                notifyDataSetChanged();
            }
        });


        edittext3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.text3 = edittext3.getText().toString().trim();
                clickdata.put(position,mclick);
                notifyDataSetChanged();
            }
        });



        edittext4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.text4 = edittext4.getText().toString().trim();
                clickdata.put(position,mclick);
                notifyDataSetChanged();
            }
        });


        edittext5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GrideClickBean mclick = clickdata.get(position);
                mclick.text5 = edittext5.getText().toString().trim();
                clickdata.put(position,mclick);
                notifyDataSetChanged();
            }
        });



        return convertView;
    }

    private boolean clickdover(int type,GrideClickBean data){
        new LogUntil(mContext,"clickmessage",new Gson().toJson(data));
        if(type==2){
            if(!TextUtils.isEmpty(data.text1)&&!TextUtils.isEmpty(data.text2)){
                return  false;
            }else{
                return true;
            }
        }else{
            if(!TextUtils.isEmpty(data.text2)&&!TextUtils.isEmpty(data.text3)&&!TextUtils.isEmpty(data.text4)&&!TextUtils.isEmpty(data.text5)){
                return  false;
            }else{
                return true;
            }
        }
    }


    public  List<ChoiceTask_dataBean> getchoicedata(){

        List<ChoiceTask_dataBean> choicedata = new ArrayList<>();
        for(int i = 0;i<listdat.size();i++){

            if(!clickdover(task_type.tasktype,clickdata.get(i))&&clickdata.get(i).clickd){
                ChoiceTask_dataBean choice = new ChoiceTask_dataBean();
                choice.clickdata = clickdata.get(i);
                choice.levellistdat = listdat.get(i);
                choice.ID = id;
                choicedata.add(choice);
            }


        }
        return choicedata;
    };
}
