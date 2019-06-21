package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.MissionCompliance_ListdatBean;
import com.scmsw.kingofactivity.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GuizeAdpater extends BaseAdapter {


    Context mContext;
    List<MissionCompliance_ListdatBean> data = new ArrayList<>();

    static HashMap<Integer,GuizeHashData> clickdata = new HashMap<>();
    public GuizeAdpater(Context mContext,List<MissionCompliance_ListdatBean> data ){
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
            convertView = UIUtils.inflate(mContext, R.layout.item_gz);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView text_btn1 = convertView.findViewById(R.id.text_btn1);
        TextView text_btn2 = convertView.findViewById(R.id.text_btn2);
        TextView db_text = convertView.findViewById(R.id.db_text);
        TextView chaoe_text = convertView.findViewById(R.id.chaoe_text);

      final   EditText editext1 = convertView.findViewById(R.id.editext1);
        TextView text1 = convertView.findViewById(R.id.text1);
      final   EditText editext2 = convertView.findViewById(R.id.editext2);
        TextView text2 = convertView.findViewById(R.id.text2);
      final   EditText editext3 = convertView.findViewById(R.id.editext3);
        TextView text3 = convertView.findViewById(R.id.text3);
      final   EditText editext4 = convertView.findViewById(R.id.editext4);
        TextView text4 = convertView.findViewById(R.id.text4);


        if(!TextUtils.isEmpty(data.get(position).RoleTargetType)){
            title.setText("下级类型"+data.get(position).RoleTargetType);
        }

        if(clickdata.get(position)!=null){

            if(clickdata.get(position).btn1type){
                text1.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                text3.setVisibility(View.GONE);
                text4.setVisibility(View.GONE);
                editext1.setVisibility(View.VISIBLE);
                editext2.setVisibility(View.VISIBLE);
                editext3.setVisibility(View.VISIBLE);
                editext4.setVisibility(View.VISIBLE);
                text_btn1.setText("保存");
                text_btn2.setText("取消");
            }else{
                editext1.setVisibility(View.GONE);
                editext2.setVisibility(View.GONE);
                editext3.setVisibility(View.GONE);
                editext4.setVisibility(View.GONE);

                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                text3.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);

                text_btn1.setText("修改");
                text_btn2.setText("删除");
            }

        }

        if(!TextUtils.isEmpty(data.get(position).StandardQty)){
            text1.setText(data.get(position).StandardQty);
            editext1.setText(data.get(position).StandardQty);
        }


        if(!TextUtils.isEmpty(data.get(position).StandardType)){
            if(data.get(position).StandardType.equals("1")){
                db_text.setText("达标奖金");
                text2.setText(data.get(position).StandardBonusAmount);
                editext2.setText(data.get(position).StandardBonusAmount);

            }else if(data.get(position).StandardType.equals("2")){

                db_text.setText("达标比例");
                text2.setText(data.get(position).StandardRatio);
                editext2.setText(data.get(position).StandardRatio);
            }

        }

        if(!TextUtils.isEmpty(data.get(position).OverfulfilQty)){
            text3.setText(data.get(position).OverfulfilQty);
            editext3.setText(data.get(position).OverfulfilQty);
        }


        if(!TextUtils.isEmpty(data.get(position).OverfulfilType)){
            if(data.get(position).OverfulfilType.equals("1")){
                chaoe_text.setText("超额奖金");
                text4.setText(data.get(position).OverfulfilBonusAmount);
                editext4.setText(data.get(position).OverfulfilBonusAmount);

            }else if(data.get(position).OverfulfilType.equals("2")){

                chaoe_text.setText("超额比例");
                text4.setText(data.get(position).OverfulfilRatio);
                editext4.setText(data.get(position).OverfulfilRatio);
            }
        }





        text_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GuizeHashData mdata = clickdata.get(position);
                if(mdata.btn1type){
                    String text1 = editext1.getText().toString().trim();
                    String text2 = editext2.getText().toString().trim();
                    String text3 = editext3.getText().toString().trim();
                    String text4 = editext4.getText().toString().trim();

                    MissionCompliance_ListdatBean savedata = data.get(position);
                    savedata.StandardQty = text1;
                    if(!TextUtils.isEmpty(savedata.StandardType)){
                        if(savedata.StandardType.equals("1")){
                          savedata.StandardBonusAmount = text2;

                        }else if(savedata.StandardType.equals("2")){

                         savedata.StandardRatio = text2;
                        }
                    }
                    savedata.OverfulfilQty = text3;
                    if(!TextUtils.isEmpty(savedata.OverfulfilType)){
                        if(savedata.OverfulfilType.equals("1")){
                            savedata.OverfulfilBonusAmount = text4;

                        }else if(savedata.OverfulfilType.equals("2")){

                            savedata.OverfulfilRatio = text4;
                        }
                    }




                }
                mdata.btn1type = !mdata.btn1type;
                clickdata.put(position,mdata);
                notifyDataSetChanged();
            }
        });

        text_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuizeHashData cdata = clickdata.get(position);
                if(cdata.btn1type){
                    cdata.btn1type = !cdata.btn1type;
                    clickdata.put(position,cdata);
                    notifyDataSetChanged();
                }else {
                    data.remove(position);
                    notifyDataSetChanged();
                }

            }
        });



        return convertView;
    }


    public List<MissionCompliance_ListdatBean> getcilckdata(){

        return data;

    }

    public void setdat(List<MissionCompliance_ListdatBean> mdata){
        this.data.clear();
        this.data.addAll(mdata);
        for(int i =0;i<data.size();i++){

            clickdata.put(i,new GuizeHashData());
        }
        notifyDataSetChanged();
    }

    public void Adddata(MissionCompliance_ListdatBean mdata){
        this.data.add(0,mdata);
        for(int i =0;i<data.size();i++){

            clickdata.put(i,new GuizeHashData());
        }
        notifyDataSetChanged();
    }


    public class  GuizeHashData implements Serializable{
        public boolean btn1type = false;
        public int position = -1;
        public String  text1;
        public String  text2;
        public String  text3;
        public String  text4;
    }
}
