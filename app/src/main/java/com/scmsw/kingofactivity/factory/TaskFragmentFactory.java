package com.scmsw.kingofactivity.factory;

import android.support.v4.app.Fragment;

import com.scmsw.kingofactivity.fragment.TaskItemFragment1;
import com.scmsw.kingofactivity.fragment.TaskItemFragment2;
import com.scmsw.kingofactivity.fragment.TaskItemFragment3;

import java.util.HashMap;

public class TaskFragmentFactory {
    public static String TAG="TaskFragmnetFactory";
    private static final int TRANSLATE_FRAGMENT = 0;
    private static final int INFOMATION_FRAGMENT = 1;
    private static final int USER_HOUSE = 2;


    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;

        switch (index) {
            case TRANSLATE_FRAGMENT:
                fragment = new TaskItemFragment1();
                break;
            case INFOMATION_FRAGMENT:
                fragment = new TaskItemFragment2();
                break;
            case USER_HOUSE:
                fragment = new TaskItemFragment3();
                break;

        }


        return fragment;

    }
}
