package com.test.anjane.action;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.test.anjane.R;

import com.test.anjane.action.Car.CarAction;
import com.test.anjane.action.Rain.RainAction;
import com.test.anjane.action.Snow.SnowAction;
import com.test.anjane.action.earthquakes.EarthFragmentAdapter;
import com.test.anjane.action.Wind.WindAction;

public class Action extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.action );

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CardView wind = (CardView) findViewById(R.id.Cwind);
        CardView rain = (CardView) findViewById(R.id.Crain);
        CardView snow = (CardView) findViewById(R.id.Csnow);
        CardView dust = (CardView) findViewById(R.id.Cdust);
        CardView earthquake = (CardView) findViewById(R.id.Cearthquake);
        CardView car = (CardView) findViewById(R.id.Ccar);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Cwind :
                        Intent intent = new Intent( getApplicationContext(), WindAction.class );
                        startActivity( intent );
                        break;
                    case R.id.Crain:
                        intent = new Intent( getApplicationContext(), RainAction.class );
                        startActivity( intent );
                        break;
                    case R.id.Csnow :
                        intent = new Intent( getApplicationContext(), SnowAction.class );
                        startActivity( intent );
                        break;
                    case R.id.Cdust :
                        intent = new Intent( getApplicationContext(), DustAction.class );
                        startActivity( intent );
                        break;
                    case R.id.Cearthquake :
                        intent = new Intent( getApplicationContext(), EarthFragmentAdapter.class );
                        startActivity( intent );
                        break;
                    case R.id.Ccar :
                        intent = new Intent( getApplicationContext(), CarAction.class );
                        startActivity( intent );
                        break;

                }
            }
        };
        wind.setOnClickListener(clickListener);
        rain.setOnClickListener(clickListener);
        snow.setOnClickListener(clickListener);
        dust.setOnClickListener(clickListener);
        earthquake.setOnClickListener(clickListener);
        car.setOnClickListener(clickListener);
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