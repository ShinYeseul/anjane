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

public class NNFragment extends Fragment {

    private RecyclerView naturalRecyclerView;
    private RecyclerView.Adapter naturalViewAdapter;
    private RecyclerView.LayoutManager naturalLayoutManager;
    private TextView newsMessage;

    private String htmlPageUrl = "https://www.yna.co.kr/local/all"; //파싱할 홈페이지 주소의 URL주소

    public NNFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_nn, container, false);
        naturalRecyclerView = v.findViewById(R.id.natural_newslist);
        naturalLayoutManager = new LinearLayoutManager(getContext());
        naturalRecyclerView.setLayoutManager(naturalLayoutManager);

        newsMessage = v.findViewById(R.id.Nnews_message);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
//        jsoupAsyncTask.execute();
        jsoupAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return v;
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final List<NewsDataNatural> NaturalNews = new ArrayList<>();

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

                    List naturaltitleList = new ArrayList();
                    List naturaltimeList = new ArrayList();
                    List naturalurlList = new ArrayList();
                    List naturalimagelList = new ArrayList();

                    for(int i = 0; i < time.size(); i++){
                        if(title.get(i).toString().contains("산불") ||
                                title.get(i).toString().contains("불 ") ||
                                title.get(i).toString().contains("날씨") ||
                                title.get(i).toString().contains("자연재해") ||
                                title.get(i).toString().contains("자연재난") ||
                                title.get(i).toString().contains("호우") ||
                                title.get(i).toString().contains("대설") ||
                                title.get(i).toString().contains("강풍") ||
                                title.get(i).toString().contains("미세먼지") ||
                                title.get(i).toString().contains("건조") ||
                                title.get(i).toString().contains("폭우") ||
                                title.get(i).toString().contains("더위") ||
                                title.get(i).toString().contains("소나기") ||
                                title.get(i).toString().contains("지진"))
                        {

                            naturaltitleList.add(title.get(i).text());
                            naturaltimeList.add(time.get(i).text());

                            String[] array = url.get(i).toString().split("\"");
                            String str = array[1].replace("//", "");
                            naturalurlList.add("http:" + str);

                            String[] imageArray = image.get(i).toString().split("\"");
                            System.out.println(imageArray[1]);
                            naturalimagelList.add("http:" + imageArray[1]);

                        }
                    }

                    for (int i = 0; i < naturaltitleList.size(); i++) {
                        NewsDataNatural newsDataNatural = new NewsDataNatural();
                        newsDataNatural.setTitleNatural(naturaltitleList.get(i).toString());
                        newsDataNatural.setTimeNatural(naturaltimeList.get(i).toString());
                        newsDataNatural.setUrlNatural(naturalurlList.get(i).toString());
                        newsDataNatural.setImageNatural(naturalimagelList.get(i).toString());
                        NaturalNews.add(newsDataNatural);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    naturalViewAdapter = new NewsAdapterNatural(NaturalNews, getActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Object obj = v.getTag();
                            if (obj != null) {
                                int position = (int) obj;
                                ((NewsAdapterNatural) naturalViewAdapter).getNews(position).getTitleNatural();
                                String url = ((NewsAdapterNatural) naturalViewAdapter).getNews(position).getUrlNatural();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        }
                    });

                    if(NaturalNews.size() == 0) {
                        newsMessage.setText("최근 자연재난 뉴스가 없습니다.");
                    }
                    naturalRecyclerView.setAdapter(naturalViewAdapter);
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void result) { }
    }
}