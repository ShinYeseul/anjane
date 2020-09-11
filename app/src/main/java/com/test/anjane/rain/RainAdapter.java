package com.test.anjane.rain;

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

import com.test.anjane.R;
import java.util.List;

public class RainAdapter extends RecyclerView.Adapter<RainAdapter.RainViewHolder> {
    private List<RainData> mDataset;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private  OnItemClickListener onItemClickListener;

    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    private Context context;

    public class RainViewHolder extends RecyclerView.ViewHolder{
        private TextView rain_warnStress, rain_warnStress_info, rain_areaCode, rain_tmFc,rainstarttime,rainarea;
        private ImageView imgrain,uparrow;
        private LinearLayout recyclerlayout,raindropdown,detaillayout;

        OnViewHolderItemClickListener onViewHolderItemClickListener;

        public RainViewHolder(View v) {
            super(v);

            rain_warnStress = v.findViewById( R.id.rain_warnStress);
            rain_warnStress_info = v.findViewById(R.id.rain_warnStress_info);
            rain_areaCode = v.findViewById(R.id.rain_areaCode);
            rain_tmFc = v.findViewById(R.id.rain_tmFc);
            imgrain = v.findViewById(R.id.imgrain);
            uparrow=v.findViewById( R.id.uparrow );
            recyclerlayout=v.findViewById( R.id.recyclerlayout_rain );
            raindropdown = v.findViewById( R.id.raindropdown );
            detaillayout = v.findViewById( R.id.detaillayout );
            raindropdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewHolderItemClickListener.onViewHolderItemClick();
                }
            });

            rainstarttime = v.findViewById( R.id.rain_startTime );
            rainarea=v.findViewById(R.id.rain_area);
            v.setClickable(true);
            v.setEnabled(true);

        }

        public void onBind(RainData rain ,int position, SparseBooleanArray selectedItems){
            if (rain.getRain_warnStress().equals( "0" ))
                rain_warnStress.setText( "호우주의보" );
            else if (rain.getRain_warnStress().equals( "1" ))
                rain_warnStress.setText( "호우경보" );

            if (rain.getRain_warnStress_info().equals( "0" ))
                rain_warnStress_info.setText( "강우량이 60mm이상" );
            else if (rain.getRain_warnStress_info().equals( "1" ))
                rain_warnStress_info.setText( "강우량이 90mm이상" );

            rain_areaCode.setText( rain.getRain_areaCode() );
            rain_tmFc.setText( rain.getRain_tmFc() );

            if (rain.getImgrain().equals( "0" ))
               imgrain.setImageResource( R.drawable.rain_skyblue );
            else if (rain.getImgrain().equals( "1" ))
                imgrain.setImageResource( R.drawable.rain_blue );

           rainstarttime.setText( rain.getRain_starttime());
           rainarea.setText("지역이름: " + rain.getRain_areaCode());


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
                    detaillayout.getLayoutParams().height = value;
                    detaillayout.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    detaillayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                }
            });
            // Animation start
            va.start();
        }


    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }


    }

    public RainAdapter(List<RainData> list, OnItemClickListener onItemClickListener) {
        mDataset = list;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RainAdapter.RainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.rain_temp_row, parent, false);
        RainViewHolder vh = new RainViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템에 표시
    @Override
    public void onBindViewHolder(  RainViewHolder holder, final int position) {
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

        holder.recyclerlayout.setOnClickListener( new View.OnClickListener() {
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


    public RainData getRain(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
}