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

public class WindTest extends AppCompatActivity  implements MapView.MapViewEventListener {


    FloatingActionButton btn_location;

    private MapView windmap;
    private LinearLayout wind_dropdown, wind_dropdown2,detaillayout_windtest,detaillayout_windtest2,
            recyclerlayout_windtest,recyclerlayout_windtest2;




    private MapPOIItem mDefaultMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.windtest);
        recyclerlayout_windtest = findViewById(R.id.recyclerlayout_windtest);
        recyclerlayout_windtest2 = findViewById(R.id.recyclerlayout_windtest2);
        detaillayout_windtest = findViewById(R.id.detaillayout_windtest);
        detaillayout_windtest2 = findViewById(R.id.detaillayout_windtest2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        wind_dropdown = findViewById(R.id.winddropdowntest);
        wind_dropdown2 = findViewById(R.id.winddropdowntest2);
        recyclerlayout_windtest.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.504122, 126.788261);

                windmap.removeAllCircles();
                windmap.removeAllPOIItems();
                mDefaultMarker = new MapPOIItem();
                mDefaultMarker.setTag( 0 );
                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                mDefaultMarker.setSelectedMarkerType( MapPOIItem.MarkerType.RedPin );
                mDefaultMarker.setItemName("떠라@@");

                windmap.selectPOIItem( mDefaultMarker, true );
                windmap.setMapCenterPoint( DEFAULT_MARKER_POINT, false );
                windmap.setZoomLevel(10, false);
                windmap.addPOIItem( mDefaultMarker );
                windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });
        recyclerlayout_windtest2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.550320, 126.981301);

                windmap.removeAllCircles();
                windmap.removeAllPOIItems();

                mDefaultMarker = new MapPOIItem();
                mDefaultMarker.setTag( 0 );
                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                mDefaultMarker.setSelectedMarkerType( MapPOIItem.MarkerType.RedPin );
                mDefaultMarker.setItemName("떠라@@");

                windmap.selectPOIItem( mDefaultMarker, true );
                windmap.setMapCenterPoint( DEFAULT_MARKER_POINT, false );
                windmap.setZoomLevel(10, false);
                windmap.addPOIItem( mDefaultMarker );
                windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });
        wind_dropdown.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                detaillayout_windtest.setVisibility(detaillayout_windtest.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });
        wind_dropdown2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                detaillayout_windtest2.setVisibility(detaillayout_windtest2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);


            }
        });


        windmap = findViewById(R.id.wind_map);
        windmap.setZoomLevel(8, true);
        windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        windmap.setShowCurrentLocationMarker(false);
        windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        windmap.setShowCurrentLocationMarker(true);
        windmap.setMapViewEventListener(this);

        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                GpsTracker gpsTracker2 = new GpsTracker(WindTest.this);

                windmap.removeAllCircles();
                windmap.removeAllPOIItems();

                double latitude = gpsTracker2.getLatitude();
                double longitude = gpsTracker2.getLongitude();

                MapPoint mp1234 = null;
                mp1234 = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                windmap.setMapCenterPoint(mp1234, true);
                windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

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
        windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}