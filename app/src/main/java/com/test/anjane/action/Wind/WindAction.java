package com.test.anjane.action.Wind;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.test.anjane.R;

public class WindAction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.wind_action );

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Tablayout
        TabLayout windtabs = (TabLayout) findViewById(R.id.windtabs);
        windtabs.addTab(windtabs.newTab().setText("강풍대비"));
        windtabs.addTab(windtabs.newTab().setText("강풍 발생 시"));
        windtabs.setTabGravity(windtabs.GRAVITY_FILL);

        //Adapter
        final ViewPager windviewPager = (ViewPager) findViewById(R.id.windviewpager);
        final WindFragmnetAdapter myPagerAdapter = new WindFragmnetAdapter(getSupportFragmentManager(), 2);
        windviewPager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        windtabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(windviewPager));
        windviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(windtabs));


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
