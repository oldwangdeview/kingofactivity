package com.scmsw.kingofactivity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.interfice.UpdateMoney_DIalog_ClickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 任务详情修改PK金
 */
public class UpdateMoneyDialog extends Dialog {

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
    private UpdateMoneyDialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public UpdateMoneyDialog(@NonNull Context context, int themeResId) {
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

        @BindView(R.id.btn_right_dialog_ok)
        Button right_btn;
        @BindView(R.id.input_editext)
        EditText input_text;

        private UpdateMoneyDialog dialog;
        private View layout;
        UpdateMoney_DIalog_ClickLister mlister;

        private Context mContext;


        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new UpdateMoneyDialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_updatemoney, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }



        public UpdateMoneyDialog build() {
            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlister!=null){
                        String data = input_text.getText().toString().trim();
                        mlister.click(v,data,0,"");
                    }
                }
            });
            dialog.setContentView(layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }

        public Builder setcliklistrer(UpdateMoney_DIalog_ClickLister mlister){
            this.mlister = mlister;
            return this;
        }

    }
}
