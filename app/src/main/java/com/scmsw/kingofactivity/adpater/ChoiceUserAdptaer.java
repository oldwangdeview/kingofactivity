package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.Choice_user_ListdataBean;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoiceUserAdptaer  extends BaseAdapter {
    Context mContext;
    private List<Choice_user_ListdataBean> listdat = new ArrayList<>();
    private static HashMap<Integer ,Boolean> clickhashmap = new HashMap<>();
    public ChoiceUserAdptaer(Context mContext,List<Choice_user_ListdataBean> listdat){

        this.mContext = mContext;
        this.listdat = listdat;
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
            convertView = UIUtils.inflate(mContext, R.layout.item_choiceuser);
        }
        ImageView Choiceimage = convertView.findViewById(R.id.choiceimage);
        ImageView heandimage = convertView.findViewById(R.id.heandimage);
        TextView nickname = convertView.findViewById(R.id.nickname);

        if(clickhashmap.get(position)!=null&&clickhashmap.get(position)){
            Choiceimage.setImageResource(R.mipmap.xuanzhong);
        }else{
            Choiceimage.setImageResource(R.mipmap.unselected);
        }

        if(!TextUtils.isEmpty(listdat.get(position).NickName)){
            nickname.setText(listdat.get(position).NickName);
        }else if(!TextUtils.isEmpty(listdat.get(position).Account)){
            nickname.setText(listdat.get(position).Account);
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickhashmap.put(position,!clickhashmap.get(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void setdata (List<Choice_user_ListdataBean> listdat){
        this.listdat = listdat;
        for(int i =0;i<listdat.size();i++){
            clickhashmap.put(i,false);
        }
        notifyDataSetChanged();

    }


    public List<Choice_user_ListdataBean> getChoiceUserdata(){
        List<Choice_user_ListdataBean> clickdata = new ArrayList<>();
        for(int i =0;i<listdat.size();i++){
            if(clickhashmap.get(i)){
                clickdata.add(listdat.get(i));
            }
        }
        return clickdata;
    }
}
