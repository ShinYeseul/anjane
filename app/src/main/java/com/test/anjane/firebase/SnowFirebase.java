package com.test.anjane.firebase;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SnowFirebase extends AppCompatActivity {
    private static final String TAG = "SnowFirebase";
    private FirebaseFirestore db;

    boolean resultMsg = false,
            areaName = false,
            warnStress = false,
            startTime = false;
    ArrayList<String> areanameitem = new ArrayList<>();
    ArrayList<String> warnstressitem = new ArrayList<>();
    ArrayList<String> starttimeitem = new ArrayList<>();

    private String formatDate;
    private String startDate;
    List<List<Address>> address = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        java.text.DateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
        formatDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(cal.DATE, -6); // 7일(일주일)을 뺀다
        startDate = format.format(cal.getTime());

        db = FirebaseFirestore.getInstance();

        getData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(areanameitem.size() == 0) {
            finish();
            //  onAuthSuccess(null, null, null);
        } else {
            for (int i = 0; i < areanameitem.size(); i++) {
                onAuthSuccess(areanameitem.get(i), warnstressitem.get(i), starttimeitem.get(i));
            }
        }
    }

    private void onAuthSuccess(String areaname, String warnstress, String startTime) {
        db.collection("snow").document(areaname).set(toMap(areaname, warnstress, startTime));
    }

    @Exclude
    public Map<String, Object> toMap(String areaname, String warnstress, String startTime) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("areaname", areaname);
        result.put("warnstress", warnstress);
        result.put("startTime", startTime);

        return result;
    }

    public void getData(){
        StrictMode.enableDefaults();
        try{
            URL url = new URL("http://apis.data.go.kr/1360000/WthrWrnInfoService/getPwnCd?serviceKey=S8lyiY1crKS8rseJMPCvPwBEUEvsSGUD4jc6f9Fxeb3TSOC6NY4lrNv6qr%" +
                    "2BWSdyy00n6ANIa9tGp9x9Qbh%2FEiw%3D%3D&numOfRows=20&pageNo=1" +
                    "&dataType=XML&fromTmFc=" + startDate + "&toTmFc=" + formatDate + "&warningType=8"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), "UTF-8");

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (areanameitem.size() < 20 || parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("resultMsg")){ //title 만나면 내용을 받을수 있게 하자
                            resultMsg = true;
                        }
                        if(parser.getName().equals("areaName")){ //title 만나면 내용을 받을수 있게 하자
                            areaName = true;
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
                            if(parser.getText().equals("NO_DATA")){
                                return;
                            }
                        }
                        if(areaName){ //isTitle이 true일 때 태그의 내용을 저장.
                            areaName = false;
                            areanameitem.add(parser.getText());
                            geocoder(parser.getText());
                        }
                        if(warnStress){ //isMapy이 true일 때 태그의 내용을 저장.
                            warnStress = false;
                            warnstressitem.add(parser.getText());
                        }
                        if(startTime){ //isMapy이 true일 때 태그의 내용을 저장.
                            startTime = false;
                            starttimeitem.add(parser.getText());
                        }
                        System.out.println(parser.getText());
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

    public void geocoder(String add) throws IOException {
        final Geocoder geocoder = new Geocoder(getApplicationContext());
        address.add( geocoder.getFromLocationName( add, 20 ) ); // 읽을개수
    }

    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}