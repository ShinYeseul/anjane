package com.test.anjane;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.anjane.GpsTracker;
import com.test.anjane.R;
import com.test.anjane.rain.OnViewHolderItemClickListener;
import com.test.anjane.wind.WindAdapter;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RainTest extends AppCompatActivity  implements MapView.MapViewEventListener {


    FloatingActionButton btn_location;

    private MapView rainmap;
    private LinearLayout rain_dropdown, rain_dropdown2,detaillayout,detaillayout2,
            recyclerlayout_rain,recyclerlayout_rain2;




    private MapPOIItem mDefaultMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raintest);

        recyclerlayout_rain = findViewById(R.id.recyclerlayout_rain);
        recyclerlayout_rain2 = findViewById(R.id.recyclerlayout_rain2);
        detaillayout = findViewById(R.id.detaillayout);
        detaillayout2 = findViewById(R.id.detaillayout2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        rain_dropdown = findViewById(R.id.raindropdown);
        rain_dropdown2 = findViewById(R.id.raindropdown2);
        recyclerlayout_rain.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(35.162685, 129.048444);

                rainmap.removeAllCircles();
                rainmap.removeAllPOIItems();
                mDefaultMarker = new MapPOIItem();
                mDefaultMarker.setTag( 0 );
                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                mDefaultMarker.setSelectedMarkerType( MapPOIItem.MarkerType.RedPin );
                mDefaultMarker.setItemName("떠라@@");

                rainmap.selectPOIItem( mDefaultMarker, true );
                rainmap.setMapCenterPoint( DEFAULT_MARKER_POINT, false );
                rainmap.setZoomLevel(10, false);
                rainmap.addPOIItem( mDefaultMarker );
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });
        recyclerlayout_rain2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(36.804574, 127.204811);

                rainmap.removeAllCircles();
                rainmap.removeAllPOIItems();

                mDefaultMarker = new MapPOIItem();
                mDefaultMarker.setTag( 0 );
                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                mDefaultMarker.setSelectedMarkerType( MapPOIItem.MarkerType.RedPin );
                mDefaultMarker.setItemName("떠라@@");

                rainmap.selectPOIItem( mDefaultMarker, true );
                rainmap.setMapCenterPoint( DEFAULT_MARKER_POINT, false );
                rainmap.setZoomLevel(10, false);
                rainmap.addPOIItem( mDefaultMarker );
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });
        rain_dropdown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                detaillayout.setVisibility(detaillayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });
        rain_dropdown2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                detaillayout2.setVisibility(detaillayout2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);


            }
        });


        rainmap = findViewById(R.id.rain_map);
        rainmap.setZoomLevel(8, true);
        rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        rainmap.setShowCurrentLocationMarker(false);
        rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        rainmap.setShowCurrentLocationMarker(true);
        rainmap.setMapViewEventListener(this);

        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                GpsTracker gpsTracker2 = new GpsTracker(RainTest.this);

                rainmap.removeAllCircles();
                rainmap.removeAllPOIItems();

                double latitude = gpsTracker2.getLatitude();
                double longitude = gpsTracker2.getLongitude();

                MapPoint mp1234 = null;
                mp1234 = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                rainmap.setMapCenterPoint(mp1234, true);
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });

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

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}