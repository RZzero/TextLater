package com.gestion.textlater.textlater;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MenuFragmentAdapter extends FragmentPagerAdapter {
    Context mContext;
    public MenuFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 1){
            return new HistorialFragment();
        }else{
            return new LeerMensajesFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.Leer);

            default:
                return mContext.getString(R.string.Historial);

        }

    }
}
