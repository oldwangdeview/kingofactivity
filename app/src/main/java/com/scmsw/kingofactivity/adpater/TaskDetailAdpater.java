package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class TaskDetailAdpater extends BaseAdapter {
    private  List<TaskListData_ListBean> listdata;
    private Context mContext;
    public TaskDetailAdpater(Context mContext, List<TaskListData_ListBean> listdata){
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
            convertView = UIUtils.inflate(mContext, R.layout.item_taskdetail_item);
        }
        TextView name = convertView.findViewById(R.id.name);
        ProgressBar progressbar = convertView.findViewById(R.id.progressbar);
        TextView wanchenglv = convertView.findViewById(R.id.wanchenglv);
        TextView payfor_type = convertView.findViewById(R.id.payfor_type);
        TextView title_text = convertView.findViewById(R.id.title_text);

        if(!TextUtils.isEmpty(listdata.get(position).fbtopersonname)){
            name.setText(listdata.get(position).fbtopersonname);

        }else
        if(!TextUtils.isEmpty(listdata.get(position).fbtoperspnid)){
            name.setText(listdata.get(position).fbtoperspnid);
        }
        if(!TextUtils.isEmpty(listdata.get(position).ComQty)&&!TextUtils.isEmpty(listdata.get(position).MissionQty)){

            try{
                Double data1 = Double.parseDouble(listdata.get(position).ComQty);
                Double data2 = Double.parseDouble(listdata.get(position).MissionQty);

                double bili = data1/data2*100;
                if(bili>100){
                    progressbar.setProgress(100);
                }else{
                    progressbar.setProgress((int)bili);
                }
                wanchenglv.setText(String.format("%.2f",bili)+"%");
                title_text.setText(listdata.get(position).ComQty+"/"+listdata.get(position).MissionQty);
            }catch (Exception e){

            }

        }
        if(!TextUtils.isEmpty(listdata.get(position).GuaranteeMoneyDetailState)){
            payfor_type.setText(listdata.get(position).GuaranteeMoneyDetailState.equals("0")?"不用交":
                    listdata.get(position).GuaranteeMoneyDetailState.equals("1")?"未支付":
                            listdata.get(position).GuaranteeMoneyDetailState.equals("2")?"已支付":"" );
        }


        return convertView;
    }
}
