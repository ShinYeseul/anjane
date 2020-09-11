package com.test.anjane.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.anjane.R;

public class login extends AppCompatActivity  {
    private Button bt_start;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.post_main );

        bt_start = (Button) findViewById(R.id.bt_start);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.bt_start :
                        Intent intent = new Intent( getApplicationContext(), loginactivitiy.class );
                        startActivity( intent );
                        break;
                }
            }
        };
        bt_start.setOnClickListener(clickListener);
    }

    //닫기 버튼
    public  void  onXButtonClicked(View v){
        finish();
    }
}