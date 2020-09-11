package com.test.anjane.post;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.test.anjane.R;
import com.test.anjane.post.fragment.MyPostsFragment;


public class MyPostList extends AppCompatActivity  {

    private FragmentManager fragmentManager;
    private MyPostsFragment fragmentA;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.mypost_list );

        fragmentManager = getSupportFragmentManager();
        fragmentA = new MyPostsFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl, fragmentA).commitAllowingStateLoss();
    }
}