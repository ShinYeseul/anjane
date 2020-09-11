package com.test.anjane.covid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.test.anjane.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class COVIDFragmnet extends Fragment {
    private Button btnSeoul; //서울
    private Button btnGyeonggi; //경기
    private Button btnGangwon; //강원
    private Button btnIncheon; //인천
    private Button btnSejong; //세종
    private Button btnGwangju; //광주
    private Button btnUlsan; //울산
    private Button btnBusan; //부산
    private Button btnDaegu; //대구
    private Button btnDaejeon; //대전
    private Button btnGyeongbuk; //경북
    private Button btnGyeongnam; //경남
    private Button btnChungbuk; //충북
    private Button btnChungnam; //충남
    private Button btnJeonbuk; //전북
    private Button btnJeonnam; //전남
    private Button btnJeju; //제주

    private String covidDaypersons; //일일 확진자
    private String covidDaycurepersons; //일일 완치자
    private String covidCountnum1; //누적 확진 환자
    private String covidBeforenum1; //전일 대비
    private String covidCountnum2; //누적 확진 환자
    private String covidBeforenum2; //전일 대비
    private String covidCountnum3; //누적 확진 환자
    private String covidBeforenum3; //전일 대비
    private String covidCountnum4; //누적 확진 환자
    private String covidBeforenum4; //전일 대비

    private TextView daypersons; //일일 확진자
    private TextView daycurepersons; //일일 완치자
    private TextView countnum1; //전일 대비
    private TextView beforenum1; //누적 확진자
    private TextView countnum2; //전일 대비
    private TextView beforenum2; //누적 확진자
    private TextView countnum3; //전일 대비
    private TextView beforenum3; //누적 확진자
    private TextView countnum4; //전일 대비
    private TextView beforenum4; //누적 확진자

    private ProgressBar dialog;
    private Context mContext;
    private String htmlPageUrl = "http://ncov.mohw.go.kr/"; //파싱할 홈페이지 주소의 URL주소

    Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if (context instanceof Activity){
            activity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.covid, container, false);

        dialog = v.findViewById(R.id.progressBar);

        btnSeoul = v.findViewById(R.id.BtnSeoul);
        btnGyeonggi = v.findViewById(R.id.BtnGyeonggi);
        btnGangwon = v.findViewById(R.id.BtnGangwon);
        btnIncheon = v.findViewById(R.id.BtnIncheon);
        btnSejong = v.findViewById(R.id.BtnSejong);
        btnGwangju = v.findViewById(R.id.BtnGwangju);
        btnUlsan = v.findViewById(R.id.BtnUlsan);
        btnBusan = v.findViewById(R.id.BtnBusan);
        btnDaegu = v.findViewById(R.id.BtnDaegu);
        btnDaejeon = v.findViewById(R.id.BtnDaejeon);
        btnGyeongbuk = v.findViewById(R.id.BtnGyeongbuk);
        btnGyeongnam = v.findViewById(R.id.BtnGyeongnam);
        btnChungbuk = v.findViewById(R.id.BtnChungbuk);
        btnChungnam = v.findViewById(R.id.BtnChungnam);
        btnJeonbuk = v.findViewById(R.id.BtnJeonbuk);
        btnJeonnam = v.findViewById(R.id.BtnJeonnam);
        btnJeju = v.findViewById(R.id.BtnJeju);

        daypersons = (TextView) v.findViewById( R.id.daypersons );
        daycurepersons = (TextView) v.findViewById( R.id.daycurepersons );
        countnum1 = (TextView) v.findViewById( R.id.countnum1 );
        beforenum1 = (TextView) v.findViewById( R.id.beforenum1 );
        countnum2 = (TextView) v.findViewById( R.id.countnum2 );
        beforenum2 = (TextView) v.findViewById( R.id.beforenum2 );
        countnum3 = (TextView) v.findViewById( R.id.countnum3);
        beforenum3 = (TextView) v.findViewById( R.id.beforenum3 );
        countnum4 = (TextView) v.findViewById( R.id.countnum4);
        beforenum4 = (TextView) v.findViewById( R.id.beforenum4 );

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        return  v;

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            final List<COVIDData> Corona = new ArrayList<>();
            for (int page = 1; page < 18; page++) {
                try {
                    Document doc = (Document) Jsoup.connect(htmlPageUrl).get();
                    //제목
                    Elements all = doc.select("div.rpsa_detail").select("div#map_city" + page);

                    List nameList = new ArrayList();
                    List accumulatedList = new ArrayList();
                    List todayList = new ArrayList();
                    List isolationList = new ArrayList();
                    List releaseList = new ArrayList();
                    List eathList = new ArrayList();

                    if(page == 1) {
                        Elements person = doc.select("span.data"); //일일 확진자
                        List<String> today = new ArrayList<>();
                        Elements covidCountnumAll = doc.select("div.liveNum").select("span.num");  //누적 수
                        Elements beforeAll = doc.select("span.before"); //전일 대비

                        for(Element p: person){
                            today.add(p.text());
                        }

                        covidDaypersons = today.get(0);
                        covidDaycurepersons = today.get(1);

                        String split = " ";
                        String[] str = covidCountnumAll.text().split(split);
                        covidCountnum1 = str[0].substring(4); //누적 확진 환자
                        covidCountnum2 = str[1]; //완치
                        covidCountnum3 = str[2]; //치료중
                        covidCountnum4 = str[3]; //사망

                        String[] str1 = beforeAll.text().split(split);
                        covidBeforenum1 = str1[1] + str1[2]; //누적 확진 환자
                        covidBeforenum2 = str1[3] + str1[4]; //완치
                        covidBeforenum3 = str1[5] + str1[6]; //치료중
                        covidBeforenum4 = str1[7] + str1[8]; //사망
                    }

                    for (int i = 0; i < all.size(); i++) {
                        String split = " ";
                        String[] str = all.get(i).text().split(split);
                        nameList.add(str[0]);
                        accumulatedList.add(str[10]);
                        todayList.add(str[14]);
                        isolationList.add(str[16]);
                        releaseList.add(str[19]);
                        eathList.add(str[21]);
                    }

                    for (int i = 0; i < nameList.size(); i++) {
                        COVIDData coronaData = new COVIDData();
                        coronaData.setName(nameList.get(i).toString());
                        coronaData.setAccumulated(accumulatedList.get(i).toString());
                        coronaData.setToday(todayList.get(i).toString());
                        coronaData.setIsolation(isolationList.get(i).toString());
                        coronaData.setRelease(releaseList.get(i).toString());
                        coronaData.setEath(eathList.get(i).toString());
                        Corona.add(coronaData);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    daypersons.setText( covidDaypersons + "명");
                    daycurepersons.setText(covidDaycurepersons + "명");
                    countnum1.setText(covidCountnum1);
                    countnum2.setText(covidCountnum2);
                    countnum3.setText(covidCountnum3);
                    countnum4.setText(covidCountnum4);
                    beforenum1.setText(covidBeforenum1);
                    beforenum2.setText(covidBeforenum2);
                    beforenum3.setText(covidBeforenum3);
                    beforenum4.setText(covidBeforenum4);

                    btnSeoul.setText(Corona.get(0).getName() + "\n" + Corona.get(0).getAccumulated() + "\n" + Corona.get(0).getToday());
                    btnSeoul.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(0).getName()).setMessage(Corona.get(0).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnBusan.setText(Corona.get(1).getName() + "\n" + Corona.get(1).getAccumulated() + "\n" + Corona.get(1).getToday());
                    btnBusan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(1).getName()).setMessage(Corona.get(1).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnDaegu.setText(Corona.get(2).getName() + "\n" + Corona.get(2).getAccumulated() + "\n" + Corona.get(2).getToday());
                    btnDaegu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(2).getName()).setMessage(Corona.get(2).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnIncheon.setText(Corona.get(3).getName() + "\n" + Corona.get(3).getAccumulated() + "\n" + Corona.get(3).getToday());
                    btnIncheon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(3).getName()).setMessage(Corona.get(3).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnGwangju.setText(Corona.get(4).getName() + "\n" + Corona.get(4).getAccumulated() + "\n" + Corona.get(4).getToday());
                    btnGwangju.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(4).getName()).setMessage(Corona.get(4).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnDaejeon.setText(Corona.get(5).getName() + "\n" + Corona.get(5).getAccumulated() + "\n" + Corona.get(5).getToday());
                    btnDaejeon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(5).getName()).setMessage(Corona.get(5).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnUlsan.setText(Corona.get(6).getName() + "\n" + Corona.get(6).getAccumulated() + "\n" + Corona.get(6).getToday());
                    btnUlsan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(6).getName()).setMessage(Corona.get(6).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnSejong.setText(Corona.get(7).getName() + "\n" + Corona.get(7).getAccumulated() + "\n" + Corona.get(7).getToday());
                    btnSejong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(7).getName()).setMessage(Corona.get(7).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnGyeonggi.setText(Corona.get(8).getName() + "\n" + Corona.get(8).getAccumulated() + "\n" + Corona.get(8).getToday());
                    btnGyeonggi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(8).getName()).setMessage(Corona.get(8).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnGangwon.setText(Corona.get(9).getName() + "\n" + Corona.get(9).getAccumulated() + "\n" + Corona.get(9).getToday());
                    btnGangwon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(9).getName()).setMessage(Corona.get(9).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnChungbuk.setText(Corona.get(10).getName() + "\n" + Corona.get(10).getAccumulated() + "\n" + Corona.get(10).getToday());
                    btnChungbuk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(10).getName()).setMessage(Corona.get(10).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnChungnam.setText(Corona.get(11).getName() + "\n" + Corona.get(11).getAccumulated() + "\n" + Corona.get(11).getToday());
                    btnChungnam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(11).getName()).setMessage(Corona.get(11).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnJeonbuk.setText(Corona.get(12).getName() + "\n" + Corona.get(12).getAccumulated() + "\n" + Corona.get(12).getToday());
                    btnJeonbuk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(12).getName()).setMessage(Corona.get(12).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnJeonnam.setText(Corona.get(13).getName() + "\n" + Corona.get(13).getAccumulated() + "\n" + Corona.get(13).getToday());
                    btnJeonnam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(13).getName()).setMessage(Corona.get(13).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnGyeongbuk.setText(Corona.get(14).getName() + "\n" + Corona.get(14).getAccumulated() + "\n" + Corona.get(14).getToday());
                    btnGyeongbuk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(14).getName()).setMessage(Corona.get(14).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnGyeongnam.setText(Corona.get(15).getName() + "\n" + Corona.get(15).getAccumulated() + "\n" + Corona.get(15).getToday());
                    btnGyeongnam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(15).getName()).setMessage(Corona.get(15).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    btnJeju.setText(Corona.get(16).getName() + "\n" + Corona.get(16).getAccumulated() + "\n" + Corona.get(16).getToday());
                    btnJeju.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(Corona.get(16).getName()).setMessage(Corona.get(16).getAll());
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });

                    dialog.setVisibility(View.GONE);
                }
            });

            return null;
        }
    }
}