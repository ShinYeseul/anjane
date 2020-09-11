package com.test.anjane.rain;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.anjane.GpsTracker;
import com.test.anjane.R;
import com.test.anjane.wind.Wind;

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

public class Rain extends AppCompatActivity implements MapView.MapViewEventListener {
    private String formatDate;
    private String startDate;

    boolean resultMsg = false, //결과메시지
            areaName = false, //구역명
            command = false,
            tmFc = false,
            warnStress = false,
            startTime = false;

    private String resultmsg;
    private ArrayList<String> areanameitem = new ArrayList<>();
    private ArrayList<String> commanditem = new ArrayList<>();
    private ArrayList<String> tmfcitem = new ArrayList<>();
    private ArrayList<String> warnstressitem = new ArrayList<>();
    private ArrayList<String> starttimeitem = new ArrayList<>();

    private MapView rainmap;

    private RecyclerView rainRecyclerView;
    private RecyclerView.LayoutManager rainLayoutManager;
    private RecyclerView.Adapter rainViewAdapter;

    final Geocoder geocoder = new Geocoder( this );
    private MapPOIItem mDefaultMarker;
    FloatingActionButton btn_location;
    private TextView rainwarnStresstv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.rain );

        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.text.DateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
        formatDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        cal.add(cal.DATE, -6); // 7일(일주일)을 뺀다
        startDate = format.format(cal.getTime());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rainwarnStresstv = findViewById( R.id.rain_message );
        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                GpsTracker gpsTracker2 =new GpsTracker(Rain.this);

                rainmap.removeAllCircles();
                rainmap.removeAllPOIItems();

                double latitude = gpsTracker2.getLatitude();
                double longitude = gpsTracker2.getLongitude();

                MapPoint mp1234 = null;
                mp1234=MapPoint.mapPointWithGeoCoord(latitude,longitude);
                rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                rainmap.setMapCenterPoint(mp1234,true);


            }
        });
        new Thread( new Runnable() {
            @Override
            public void run() {
                getData();

                Rain.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        if(resultMsgCheck() == false){

                        }
                        else {
                            if(commandCheck()==false){
                                getitem();
                                rainwarnStresstv.setText( "현재 해당 특보가 없습니다." );
                            }

                        }
                    }
                } );
            }
        } ).start();

        rainmap = findViewById(R.id.rain_map);
        rainmap.setZoomLevel( 8, true );
        rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        rainmap.setShowCurrentLocationMarker(false);
        rainmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        rainmap.setShowCurrentLocationMarker(true);
        rainmap.setMapViewEventListener(this);

        rainRecyclerView = findViewById(R.id.rain_Hlist);
        rainLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) rainLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        rainRecyclerView.setLayoutManager(rainLayoutManager);

    }

    public void getData(){

        try{
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getPwnCd?serviceKey=S8lyiY1crKS8rseJMPCvPwBEUEvsSGUD4jc6f9Fxeb3TSOC6NY4lrNv6qr%" +
                    "2BWSdyy00n6ANIa9tGp9x9Qbh%2FEiw%3D%3D&numOfRows=20&pageNo=1" +
                    "&dataType=XML&fromTmFc=" + startDate + "&toTmFc=" + formatDate + "&warningType=2"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), "UTF-8");

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("resultMsg")){ //title 만나면 내용을 받을수 있게 하자
                            resultMsg = true;
                        }
                        if(parser.getName().equals("areaName")){ //title 만나면 내용을 받을수 있게 하자
                            areaName = true;
                        }
                        if(parser.getName().equals("command")){ //mapx 만나면 내용을 받을수 있게 하자
                            command = true;
                        }
                        if(parser.getName().equals("tmFc")){ //mapy 만나면 내용을 받을수 있게 하자
                            tmFc = true;
                        }
                        if(parser.getName().equals("warnStress")){ //mapy 만나면 내용을 받을수 있게 하자
                            warnStress = true;
                        }
                        if(parser.getName().equals("startTime")){ //mapy 만나면 내용을 받을수 있게 하자
                            startTime = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if(resultMsg){ //isTitle이 true일 때 태그의 내용을 저장.
                            resultMsg = false;
                            resultmsg = parser.getText();
                        }
                        if(areaName){ //isTitle이 true일 때 태그의 내용을 저장.
                            areaName = false;
                            areanameitem.add(parser.getText());
                        }
                        if(command){ //isMapx이 true일 때 태그의 내용을 저장.
                            command = false;
                            commanditem.add(parser.getText());
                        }
                        if(tmFc){ //isMapy이 true일 때 태그의 내용을 저장.
                            tmFc = false;
                            tmfcitem.add(parser.getText());
                        }
                        if(warnStress){ //isMapy이 true일 때 태그의 내용을 저장.
                            warnStress = false;
                            warnstressitem.add(parser.getText());
                        }
                        if(startTime){ //isMapy이 true일 때 태그의 내용을 저장.
                            startTime = false;
                            starttimeitem.add(parser.getText());
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                parserEvent = parser.next();

            }
        } catch(Exception e){
            Log.d("파싱 오류", e.toString());
        }

    }

    public boolean resultMsgCheck() {

        if(resultmsg.equals("NORMAL_SERVICE")) {
            return true;
        }
        return false;
    }

    public boolean commandCheck() {

        if(commanditem.equals("2")) {
            return true;
        }
        return false;
    }

    public void getitem(){
        final List<RainData> rainData = new ArrayList<>();
        for (int i = 0; i < areanameitem.size(); i++) {
            if (commanditem.get(i).equals("1")) {

                final RainData RainData = new RainData();
                RainData.setRain_warnStress(warnstressitem.get(i));
                RainData.setRain_warnStress_info(warnstressitem.get(i));
                RainData.setRain_areaCode(areanameitem.get(i));

                String cutted_starttime;
                cutted_starttime=cutting(starttimeitem.get(i));
                RainData.setRain_starttime("발효시각: "+cutted_starttime);

                String tmFcDate;
                tmFcDate = cutting(tmfcitem.get(i));
                RainData.setRain_tmFc(tmFcDate);

                RainData.setImgrain(warnstressitem.get(i));

                rainData.add(RainData);
                rainViewAdapter = new RainAdapter( rainData, new RainAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //geocoder코딩
                        List<Address> list = null;

                        String str = ((RainAdapter) rainViewAdapter).getRain( position ).getRain_areaCode();
                        Log.d( "Re", "position" + str );
                        try {
                            list = geocoder.getFromLocationName( str, 5 );
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e( "test", "입출력 오류- 서버에서 주소변환시 에러" );
                        }

                        if (list != null) {
                            if (list.size() == 0) {
                                System.out.println( "해당되는 주소 정보가 없다." );
                            } else {
                                Address addr = list.get( 0 );
                                double lat = addr.getLatitude();
                                double lon = addr.getLongitude();

                                String sss = String.format( "geo:%f,%f", lat, lon );
                                System.out.println( sss );

                                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord( lat, lon );

                                rainmap.removeAllPOIItems();
                                mDefaultMarker = new MapPOIItem();
                                mDefaultMarker.setTag( 0 );
                                mDefaultMarker.setItemName(str );
                                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                                mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );


                                rainmap.addPOIItem( mDefaultMarker );
                                rainmap.selectPOIItem( mDefaultMarker, true );
                                rainmap.setMapCenterPoint( DEFAULT_MARKER_POINT, true );
                                rainmap.setZoomLevel(8,true);
                            }
                        }
                    }
                });
                rainRecyclerView.setAdapter(rainViewAdapter);
            }
        }
    }

    public String cutting(String tmFc){
        String formattingString=null;
        String A="-";
        String B=":";
        String C=" ";
        String year;
        String month;
        String day;
        String hour;
        String minute;
        year=tmFc.substring(0,4);
        month=tmFc.substring(4,6);
        day=tmFc.substring(6,8);
        hour=tmFc.substring(8,10);
        minute=tmFc.substring(10);

        formattingString = year+A+month+A+day+C+hour+B+minute;

        return formattingString;
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