package com.test.anjane.post;

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
import com.test.anjane.post.fragment.MapFragment;
import com.test.anjane.post.fragment.MeFragment;
import com.test.anjane.post.fragment.RecentPostsFragment;

public class Post extends BaseActivity  {

    private static final String TAG = "Post";

    private BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction it;
    private MapFragment post_Map;
    private RecentPostsFragment post_List;
    private MeFragment post_My;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.post );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_chevron_left_black_24dp ); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled( false );


        bottomNavigationView = findViewById(R.id.postNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.postMap:
                        setFrag(0);
                        break;
                    case R.id.postList:
                        setFrag(1);
                        break;
                    case R.id.postMy:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });
        post_Map = new MapFragment();
        post_List = new RecentPostsFragment();
        post_My = new MeFragment();

        setFrag(0); //펏 프래그먼트 화면을 무엇으로 지정해 줄 것인지 선택
    }

    //프래그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        it = fm.beginTransaction();

        switch (n) {
            case 0:
                it.replace(R.id.post_fram, post_Map);
                it.commit();
                break;
            case 1:
                it.replace(R.id.post_fram, post_List);
                it.commit();
                break;
            case 2:
                it.replace(R.id.post_fram, post_My);
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
        startActivity(new Intent(Post.this, Edit.class));
    }

}