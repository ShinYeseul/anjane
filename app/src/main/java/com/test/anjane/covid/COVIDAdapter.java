package com.test.anjane.covid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.test.anjane.R;

import java.util.List;

public class COVIDAdapter extends RecyclerView.Adapter<COVIDAdapter.CoronaViewHolder> {
    private List<COVIDData> mDataset;

    public static class CoronaViewHolder extends RecyclerView.ViewHolder{
        public TextView TextView_title; //지역 이름
        public TextView TextView_ratio; //누적 확진자
        public TextView TextView_today; //오늘 확진자
        public TextView TextView_isolation; //격리 중
        public TextView TextView_release; //격리 해제
        public TextView TextView_eath; //사망자
        public View rootView;

        public CoronaViewHolder(View v) {
            super(v);
            TextView_title = v.findViewById(R.id.textViewCoronaTitle);
            TextView_ratio = v.findViewById(R.id.textViewCoronaRatio);
            TextView_today = v.findViewById(R.id.textViewCoronaToday);
            TextView_isolation = v.findViewById(R.id.textViewCoronaIsolation);
            TextView_release = v.findViewById(R.id.textViewCoronaRelease);
            TextView_eath = v.findViewById(R.id.textViewCoronaEath);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
        }
    }

    public COVIDAdapter(List<COVIDData> newsDataset) {
        mDataset = newsDataset;
    }

    @Override
    public COVIDAdapter.CoronaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.covid_row, parent, false);
        CoronaViewHolder vh = new CoronaViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CoronaViewHolder holder, int position) {
        COVIDData corona = mDataset.get(position);
        holder.TextView_title.setText(corona.getName());
        holder.TextView_ratio.setText("누적 확진자 : " + corona.getAccumulated());
        holder.TextView_today.setText("오늘 추가 확진자 : " + corona.getToday());
        holder.TextView_isolation.setText("격리중 : " + corona.getIsolation());
        holder.TextView_release.setText("격리해제 : " + corona.getRelease());
        holder.TextView_eath.setText("사망자 : " + corona.getEath());
        holder.rootView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}
