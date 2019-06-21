package com.scmsw.kingofactivity.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.scmsw.kingofactivity.factory.TaskFragmentFactory;

public class TaskPagerAdpater extends FragmentPagerAdapter {
    public TaskPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TaskFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
