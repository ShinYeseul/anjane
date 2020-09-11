package com.test.anjane.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.anjane.R;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.NewsViewHolder> {
    private List<WeatherData> mDataset;

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public TextView weather_date, weather_temp;
        public ImageView weather_Image;
        public NewsViewHolder(View v) {
            super(v);
            weather_date = v.findViewById(R.id.mainweather_date);
            weather_Image = v.findViewById(R.id.weather_image);
            weather_temp = v.findViewById(R.id.mainweather_temp);

            v.setClickable(true);
            v.setEnabled(true);
        }
    }

    public WeatherAdapter(List<WeatherData> weatherDataset, Context context) {
        mDataset = weatherDataset;
        Fresco.initialize(context);
    }

    @Override
    public WeatherAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.mainweather_temp_row, parent, false);
        NewsViewHolder vh = new NewsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        WeatherData weather = mDataset.get(position);
        holder.weather_date.setText(weather.getWeatherdate());
        holder.weather_temp.setText(weather.getWeathertemp());
        if(weather.getWeatherimage().equals("맑음"))
            holder.weather_Image.setImageResource( R.drawable.sun );
        else if(weather.getWeatherimage().equals("구름 많음"))
            holder.weather_Image.setImageResource(R.drawable.clouds);
        else if(weather.getWeatherimage().equals("구름 조금"))
            holder.weather_Image.setImageResource(R.drawable.fewcloud);
        else if(weather.getWeatherimage().equals("흐림"))
            holder.weather_Image.setImageResource(R.drawable.cloud);
        else if(weather.getWeatherimage().equals("비"))
            holder.weather_Image.setImageResource(R.drawable.rain);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public WeatherData getNews(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
}