package com.test.anjane.firebase;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EarthquakesFirebase extends AppCompatActivity {

    private static final String TAG = "EarthquakesFirebase";
    private FirebaseFirestore db;
    public Document doc = null;
    public String day;
    public Elements contents;
    public ArrayList<String> listgps = new ArrayList();
    public ArrayList<String> listTime = new ArrayList<>();
    public ArrayList<Double> listsize = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        day = sdf.format(calendar.getTime());

        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("https://www.weather.go.kr/weather/earthquake_volcano/domesticlist.jsp?startTm=" + day +
                            "&endTm=" + day +
                            "&startSize=999&endSize=999&startLat=&endLat=&startLon=&endLon=&lat=&lon=&dist=&keyword=&x=14&y=8&schOption=T").get();//naver페이지를 불러옴
                    contents = doc.select("table#excel_body").select("tbody").select("td" + "\n");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴

                    for (int e = 7; e < contents.size(); e += 10) {
                        Element ee = doc.select("td").get(e);
                        listgps.add(ee.text());
                    }
                    for (int c = 1; c < contents.size(); c += 10) {
                        Element cc = doc.select("td").get(c);
                        listTime.add(cc.text().replaceAll("/", "-"));
                    }
                    for (int d = 2; d < contents.size(); d += 10) {
                        Element dd = doc.select("td").get(d);
                        listsize.add(Double.parseDouble(dd.text()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listgps.size() == 0) {
                    onAuthSuccess(null, null, null);
                    return;
                } else {
                    for (int i = 0; i < listgps.size(); i++) {
                        onAuthSuccess(listgps.get(i), listTime.get(i), listsize.get(i));
                    }
                }
            }
        }.execute();
    }

    private void onAuthSuccess(String gps, String time, Double size) {
        if(size > 1.5) db.collection("earthquakes").document(gps).set(toMap(gps, time, size));
    }

    @Exclude
    public Map<String, Object> toMap(String gps, String time, Double size) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gps", gps);
        result.put("time", time);
        result.put("size", size);

        return result;
    }

    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}