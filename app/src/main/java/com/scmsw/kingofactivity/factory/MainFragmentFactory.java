package com.scmsw.kingofactivity.factory;

import android.support.v4.app.Fragment;

import com.scmsw.kingofactivity.fragment.HomeFragment;
import com.scmsw.kingofactivity.fragment.TaskKingFragment;
import com.scmsw.kingofactivity.fragment.UserFragmnet;

import java.util.HashMap;

/**
 * Created by oldwang on 2018/12/30 0030.
 */

public class MainFragmentFactory {

    public static String TAG="MainFragmentFactory";
    private static final int TRANSLATE_FRAGMENT = 0;
    private static final int INFOMATION_FRAGMENT = 1;
    private static final int USER_HOUSE = 2;



    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
//        if (index!=TRANSLATE_FRAGMENT&&mMap.containsKey(index)){
//            fragment=   mMap.get(index);
//        }else {
            switch (index) {
                case TRANSLATE_FRAGMENT:
                    fragment = new HomeFragment();
                    break;
                case INFOMATION_FRAGMENT:
                    fragment = new TaskKingFragment();
                    break;
                case USER_HOUSE:
                    fragment = new UserFragmnet();
                    break;

            }

//            mMap.put(index,fragment);
//
//        }
        return fragment;

    }



}
