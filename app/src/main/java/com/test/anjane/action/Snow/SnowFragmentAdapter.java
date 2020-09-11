package com.test.anjane.action.Snow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SnowFragmentAdapter extends FragmentPagerAdapter {

    int mNumOfTabs; //탭의 갯수

    public SnowFragmentAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumOfTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                SnowFragment tab1= new SnowFragment();
                return tab1;
            case 1:
                Snow1Fragment tab2 = new Snow1Fragment();
                return tab2;

            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
