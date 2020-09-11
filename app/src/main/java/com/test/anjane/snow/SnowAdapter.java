package com.test.anjane.snow;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.test.anjane.R;
import com.test.anjane.rain.OnViewHolderItemClickListener;
import com.test.anjane.wind.WindAdapter;

import java.util.List;

public class SnowAdapter extends RecyclerView.Adapter<SnowAdapter.SnowViewHolder> {
    private List<SnowData> mDataset;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    private Context con;


    public  class SnowViewHolder extends RecyclerView.ViewHolder{
        public TextView snow_warnStress, snow_warnStress_info, snow_areaCode, snow_tmFc,snow_starttime,snow_area;;
        public ImageView imgsnow,ivarrow;
        private LinearLayout recyclerlayout_snow,snow_dropdown,detaillayout_snow;
        OnViewHolderItemClickListener onViewHolderItemClickListener;

        public SnowViewHolder(View v) {
            super(v);

            snow_warnStress = v.findViewById( R.id.snow_warnStress);
            snow_warnStress_info = v.findViewById(R.id.snow_warnStress_info);
            snow_areaCode = v.findViewById(R.id.snow_areaCode);
            snow_tmFc = v.findViewById(R.id.snow_tmFc);
            imgsnow = v.findViewById(R.id.imgsnow);
            recyclerlayout_snow =v.findViewById( R.id.recyclerlayout_snow );
            ivarrow = v.findViewById( R.id.ivarrow );
            snow_dropdown = v.findViewById( R.id.snowdropdown );
            snow_dropdown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewHolderItemClickListener.onViewHolderItemClick();
                }
            });


            snow_starttime = v.findViewById( R.id.snow_startTime );
            snow_area=v.findViewById(R.id.snow_area);
            detaillayout_snow = v.findViewById( R.id.detaillayout_snow );
            v.setClickable(true);
            v.setEnabled(true);
        }

        public void onBind(SnowData snow , int position, SparseBooleanArray selectedItems){
            if(snow.getSnow_warnStress().equals( "0" ))
                snow_warnStress.setText("대설주의보");
            else if(snow.getSnow_warnStress().equals( "1" ))
                snow_warnStress.setText("대설경보");

            if(snow.getSnow_warnStress_info().equals( "0" ))
                snow_warnStress_info.setText("신적설 5cm이상");
            else if(snow.getSnow_warnStress_info().equals( "1" ))
               snow_warnStress_info.setText( "신적설 20cm이상" );

            snow_areaCode.setText(snow.getSnow_areaCode());
            snow_tmFc.setText(snow.getSnow_tmFc());

            if(snow.getImgsnow().equals("0"))
                imgsnow.setImageResource( R.drawable.snow_sky );
            else if(snow.getImgsnow().equals("1"))
                imgsnow.setImageResource( R.drawable.snow_blue );

           snow_starttime.setText( snow.getSnow_starttime());
           snow_area.setText( "지역이름: " + snow.getSnow_areaCode());

            changeVisibility(selectedItems.get(position));
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 60;
            float d = con.getResources().getDisplayMetrics().density;
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
                    detaillayout_snow.getLayoutParams().height = value;
                    detaillayout_snow.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    detaillayout_snow.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


                }
            });
            // Animation start
            va.start();
        }


        public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }


    }

    public SnowAdapter(List<SnowData> snowDataset, OnItemClickListener onItemClickListener) {
        mDataset = snowDataset;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SnowAdapter.SnowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.snow_temp_row, parent, false);
        SnowViewHolder vh = new SnowViewHolder(v);
        con = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(SnowViewHolder holder, final int position) {

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

        holder.recyclerlayout_snow.setOnClickListener( new View.OnClickListener() {
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

    public SnowData getSnow(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
}