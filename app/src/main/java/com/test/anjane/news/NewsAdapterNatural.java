package com.test.anjane.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.anjane.R;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class NewsAdapterNatural extends RecyclerView.Adapter<NewsAdapterNatural.NewsViewHolder> {
    private List<NewsDataNatural> mDataset;
    private static View.OnClickListener onClickListener;

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_title;
        public TextView TextView_time;
        public ImageView ImageView_image;
        public View rootView;
        public NewsViewHolder(View v) {
            super(v);
            TextView_title = v.findViewById(R.id.textViewNewsTitle);
            TextView_time = v.findViewById(R.id.textViewNewsTime);
            ImageView_image = v.findViewById(R.id.ivNews);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            v.setOnClickListener(onClickListener);
        }
    }

    public NewsAdapterNatural(List<NewsDataNatural> newsDataset, Context context, View.OnClickListener onClick) {
        mDataset = newsDataset;
        onClickListener = onClick;
        Fresco.initialize(context);
    }

    @Override
    public NewsAdapterNatural.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
        NewsViewHolder vh = new NewsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        final NewsDataNatural news = mDataset.get(position);
        holder.TextView_title.setText(news.getTitleNatural());
        holder.TextView_time.setText("연합뉴스 " + news.getTimeNatural());

        new Thread() {
            public void run() {
                try {
                    URL url = new URL(news.getImageNatural());
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    Bitmap bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    holder.ImageView_image.setImageBitmap(bm);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        }.start();

        holder.rootView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public NewsDataNatural getNews(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
}