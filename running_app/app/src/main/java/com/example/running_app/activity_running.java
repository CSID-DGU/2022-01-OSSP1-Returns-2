package com.example.running_app;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationRequest;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class activity_running extends AppCompatActivity implements OnMapReadyCallback {

    //멤버 변수화
    TextView runningTime, runningDistance;
    Button start_btn, end_btn, running_btn, home_btn, profile_btn;
    private GoogleMap googleMap;

    //상태를 표시하는 상수 지정.
    //각각의 숫자는 독립적인 개별 '상태' 의미

    public static final int INIT = 0; //처음
    public static final int RUN = 1; //실행중
    public static final int PAUSE = 2; //정지
    public static final int END = 3; //러닝 끝 + 초기화

    //상태값을 저장하는 변수
    //INIT은 초기값이다. 그걸 status 안에 넣는다. (0을 넣은 것)
    public static int status = INIT;

    //기록할때 순서 체크를 위한 변수
    private int cnt = 1;

    //타이머 시간 값을 저장할 변수
    private long baseTime, pauseTime;
    private static double runDistance = 0.0;
    private static double beforeLat = 0.0;
    private static double beforeLon = 0.0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_running);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.running);
        mapFragment.getMapAsync(this);


        //거리 초기화
        runDistance = 0.0;

        //거리 구하는 공식은 이런식으로 하면 될듯..
//        Location locA = new Location("point A");
//        locA.setLatitude(37.5582876);
//        locA.setLongitude(127.0001671);
//        Location locB = new Location("point B");
//        locB.setLatitude(37.557442);
//        locB.setLongitude(127.001449);
//
//        double distance = locA.distanceTo(locB);
//        Toast.makeText(activity_running.this, ""+distance, Toast.LENGTH_SHORT).show();


        // 하단 버튼 구성
        //본인 자신은 안 떠도 됨.
//        // running 탭 버튼
//        running_btn = findViewById(R.id.running_btn);
//        running_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),activity_running.class);
//                startActivity(intent);
//            }
//        });

        // home 버튼
        home_btn = findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_home.class);
                startActivity(intent);
                finish();
            }
        });

        //프로필 버튼
        profile_btn = findViewById(R.id.profile_btn);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_profile.class);
                startActivity(intent);
                finish();
            }
        });

        //객체화
        runningTime = (TextView) findViewById(R.id.time);
        start_btn = findViewById(R.id.runningStart);
        end_btn = findViewById(R.id.runningEnd);
        // runningDistance = findViewById(R.id.distance);

        start_btn.setOnClickListener(onClickListener);
        end_btn.setOnClickListener(onClickListener2);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.runningStart:
                    start_btn.setText("일시 중단");
                    startLocationService();
                    StaButton();
            }
        }
    };
    View.OnClickListener onClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String run_end = runningTime.getText().toString();
            // 여기서 서버로 달리기 정보 보내주면 됨.

            Log.i("testCode", run_end);
            Toast.makeText(activity_running.this, run_end, Toast.LENGTH_SHORT).show();
            status = END;
            handler.sendEmptyMessage(0);
        }
    };


    private void StaButton() {
        switch (status) {
            case INIT:
                //어플리케이션이 실행되고 나서 실제로 경과된 시간
                baseTime = SystemClock.elapsedRealtime();

                //핸들러 실행
                handler.sendEmptyMessage(0);

                //상태 변환
                status = RUN;
                break;
            case RUN:
                //핸들러 정지
                handler.removeMessages(0);

                //정지 시간 체크
                pauseTime = SystemClock.elapsedRealtime();

                start_btn.setText("다시 시작");

                //상태 변환
                status = PAUSE;
                break;
            case PAUSE:
                long reStart = SystemClock.elapsedRealtime();
                baseTime += (reStart - pauseTime);

                handler.sendEmptyMessage(0);

                start_btn.setText("일시 중단");

                status = RUN;
                break;
        }
    }

    private String getTime() {
        //경과된 시간 체크
        long nowTime = SystemClock.elapsedRealtime();
        //시스템이 부팅된 이후의 시간?
        long overTime = nowTime - baseTime;

        long h = overTime / 1000 / 3600;
        long m = overTime / 1000 / 60;
        long s = (overTime / 1000) % 60;

        String recTime = String.format("%02d:%02d:%02d", h, m, s);

        return recTime;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            runningTime.setText(getTime());
            if (status == END) {
                pauseTime = SystemClock.elapsedRealtime();
                baseTime = SystemClock.elapsedRealtime();
                runningTime.setText("00:00:00");
                start_btn.setText("달리기 시작");
                status = PAUSE;
                return;
            }
            handler.sendEmptyMessage(0);
        }
    };


    @Override // 우선은 동국대로 해놨는데, 자기 위치로 하게 바꿔야함.
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        //37.5582876,127.0001671 동국대
        LatLng latLng = new LatLng(37.5582876, 127.0001671);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("여기");
        markerOptions.snippet("여기엔 간단한 설명");
        googleMap.addMarker(markerOptions);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity_running.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void startLocationService() {
        //get manager instance
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //set listener
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, gpsListener);
    }
    private class GPSListener implements LocationListener{
        public void onLocationChanged(Location location){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double distance;
            if(beforeLat == 0.0){
                beforeLat = latitude;
            }
            if(beforeLon == 0.0){
                beforeLon = longitude;
            }
            String msg = "Latitude : " + latitude + "\nLongitude: "+ longitude;
            Log.i("", msg);
            Location before = new Location("beforeLat, beforeLon");
            before.setLatitude(beforeLat);
            before.setLongitude(beforeLon);
            Location current = new Location("latitude, longitude");
            current.setLatitude(latitude);
            current.setLongitude(longitude);
            distance = before.distanceTo(current);
            runDistance += distance;
            Log.i("누적 거리", ""+runDistance);
            beforeLat = latitude;
            beforeLon = longitude;
        }
        public void onProviderDisabled(String provider){

        }
        public void onProviderEnabled(String provider){

        }
        public void onStatusChanged(String provider, int status, Bundle extras){

        }
    }
}