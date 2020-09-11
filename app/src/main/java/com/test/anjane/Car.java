package com.test.anjane;

import android.app.Dialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Car extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, MapView.POIItemEventListener,
        MapView.MapViewEventListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener, View.OnClickListener {
    private MapView mMapView;
    public ArrayList<String> a = new ArrayList();
    public double latitude;
    public double longitude;

    private LinearLayout subLayout_2;
    private TextView sub_title,sub_title3;
    private TextView sub_text2,sub_text3;

    public double selected_distance = 0;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2,fab3,fab4;
    public MapPoint MyLocation_point = null;


    Document doc = null;

    Button chartbt;
    FloatingActionButton btn_location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.car);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sub_text2=findViewById(R.id.Sub_text2);
        sub_text3=findViewById(R.id.Sub_text3);
        subLayout_2=findViewById(R.id.Sub_layout2);
        sub_title=findViewById(R.id.Sub_title);

        sub_title3=findViewById(R.id.Sub_title3);
        subLayout_2.setVisibility(View.GONE);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);

        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);
        //Set toolbar
        mMapView = findViewById(R.id.map_view);


        GpsTracker gpsTracker = new GpsTracker(Car.this);
        double latitude1 = gpsTracker.getLatitude();
        double longitude1 = gpsTracker.getLongitude();
        final MapPOIItem MyLocation_marker=new MapPOIItem();
        MyLocation_point=MapPoint.mapPointWithGeoCoord(latitude1,longitude1);
        MyLocation_marker.setItemName("내위치");
        MyLocation_marker.setMapPoint(MyLocation_point);

        mMapView.addPOIItem(MyLocation_marker);
        mMapView.setZoomLevel(2,true);
        mMapView.setMapCenterPoint(MyLocation_point,true);

        mMapView.setMapViewEventListener(this);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);

        mMapView.removeAllCircles();
        mMapView.removeAllPOIItems();




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //뒤로가기 버튼
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GpsTracker gpsTracker = new GpsTracker(Car.this);
                double latitude1 = gpsTracker.getLatitude();
                double longitude1 = gpsTracker.getLongitude();
                MapPoint MyLocation_point = null;

                MyLocation_point=MapPoint.mapPointWithGeoCoord(latitude1,longitude1);
                mMapView.setMapCenterPoint(MyLocation_point,true);
                mMapView.setZoomLevel(2,true);
                mMapView.selectPOIItem(MyLocation_marker,true);
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                mMapView.setShowCurrentLocationMarker(false);
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                mMapView.setShowCurrentLocationMarker(true);


            }
        });

        GetXMLTask task = new GetXMLTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=125.625&MaxX=129.741&MinY=34.206%20&MaxY=38.753&type=its");

    }

    MapView.POIItemEventListener piel = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
            mMapView.setMapCenterPoint(mapPOIItem.getMapPoint(), true);

            if(mapPOIItem.getItemName()!="내위치") {
                SetSubLayout(mapPOIItem);
                Log.d("이벤트110", "1");
                subLayout_2.setVisibility(View.GONE);
                subLayout_2.setVisibility(View.VISIBLE);

            }


        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };
    //fab 이벤트 설정 구간
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                anim();

                break;
            case R.id.fab1:
                anim();
                Toast.makeText(getBaseContext(), "현재위치 기준 10KM검색중", Toast.LENGTH_SHORT).show();
                mMapView.setZoomLevel(8,true);
                mMapView.setMapCenterPoint(MyLocation_point,true);
                mMapView.removeAllPOIItems();
                selected_distance=10000;

                GetXMLTask5 task5_1 = new GetXMLTask5();
                task5_1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=0&MaxX=999&MinY=0%20&MaxY=99&type=ex");
                break;
            case R.id.fab2:
                anim();

                Toast.makeText(getBaseContext(), "현재위치 기준 50KM검색중", Toast.LENGTH_SHORT).show();
                mMapView.setZoomLevel(8,true);
                mMapView.setMapCenterPoint(MyLocation_point,true);

                mMapView.removeAllPOIItems();
                selected_distance=50000;

                GetXMLTask5 task5_2 = new GetXMLTask5();
                task5_2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=0&MaxX=999&MinY=0%20&MaxY=99&type=ex");
                break;
            case R.id.fab3:
                anim();
                mMapView.setZoomLevel(10,true);
                mMapView.removeAllPOIItems();
                mMapView.setMapCenterPoint(MyLocation_point,true);

                Toast.makeText(getBaseContext(), "현재위치 기준 100KM검색중", Toast.LENGTH_SHORT).show();
                selected_distance=100000;

                GetXMLTask5 task5_3 = new GetXMLTask5();
                task5_3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=0&MaxX=999&MinY=0%20&MaxY=99&type=ex");
                break;
            case R.id.fab4:
                anim();
                mMapView.setZoomLevel(12,true);
                mMapView.removeAllPOIItems();
                mMapView.setMapCenterPoint(MyLocation_point,true);

                Toast.makeText(getBaseContext(), "모두 검색중", Toast.LENGTH_SHORT).show();
                selected_distance=10000000;
                GetXMLTask5 task5_4 = new GetXMLTask5();
                task5_4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=0&MaxX=999&MinY=0%20&MaxY=99&type=ex");

                break;
        }
    }

    private class GetXMLTask extends AsyncTask<String, Void, Document> {//XMLTask = 국도 교통사고 파싱
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();
            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();
            //  mMapView.removeAllPOIItems();

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            for (int i = 0; i < nodeList.getLength(); i++) {
                MapPOIItem marker = new MapPOIItem();
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                //도로 종류
                NodeList nameList = fstElmnt.getElementsByTagName("type");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();

                //사고내용
                NodeList incidentmsg = fstElmnt.getElementsByTagName("incidentmsg");
                NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());

                HashMap<String, String> body = new HashMap<>();
                String incidenttype="국도";
                String blocktype="NULL";
                marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                    blocktype="통제없음";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                {
                    blocktype="갓길통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                    blocktype="차로부분통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                    blocktype="전면통제";
                }

                //사고 내용이 없을 경우에 대한 예외처리
                try{
                    incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                }catch(NullPointerException e){
                    System.out.println("예외2");
                    incimsg = "사고내용미상";
                }

                // 말풍선을위한 body 초기화

                if(Distance(latitude,longitude,latitude1,longitude1) <= 731120000) {

                    body.put("type", blocktype);
                    body.put("statusmsg", incimsg);
                    body.put("key", "00");
                    marker.setItemName("교통사고[" + incidenttype + "]");
                    mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    marker.setMapPoint(mp);
                    marker.setUserObject(body);

                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker.setCustomImageResourceId(R.drawable.trafficaff);
                    mMapView.setPOIItemEventListener(piel);

                    //       marker.setShowCalloutBalloonOnTouch(false);
                    marker.setShowDisclosureButtonOnCalloutBalloon(false);
                    marker.setShowCalloutBalloonOnTouch(false);
                    mMapView.addPOIItem(marker);
                }

            }
            GetXMLTask2 task2 = new GetXMLTask2();
            task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=0&MaxX=999&MinY=0%20&MaxY=99&type=ex");

            super.onPostExecute(doc);
        }
    }

    private class GetXMLTask2 extends AsyncTask<String, Void, Document> {//XMLTask2 = 고속도로 교통사고 파싱
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc){
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();

            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            for (int i = 0; i < nodeList.getLength(); i++) {
                MapPOIItem marker = new MapPOIItem();
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                //도로 종류
                NodeList nameList = fstElmnt.getElementsByTagName("type");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String startday;

                //사고내용
                NodeList incidentmsg = fstElmnt.getElementsByTagName("incidentmsg");
                NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());

                HashMap<String, String> body = new HashMap<>();
                String incidenttype="고속도로";
                String blocktype="NULL";

                marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                    blocktype="통제없음";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                {
                    blocktype="갓길통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                    blocktype="차로부분통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                    blocktype="전면통제";
                }

                //사고 내용이 없을 경우에 대한 예외처리
                try{
                    incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                }catch(NullPointerException e){
                    System.out.println("예외2");
                    incimsg = "사고내용미상";

                }

                // 말풍선을위한 body 초기화


                if(Distance(latitude,longitude,latitude1,longitude1) <= 122230000) {
                    body.put("type",blocktype);
                    body.put("statusmsg",incimsg);

                    body.put("key","01");

                    marker.setItemName("교통사고["+incidenttype+"]");
                    mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    marker.setMapPoint(mp);
                    System.out.println(incimsg+"\n"+incidenttype+"\n"+blocktype);
                    marker.setUserObject(body);

                    mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker.setCustomImageResourceId(R.drawable.trafficaff);

                    //        marker.setShowCalloutBalloonOnTouch(false);
                    marker.setShowDisclosureButtonOnCalloutBalloon(false);
                    mMapView.setPOIItemEventListener(piel);
                    marker.setShowCalloutBalloonOnTouch(false);
                    mMapView.addPOIItem(marker);
                }}
            GetXMLTask3 task3 = new GetXMLTask3();
            task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NEventIdentity?key=1586662500411&ReqType=2&MinX=125.625&MaxX=129.741&MinY=34.206%20&MaxY=38.753&type=its");

            super.onPostExecute(doc);
        }
    }

    //국도 공사
    private class GetXMLTask3 extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();
            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();

            String s = "";

            NodeList nodeList = doc.getElementsByTagName("data");

            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    MapPOIItem marker = new MapPOIItem();
                    Node node = nodeList.item(i); //data엘리먼트 노드
                    Element fstElmnt = (Element) node;
                    //도로 종류
                    NodeList nameList = fstElmnt.getElementsByTagName("type");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();

                    //사고내용
                    NodeList incidentmsg = fstElmnt.getElementsByTagName("eventstatusmsg");
                    NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                    NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                    latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                    NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                    longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());
                    NodeList startdate = fstElmnt.getElementsByTagName("eventstartday");
                    String startday;

                    startday=startdate.item(0).getChildNodes().item(0).getNodeValue();
                    System.out.println(startday+"\n");

                    HashMap<String, String> body = new HashMap<>();
                    String incidenttype="국도";
                    String blocktype="NULL";
                    marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                    if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                        blocktype="통제없음";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                    {
                        blocktype="갓길통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                        blocktype="차로부분통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                        blocktype="전면통제";
                    }

                    //사고 내용이 없을 경우에 대한 예외처리
                    try{
                        incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                    }catch(NullPointerException e){
                        System.out.println("예외2");
                        incimsg = "공사내용미상";

                    }

                    // 말풍선을위한 body 초기화

                    if(Distance(latitude,longitude,latitude1,longitude1) <= 312320000) {
                        body.put("type",blocktype);
                        body.put("statusmsg",incimsg);
                        body.put("startday",startday);
                        body.put("key","11");
                        marker.setItemName("공사정보["+incidenttype+"]");
                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                        marker.setMapPoint(mp);
                        System.out.println(incimsg+"\n"+incidenttype+"\n"+blocktype);
                        marker.setUserObject(body);

                        marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.construction);
                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                        //      marker.setShowCalloutBalloonOnTouch(false);
                        marker.setShowDisclosureButtonOnCalloutBalloon(false);
                        mMapView.setPOIItemEventListener(piel);
                        Log.d("공사2","파싱중");
                        marker.setShowCalloutBalloonOnTouch(false);
                        mMapView.addPOIItem(marker);
                    }}
            }catch (NullPointerException e){

                System.out.println("예외3");
                Toast.makeText(Car.this, "국도 공사 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(doc);
            GetXMLTask4 task4 = new GetXMLTask4();
            task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NEventIdentity?key=1586662500411&ReqType=2&MinX=125.625&MaxX=129.741&MinY=34.206%20&MaxY=38.753&type=ex");

        }

    }

    //고속도로 공사상황
    private class GetXMLTask4 extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();
            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    MapPOIItem marker = new MapPOIItem();
                    Node node = nodeList.item(i); //data엘리먼트 노드

                    Element fstElmnt = (Element) node;
                    //도로 종류
                    NodeList nameList = fstElmnt.getElementsByTagName("type");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();

                    //사고내용
                    NodeList incidentmsg = fstElmnt.getElementsByTagName("eventstatusmsg");
                    NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                    NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                    latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                    NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                    longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());
                    NodeList startdate = fstElmnt.getElementsByTagName("eventstartday");
                    String startday;

                    startday=startdate.item(0).getChildNodes().item(0).getNodeValue();
                    System.out.println(startday+"\n");

                    HashMap<String, String> body = new HashMap<>();
                    String incidenttype="고속도로";
                    String blocktype="NULL";
                    marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                    if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                        blocktype="통제없음";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                    {
                        blocktype="갓길통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                        blocktype="차로부분통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                        blocktype="전면통제";
                    }

                    //사고 내용이 없을 경우에 대한 예외처리
                    try{
                        incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                    }catch(NullPointerException e){
                        System.out.println("예외2");
                        incimsg = "공사내용미상";
                    }

                    startday=startday.substring(0,8);

                    // 말풍선을위한 body 초기화

                    if(Distance(latitude,longitude,latitude1,longitude1) <= 312320000) {
                        body.put("type", blocktype);
                        body.put("statusmsg", incimsg);
                        body.put("startday", startday);
                        body.put("key", "12");
                        marker.setItemName("공사정보[" + incidenttype + "]");
                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                        marker.setMapPoint(mp);
                        System.out.println(incimsg + "\n" + incidenttype + "\n" + blocktype);
                        marker.setUserObject(body);

                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.construction);

//                        marker.setShowCalloutBalloonOnTouch(false);
                        marker.setShowDisclosureButtonOnCalloutBalloon(false);
                        mMapView.setPOIItemEventListener(piel);
                        Log.d("공사1","파싱중");
                        marker.setShowCalloutBalloonOnTouch(false);
                        mMapView.addPOIItem(marker);
                    }
                }
            }catch (NullPointerException e){

                System.out.println("예외3");
                Toast.makeText(Car.this, "국도 사고 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(Car.this, "검색이 완료되었습니다.", Toast.LENGTH_SHORT).show();

            super.onPostExecute(doc);
        }

    }

    private class GetXMLTask5 extends AsyncTask<String, Void, Document> {//XMLTask = 국도 교통사고 파싱
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }

            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();
            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();
            //  mMapView.removeAllPOIItems();

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            for (int i = 0; i < nodeList.getLength(); i++) {
                MapPOIItem marker = new MapPOIItem();
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                //도로 종류
                NodeList nameList = fstElmnt.getElementsByTagName("type");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();

                //사고내용
                NodeList incidentmsg = fstElmnt.getElementsByTagName("incidentmsg");
                NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());

                HashMap<String, String> body = new HashMap<>();
                String incidenttype="국도";
                String blocktype="NULL";
                marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                    blocktype="통제없음";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                {
                    blocktype="갓길통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                    blocktype="차로부분통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                    blocktype="전면통제";
                }

                //사고 내용이 없을 경우에 대한 예외처리
                try{
                    incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                }catch(NullPointerException e){
                    System.out.println("예외2");
                    incimsg = "사고내용미상";
                }

                // 말풍선을위한 body 초기화

                if(Distance(latitude,longitude,latitude1,longitude1) <= selected_distance) {

                    body.put("type", blocktype);
                    body.put("statusmsg", incimsg);
                    body.put("key", "00");
                    marker.setItemName("교통사고[" + incidenttype + "]");
                    mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    marker.setMapPoint(mp);
                    marker.setUserObject(body);

                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker.setCustomImageResourceId(R.drawable.trafficaff);
                    mMapView.setPOIItemEventListener(piel);

                    //       marker.setShowCalloutBalloonOnTouch(false);
                    marker.setShowDisclosureButtonOnCalloutBalloon(false);
                    marker.setShowCalloutBalloonOnTouch(false);
                    mMapView.addPOIItem(marker);
                }

            }
            GetXMLTask6 task6 = new GetXMLTask6();
            task6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NIncidentIdentity?key=1586662500411&ReqType=2&MinX=0&MaxX=999&MinY=0%20&MaxY=99&type=ex");

            super.onPostExecute(doc);
        }
    }

    private class GetXMLTask6 extends AsyncTask<String, Void, Document> {//XMLTask2 = 고속도로 교통사고 파싱
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc){
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();

            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            for (int i = 0; i < nodeList.getLength(); i++) {
                MapPOIItem marker = new MapPOIItem();
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                //도로 종류
                NodeList nameList = fstElmnt.getElementsByTagName("type");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                String startday;

                //사고내용
                NodeList incidentmsg = fstElmnt.getElementsByTagName("incidentmsg");
                NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());

                HashMap<String, String> body = new HashMap<>();
                String incidenttype="고속도로";
                String blocktype="NULL";

                marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                    blocktype="통제없음";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                {
                    blocktype="갓길통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                    blocktype="차로부분통제";
                }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                    blocktype="전면통제";
                }

                //사고 내용이 없을 경우에 대한 예외처리
                try{
                    incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                }catch(NullPointerException e){
                    System.out.println("예외2");
                    incimsg = "사고내용미상";

                }

                // 말풍선을위한 body 초기화


                if(Distance(latitude,longitude,latitude1,longitude1) <= selected_distance) {
                    body.put("type",blocktype);
                    body.put("statusmsg",incimsg);

                    body.put("key","01");

                    marker.setItemName("교통사고["+incidenttype+"]");
                    mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                    marker.setMapPoint(mp);
                    System.out.println(incimsg+"\n"+incidenttype+"\n"+blocktype);
                    marker.setUserObject(body);

                    mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker.setCustomImageResourceId(R.drawable.trafficaff);

                    //        marker.setShowCalloutBalloonOnTouch(false);
                    marker.setShowDisclosureButtonOnCalloutBalloon(false);
                    mMapView.setPOIItemEventListener(piel);
                    marker.setShowCalloutBalloonOnTouch(false);
                    mMapView.addPOIItem(marker);
                }}
            GetXMLTask7 task7 = new GetXMLTask7();
            task7.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NEventIdentity?key=1586662500411&ReqType=2&MinX=125.625&MaxX=129.741&MinY=34.206%20&MaxY=38.753&type=its");

            super.onPostExecute(doc);
        }
    }

    //국도 공사
    private class GetXMLTask7 extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();
            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();

            String s = "";

            NodeList nodeList = doc.getElementsByTagName("data");

            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    MapPOIItem marker = new MapPOIItem();
                    Node node = nodeList.item(i); //data엘리먼트 노드
                    Element fstElmnt = (Element) node;
                    //도로 종류
                    NodeList nameList = fstElmnt.getElementsByTagName("type");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();

                    //사고내용
                    NodeList incidentmsg = fstElmnt.getElementsByTagName("eventstatusmsg");
                    NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                    NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                    latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                    NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                    longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());
                    NodeList startdate = fstElmnt.getElementsByTagName("eventstartday");
                    String startday;

                    startday=startdate.item(0).getChildNodes().item(0).getNodeValue();
                    System.out.println(startday+"\n");

                    HashMap<String, String> body = new HashMap<>();
                    String incidenttype="국도";
                    String blocktype="NULL";
                    marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                    if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                        blocktype="통제없음";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                    {
                        blocktype="갓길통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                        blocktype="차로부분통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                        blocktype="전면통제";
                    }

                    //사고 내용이 없을 경우에 대한 예외처리
                    try{
                        incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                    }catch(NullPointerException e){
                        System.out.println("예외2");
                        incimsg = "공사내용미상";

                    }

                    // 말풍선을위한 body 초기화

                    if(Distance(latitude,longitude,latitude1,longitude1) <= selected_distance) {
                        body.put("type",blocktype);
                        body.put("statusmsg",incimsg);
                        body.put("startday",startday);
                        body.put("key","11");
                        marker.setItemName("공사정보["+incidenttype+"]");
                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                        marker.setMapPoint(mp);
                        System.out.println(incimsg+"\n"+incidenttype+"\n"+blocktype);
                        marker.setUserObject(body);

                        marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.construction);
                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                        //      marker.setShowCalloutBalloonOnTouch(false);
                        marker.setShowDisclosureButtonOnCalloutBalloon(false);
                        mMapView.setPOIItemEventListener(piel);
                        Log.d("공사2","파싱중");
                        marker.setShowCalloutBalloonOnTouch(false);
                        mMapView.addPOIItem(marker);
                    }}
            }catch (NullPointerException e){

                System.out.println("예외3");
                Toast.makeText(Car.this, "국도 공사 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(doc);
            GetXMLTask8 task8 = new GetXMLTask8();
            task8.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://openapi.its.go.kr:8082/api/NEventIdentity?key=1586662500411&ReqType=2&MinX=125.625&MaxX=129.741&MinY=34.206%20&MaxY=38.753&type=ex");

        }

    }

    //고속도로 공사상황
    private class GetXMLTask8 extends AsyncTask<String, Void, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            GpsTracker gpsTracker = new GpsTracker(Car.this);
            double latitude1 = gpsTracker.getLatitude();
            double longitude1 = gpsTracker.getLongitude();
            MapPoint ma = null;
            MapPoint mp = null;
            double latitude;
            double longitude;
            String incimsg;
            ma = mMapView.getMapCenterPoint();

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("data");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    MapPOIItem marker = new MapPOIItem();
                    Node node = nodeList.item(i); //data엘리먼트 노드

                    Element fstElmnt = (Element) node;
                    //도로 종류
                    NodeList nameList = fstElmnt.getElementsByTagName("type");
                    Element nameElement = (Element) nameList.item(0);
                    nameList = nameElement.getChildNodes();

                    //사고내용
                    NodeList incidentmsg = fstElmnt.getElementsByTagName("eventstatusmsg");
                    NodeList LanesBlockType = fstElmnt.getElementsByTagName("lanesblocktype");

                    NodeList ycord = fstElmnt.getElementsByTagName("coordy");

                    latitude = Double.parseDouble(ycord.item(0).getChildNodes().item(0).getNodeValue());

                    NodeList xcord = fstElmnt.getElementsByTagName("coordx");

                    longitude = Double.parseDouble(xcord.item(0).getChildNodes().item(0).getNodeValue());
                    NodeList startdate = fstElmnt.getElementsByTagName("eventstartday");
                    String startday;

                    startday=startdate.item(0).getChildNodes().item(0).getNodeValue();
                    System.out.println(startday+"\n");

                    HashMap<String, String> body = new HashMap<>();
                    String incidenttype="고속도로";
                    String blocktype="NULL";
                    marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);

                    if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("0")){
                        blocktype="통제없음";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("1"))
                    {
                        blocktype="갓길통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("2")){
                        blocktype="차로부분통제";
                    }else if(LanesBlockType.item(0).getChildNodes().item(0).getNodeValue().contains("3")){
                        blocktype="전면통제";
                    }

                    //사고 내용이 없을 경우에 대한 예외처리
                    try{
                        incimsg = incidentmsg.item(0).getChildNodes().item(0).getNodeValue();

                    }catch(NullPointerException e){
                        System.out.println("예외2");
                        incimsg = "공사내용미상";
                    }

                    startday=startday.substring(0,8);

                    // 말풍선을위한 body 초기화

                    if(Distance(latitude,longitude,latitude1,longitude1) <= selected_distance) {
                        body.put("type", blocktype);
                        body.put("statusmsg", incimsg);
                        body.put("startday", startday);
                        body.put("key", "12");
                        marker.setItemName("공사정보[" + incidenttype + "]");
                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);
                        marker.setMapPoint(mp);
                        System.out.println(incimsg + "\n" + incidenttype + "\n" + blocktype);
                        marker.setUserObject(body);

                        mp = MapPoint.mapPointWithGeoCoord(latitude, longitude);

                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.construction);

//                        marker.setShowCalloutBalloonOnTouch(false);
                        marker.setShowDisclosureButtonOnCalloutBalloon(false);
                        mMapView.setPOIItemEventListener(piel);
                        Log.d("공사1","파싱중");
                        marker.setShowCalloutBalloonOnTouch(false);
                        mMapView.addPOIItem(marker);
                    }
                }
            }catch (NullPointerException e){

                System.out.println("예외3");
                Toast.makeText(Car.this, "국도 사고 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(doc);
            Toast.makeText(Car.this, "검색이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            selected_distance=0;
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
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

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
        subLayout_2.setVisibility(View.GONE);
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {




//            ((TextView) mCalloutBalloon.findViewById(R.id.car_title)).setText(marker.getItemName());
//
//
        //           HashMap<String, String> body = (HashMap<String, String>) mapPOIItem.getUserObject();
        //           String date = body.get("type");
//            String spin2 = body.get("statusmsg");
//            String startday = "시작일";
//
//            try {
//                startday= body.get("startday");
//
//            }catch (NullPointerException e){
//
//            }
//
//            ((TextView) mCalloutBalloon.findViewById(R.id.content1)).setText(date);
//            ((TextView) mCalloutBalloon.findViewById(R.id.content2)).setText(spin2);
//
//            //국도,고속도로 사고 파싱할때 필요없는거 안보이게
//            if(body.get("key")=="0"){
//                mCalloutBalloon.findViewById(R.id.subtitle3).setVisibility(View.INVISIBLE);
//                mCalloutBalloon.findViewById(R.id.content3).setVisibility(View.INVISIBLE);
//                mCalloutBalloon.findViewById(R.id.view3).setVisibility(View.INVISIBLE);
//                //공사정보 파싱할때 필요한레이아웃 다시 보이게
//            }else if(body.get("key")=="1") {
//                mCalloutBalloon.findViewById(R.id.subtitle3).setVisibility(View.VISIBLE);
//                mCalloutBalloon.findViewById(R.id.content3).setVisibility(View.VISIBLE);
//                mCalloutBalloon.findViewById(R.id.view3).setVisibility(View.VISIBLE);
//
//                ((TextView) mCalloutBalloon.findViewById(R.id.content3)).setText(startday);
//
//            }
//
//

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }


    public double Distance(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
        Location startPos = new Location("PointA");
        Location endPos = new Location("PointB");

        startPos.setLatitude(_latitude1);
        startPos.setLongitude(_longitude1);
        endPos.setLatitude(_latitude2);
        endPos.setLongitude(_longitude2);

        double distance = startPos.distanceTo(endPos);

        return distance;
    }

    public void SetSubLayout(MapPOIItem marker){
        if(marker.getItemName().contains("내위치"))
        {
            return ;
        }else {
            marker.getUserObject();


            HashMap<String, String> body = (HashMap<String, String>) marker.getUserObject();
            switch(body.get("key")){
                case "00" :
                    sub_title.setText("국도 교통사고");
                    sub_title3.setText("차선 통제");

                    switch(body.get("statusmsg")){
                        case "": {
                            sub_text2.setText("사고내용 미상");
                            break;
                        }
                        case " ":{
                            sub_text2.setText("사고내용 미상");
                            break;
                        }
                        default : {
                            sub_text2.setText(body.get("statusmsg"));
                            break;

                        }

                    }
                    switch(body.get("type")){
                        case "":{
                            sub_text3.setText("차선통제 미상");
                            break;
                        }
                        case " ":{
                            sub_text3.setText("차선통제 미상");
                            break;

                        }
                        default : {
                            sub_text3.setText(body.get("type"));
                            break;
                        }

                    }
                    break;
                case "01" :
                    sub_title.setText("고속도로 교통사고");
                    sub_title3.setText("차선 통제");




                    switch(body.get("statusmsg")){
                        case "": {
                            sub_text2.setText("사고내용 미상");
                            break;
                        }
                        case " ":{
                            sub_text2.setText("사고내용 미상");
                            break;
                        }
                        default : {
                            sub_text2.setText(body.get("statusmsg"));
                            break;

                        }

                    }
                    switch(body.get("type")) {
                        case "": {
                            sub_text3.setText("차선통제 미상");
                            break;
                        }
                        case " ": {
                            sub_text3.setText("차선통제 미상");
                            break;

                        }
                        default: {
                            sub_text3.setText(body.get("type"));
                            break;
                        }

                    }
                    break;
                case "11" :
                    sub_title.setText("국도 공사");
                    sub_title3.setText("공사 시작");

                    switch(body.get("statusmsg")){
                        case "": {
                            sub_text2.setText("공사내용 미상");
                            break;
                        }
                        case " ":{
                            sub_text2.setText("공사내용 미상");
                            break;
                        }
                        default : {
                            sub_text2.setText(body.get("statusmsg"));
                            break;

                        }

                    }

                    switch(body.get("startday")){
                        case "":{
                            sub_text3.setText("공사 시작일 미상");
                            break;
                        }
                        case " ":{
                            sub_text3.setText("공사 시작일 미상");
                            break;

                        }
                        default : {
                            sub_text3.setText(cutting(body.get("startday")));
                            break;
                        }

                    }
                    break;
                case "12" :
                    sub_title.setText("고속도로 공사");
                    sub_title3.setText("공사 시작");

                    switch(body.get("statusmsg")){
                        case "": {
                            sub_text2.setText("공사내용 미상");
                            break;
                        }
                        case " ":{
                            sub_text2.setText("공사내용 미상");
                            break;
                        }
                        default : {
                            sub_text2.setText(body.get("statusmsg"));
                            break;

                        }


                    }
                    switch(body.get("startday")){
                        case "":{
                            sub_text3.setText("공사 시작일 미상");
                            break;
                        }
                        case " ":{
                            sub_text3.setText("공사 시작일 미상");
                            break;

                        }
                        default : {
                            sub_text3.setText(cutting(body.get("startday")));
                            break;
                        }

                    }

                    break;
            };

//           String date = body.get("type");
//            String spin2 = body.get("statusmsg");
//            String startday = "시작일";
//
//            try {
//                startday= body.get("startday");
//
//            }catch (NullPointerException e){
//
//            }
//
//            ((TextView) mCalloutBalloon.findViewById(R.id.content1)).setText(date);
//            ((TextView) mCalloutBalloon.findViewById(R.id.content2)).setText(spin2);
//
//            //국도,고속도로 사고 파싱할때 필요없는거 안보이게
//            if(body.get("key")=="0"){
//                mCalloutBalloon.findViewById(R.id.subtitle3).setVisibility(View.INVISIBLE);
//                mCalloutBalloon.findViewById(R.id.content3).setVisibility(View.INVISIBLE);
//                mCalloutBalloon.findViewById(R.id.view3).setVisibility(View.INVISIBLE);
//                //공사정보 파싱할때 필요한레이아웃 다시 보이게
//            }else if(body.get("key")=="1") {
//                mCalloutBalloon.findViewById(R.id.subtitle3).setVisibility(View.VISIBLE);
//                mCalloutBalloon.findViewById(R.id.content3).setVisibility(View.VISIBLE);
//                mCalloutBalloon.findViewById(R.id.view3).setVisibility(View.VISIBLE);
//
//                ((TextView) mCalloutBalloon.findViewById(R.id.content3)).setText(startday);
//
//            }
//
//

        }



    }

    public String cutting(String date){
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
            year = date.substring(0, 4);
            month = date.substring(4, 6);
            day = date.substring(6, 8);

            formattingString = year + A + month + A + day;

            return formattingString;
        }catch (StringIndexOutOfBoundsException e){
            return date;
        }


    }
    public void anim() {

        if (isFabOpen) {

            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);

            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
        }
    }
}

