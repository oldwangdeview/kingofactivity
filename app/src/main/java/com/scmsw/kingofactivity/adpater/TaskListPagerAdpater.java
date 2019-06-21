package com.scmsw.kingofactivity.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.scmsw.kingofactivity.factory.TaskListFragmentFactory;

public class TaskListPagerAdpater extends FragmentPagerAdapter {
    public TaskListPagerAdpater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TaskListFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
