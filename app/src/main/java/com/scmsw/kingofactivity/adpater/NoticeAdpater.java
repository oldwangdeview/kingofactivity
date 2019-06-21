package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.base.BaseRecycleAdapter;
import com.scmsw.kingofactivity.bean.HomesBean;
import com.scmsw.kingofactivity.help.RecycleViewHolder;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;

import java.util.List;

public class NoticeAdpater extends BaseRecycleAdapter<HomesBean> {
    TextView title_name ;
    TextView time;
    TextView content;
    private ListOnclickLister mlister;
    public NoticeAdpater(Context context, List<HomesBean> datas) {
        super(context, datas, R.layout.item_notice);
    }

    @Override
    protected void setData(RecycleViewHolder holder, HomesBean homesBean, final int position) {

        title_name = holder.getItemView(R.id.title_name);
        time = holder.getItemView(R.id.time);
        content = holder.getItemView(R.id.content);

        if(!TextUtils.isEmpty(homesBean.Title)){
            title_name.setText(homesBean.Title);
        }

        if(!TextUtils.isEmpty(homesBean.Time)){
            time.setText(homesBean.Time);
        }
        if(!TextUtils.isEmpty(homesBean.strContent)){
            content.setText(homesBean.strContent);
        }

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }

    public void setonlistclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
