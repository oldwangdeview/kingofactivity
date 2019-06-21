package com.scmsw.kingofactivity.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.scmsw.kingofactivity.factory.AduitFragmnetFactory;
import com.scmsw.kingofactivity.factory.MainFragmentFactory;

public class AduitPagerAdpater extends FragmentPagerAdapter {
    public AduitPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return AduitFragmnetFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
