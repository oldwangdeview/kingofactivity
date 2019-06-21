package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.Money_listdataBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class MoneyAdpater extends BaseRecycleAdapter<Money_listdataBean> {
    TextView money_title;
    TextView money;
    TextView money_number;
    TextView payfor_type;
    TextView money_message;
    TextView money_time;

    public MoneyAdpater(Context context, List<Money_listdataBean> datas) {
        super(context, datas, R.layout.item_money_list);
    }

    @Override
    protected void setData(RecycleViewHolder holder, Money_listdataBean s, int position) {
        money_title = holder.getItemView(R.id.money_title);
        money = holder.getItemView(R.id.money);
        money_number = holder.getItemView(R.id.money_number);
        payfor_type = holder.getItemView(R.id.payfor_type);
        money_message = holder.getItemView(R.id.money_message);
        money_time = holder.getItemView(R.id.money_time);


        if(!TextUtils.isEmpty(s.Title)){
            money_title.setText(s.Title);
        }

        if(!TextUtils.isEmpty(s.Amount)){
            money.setText(s.Amount);
        }

        if(!TextUtils.isEmpty(s.Number)){
            money_number.setText(s.Number);
        }
        if(!TextUtils.isEmpty(s.PaymentMethod)){
            payfor_type.setText(s.PaymentMethod);
        }
        if(!TextUtils.isEmpty(s.Remarks)){
            money_message.setText(s.Remarks);
        }
        if(!TextUtils.isEmpty(s.Datatime)){
            money_time.setText(UIUtils.gettime(s.Datatime));
        }
    }
}
