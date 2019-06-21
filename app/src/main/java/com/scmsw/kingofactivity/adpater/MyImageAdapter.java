package com.scmsw.kingofactivity.adpater;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scmsw.kingofactivity.R;
import com.scmsw.kingofactivity.interfice.ListOnclickLister;
import com.scmsw.kingofactivity.util.UIUtils;

import java.util.List;

public class MyImageAdapter extends PagerAdapter {
    public static final String TAG = MyImageAdapter.class.getSimpleName();
    private List<String> imageUrls;
    private Context mContext;
    ListOnclickLister mlister;

    public MyImageAdapter(List<String> imageUrls,  Context mContext) {
        this.imageUrls = imageUrls;
        this.mContext = mContext;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String url = imageUrls.get(position);
        View imageview = UIUtils.inflate(mContext, R.layout.view_image);


        ImageView mimageview = imageview.findViewById(R.id.imageview);
        if(!TextUtils.isEmpty(url)){
            UIUtils.loadImageView(mContext,url,mimageview);
        }
        mimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,-1);
                }
            }
        });
        container.addView(imageview);
        return imageview;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public void clickimage(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}