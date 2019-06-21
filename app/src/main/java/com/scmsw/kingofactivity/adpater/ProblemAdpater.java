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

public class ProblemAdpater extends BaseRecycleAdapter<HomesBean> {

    TextView problem;
    private ListOnclickLister mlister;
    public ProblemAdpater(Context context, List<HomesBean> datas) {
        super(context, datas, R.layout.item_problem);
    }

    @Override
    protected void setData(RecycleViewHolder holder, HomesBean homesBean, final int position) {

        problem = holder.getItemView(R.id.title);
        if(!TextUtils.isEmpty(homesBean.Title)){
            problem.setText(homesBean.Title);
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

    public void setListonCLickLister(ListOnclickLister mlister){
        this.mlister = mlister;

    }
}
