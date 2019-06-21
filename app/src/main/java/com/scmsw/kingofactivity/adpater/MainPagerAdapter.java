package com.scmsw.kingofactivity.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.scmsw.kingofactivity.factory.MainFragmentFactory;


/**
 * Created by oldwang on 2018/12/30 0030.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MainFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
