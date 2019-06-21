package com.scmsw.kingofactivity.factory;

import android.support.v4.app.Fragment;

import com.scmsw.kingofactivity.fragment.TaskListFragemnt1;
import com.scmsw.kingofactivity.fragment.TaskListFragment;
import com.scmsw.kingofactivity.fragment.TaskListFragment2;
import com.scmsw.kingofactivity.fragment.TaskListFragment3;

import java.util.HashMap;

public class TaskListFragmentFactory {
    public static String TAG="TaskListFragmentFactory";
    private static final int TASKLIST_NOTOVER = 1;
    private static final int TASKLIST_OVER = 2;
    private static final int TASKLIST_WGS = 0;
    private static final int TASKLIST_YZUF = 3;


    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;

        switch (index) {
            case TASKLIST_NOTOVER:
                fragment = new TaskListFragemnt1();
                break;
            case TASKLIST_OVER:
                fragment = new TaskListFragment2();
                break;
            case TASKLIST_WGS:
                fragment = new TaskListFragment();
                break;
            case  TASKLIST_YZUF:
                fragment = new TaskListFragment3();
                break;

        }


        return fragment;

    }
}
