package com.test.anjane;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.tabs.TabLayout;
import com.test.anjane.action.Action;

import com.test.anjane.covid.COVID;
import com.test.anjane.dust.DustActivity;
import com.test.anjane.login.login;
import com.test.anjane.shelter.Shelter;
import com.test.anjane.snow.Snow;
import com.test.anjane.weather.WeatherAdapter;
import com.test.anjane.weather.WeatherData;
import com.test.anjane.earthquakes.Earthquakes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends AppCompatActivity {
    private GpsTracker gpsTracker;
    public double latitude;
    public double longitude;

    //날씨
    org.w3c.dom.Document doc = null;
    TextView tvtemp,tvforecast_term;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};
    TextView  tempmin,tempmax;
    private DrawerLayout drawer;
    private MainBackPressCloseHandler mainBackPressCloseHandler ;
    private DrawerLayout mDrawerLayout;

    private TextView textview_address;
    private ImageView ivmainweather;

    //현재시간 구하는알고리즘
    private long currenttime= System.currentTimeMillis();
    Date date=new Date(currenttime);
    SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
    String formatDate=sdfNow.format(date);

    private RecyclerView weatherRecyclerView;
    private RecyclerView.Adapter weatherViewAdapter;
    private RecyclerView.LayoutManager weatherLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
        tempmax=findViewById(R.id.tempmax);
        tempmin=findViewById(R.id.tempmin);
        //날씨
        tvtemp = (TextView) findViewById(R.id.tvtemperature);
        ivmainweather = (ImageView)findViewById(R.id.ivmainweather);

        tvforecast_term = (TextView) findViewById(R.id.tvforecast_term);

        textview_address = (TextView)findViewById(R.id.tvlocation);

        mainBackPressCloseHandler = new MainBackPressCloseHandler(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        weatherRecyclerView = findViewById(R.id.weather_Hlist);
        weatherLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) weatherLayoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        weatherRecyclerView.setLayoutManager(weatherLayoutManager);

        drawer = findViewById(R.id.drawer_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        LinearLayout windlayout = (LinearLayout) findViewById(R.id.windlayout);
        LinearLayout rainlayout = (LinearLayout) findViewById(R.id.rainlayout);
        LinearLayout snowlayout = (LinearLayout) findViewById(R.id.snowlayout);
        LinearLayout dustlayout = (LinearLayout) findViewById(R.id.dustlayout);
        LinearLayout earthlayout = (LinearLayout) findViewById(R.id.earthlayout);
        LinearLayout carlayout = (LinearLayout) findViewById(R.id.carlayout);
        LinearLayout covidlayout = (LinearLayout) findViewById(R.id.covidlayout);
        LinearLayout actionlayout = (LinearLayout) findViewById(R.id.actionlayout);
        LinearLayout shelterlayout = (LinearLayout) findViewById(R.id.shelterlayout);
        LinearLayout postlayout = (LinearLayout) findViewById(R.id.postlayout);
        ImageView settingbt = (ImageView)  findViewById( R.id.settingbt );
        ImageView closebt = (ImageView) findViewById( R.id.closebt );


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.windlayout :
                       /* Intent intent = new Intent( getApplicationContext(), Wind.class );
                        startActivity( intent );*/
                        Intent intent = new Intent( getApplicationContext(), WindTest.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.rainlayout:
                     /*   intent = new Intent( getApplicationContext(), Rain.class );
                        startActivity( intent );*/
                        intent = new Intent( getApplicationContext(), RainTest.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.snowlayout :
                        intent = new Intent( getApplicationContext(), Snow.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.dustlayout :
                        intent = new Intent( getApplicationContext(), DustActivity.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.earthlayout :
                        intent = new Intent( getApplicationContext(), Earthquakes.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.carlayout :
                        intent = new Intent( getApplicationContext(),Car.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.covidlayout :
                        intent = new Intent( getApplicationContext(), COVID.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.actionlayout :
                        intent = new Intent( getApplicationContext(), Action.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.shelterlayout :
                        intent = new Intent( getApplicationContext(), Shelter.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.postlayout :
                        intent = new Intent( getApplicationContext(), login.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.settingbt :
                        intent = new Intent( getApplicationContext(),Setting.class );
                        startActivity( intent );
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.closebt :
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }
        };
        windlayout.setOnClickListener(clickListener);
        rainlayout.setOnClickListener(clickListener);
        snowlayout.setOnClickListener(clickListener);
        dustlayout.setOnClickListener(clickListener);
        earthlayout.setOnClickListener(clickListener);
        carlayout.setOnClickListener(clickListener);
        covidlayout.setOnClickListener(clickListener);
        actionlayout.setOnClickListener(clickListener);
        shelterlayout.setOnClickListener(clickListener);
        postlayout.setOnClickListener(clickListener);
        settingbt.setOnClickListener(clickListener);
        closebt.setOnClickListener(clickListener);


        //Tablayout
        TabLayout newstabs = (TabLayout) findViewById(R.id.newstabs);
        newstabs.addTab(newstabs.newTab().setText("자연재난"));
        newstabs.addTab(newstabs.newTab().setText("사회재난"));
        newstabs.setTabGravity(newstabs.GRAVITY_FILL);

        //Adapter
        final ViewPager newsviewPager = (ViewPager) findViewById(R.id.newsviewpager);
        final FragmnetNNAdapter myPagerAdapter = new FragmnetNNAdapter(getSupportFragmentManager(), 2);
        newsviewPager.setAdapter(myPagerAdapter);

        //탭 선택 이벤트
        newstabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(newsviewPager));
        newsviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(newstabs));


    }
    @Override
    public void onStart() {
        super.onStart();

        gpsTracker = new GpsTracker(MainActivity.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity.GetXMLTask task = new MainActivity.GetXMLTask();

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + latitude + "gridy=" + longitude);

        String address = getCurrentAddress(latitude, longitude);
        String address1 = address.replaceAll("대한민국", "").replaceAll("[0-9]", "").replaceAll("-", "");
        textview_address.setText(address1);

        find_weather();
    }

    //닫기 버튼
    public  void  onButton2Clicked(View v){
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        mainBackPressCloseHandler.onBackPressed();
    }

    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();

            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }

    public void find_weather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&units=metric&appid=86b56ea39f0cc23294cde5085116c63c";

        JsonObjectRequest jor = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject main_object = response.getJSONObject(("main"));
                    JSONArray array = response.getJSONArray(("weather"));
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String temp_min = String.valueOf(main_object.getDouble("temp_min"));
                    String temp_max = String.valueOf(main_object.getDouble("temp_max"));
                    tempmax.setText(temp_max);
                    tempmin.setText(temp_min);

                    tvtemp.setText(temp);

                    if( description.equals("few clouds")){
                        ivmainweather.setImageResource(R.drawable.fewcloud);
                        description = "구름조금";
                    }else if(description.equals("clear sky")) {
                        ivmainweather.setImageResource(R.drawable.sun);
                        description ="맑음";
                    }else if(description.equals("rain")) {
                        ivmainweather.setImageResource(R.drawable.rain);
                        description = "비";
                    }else if(description.equals("snow")) {
                        ivmainweather.setImageResource(R.drawable.snow);
                        description = "눈";
                    }else if(description.equals("mist")) {
                        ivmainweather.setImageResource(R.drawable.mist);
                        description = "안개";
                    }else if(description.equals("thunderstorm")) {
                        ivmainweather.setImageResource(R.drawable.thunderstorm);
                        description = "천둥번개";
                    }else if(description.equals("shower rain")) {
                        ivmainweather.setImageResource(R.drawable.showerrain);
                        description = "소나기";
                    }else if(description.equals("scattered clouds")) {
                        ivmainweather.setImageResource(R.drawable.clouds);
                        description = "구름많음";
                    }else if(description.equals("overcast clouds")) {
                        ivmainweather.setImageResource(R.drawable.cloud);
                        description = "흐림";
                    }
                    else if(description.equals("broken clouds")) {
                        ivmainweather.setImageResource(R.drawable.fewcloud);
                        description = "깨진 구름";
                    }

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    String formatted_date = sdf.format(calendar.getTime());


                    double temp_double = Double.parseDouble(temp);
                    double centi = (temp_double);
                    centi = Math.round(centi);
                    int i = (int) centi;
                    tvtemp.setText(String.valueOf(i)+"℃");
                    tvforecast_term.setText(description);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }



    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    public class GetXMLTask extends AsyncTask<String, Void, Document> {

        @Override
        protected org.w3c.dom.Document doInBackground(String... urls) {
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
        int num = 0;
        @Override
        protected void onPostExecute(org.w3c.dom.Document doc) {
            String a;
            int ho=0;
            String s = "";
            String ss = "";
            String sss = "";
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
                NodeList websiteList = fstElmnt.getElementsByTagName("wfKor");
                //<wfKor>맑음</wfKor> =====> <wfKor> 태그의 첫번째 자식노드는 TextNode 이고 TextNode의 값은 맑음
                NodeList websiteLis = fstElmnt.getElementsByTagName("hour");
                ho = Integer.parseInt(websiteLis.item(0).getChildNodes().item(0).getNodeValue()) - 3;


                s +=  websiteLis.item(0).getChildNodes().item(0).getNodeValue() + "시";
                ss +=  ((Node) nameList.item(0)).getNodeValue() + "℃    ";

                weatherText.add(websiteList.item(0).getChildNodes().item(0).getNodeValue());

                WeatherData weatherData = new WeatherData();
                if (a.contains("0")) {
                    sss = "오늘 ";

                }else if(a.contains(("1"))){
                    sss = "내일 ";//tomorrowInstr + "요일 ";
                }else if(a.contains(("2"))) {
                    sss = "모레 ";//afterInstr + " 요일 ";
                }

                while(true) {
                    if(num == 0 && sss == "오늘 ") {
                        weatherData.setWeatherdate(sss + s);
                        num = 1;
                        break;
                    } else if(num == 1 && sss == "내일 ") {
                        weatherData.setWeatherdate(sss + s);
                        num = 2;
                        break;
                    } else if(num == 2 && sss == "모레 ") {
                        weatherData.setWeatherdate(sss + s);
                        num = 3;
                        break;
                    } else {
                        weatherData.setWeatherdate(s);
                        break;
                    }
                }

                weatherData.setWeatherimage(weatherText.get(i));

                weatherData.setWeathertemp(ss);
                weather.add(weatherData);

                s = "";
                ss = "";
            }

            weatherViewAdapter = new WeatherAdapter(weather, MainActivity.this);

            weatherRecyclerView.setAdapter(weatherViewAdapter);

            super.onPostExecute(doc);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings :
                drawer.openDrawer( Gravity.RIGHT );
                return true ;
            case R.id.Legend:
                Intent intent = new Intent( getApplicationContext(), Legend.class );
                startActivity( intent );
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}