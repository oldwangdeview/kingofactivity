package com.scmsw.kingofactivity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.interfice.InPutTaskSizeClickLister;
import com.scmsw.kingofactivity.interfice.UpdateMoney_DIalog_ClickLister;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Input_Money_Dialog   extends Dialog {

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
    private Input_Money_Dialog(@NonNull Context context) {
        super(context, R.style.tipsDialogStyle);
    }

    public Input_Money_Dialog(@NonNull Context context, int themeResId) {
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


        private Input_Money_Dialog dialog;
        private View layout;
        private Context mContext;


        @BindView(R.id.layout_1)
        LinearLayout layout1;
        @BindView(R.id.layout_2)
        LinearLayout layout2;
        @BindView(R.id.image1)
        ImageView image1;
        @BindView(R.id.image2)
        ImageView image2;
        @BindView(R.id.text1)
        TextView text1;
        @BindView(R.id.text2)
        TextView text2;
        @BindView(R.id.inoput_editext)
        EditText input_editext;
        @BindView(R.id.inoput_editext2)
        EditText input_deitext2;
        @BindView(R.id.ojbk_btn)
        TextView ok_btn;


        private String text1string = "";
        private String text2srting = "";

        private int choietype = 1;

        private UpdateMoney_DIalog_ClickLister mlister = null;
        private int mintext = 0;
        private int maxint  = 0;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            dialog = new Input_Money_Dialog(context);
            dialog.setTag(this);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_inputmoney, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ButterKnife.bind(this, layout);
        }


        public Builder setText1(String text1){
            this.text1string = text1;
            return this;
        }

        public Builder setText2(String text2){
            this.text2srting = text2;
            return this;
        }

        public Builder setMoneyclicklister(UpdateMoney_DIalog_ClickLister mlister){
            this.mlister = mlister;
            return this;
        }


        public Input_Money_Dialog Builder(){


            if(!TextUtils.isEmpty(text1string)){
                text1.setText(text1string);
            }
            if(!TextUtils.isEmpty(text2srting)){
                text2.setText(text2srting);
            }

            layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choietype = 1;
                    updatecliclview();
                }
            });
            layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choietype = 2;
                    updatecliclview();
                }
            });
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlister!=null){
                        if(choietype==2) {
                            String text = input_editext.getText().toString().trim();
                            if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(input_deitext2.getText().toString().trim())) {
                                mlister.click(v, text, choietype, input_deitext2.getText().toString().trim());
                            }
                        }else{
                            mlister.click(v, "", choietype, "");
                        }
                    }
                }
            });

            return dialog;
        }

        private void updatecliclview(){
            switch (choietype){
                case 1:
                    image1.setImageResource(R.mipmap.xuanzhong);
                    image2.setImageResource(R.mipmap.unselected);
                    input_editext.setVisibility(View.INVISIBLE);
                    input_deitext2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    image2.setImageResource(R.mipmap.xuanzhong);
                    image1.setImageResource(R.mipmap.unselected);
                    input_editext.setVisibility(View.VISIBLE);
                    input_deitext2.setVisibility(View.VISIBLE);
                    break;
            }

        }

    }
}
