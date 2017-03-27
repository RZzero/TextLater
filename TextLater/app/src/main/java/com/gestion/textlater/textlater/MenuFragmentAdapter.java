package com.gestion.textlater.textlater;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kyonru on 26/03/17.
 */

public class MenuFragmentAdapter extends FragmentPagerAdapter {
    Context mContext;
    public MenuFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){

        }else{

        }

        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
