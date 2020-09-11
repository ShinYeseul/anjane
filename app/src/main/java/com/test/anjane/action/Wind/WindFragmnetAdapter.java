package com.test.anjane.action.Wind;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WindFragmnetAdapter extends FragmentPagerAdapter {

    int mNumOfTabs1; //탭의 갯수

    public WindFragmnetAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumOfTabs1 = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                WindFragment windtab = new WindFragment();
                return windtab;
            case 1:
                Wind1Fragment wind1tab = new Wind1Fragment();
                return wind1tab;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs1;
    }
}