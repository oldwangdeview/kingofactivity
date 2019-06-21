package com.scmsw.kingofactivity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.TaskListData_ListBean;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuditTaskDetailDialog extends Dialog {

    /**
     * 绑定Dialog标签
     */
    private Object tag;

    public interface OnRightClickListener {
        void onRightClick(Object args);
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 设置弹框默认风格
     */
    private AuditTaskDetailDialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public AuditTaskDetailDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public TipsDialog setContent(String newContent) {
        TipsDialog.Builder builder = (TipsDialog.Builder) getTag();
        return builder.setContentView(newContent).build();
    }
    /**
     * 创建Dialog
     *
     * @author fengzhen
     * @version v1.0, 2018/3/29
     */
    public static class Builder {

        @BindView(R.id.task_name)
        TextView task_name;
        @BindView(R.id.gettask_user)
        TextView gettask_user;
        @BindView(R.id.task_money)
        TextView task_money;
        @BindView(R.id.baoz_money)
        TextView baoz_money;
        @BindView(R.id.gettask_size)
        TextView gettask_size;
        @BindView(R.id.task_time)
        TextView task_time;
        @BindView(R.id.task_message)
        TextView task_message;


        @BindView(R.id.button_1)
        TextView button_1;
        @BindView(R.id.button_2)
        TextView button_2;


        UserDontGetTaskListDataListBean data;
        TaskListData_ListBean choicedata;
        private AuditTaskDetailDialog dialog;
        private View layout;
        private Context mContext;

        private ListOnclickLister mlister;

        private View.OnClickListener okbtnclickListener;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new AuditTaskDetailDialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_taskdetil, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }

        public Builder setDetailData(UserDontGetTaskListDataListBean data, TaskListData_ListBean choicedata){
            this.choicedata = choicedata;
            this.data = data;
            return this;
        }

        public Builder setbuttonclicklister(ListOnclickLister mlister){
            this.mlister = mlister;
            return  this;
        }
        public AuditTaskDetailDialog Builder(){


            if(data!=null){
                if(!TextUtils.isEmpty(data.MissionName)){
                    task_name.setText(data.MissionName);
                }
                if(!TextUtils.isEmpty(choicedata.fbtopersonname)){
                    gettask_user.setText(choicedata.fbtopersonname);
                }

                if(!TextUtils.isEmpty(data.PkAmount)){
                    task_money.setText(data.PkAmount);
                }

                if(!TextUtils.isEmpty(data.GuaranteeMoney)){
                    baoz_money.setText(data.GuaranteeMoney);
                }
                if(!TextUtils.isEmpty(choicedata.MissionQty)){
                    gettask_size.setText(choicedata.MissionQty);
                }
                if(!TextUtils.isEmpty(data.MissionStartDatetime)&&!TextUtils.isEmpty(data.MissionEndDatetime)){
                    task_time.setText(UIUtils.gettime(data.MissionStartDatetime)+"-"+UIUtils.gettime(data.MissionEndDatetime));

                }

                if(!TextUtils.isEmpty(data.MissionDescribe)){
                    task_message.setText(data.MissionDescribe);
                }else if(!TextUtils.isEmpty(choicedata.MissionDescribe)){
                    task_message.setText(choicedata.MissionDescribe);
                }


                if(!TextUtils.isEmpty(choicedata.States)){
                    if(choicedata.States.equals("0")){
                        button_1.setVisibility(View.VISIBLE);
                        button_2.setVisibility(View.VISIBLE);
                        button_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mlister!=null){
                                    mlister.onclick(v,1);
                                }
                            }
                        });
                        button_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(mlister!=null){
                                    mlister.onclick(v,2);
                                }
                            }
                        });
                    }else{
                        button_1.setVisibility(View.GONE);
                        button_2.setVisibility(View.GONE);
                    }
                }else{
                    button_1.setVisibility(View.GONE);
                    button_2.setVisibility(View.GONE);
                }

            }
            return dialog;
        }

    }
}
