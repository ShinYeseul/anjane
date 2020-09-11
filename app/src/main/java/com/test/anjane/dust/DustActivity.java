package com.test.anjane.dust;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.type.ColorOrBuilder;
import com.test.anjane.GpsTracker;
import com.test.anjane.R;
import com.test.anjane.snow.Snow;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DustActivity extends AppCompatActivity {
    private MapView dustMap;
    private MapPOIItem customMarker;
    private ArrayList<HashMap<String, String>> item = new ArrayList<>();
    double lat;
    double lon;

    static Context mContext;


    boolean dataTime = false,
            seoul = false, //서울
            busan = false, //부산
            daegu = false, //대구
            incheon = false, //인천
            gwangju = false, //광주
            daejeon = false, //대전
            ulsan=false, //울산
            gyeonggi = false, //경기
            gangwon = false, //강원
            chungbuk = false, //충북
            chungnam = false, //충남
            jeonbuk = false, //전북
            jeonnam = false, //전남
            gyeongbuk = false, //경북
            gyeongnam = false, //경남
            jeju = false, //제주
            sejong = false; //세종

    String name[] = {
            "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.dust );

        getItem();

        dustMap = findViewById(R.id.dust_map);
        dustMap.setZoomLevel(12, true);
        dustMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        dustMap.setShowCurrentLocationMarker(false);



        for(int i = 0; i < 18; i++) {
            Set set = item.get(i).keySet();
            Iterator iterator = set.iterator();
            String key = iterator.next().toString();
            switch (key){
                case "dataTime":
                    TextView time = findViewById(R.id.dust_Time);
                    time.setText(item.get(i).get(key));
                    break;
                case "seoul": //서울
                    lat = 37.557545;
                    lon = 126.991898;
                    createDefaultMarker(dustMap, lat, lon, i, key, "서울");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "busan": //부산
                    lat = 35.18;
                    lon = 129.07;
                    createDefaultMarker(dustMap, lat, lon, i, key, "부산");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "daegu": //대구
                    lat = 35.87;
                    lon = 128.60;
                    createDefaultMarker(dustMap, lat, lon, i, key, "대구");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "incheon": //인천
                    lat = 37.45;
                    lon = 126.70;
                    createDefaultMarker(dustMap, lat, lon, i, key, "인천");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "gwangju": //광주
                    lat = 35.16;
                    lon = 126.85;
                    createDefaultMarker(dustMap, lat, lon, i, key, "광주");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "daejeon": //대전
                    lat = 36.35;
                    lon = 127.38;
                    createDefaultMarker(dustMap, lat, lon, i, key, "대전");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "ulsan": //울산
                    lat = 35.53;
                    lon = 129.31;
                    createDefaultMarker(dustMap, lat, lon, i, key, "울산");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "gyeonggi"://경기
                    lat = 37.425349;
                    lon = 127.521029;
                    createDefaultMarker(dustMap, lat, lon, i, key, "경기");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "gangwon": //강원
                    lat = 37.846700;
                    lon = 128.123431;
                    createDefaultMarker(dustMap, lat, lon, i, key, "강원");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "chungbuk": //충북
                    lat = 37.014857;
                    lon = 127.698585;
                    createDefaultMarker(dustMap, lat, lon, i, key, "충북");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "chungnam": //충남
                    lat = 24.881824;
                    lon = 126.943181;
                    createDefaultMarker(dustMap, lat, lon, i, key, "충남");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "jeonbuk": //전북
                    lat = 35.749137;
                    lon = 127.129948;
                    createDefaultMarker(dustMap, lat, lon, i, key, "전북");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "jeonnam": //전남
                    lat = 34.875065;
                    lon = 126.954167;
                    createDefaultMarker(dustMap, lat, lon, i, key, "전남");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "gyeongbuk": //경북
                    lat = 36.293383;
                    lon = 128.832829;
                    createDefaultMarker(dustMap, lat, lon, i, key, "경북");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "gyeongnam": //경남
                    lat = 35.461067;
                    lon = 128.184636;
                    createDefaultMarker(dustMap, lat, lon, i, key, "경남");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "jeju":
                    lat = 33.385993;
                    lon = 126.558659;
                    createDefaultMarker(dustMap, lat, lon, i, key, "제주");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
                case "sejong":
                    lat = 36.592906;
                    lon = 127.292253;
                    createDefaultMarker(dustMap, lat, lon, i, key, "세종");
                    dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    break;
            }
        }

        /*chartbt = findViewById(R.id.chartbt);
        chartbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(DustActivity.this);
                dialog.setContentView(R.layout.customdialog_dust );

                ImageView ivgood = (ImageView) dialog.findViewById(R.id.dust_ivgood);
                ivgood.setImageResource(R.drawable.dust_blue);
                ImageView ivnormarl = (ImageView) dialog.findViewById(R.id.dust_ivnormal);
                ivnormarl.setImageResource(R.drawable.dust_green);
                ImageView ivbad = (ImageView) dialog.findViewById(R.id.dust_ivbad);
                ivbad.setImageResource(R.drawable.dust_yellow);
                ImageView ivverybad = (ImageView) dialog.findViewById(R.id.dust_ivverybad);
                ivverybad.setImageResource(R.drawable.dust_red);

                TextView tvgood = (TextView) dialog.findViewById(R.id.dust_tvgood);
                tvgood.setText("좋음:0~30");
                TextView tvnormal = (TextView) dialog.findViewById(R.id.dust_tvnormal);
                tvnormal.setText("보통:~80");
                TextView tvbad = (TextView) dialog.findViewById(R.id.dust_tvbad);
                tvbad.setText("나쁨:~150");
                TextView tvverybad = (TextView) dialog.findViewById(R.id.dust_tvverybad);
                tvverybad.setText("매우 나쁨:151~");
                dialog.show();
            }
        });*/

        mContext = this;

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_chevron_left_black_24dp ); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled( false );
    }

    //말풍선
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate( R.layout.customballoon_dust, null );
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getCalloutBalloon(MapPOIItem mDefaultMarker) {
            HashMap<String, String> content = (HashMap<String, String>)mDefaultMarker.getUserObject();
            String address = content.get("address");
            ((TextView) mCalloutBalloon.findViewById(R.id.dust_address)).setText(address);
            String dust = content.get("dust");
            int dust_value= Integer.parseInt(dust);



            if(dust_value<=30){
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setTextColor(Color.rgb(33,150,243));
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setText(dust);
            }
            else if(dust_value<=80&&dust_value>30)
            {
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setTextColor(Color.rgb(76,175,89));
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setText(dust);
            }
            else if(dust_value<=150&&dust_value>80)
            {
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setTextColor(Color.rgb(255,235,59));
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setText(dust);
            }
            else if(dust_value>150)
            {
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setTextColor(Color.rgb(255,0,0));
                ((TextView) mCalloutBalloon.findViewById(R.id.dust_dust)).setText(dust);
            }

                return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem DEFAULT_MARKER_POINT) {
            return null;
        }
    }

    private void createDefaultMarker(MapView mapView, double lat, double lon, int i, String key, String name) {
        String value = item.get(i).get(key);

        MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord( lat, lon );

        customMarker = new MapPOIItem();
        customMarker.setMapPoint( DEFAULT_MARKER_POINT );

        HashMap<String, String> body = new HashMap<>();
        body.put( "address", name );
        body.put( "dust", value );
        customMarker.setUserObject( body );
        customMarker.setItemName("미세먼지 농도");

        dustMap.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        if (Integer.parseInt(value) <= 30) {
            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageResourceId(R.drawable.dust_blue); // 마커 이미지.
            customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        } else if (Integer.parseInt(value) > 30 && Integer.parseInt(value) <= 80) {
            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageResourceId(R.drawable.dust_green); // 마커 이미지.
            customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        } else if (Integer.parseInt(value) > 80 && Integer.parseInt(value) <= 150) {
            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageResourceId(R.drawable.dust_yellow); // 마커 이미지.
            customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        } else {
            customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageResourceId(R.drawable.dust_red); // 마커 이미지.
            customMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        }
        mapView.addPOIItem( customMarker );
        dustMap.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.95, 128.25), false);
    }

    public void getItem() {
        StrictMode.enableDefaults();
        try{
            URL url = new URL(
                    "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?"
                            + "serviceKey=b1HYVglZ3aRbVvFzQoiJQoL2JPlcC6%2FmnEN4KMAJ9WjoA8qlFYCrNjnJenKs2gRASCwfXtymVcRXz95NRof9wg%3D%3D&numOfRows=10"
                            + "&pageNo=1&itemCode=PM10&dataGubun=HOUR&searchCondition=MONTH"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (item.size() < 18 || parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("dataTime")){ //title 만나면 내용을 받을수 있게 하자
                            dataTime = true;
                        }
                        if(parser.getName().equals("seoul")){ //mapy 만나면 내용을 받을수 있게 하자
                            seoul = true;
                        }
                        if(parser.getName().equals("busan")){ //mapy 만나면 내용을 받을수 있게 하자
                            busan = true;
                        }
                        if(parser.getName().equals("daegu")){ //mapy 만나면 내용을 받을수 있게 하자
                            daegu = true;
                        }
                        if(parser.getName().equals("incheon")){ //mapy 만나면 내용을 받을수 있게 하자
                            incheon = true;
                        }
                        if(parser.getName().equals("gwangju")){ //mapy 만나면 내용을 받을수 있게 하자
                            gwangju = true;
                        }
                        if(parser.getName().equals("daejeon")){ //mapy 만나면 내용을 받을수 있게 하자
                            daejeon = true;
                        }
                        if(parser.getName().equals("ulsan")){ //mapy 만나면 내용을 받을수 있게 하자
                            ulsan = true;
                        }
                        if(parser.getName().equals("gyeonggi")){ //mapy 만나면 내용을 받을수 있게 하자
                            gyeonggi = true;
                        }
                        if(parser.getName().equals("gangwon")){ //mapy 만나면 내용을 받을수 있게 하자
                            gangwon = true;
                        }
                        if(parser.getName().equals("chungbuk")){ //mapy 만나면 내용을 받을수 있게 하자
                            chungbuk = true;
                        }
                        if(parser.getName().equals("chungnam")){ //mapy 만나면 내용을 받을수 있게 하자
                            chungnam = true;
                        }
                        if(parser.getName().equals("jeonbuk")){ //mapy 만나면 내용을 받을수 있게 하자
                            jeonbuk = true;
                        }
                        if(parser.getName().equals("jeonnam")){ //mapy 만나면 내용을 받을수 있게 하자
                            jeonnam = true;
                        }
                        if(parser.getName().equals("gyeongbuk")){ //mapy 만나면 내용을 받을수 있게 하자
                            gyeongbuk = true;
                        }
                        if(parser.getName().equals("gyeongnam")){ //mapy 만나면 내용을 받을수 있게 하자
                            gyeongnam = true;
                        }
                        if(parser.getName().equals("jeju")){ //mapy 만나면 내용을 받을수 있게 하자
                            jeju = true;
                        }
                        if(parser.getName().equals("sejong")){ //mapy 만나면 내용을 받을수 있게 하자
                            sejong = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        HashMap<String, String> hashMap = new HashMap<>();

                        if(dataTime) {
                            dataTime = false;
                            hashMap.put("dataTime", parser.getText());
                            item.add(hashMap);
                        }
                        if(seoul) {
                            seoul = false;
                            hashMap.put("seoul", parser.getText());
                            item.add(hashMap);
                        }
                        if(busan) {
                            busan = false;
                            hashMap.put("busan", parser.getText());
                            item.add(hashMap);
                        }
                        if(daegu) {
                            daegu = false;
                            hashMap.put("daegu", parser.getText());
                            item.add(hashMap);
                        }
                        if(incheon) {
                            incheon = false;
                            hashMap.put("incheon", parser.getText());
                            item.add(hashMap);
                        }
                        if(gwangju) {
                            gwangju = false;
                            hashMap.put("gwangju", parser.getText());
                            item.add(hashMap);
                        }
                        if(daejeon) {
                            daejeon = false;
                            hashMap.put("daejeon", parser.getText());
                            item.add(hashMap);
                        }
                        if(ulsan) {
                            ulsan = false;
                            hashMap.put("ulsan", parser.getText());
                            item.add(hashMap);
                        }
                        if(gyeonggi) {
                            gyeonggi = false;
                            hashMap.put("gyeonggi", parser.getText());
                            item.add(hashMap);
                        }
                        if(gangwon){ //isMapy이 true일 때 태그의 내용을 저장.
                            gangwon = false;
                            hashMap.put("gangwon", parser.getText());
                            item.add(hashMap);
                        }
                        if(chungbuk){ //isMapy이 true일 때 태그의 내용을 저장.
                            chungbuk = false;
                            hashMap.put("chungbuk", parser.getText());
                            item.add(hashMap);
                        }
                        if(chungnam){ //isMapy이 true일 때 태그의 내용을 저장.
                            chungnam = false;
                            hashMap.put("chungnam", parser.getText());
                            item.add(hashMap);
                        }
                        if(jeonbuk){ //isMapy이 true일 때 태그의 내용을 저장.
                            jeonbuk = false;
                            hashMap.put("jeonbuk", parser.getText());
                            item.add(hashMap);
                        }
                        if(jeonnam){ //isMapy이 true일 때 태그의 내용을 저장.
                            jeonnam = false;
                            hashMap.put("jeonnam", parser.getText());
                            item.add(hashMap);
                        }
                        if(gyeongbuk){ //isMapy이 true일 때 태그의 내용을 저장.
                            gyeongbuk = false;
                            hashMap.put("gyeongbuk", parser.getText());
                            item.add(hashMap);
                        }
                        if(gyeongnam){ //isMapy이 true일 때 태그의 내용을 저장.
                            gyeongnam = false;
                            hashMap.put("gyeongnam", parser.getText());
                            item.add(hashMap);
                        }
                        if(jeju){ //isMapy이 true일 때 태그의 내용을 저장.
                            jeju = false;
                            hashMap.put("jeju", parser.getText());
                            item.add(hashMap);
                        }
                        if(sejong){ //isMapy이 true일 때 태그의 내용을 저장.
                            sejong = false;
                            hashMap.put("sejong", parser.getText());
                            item.add(hashMap);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            Log.d("파싱 오류", e.toString());
        }
    }

    public ArrayList<HashMap<String, String>> retItem() {
        return item;
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