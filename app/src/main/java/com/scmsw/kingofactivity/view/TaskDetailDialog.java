package com.scmsw.kingofactivity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailDialog extends Dialog {

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
    private TaskDetailDialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public TaskDetailDialog(@NonNull Context context, int themeResId) {
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


        private TaskDetailDialog dialog;
        private View layout;
        private Context mContext;

        @BindView(R.id.task_size)
        TextView task_siez;
        @BindView(R.id.starttime)
        TextView starttime;
        @BindView(R.id.end_time)
        TextView end_time;
        @BindView(R.id.pk_money)
        TextView pk_money;
        @BindView(R.id.bz_money)
        TextView bz_money;
        @BindView(R.id.add_user)
        TextView add_user;
        @BindView(R.id.task_message)
        TextView task_message;
        @BindView(R.id.task_taskdetail)
        TextView task_taskdetail;



        private UserDontGetTaskListDataListBean bendata;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new TaskDetailDialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_gettaskdetail, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }

        public TaskDetailDialog.Builder setAdpater(UserDontGetTaskListDataListBean bendata){
            this.bendata = bendata;
            return this;
        }


        public TaskDetailDialog Builder(){


            if(bendata!=null){
                if(!TextUtils.isEmpty(bendata.MissionName)){
                    task_taskdetail.setText(bendata.MissionName);
                }else{
                    task_taskdetail.setText("");
                }
                if(!TextUtils.isEmpty(bendata.MissionTargetQuantized)){
                    task_siez.setText(bendata.MissionTargetQuantized);
                }else{
                    task_siez.setText("");
                }

                if(!TextUtils.isEmpty(bendata.MissionStartDatetime)){
                    starttime.setText(bendata.MissionStartDatetime);
                }else{
                    starttime.setText("");
                }
                if(!TextUtils.isEmpty(bendata.MissionEndDatetime)){
                    end_time.setText(bendata.MissionEndDatetime);
                }else{
                    end_time.setText("");
                }

                if(!TextUtils.isEmpty(bendata.PkAmount)){
                    pk_money.setText(bendata.PkAmount);
                }else{
                    pk_money.setText("");
                }

                if(!TextUtils.isEmpty(bendata.GuaranteeMoney)){
                    bz_money.setText(bendata.GuaranteeMoney);
                }else{
                    bz_money.setText("");
                }

                if(!TextUtils.isEmpty(bendata.FBpersonid)){
                    add_user.setText(bendata.FBpersonid);
                }else{
                    add_user.setText("");
                }
                if(!TextUtils.isEmpty(bendata.MissionDescribe)){
                    task_message.setText(bendata.MissionDescribe);
                }else{
                    task_message.setText("");
                }

            }



            return dialog;
        }

    }
}
