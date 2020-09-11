package com.test.anjane;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        try {
            Thread.sleep( 5000 );
            Intent mainIntent = new Intent( this, MainActivity.class );
            startActivity( mainIntent );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}