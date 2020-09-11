package com.test.anjane.wind;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.test.anjane.R;
import com.test.anjane.news.NewsDataSocial;
import com.test.anjane.rain.OnViewHolderItemClickListener;
import com.test.anjane.rain.RainData;


import java.util.List;

public class WindAdapter extends RecyclerView.Adapter<WindAdapter.WindViewHolder> {
    private List<WindData> mDataset;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private  OnItemClickListener onItemClickListener;

    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    private Context context;


    public class WindViewHolder extends RecyclerView.ViewHolder{
        private TextView wind_warnStress, wind_warnStress_info, wind_areaCode, wind_tmFc,windstarttime,windarea;
        private ImageView imgwind;
        private LinearLayout recyclerlayout_wind,wind_dropdown,detaillayout_wind;

        OnViewHolderItemClickListener onViewHolderItemClickListener;

        public WindViewHolder(View v) {
            super(v);

            wind_warnStress = v.findViewById( R.id.wind_warnStress);
            wind_warnStress_info = v.findViewById(R.id.wind_warnStress_info);
            wind_areaCode = v.findViewById(R.id.wind_areaCode);
            wind_tmFc = v.findViewById(R.id.wind_tmFc);
            imgwind = v.findViewById(R.id.imgwind);
            recyclerlayout_wind=v.findViewById( R.id.recyclerlayout_wind );
            wind_dropdown = v.findViewById( R.id.winddropdown );
            windstarttime = v.findViewById( R.id.wind_startTime );
            windarea=v.findViewById(R.id.wind_area);
            detaillayout_wind = v.findViewById( R.id.detaillayout_wind );
            v.setClickable(true);
            v.setEnabled(true);

            wind_dropdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewHolderItemClickListener.onViewHolderItemClick();
                }
            });

        }

        public void onBind(WindData wind , int position, SparseBooleanArray selectedItems){
            if (wind.getWind_warnStress().equals( "0" ))
                wind_warnStress.setText( "강풍주의보" );
            else if (wind.getWind_warnStress().equals( "1" ))
                wind_warnStress.setText( "강풍경보" );

            if (wind.getWind_warnStress_info().equals( "0" ))
                wind_warnStress_info.setText( "풍속이 14m/s이상" );
            else if (wind.getWind_warnStress_info().equals( "1" ))
                wind_warnStress_info.setText( "강우량이 21m/s이상" );

            wind_areaCode.setText( wind.getWind_areaCode() );
            wind_tmFc.setText( wind.getWind_tmFc() );

            if (wind.getImgwind().equals( "0" ))
                imgwind.setImageResource( R.drawable.wind );
            else if (wind.getImgwind().equals( "1" ))
                imgwind.setImageResource( R.drawable.wind_blue );

            windstarttime.setText( wind.getWind_starttime());
            windarea.setText("지역이름: " + wind.getWind_areaCode());

            changeVisibility(selectedItems.get(position));
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 60;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    detaillayout_wind.getLayoutParams().height = value;
                    detaillayout_wind.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    detaillayout_wind.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                }
            });
            // Animation start
            va.start();
        }


        public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }


    }

    public WindAdapter(List<WindData> list, OnItemClickListener onItemClickListener) {
        mDataset = list;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public WindAdapter.WindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.wind_temp_row, parent, false);
        WindViewHolder vh = new WindViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템에 표시
    @Override
    public void onBindViewHolder( WindViewHolder holder, final int position) {

        holder.onBind(mDataset.get( position ), position, selectedItems);
        holder.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
                if (selectedItems.get(position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;
            }
        });

        holder.recyclerlayout_wind.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick( v,position );
            }
        } );

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public WindData getWind(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
}