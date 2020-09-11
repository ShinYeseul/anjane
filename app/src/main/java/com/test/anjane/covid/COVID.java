package com.test.anjane.covid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.test.anjane.MainActivity;
import com.test.anjane.R;
import com.test.anjane.post.BaseActivity;
import com.test.anjane.post.Edit;

public class COVID extends BaseActivity {

    private static final String TAG = "COVID";

    private BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction it;
    private COVIDFragmnet covid;
    private Mask mask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.covid_main );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_chevron_left_black_24dp ); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled( false );


        bottomNavigationView = findViewById(R.id.covidNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.situration:
                        setFrag(0);
                        break;
                    case R.id.mask:
                        setFrag(1);
                        break;
                }
                return true;
            }
        });
        covid = new COVIDFragmnet();
        mask = new Mask();

        setFrag(0); //펏 프래그먼트 화면을 무엇으로 지정해 줄 것인지 선택
    }

    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        it = fm.beginTransaction();

        switch (n) {
            case 0:
                it.replace(R.id.covid_fram, covid);
                it.commit();
                break;
            case 1:
                it.replace(R.id.covid_fram, mask);
                it.commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickButton(View v) {
        startActivity(new Intent( COVID.this, Edit.class));
    }

}