package com.test.anjane.action.earthquakes;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.tabs.TabLayout;
import com.test.anjane.R;
import com.test.anjane.earthquakes.Earth2Fragment;
import com.test.anjane.post.BaseActivity;

public class EarthFragmentAdapter extends BaseActivity {

    private static final String TAG = "earthquakes_action";

    private TabLayout earthquakes;
    private FragmentManager fm;
    private FragmentTransaction it;
    private EarthFragment earthquakes_before;
    private Earth1Fragment earthquakes_action;
    private Earth2Fragment earthquakes_after;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquakes_action);

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_chevron_left_black_24dp ); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled( false );

        earthquakes  = (TabLayout) findViewById(R.id.earthquakesTabs);
        earthquakes.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                setFrag(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }
        });
        earthquakes_before = new EarthFragment();
        earthquakes_action = new Earth1Fragment();
        earthquakes_after = new com.test.anjane.earthquakes.Earth2Fragment();

        setFrag(0); //펏 프래그먼트 화면을 무엇으로 지정해 줄 것인지 선택

    }
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        it = fm.beginTransaction();

        switch (n) {
            case 0:
                it.replace(R.id.earthquakes_fram, earthquakes_before);
                it.commit();
                break;
            case 1:
                it.replace(R.id.earthquakes_fram, earthquakes_action);
                it.commit();
                break;
            case 2:
                it.replace(R.id.earthquakes_fram, earthquakes_after);
                it.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
