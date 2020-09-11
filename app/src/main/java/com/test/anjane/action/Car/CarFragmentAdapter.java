package com.test.anjane.action.Car;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CarFragmentAdapter extends FragmentPagerAdapter {

    int mNumOfTabs; //탭의 갯수

    public CarFragmentAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumOfTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                Car1Fragment tab1= new Car1Fragment();
                return tab1;
            case 1:

                CarFragment tab2 = new CarFragment();
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
