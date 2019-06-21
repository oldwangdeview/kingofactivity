package com.scmsw.kingofactivity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoiceChallengTaskDialog extends Dialog {

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
    private ChoiceChallengTaskDialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public ChoiceChallengTaskDialog(@NonNull Context context, int themeResId) {
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


        private ChoiceChallengTaskDialog dialog;
        private View layout;
        private Context mContext;

        @BindView(R.id.listview)
        ListView listview;
        @BindView(R.id.ok_btuuton)
         Button okbtn;
        private BaseAdapter madpater;
        private View.OnClickListener okbtnclickListener;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new ChoiceChallengTaskDialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_choicechallengtask, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }

        public Builder setAdpater(BaseAdapter adpater){
            this.madpater = adpater;
             return this;
        }

        public Builder setOjBkButtonClickLister(View.OnClickListener lister){
            this.okbtnclickListener = lister;
            return this;
        }
        public ChoiceChallengTaskDialog Builder(){
            if(madpater!=null){
                listview.setAdapter(madpater);
            }
            if(okbtnclickListener!=null){
                okbtn.setOnClickListener(okbtnclickListener);
            }
            return dialog;
        }

    }
}
