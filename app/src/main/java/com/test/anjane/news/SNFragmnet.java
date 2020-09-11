package com.test.anjane.news;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.anjane.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SNFragmnet extends Fragment {

    private RecyclerView socialRecyclerView;
    private RecyclerView.Adapter socialViewAdapter;
    private RecyclerView.LayoutManager socialLayoutManager;
    private TextView newsMessage;

    private String htmlPageUrl = "https://www.yna.co.kr/local/all"; //파싱할 홈페이지 주소의 URL주소

    public SNFragmnet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_sn, container, false);
        socialRecyclerView = v.findViewById(R.id.social_newslist);
        socialLayoutManager = new LinearLayoutManager(getContext());
        socialRecyclerView.setLayoutManager(socialLayoutManager);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        newsMessage = v.findViewById( R.id.Snews_message );


        return v;
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final List<NewsDataSocial> SocialNews = new ArrayList<>();
            for(int cnt = 1; cnt < 10; cnt++) {
                try {
                    Document doc = (Document) Jsoup.connect(htmlPageUrl + "/" + cnt).get();
                    //제목
                    Elements title = doc.select("div.item-box01").select("div.news-con").select("strong.tit-news");
                    //시간
                    Elements time = doc.select("div.item-box01").select("div.info-box01").select("span.txt-time");
                    //url
                    Elements url = doc.select("div.item-box01").select("figure.img-con").select("[href]");
                    //사진
                    Elements image = doc.select("div.item-box01").select("figure.img-con").select("[src]");

                    List socialtitleList = new ArrayList();
                    List socialtimeList = new ArrayList();
                    List socialurlList = new ArrayList();
                    List sociaimagelList = new ArrayList();

                    for(int i = 0; i < time.size(); i++){
                        if(title.get(i).toString().contains("코로나") ||
                                title.get(i).toString().contains("코로나19") ||
                                title.get(i).toString().contains("확진자") ||
                                title.get(i).toString().contains("코로나 확진자") ||
                                title.get(i).toString().contains("진료소") ||
                                title.get(i).toString().contains("치료") ||
                                title.get(i).toString().contains("사회재난") ||
                                title.get(i).toString().contains("화재") ||
                                title.get(i).toString().contains("폭발") ||
                                title.get(i).toString().contains("교통사고") ||
                                title.get(i).toString().contains("교통") ||
                                title.get(i).toString().contains("붕괴사고") ||
                                title.get(i).toString().contains("붕괴")) {

                            socialtitleList.add(title.get(i).text());
                            socialtimeList.add(time.get(i).text());

                            String[] array = url.get(i).toString().split("\"");
                            String str = array[1].replace("//", "");
                            socialurlList.add("http:" + str);

                            String[] imageArray = image.get(i).toString().split("\"");
                            System.out.println(imageArray[1]);
                            sociaimagelList.add("http:" + imageArray[1]);
                        }
                    }

                    for (int i = 0; i < socialtitleList.size(); i++) {
                        NewsDataSocial newsDataSocial = new NewsDataSocial();
                        newsDataSocial.setTitleSocial(socialtitleList.get(i).toString());
                        newsDataSocial.setTimeSocial(socialtimeList.get(i).toString());
                        newsDataSocial.setUrlSocial(socialurlList.get(i).toString());
                        newsDataSocial.setImageSocial(sociaimagelList.get(i).toString());
                        SocialNews.add(newsDataSocial);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    socialViewAdapter = new NewsAdapterSocial(SocialNews, getActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Object obj = v.getTag();
                            if (obj != null) {
                                int position = (int) obj;
                                ((NewsAdapterSocial) socialViewAdapter).getNews(position).getTitleSocial();
                                String url = ((NewsAdapterSocial) socialViewAdapter).getNews(position).getUrlSocial();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        }
                    });
                    if(SocialNews.size() == 0) {
                        newsMessage.setText("최근 사회재난 뉴스가 없습니다.");
                    }
                    else socialRecyclerView.setAdapter(socialViewAdapter);
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void result) { }
    }
}