package com.test.anjane.action.Rain;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.test.anjane.MainActivity;
import com.test.anjane.R;

public class RainAction extends AppCompatActivity {

    private TabLayout tab;
    private FragmentManager fm;
    private FragmentTransaction it;
    private RainFragment rain_past; //진행 시
    private Rain1Fragment rain_future; //진행 후

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.rain_action );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_chevron_left_black_24dp ); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled( false );

        tab = findViewById(R.id.rainTabs);

        rain_past = new RainFragment();
        rain_future = new Rain1Fragment();

        setFrag(0); //펏 프래그먼트 화면을 무엇으로 지정해 줄 것인지 선택

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        it = fm.beginTransaction();

        switch (n) {
            case 0:
                it.replace(R.id.rain_fram, rain_past);
                it.commit();
                break;
            case 1:
                it.replace(R.id.rain_fram, rain_future);
                it.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
