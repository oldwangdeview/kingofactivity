package com.scmsw.kingofactivity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.bean.UserDontGetTaskListDataListBean;
import com.scmsw.kingofactivity.interfice.InPutTaskSizeClickLister;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetTask_InputTasksize_Dialog extends Dialog {

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
    private GetTask_InputTasksize_Dialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public GetTask_InputTasksize_Dialog(@NonNull Context context, int themeResId) {
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


        private GetTask_InputTasksize_Dialog dialog;
        private View layout;
        private Context mContext;


        @BindView(R.id.input_editext)
        EditText inoutetxt;
        @BindView(R.id.ojbk_btn)
        TextView okbtn;


        private InPutTaskSizeClickLister bendata;
        private int mintext = 0;
        private int maxint  = 0;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new GetTask_InputTasksize_Dialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_gettask, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }

        public GetTask_InputTasksize_Dialog.Builder setonclicklister(InPutTaskSizeClickLister bendata){
            this.bendata = bendata;
            return this;
        }

        public Builder setMinAndMax(String min,String max){
            try {
                this.maxint = Integer.parseInt(min);
                this.maxint = Integer.parseInt(max);
            }catch (Exception e){
                e.printStackTrace();
            }
            return this;
        }


        public GetTask_InputTasksize_Dialog Builder(){


            if(mintext>0&&maxint>0){
                inoutetxt.setHint(mintext+"-"+maxint);

            }

            if(bendata!=null){
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inouttextdata = inoutetxt.getText().toString();
                        try{
                            int size = Integer.parseInt(inouttextdata);
//                            if(size>=mintext&&size<=maxint){
                                bendata.click(v,size);
//                            }
                        }catch (Exception e){

                        }

                    }
                });
            }



            return dialog;
        }

    }
}
