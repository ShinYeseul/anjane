package com.test.anjane.wind;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.anjane.GpsTracker;
import com.test.anjane.R;

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

public class Wind extends AppCompatActivity  implements MapView.MapViewEventListener {
    private String formatDate;
    private String startDate;

    boolean resultMsg = false, //결과메시지
            areaName = false, //구역명
            command = false,
            tmFc = false,
            warnStress = false,
            startTime = false;

    final Geocoder geocoder = new Geocoder( this );
    private String resultmsg;
    private ArrayList<String> areanameitem = new ArrayList<>();
    private ArrayList<String> commanditem = new ArrayList<>();
    private ArrayList<String> tmfcitem = new ArrayList<>();
    private ArrayList<String> warnstressitem = new ArrayList<>();
    private ArrayList<String> starttimeitem = new ArrayList<>();
    FloatingActionButton btn_location;

    private MapView windmap;

    private RecyclerView windRecyclerView;
    private RecyclerView.LayoutManager windLayoutManager;
    private RecyclerView.Adapter windViewAdapter;

    private MapPOIItem mDefaultMarker;

    private TextView windwarnStresstv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.wind );

        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.text.DateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
        formatDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        cal.add(cal.DATE, -6);  // 7일(일주일)을 뺀다
        startDate = format.format(cal.getTime());

        windwarnStresstv = findViewById( R.id.wind_message );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        new Thread( new Runnable() {
            @Override
            public void run() {
                getData();

                Wind.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        if(itemCheck() == false){

                        }
                        else{
                            if(commandCheck()==false){
                                getitem();
                                windwarnStresstv.setText( "현재 해당 특보가 없습니다." );
                            }

                        }

                    }
                } );
            }
        } ).start();

        windmap = findViewById(R.id.wind_map);
        windmap.setZoomLevel( 8, true );
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
                GpsTracker gpsTracker2 =new GpsTracker(Wind.this);

                windmap.removeAllCircles();
                windmap.removeAllPOIItems();

                double latitude = gpsTracker2.getLatitude();
                double longitude = gpsTracker2.getLongitude();

                MapPoint mp1234 = null;
                mp1234=MapPoint.mapPointWithGeoCoord(latitude,longitude);
                windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                windmap.setMapCenterPoint(mp1234,true);

            }
        });

        windRecyclerView = findViewById(R.id.wind_Hlist);
        windLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) windLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        windRecyclerView.setLayoutManager(windLayoutManager);

    }

    public void getData(){

        try{
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getPwnCd?serviceKey=S8lyiY1crKS8rseJMPCvPwBEUEvsSGUD4jc6f9Fxeb3TSOC6NY4lrNv6qr%" +
                    "2BWSdyy00n6ANIa9tGp9x9Qbh%2FEiw%3D%3D&numOfRows=20&pageNo=1" +
                    "&dataType=XML&fromTmFc=" + startDate + "&toTmFc=" + formatDate + "&warningType=1"
            ); //검색 URL부분
            Log.d("url", String.valueOf(url));
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();


            parser.setInput(url.openStream(), "UTF-8");

            Log.d("parser", String.valueOf(parser));
            int parserEvent = parser.getEventType();
//areanameitem.size() < 21 ||
            int i = 0 ;
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
                            Log.d("aasd",parser.getText());
                        }
                        if(warnStress){ //isMapy이 true일 때 태그의 내용을 저장.
                            warnStress = false;
                            warnstressitem.add(parser.getText());
                        }
                        if(startTime){ //isMapy이 true일 때 태그의 내용을 저장.
                            startTime = false;
                            starttimeitem.add(parser.getText());
                        }
                        i++;
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                parserEvent = parser.next();
                Log.d("parser2", String.valueOf(parser.getText()));
            }
        } catch(Exception e){
            Log.d("파싱 오류", e.toString());
        }

    }

    public boolean itemCheck() {
        if(resultmsg.contains("NORMAL_SERVICE")) {
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
        final List<WindData> windData = new ArrayList<>();
        for (int i = 0; i < areanameitem.size(); i++) {
            //
            if (commanditem.get(i).equals("1")) {

                final WindData WindData = new WindData();
                WindData.setWind_warnStress(warnstressitem.get(i));
                WindData.setWind_warnStress_info(warnstressitem.get(i));
                WindData.setWind_areaCode(areanameitem.get(i));

                try {
                    String cutted_starttime;
                    cutted_starttime = cutting(starttimeitem.get(i));
                    WindData.setWind_starttime("발효시각 : " + cutted_starttime);
                }catch(StringIndexOutOfBoundsException e){
                    WindData.setWind_starttime("발효시각 : " + starttimeitem.get(i));
                }
                try {
                    String tmFcDate;
                    tmFcDate = cutting(tmfcitem.get(i));
                    WindData.setWind_tmFc(tmFcDate);
                }catch (StringIndexOutOfBoundsException e){
                    WindData.setWind_tmFc(tmfcitem.get(i));
                }

                WindData.setImgwind(warnstressitem.get(i));

                windData.add(WindData);
                windViewAdapter = new WindAdapter( windData, new WindAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //geocoder코딩
                        List<Address> list = null;

                        String str = ((WindAdapter) windViewAdapter).getWind( position ).getWind_areaCode();
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

                                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(lat, lon);

                                windmap.removeAllPOIItems();
                                mDefaultMarker = new MapPOIItem();
                                mDefaultMarker.setTag( 0 );
                                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );
                                mDefaultMarker.setMarkerType( MapPOIItem.MarkerType.BluePin );
                                mDefaultMarker.setItemName(str);


                                windmap.setMapCenterPoint( DEFAULT_MARKER_POINT, true );
                                windmap.setZoomLevel( 8, true );
                                windmap.addPOIItem( mDefaultMarker );
                                windmap.selectPOIItem( mDefaultMarker, true );                            }
                        }
                    }
                });
                windRecyclerView.setAdapter(windViewAdapter);
            }
        }
    }

    public String cutting(String tmFc){

        try {
            String formattingString = null;
            String A = "-";
            String B = ":";
            String C = " ";
            String year;
            String month;
            String day;
            String hour;
            String minute;
            year = tmFc.substring(0, 4);
            month = tmFc.substring(4, 6);
            day = tmFc.substring(6, 8);
            hour = tmFc.substring(8, 10);
            minute = tmFc.substring(10);

            formattingString = year + A + month + A + day + C + hour + B + minute;

            return formattingString;
        }catch (StringIndexOutOfBoundsException e){

            return tmFc;
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
        windmap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}