package com.test.anjane.post.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.test.anjane.GpsTracker;
import com.test.anjane.R;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private String TAG = "MapFragment";

    private MapView mapView;
    private ViewGroup mapViewContainer;
    Context mContext;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    private FirebaseFirestore db;
    ArrayList addressList = new ArrayList<>();
    ArrayList titleList = new ArrayList<>();
    ArrayList dateList = new ArrayList<>();
    ArrayList spin2List = new ArrayList<>();
    MapPOIItem mDefaultMarker;

    String titlename;
    private FloatingActionButton mapMyLoaction;
    private FloatingActionButton dialog_post;

    private ProgressBar dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_post, container, false);

        dialog = v.findViewById(R.id.post_progressBar);
        dialog.setVisibility(View.VISIBLE);

        //지도

        mapView = new MapView(getActivity());
        mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);


        mapView.setZoomLevel(4,true);
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int i = 0;
                                addressList.add( document.getData().get( "address" ) );
                                titleList.add( document.getData().get( "title" ) );
                                dateList.add( document.getData().get( "date" ) );
                                spin2List.add( document.getData().get( "spin2" ) );
                                i++;
                            }
                            for(int i =0; i<spin2List.size();i++) {
                                    createDefaultMarker( mapView );
                            }
                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }

                });

        //구현한 CalloutBalloonAdapter 등록
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mapView.setPOIItemEventListener(piel);
        mapMyLoaction = v.findViewById(R.id.myLocation);
        mapMyLoaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                GpsTracker gpsTracker = new GpsTracker(mContext);
                double latitude1 = gpsTracker.getLatitude();
                double longitude1 = gpsTracker.getLongitude();
                MapPoint MyLocation_point = null;

                MyLocation_point=MapPoint.mapPointWithGeoCoord(latitude1,longitude1);

                mapView.setMapCenterPoint(MyLocation_point,true);
            }
        });

        dialog_post = v.findViewById(R.id.dialog_post);
        dialog_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog( getContext());
                dialog.setContentView(R.layout.customdialog_post );

                dialog.show();
            }
        });
        return v;
    }

    //말풍선
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate( R.layout.customballoon_post, null );
        }

        @Override
        public View getCalloutBalloon(MapPOIItem mDefaultMarker) {
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(mDefaultMarker.getItemName());
            HashMap<String, String> body = (HashMap<String, String>)mDefaultMarker.getUserObject();
            String date = body.get("date");
            ((TextView) mCalloutBalloon.findViewById(R.id.date)).setText(date);
            String spin2 = body.get("spin2");
            ((TextView) mCalloutBalloon.findViewById(R.id.spin2)).setText(spin2);

            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem DEFAULT_MARKER_POINT) {
            return null;
        }
    }

    //마커
    private void createDefaultMarker(MapView mapView) {
        final Geocoder geocoder = new Geocoder(getContext());
        double lat; double lon;
        List<List<Address>> list = new ArrayList<List<Address>>();
        try {
            for(int i = 0; i < addressList.size(); i++) {
                String str = addressList.get(i).toString();
                list.add(geocoder.getFromLocationName(str, 10));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (addressList.size() == 0) {

        } else {

            for (int i = 0; i < list.size(); i++) {
                Address addr = list.get( i ).get( 0 );
                lat  = addr.getLatitude();
                lon = addr.getLongitude();

                MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord( lat, lon );


                titlename = titleList.get( i ).toString();
                mDefaultMarker = new MapPOIItem();
                mDefaultMarker.setItemName( titlename );
                mDefaultMarker.setMapPoint( DEFAULT_MARKER_POINT );

                HashMap<String, String> body = new HashMap<>();
                body.put( "date", dateList.get( i ).toString() );
                body.put( "spin2", spin2List.get( i ).toString() );
                mDefaultMarker.setUserObject( body );

                //spin1: 위험정보
                if (spin2List.get(i).equals("화재위험")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.dangerouspin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                } else if (spin2List.get(i).equals("교통위험")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.dangerouspin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                else if (spin2List.get(i).equals("보행위험")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.dangerouspin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                else if (spin2List.get(i).equals("붕괴위험")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.dangerouspin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                else if (spin2List.get(i).equals("기타위험")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.dangerouspin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                //spin2: 사고발생현황
                else if (spin2List.get(i).equals("화재")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.accidentpin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                } else if (spin2List.get(i).equals("교통")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.accidentpin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                else if (spin2List.get(i).equals("기타")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.accidentpin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                //spin1: 편의정보
                else if (spin2List.get(i).equals("화장실")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.conpin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                //spin1: 기타
                else if (spin2List.get(i).equals("기타")) {
                    mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    mDefaultMarker.setCustomImageResourceId(R.drawable.conpin); // 마커 이미지.
                    mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                }
                mapView.addPOIItem( mDefaultMarker );  mapView.setMapCenterPoint(DEFAULT_MARKER_POINT,true);
            }

        }
        dialog.setVisibility(View.GONE);
    }

    MapView.POIItemEventListener piel = new MapView.POIItemEventListener() {
        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) { }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) { }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) { }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) { }
    };

    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) { }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d( "@@@", "start" );
                //위치 값을 가져올 수 있음
                mapView.setCurrentLocationTrackingMode( MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading );
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale( getActivity(), REQUIRED_PERMISSIONS[0] )) {
                    Toast.makeText( getActivity(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG ).show();
                } else {
                    Toast.makeText( getActivity(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG ).show();
                }
            }
        }
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(getContext(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void onPause() {
        super.onPause();
        mapViewContainer.removeAllViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapViewContainer.removeAllViews();
    }
    @Override

    public void onAttach(Context context) {

        super.onAttach(context);

       mContext = context;

    }
}