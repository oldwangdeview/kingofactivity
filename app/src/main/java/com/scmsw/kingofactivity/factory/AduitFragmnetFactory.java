package com.scmsw.kingofactivity.factory;

import android.support.v4.app.Fragment;

import com.scmsw.kingofactivity.fragment.AduitFragment;
import com.scmsw.kingofactivity.fragment.AduitFragment1;


import java.util.HashMap;

public class AduitFragmnetFactory {

    public static String TAG="AduitFragmnetFactory";
    private static final int TRANSLATE_FRAGMENT = 0;
    private static final int INFOMATION_FRAGMENT = 1;


    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;

        switch (index) {
            case TRANSLATE_FRAGMENT:
                fragment = new AduitFragment();
                break;
            case INFOMATION_FRAGMENT:
                fragment = new AduitFragment1();
                break;
        }


        return fragment;

    }
}
