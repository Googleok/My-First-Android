package com.example.slideview.viewpager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    MainActivity mainActivity;

    public PagerAdapter(FragmentManager fm, MainActivity mainActivity) {
        super(fm);
        this.mainActivity=mainActivity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return MainFragment.newInstance();
            case 1:
                return ListFragment.newInstance();
            case 2:
                return TransFrgment.newInstance();
            case 3:
                return MemoFragment.newInstance();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Main";
            case 1:
                return "List";
            case 2:
                return "Trans";
            case 3:
                return "Memo";
        }
        return null;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
