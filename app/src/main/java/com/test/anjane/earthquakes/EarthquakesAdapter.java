package com.test.anjane.earthquakes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.test.anjane.R;
import com.test.anjane.earthquakes.EarData;

import java.util.ArrayList;
import java.util.List;

public class EarthquakesAdapter extends RecyclerView.Adapter<EarthquakesAdapter.EarViewHolder> {
    private List<EarData> Eardata;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);

    }

    private  OnItemClickListener onItemClickListener;


    public class EarViewHolder extends RecyclerView.ViewHolder{
        private TextView ear_warnStress_info, ear_areaCode, ear_tmFc;

        private LinearLayout recyclerlayout;


        public EarViewHolder(View v) {
            super(v);

            ear_warnStress_info = v.findViewById( R.id.ear_warnStress_info);
            ear_areaCode = v.findViewById(R.id.ear_areaCode);
            ear_tmFc = v.findViewById(R.id.ear_tmFc);
            recyclerlayout=v.findViewById( R.id.recyclerlayout_ear );


            v.setClickable(true);
            v.setEnabled(true);
        }



    }

    public EarthquakesAdapter(List<EarData> list, OnItemClickListener onItemClickListener) {
        Eardata =  list;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public EarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.ear_temp_row, parent, false);
        EarViewHolder vh = new EarViewHolder(v);
        return vh;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템에 표시
    @Override
    public void onBindViewHolder( EarViewHolder holder, final int position) {
        EarData ear = Eardata.get( position );

        holder.ear_warnStress_info.setText(ear.getListgps());
        holder.ear_areaCode.setText("규모 " + ear.getListsize());
        holder.ear_tmFc.setText(ear.getListTime().substring(0,16));

        holder.recyclerlayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick( v,position );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return Eardata.size();
    }


    public EarData getEar(int position) {
        return Eardata != null ? Eardata.get(position) : null;
    }
}