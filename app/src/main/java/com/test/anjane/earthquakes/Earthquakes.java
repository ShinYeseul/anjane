package com.test.anjane.earthquakes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.test.anjane.R;
import com.test.anjane.GpsTracker;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Earthquakes extends AppCompatActivity implements MapView.MapViewEventListener {

    private net.daum.mf.map.api.MapView mMapView;
    public Elements contents;
    public Document doc = null;
    public ArrayList<String> listTime = new ArrayList<>();
    public ArrayList<String> listsize = new ArrayList<>();
    public ArrayList<String> listlat = new ArrayList();
    public ArrayList<String> listlot = new ArrayList();
    public ArrayList<String> listgps = new ArrayList();
    public String day;
    private FloatingActionButton exr;
    //    public float lat;
    public double latitude;
    public double longitude;
    //    public float lot;
    public float size;

    private MapPOIItem marker;

    public ArrayList<String> aa= new ArrayList();
    Button chartbt;
    private DocumentReference db;
    private RecyclerView earRecyclerView;
    private RecyclerView.LayoutManager earLayoutManager;
    private RecyclerView.Adapter earViewAdapter;
    private TextView earthquakesmesage;
    LinearLayout earthquakeslistlayout, earthquakeslayout;

    private Object EarData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquakes);

        earthquakeslistlayout = findViewById(R.id.earthquakeslistlayout );
        earthquakesmesage = findViewById( R.id.earthquakes_message );
        earthquakeslayout = findViewById(R.id.earthquakeslayout);

        mMapView = findViewById(R.id.earthquakes_map);
        mMapView.setZoomLevel(3,true);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        GpsTracker gpsTracker2 =new GpsTracker(Earthquakes.this);

        mMapView.removeAllCircles();
        mMapView.removeAllPOIItems();

        double latitude = gpsTracker2.getLatitude();
        double longitude = gpsTracker2.getLongitude();

        MapPoint mp1234 = null;
        mp1234=MapPoint.mapPointWithGeoCoord(latitude,longitude);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mMapView.setMapCenterPoint(mp1234,true);

        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

        exr = findViewById(R.id.btn_location);
        exr.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                GpsTracker gpsTracker2 =new GpsTracker(Earthquakes.this);

                mMapView.removeAllCircles();
                mMapView.removeAllPOIItems();

                double latitude = gpsTracker2.getLatitude();
                double longitude = gpsTracker2.getLongitude();

                MapPoint mp1234 = null;
                mp1234=MapPoint.mapPointWithGeoCoord(latitude,longitude);
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                mMapView.setMapCenterPoint(mp1234,true);
                mMapView.setZoomLevel(3,true);
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }
        });
        earRecyclerView = findViewById(R.id.ear_Hlist);
        earLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) earLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        earRecyclerView.setLayoutManager(earLayoutManager);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        day = sdf.format(calendar.getTime());
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("https://www.weather.go.kr/weather/earthquake_volcano/domesticlist.jsp?startTm=20200704"+
                            "&endTm=20200704" +
                            "&startSize=999&endSize=999&startLat=&endLat=&startLon=&endLon=&lat=&lon=&dist=&keyword=&x=14&y=8&schOption=T").get();//naver페이지를 불러옴
                    contents = doc.select("table#excel_body").select("tbody").select("td");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴

                    for (int e = 7; e < contents.size(); e += 10) {
                        Element ee = doc.select("td").get(e);
                        listgps.add(ee.text());
                    }
                    for (int c = 1; c < contents.size(); c += 10) {
                        Element cc = doc.select("td").get(c);
                        listTime.add(cc.text().replaceAll("/", "-"));
                    }
                    for (int b = 5; b < contents.size(); b += 10) {
                        Element bb = doc.select("td").get(b);
                        listlat.add(bb.text().replaceAll("N", ""));
                    }

                    for (int d = 2; d < contents.size(); d += 10) {
                        Element dd = doc.select("td").get(d);
                        listsize.add(dd.text());
                    }
                    for (int a = 6; a < contents.size(); a += 10) {
                        Element aa = doc.select("td").get(a);
                        listlot.add(aa.text().replaceAll("E", ""));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listlat.size() == 0) {
                    earthquakeslistlayout.setVisibility(View.GONE);
                    earthquakeslayout.setVisibility(View.VISIBLE);
                    earthquakesmesage.setText("현재 지진 특보가 없습니다.");
                }

                getitem();
            }
        }.execute();
        //Set toolbar





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void getitem(){
        final List<com.test.anjane.earthquakes.EarData> eardata = new ArrayList<>();
        for (int i = 0; i < listgps.size(); i++) {
            //
            final EarData Eardata = new EarData();
            Eardata.setlistTime(listTime.get(i));
            Eardata.setlistgps(listgps.get(i));
            Eardata.setlistsize(listsize.get(i));
            Eardata.setlistlat(listlat.get(i));
            Eardata.setlistlot(listlot.get(i));

            eardata.add(Eardata);
            earViewAdapter = new EarthquakesAdapter( eardata, new EarthquakesAdapter.OnItemClickListener() {@Override
            public void onItemClick(View view, int position){
                //String str = ((Earthquakes_Adapter) earViewAdapter).getEar( position ).getListgps();

                double lat = Double.parseDouble(((EarthquakesAdapter) earViewAdapter).getEar( position ).getListlat());
                double lot = Double.parseDouble(((EarthquakesAdapter) earViewAdapter).getEar( position ).getListlot());
                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(lat, lot );

                mMapView.removeAllPOIItems();
                marker = new MapPOIItem();
                marker.setTag( 0 );
                marker.setMapPoint( DEFAULT_MARKER_POINT );
                marker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                marker.setSelectedMarkerType( MapPOIItem.MarkerType.RedPin );
                marker.setItemName("떠라@@");

                mMapView.selectPOIItem( marker, true );
                mMapView.setMapCenterPoint( DEFAULT_MARKER_POINT, false );
                mMapView.setZoomLevel(10, false);
                mMapView.addPOIItem( marker );
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

            }

            });
            earRecyclerView.setAdapter(earViewAdapter);
        }
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
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}