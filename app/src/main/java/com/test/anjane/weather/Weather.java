package com.test.anjane.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.anjane.GpsTracker;
import com.test.anjane.R;
import com.test.anjane.news.NewsDataNatural;
import com.test.anjane.GpsTracker;
import com.test.anjane.weather.WeatherAdapter;
import com.test.anjane.weather.WeatherData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Weather extends AppCompatActivity  {
    public double latitude;
    public double longitude;
    private GpsTracker gpsTracker;
    Document doc = null;
    LinearLayout layout = null;
    String[] icons = { "sun", "cloud" };

    private RecyclerView weatherRecyclerView;
    private RecyclerView.Adapter weatherViewAdapter;
    private RecyclerView.LayoutManager weatherLayoutManager;
    private String weatherText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        GetXMLTask task = new GetXMLTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + latitude + "gridy=" + longitude);

        weatherRecyclerView = findViewById(R.id.weather_list);
        weatherLayoutManager = new LinearLayoutManager(this);
        weatherRecyclerView.setLayoutManager(weatherLayoutManager);
    }

    private class GetXMLTask extends AsyncTask<String, Void, Document> {

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
        protected void onPostExecute(org.w3c.dom.Document doc) {
            String a;
            int ho=0;
            String s = "";
            final List<WeatherData> weather = new ArrayList<>();
            List<String> weatherText = new ArrayList<>();

            NodeList nodeList = doc.getElementsByTagName("data");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.add(Calendar.DAY_OF_WEEK,1);
                Date tomorrow = mCalendar.getTime();
                mCalendar.add(Calendar.DAY_OF_WEEK,1);
                Date aftertomorrow = mCalendar.getTime();
                String mDateFormat = "EE";
                String tomorrowInstr = new SimpleDateFormat(mDateFormat).format(tomorrow);
                String afterInstr = new SimpleDateFormat(mDateFormat).format(aftertomorrow);
                Element fstElmnt = (Element) node;
                NodeList nameList = fstElmnt.getElementsByTagName("temp");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                NodeList ebsiteList = fstElmnt.getElementsByTagName("day");
                a = ebsiteList.item(0).getChildNodes().item(0).getNodeValue();
                if (a.contains("0")) {
                    s += "오늘 ";
                }else if(a.contains(("1"))){
                    s += tomorrowInstr + "요일 ";
                }else if(a.contains(("2"))) {
                    s += afterInstr + " 요일 ";
                }
                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");
                //<wfKor>맑음</wfKor> =====> <wfKor> 태그의 첫번째 자식노드는 TextNode 이고 TextNode의 값은 맑음
                NodeList websiteLis = fstElmnt.getElementsByTagName("hour");
                ho = Integer.parseInt(websiteLis.item(0).getChildNodes().item(0).getNodeValue()) - 3;
                if (ho < 0) {
                    ho = 21;
                }

                s += ho + "~" + websiteLis.item(0).getChildNodes().item(0).getNodeValue() + "시" + "\n";
                s += "온도 = " + ((Node) nameList.item(0)).getNodeValue() + "℃ ,";
                s += "날씨 = " + websiteList.item(0).getChildNodes().item(0).getNodeValue();

                weatherText.add(websiteList.item(0).getChildNodes().item(0).getNodeValue());

                WeatherData weatherData = new WeatherData();
                weatherData.setWeatherdate(s);
                weatherData.setWeatherimage(weatherText.get(i));

                weather.add(weatherData);

                s = "";
            }

            weatherViewAdapter = new WeatherAdapter(weather, Weather.this);

            weatherRecyclerView.setAdapter(weatherViewAdapter);

            super.onPostExecute(doc);
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
}