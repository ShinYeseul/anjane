package com.test.anjane;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.test.anjane.news.NNFragment;
import com.test.anjane.news.SNFragmnet;

public class FragmnetNNAdapter extends FragmentPagerAdapter {

    int mNumOfTabs; //탭의 갯수

    public FragmnetNNAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumOfTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                NNFragment nntab = new NNFragment();
                return nntab;
            case 1:
                SNFragmnet sntab = new SNFragmnet();
                return sntab;
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