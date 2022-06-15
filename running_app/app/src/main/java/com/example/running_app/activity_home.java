package com.example.running_app;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_home extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    static final String[] MachingProfileList = {"해당 매칭 정보"};
    private GoogleMap googleMap;
    Button status_btn, matching_btn, newRunning_btn, running_btn, home_btn, profile_btn;
    Dialog dialogStatus, dialogMarker, dialogNewRunning, dialogMatching;

    Button logout_btn;
    String inputNickname;
    RetrofitInterface service;
    public static String ts = "2022-05-28 18:00:00.0";

    public static double[] lat = new double[19];
    public static double[] lon = new double[19];
    public static String[] course_name_String = new String[19];
    public static String[] course_loc = new String[19];
    public static int room_length;
    public static int[] room_id = new int[10];
    public static String[] departure_time = new String[10];
    public static String[] mate_gender = new String[10];
    public static boolean flag = false;
    public static boolean sDialogFlag = false;
    public static String gender = "";
    public static int select_room_id = 0;
    public static int room_flag = 0;
    public static String room_departure_time="";
    public static int room_courseNo = 0;
    public static int room_running_time = 0;
    public static String room_mate_gender = "";
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    public static int recommendNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        inputNickname = auto.getString("inputNickname", null);

        service = RetrofitClient.getClient().create(RetrofitInterface.class);
        String userId = auto.getString("inputId", null);

        service.Profile(new ProfileData(userId)).enqueue(new Callback<ProfileResponse>(){
            //통신 성공시 호출
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response){
                ProfileResponse result = response.body();
                Toast.makeText(activity_home.this, ""+result.getRoom_id(), Toast.LENGTH_SHORT).show();
                room_flag = result.getRoom_id();
                Log.i("Ts", ""+room_flag);

                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("room_flag", ""+room_flag);
                autoLogin.commit();
            }
            //통신 실패시 호출
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(activity_home.this, "로드 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로드 에러 발생", t.getMessage());
            }
        });

        service = RetrofitClient.getClient().create(RetrofitInterface.class);
        service.GetCourse().enqueue(new Callback<CourseResponse>() {
            @Override
            public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                CourseResponse result = response.body();
                Course[] courseData = result.getCourse();

                for(int i=0; i<courseData.length; i++){
                    double temp1 = courseData[i].getLatitude();
                    double temp2 = courseData[i].getLongitude();
                    String temp3 = courseData[i].getCourseNo();
                    lat[i] = temp1;
                    lon[i] = temp2;
                    course_name_String[i] = temp3;
                }
                onMapReady(googleMap);
                Toast.makeText(activity_home.this, ""+courseData.length+"개 코스 정보 로드", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CourseResponse> call, Throwable t) {
                Toast.makeText(activity_home.this, "코스 로드 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("코스 로드 에러 발생", t.getMessage());
            }
        });
//
//        for (int i=0; i<19; i++){
//            Log.i("testCode",lat[i]+" "+lon[i]+" "+course_name_String[i]);
//        }
/*
        // home 화면 리스트 삭제
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,MachingProfileList);

        ListView listview = (ListView) findViewById(R.id.MachingUserList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String strText = (String) parent.getItemAtPosition(position);
            }
        });
*/
/*      //logout버튼 구현
        logout_btn = findViewById(R.id.logoutButton);
        logout_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(activity_home.this, activity_login.class);
                startActivity(intent);

                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(activity_home.this, "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
*/
        // dialog 생성
        dialogStatus = new Dialog(activity_home.this);
        dialogStatus.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogStatus.setContentView(R.layout.activity_matching);

        dialogMarker = new Dialog(activity_home.this);
        dialogMarker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMarker.setContentView(R.layout.custom_dialog);

        dialogNewRunning = new Dialog(activity_home.this);
        dialogNewRunning.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNewRunning.setContentView(R.layout.newrunning_dialog);

        dialogMatching = new Dialog(activity_home.this);
        dialogMatching.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMatching.setContentView(R.layout.matching_dialog);


        status_btn = findViewById(R.id.btn_match);
        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { sDialog(dialogStatus); }
        });

        newRunning_btn = findViewById(R.id.newRunningButton);
        newRunning_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { rDialog(dialogNewRunning); }
        });

        //매칭 버튼
        matching_btn = findViewById(R.id.matchingButton);
        matching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mDialog(dialogMatching, dialogStatus); }
        });

        //러닝 생성 버튼
        newRunning_btn = findViewById(R.id.newRunningButton);
        newRunning_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { rDialog(dialogNewRunning); }
        });


        // 하단 버튼 구성
        // running 탭 버튼
        running_btn = findViewById(R.id.running_btn);
        running_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),activity_running.class);
                startActivity(intent);
                finish();
            }
        });

        //자기 자신 누르면 안 떠도 됨.
//        // home 버튼
//        home_btn = findViewById(R.id.home_btn);
//        home_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),activity_home.class);
//                startActivity(intent);
//            }
//        });

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
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        //37.5582876,127.0001671 동국대
//        LatLng latLng0 = new LatLng(37.5582876,127.0001671);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng0,11));
//        MarkerOptions markerOptions0 = new MarkerOptions().position(latLng0).title("동국대");
//        markerOptions0.snippet("위도 : 37.5582876, 경도 : 127.0001671");
//        googleMap.addMarker(markerOptions0);

//        //자동 마커 추가 기능
//        for(int i=0; i<19; i++){
//            LatLng latLng = new LatLng(lat[i], lon[i]);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(course_name_String[i]);
//            googleMap.addMarker(markerOptions);
//            String latlon = String.valueOf(lat[i])+" "+String.valueOf(lon[i]);
//            markerOptions.snippet(latlon);
//        }

        //코스 1 37.627338,127.077278
        LatLng latLng1 = new LatLng(37.627338,127.077278);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,11));
        MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).title("코스1");
        markerOptions1.snippet("위도 : 37.627338, 경도 : 127.077278");
        googleMap.addMarker(markerOptions1);

        //코스 2 37.623154,127.077866
        LatLng latLng2 = new LatLng(37.623154,127.077866);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2,11));
        MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title("코스2");
        markerOptions2.snippet("위도 : 37.623154, 경도 : 127.077866");
        googleMap.addMarker(markerOptions2);

        //코스 3 37.537663,126.949066
        LatLng latLng3 = new LatLng(37.537663,126.949066);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng3,11));
        MarkerOptions markerOptions3 = new MarkerOptions().position(latLng3).title("코스3");
        markerOptions3.snippet("위도 : 37.537663, 경도 : 126.949066");
        googleMap.addMarker(markerOptions3);

        //코스 4 37.465908,127.093651
        LatLng latLng4 = new LatLng(37.465908,127.093651);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng4,11));
        MarkerOptions markerOptions4 = new MarkerOptions().position(latLng4).title("코스4");
        markerOptions4.snippet("위도 : 37.465908, 경도 : 127.093651");
        googleMap.addMarker(markerOptions4);

        //코스 5 37.487965,127.048233
        LatLng latLng5 = new LatLng(37.487965,127.048233);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng5,11));
        MarkerOptions markerOptions5 = new MarkerOptions().position(latLng5).title("코스5");
        markerOptions5.snippet("위도 : 37.487965, 경도 : 127.048233");
        googleMap.addMarker(markerOptions5);

        //코스 6 37.554485,127.157036
        LatLng latLng6 = new LatLng(37.554485,127.157036);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng6,11));
        MarkerOptions markerOptions6 = new MarkerOptions().position(latLng6).title("코스6");
        markerOptions6.snippet("위도 : 37.554485, 경도 : 127.157036");
        googleMap.addMarker(markerOptions6);

        //코스 7 37.55201,127.142372
        LatLng latLng7 = new LatLng(37.55201,127.142372);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng7,11));
        MarkerOptions markerOptions7 = new MarkerOptions().position(latLng7).title("코스7");
        markerOptions7.snippet("위도 : 37.55201, 경도 : 127.142372");
        googleMap.addMarker(markerOptions7);

        //코스 8 37.559193,126.824783
        LatLng latLng8 = new LatLng(37.559193,126.824783);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng8,11));
        MarkerOptions markerOptions8 = new MarkerOptions().position(latLng8).title("코스8");
        markerOptions8.snippet("위도 : 37.559193, 경도 : 126.824783");
        googleMap.addMarker(markerOptions8);

        //코스 9 37.534462,126.838135
        LatLng latLng9 = new LatLng(37.534462,126.838135);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng9,11));
        MarkerOptions markerOptions9 = new MarkerOptions().position(latLng9).title("코스9");
        markerOptions9.snippet("위도 : 37.534462, 경도 : 126.838135");
        googleMap.addMarker(markerOptions9);

        //코스 10 37.53994,126.844475
        LatLng latLng10 = new LatLng(37.53994,126.844475);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng10,11));
        MarkerOptions markerOptions10 = new MarkerOptions().position(latLng10).title("코스10");
        markerOptions10.snippet("위도 : 37.53994, 경도 : 126.844475");
        googleMap.addMarker(markerOptions10);

        //코스 11 37.649628,127.078674
        LatLng latLng11 = new LatLng(37.649628,127.078674);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng11,11));
        MarkerOptions markerOptions11 = new MarkerOptions().position(latLng11).title("코스11");
        markerOptions11.snippet("위도 : 37.649628, 경도 : 127.078674");
        googleMap.addMarker(markerOptions11);

        //코스 12 37.589363,127.051239
        LatLng latLng12 = new LatLng(37.589363,127.051239);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng12,11));
        MarkerOptions markerOptions12 = new MarkerOptions().position(latLng12).title("코스12");
        markerOptions12.snippet("위도 : 37.589363, 경도 : 127.051239");
        googleMap.addMarker(markerOptions12);

        //코스 13 37.488174,126.976784
        LatLng latLng13 = new LatLng(37.488174,126.976784);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng13,11));
        MarkerOptions markerOptions13 = new MarkerOptions().position(latLng13).title("코스13");
        markerOptions13.snippet("위도 : 37.488174, 경도 : 126.976784");
        googleMap.addMarker(markerOptions13);

        //코스 14 37.566872,126.896408
        LatLng latLng14 = new LatLng(37.566872,126.896408);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng14,11));
        MarkerOptions markerOptions14 = new MarkerOptions().position(latLng14).title("코스14");
        markerOptions14.snippet("위도 : 37.566872, 경도 : 126.896408");
        googleMap.addMarker(markerOptions14);

        //코스 15 37.535442,126.961449
        LatLng latLng15 = new LatLng(37.535442,126.961449);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng15,11));
        MarkerOptions markerOptions15 = new MarkerOptions().position(latLng15).title("코스15");
        markerOptions15.snippet("위도 : 37.535442, 경도 : 126.961449");
        googleMap.addMarker(markerOptions15);

        //코스 16 37.530254,126.994637
        LatLng latLng16 = new LatLng(37.530254,126.994637);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng16,11));
        MarkerOptions markerOptions16 = new MarkerOptions().position(latLng16).title("코스16");
        markerOptions16.snippet("위도 : 37.530254, 경도 : 126.994637");
        googleMap.addMarker(markerOptions16);

        //코스 17 37.604881,127.087692
        LatLng latLng17 = new LatLng(37.604881,127.087692);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng17,11));
        MarkerOptions markerOptions17 = new MarkerOptions().position(latLng17).title("코스17");
        markerOptions17.snippet("위도 : 37.604881, 경도 : 127.087692");
        googleMap.addMarker(markerOptions17);

        //코스 18 37.510139,127.127907
        LatLng latLng18 = new LatLng(37.510139,127.127907);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng18,11));
        MarkerOptions markerOptions18 = new MarkerOptions().position(latLng18).title("코스18");
        markerOptions18.snippet("위도 : 37.510139, 경도 : 127.127907");
        googleMap.addMarker(markerOptions18);

        //코스 19 37.510033,127.105072
        LatLng latLng19 = new LatLng(37.510033,127.105072);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng19,11));
        MarkerOptions markerOptions19 = new MarkerOptions().position(latLng19).title("코스19");
        markerOptions19.snippet("위도 : 37.510033, 경도 : 127.105072");
        googleMap.addMarker(markerOptions19);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }

        // 마커 클릭에 대한 이벤트 설정
       googleMap.setOnMarkerClickListener(this);

        startLocationService();
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void startLocationService() {
        //get manager instance
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //set listener
        activity_home.GPSListener gpsListener = new activity_home.GPSListener();
        long minTime = 5000;
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

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(activity_home.this, ""+latitude, Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(activity_home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            } else {
                checkLocationPermissionWithRationale();
            }
        }
        public void onProviderDisabled(String provider){

        }
        public void onProviderEnabled(String provider){

        }
        public void onStatusChanged(String provider, int status, Bundle extras){

        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // 마커 클릭하면 실행
        String snippetData = marker.getSnippet();
        String courseTitle = marker.getTitle().substring(2);

        String sniData = snippetData;
        String strLat;
        String strLon;

        for(int i=0; i<lat.length; i++){
            strLat = String.valueOf(lat[i]);
            strLon = String.valueOf(lon[i]);
        }

        int cno = Integer.parseInt(courseTitle);
        service = RetrofitClient.getClient().create(RetrofitInterface.class);
        service.GetRoom(cno).enqueue(new Callback<SearchActivateResponse>() {
            @Override
            public void onResponse(Call<SearchActivateResponse> call, Response<SearchActivateResponse> response) {
                SearchActivateResponse result = response.body();
                SearchActivateRoom[] searchData = result.getCourse();
                room_length = searchData.length;

                for(int i=0; i< searchData.length; i++){
                    room_id[i] = searchData[i].getRoom_id();
                    departure_time[i] = searchData[i].getDeparture_time();
                    mate_gender[i] = searchData[i].getMate_gender();
                }
            }

            @Override
            public void onFailure(Call<SearchActivateResponse> call, Throwable t) {
                Toast.makeText(activity_home.this, "매칭방 로드 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("매칭방 로드 에러 발생", t.getMessage());
            }
        });
            showDialog(dialogMarker, snippetData);

        return false;
    }


    private void checkLocationPermissionWithRationale() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("위치정보")
                        .setMessage("이 앱을 사용하기 위해서는 위치정보에 접근이 필요합니다. 위치정보 접근을 허용하여 주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity_home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
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

    public void showDialog(@NonNull Dialog dialog01, String snippetData) {
        // 이게 지도에서 마커 누르면 나오는 팝업창
        // custom_dialog.xml 띄우면 됨
        if(!flag){
            flag = true;
            return;
        }
        dialog01.show();


//        dialogMarker = new Dialog(activity_home.this);
//        dialogMarker.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogMarker.setContentView(R.layout.custom_dialog);

        // courseData 텍스트뷰 값 설정
        TextView courseData;
        courseData = dialog01.findViewById(R.id.runningCourseData);

//        String courseName = "example course name";
        String sniData = snippetData;
        String strLat;
        String strLon;

        for(int i=0; i<lat.length; i++){
             strLat = String.valueOf(lat[i]);
             strLon = String.valueOf(lon[i]);
             if(sniData.contains(strLat)){
                 courseData.setText(course_name_String[i]);
             }
        }

        try{
            String str_cno = courseData.getText().toString().substring(6);
            int cno = Integer.parseInt(str_cno);

            service = RetrofitClient.getClient().create(RetrofitInterface.class);
            service.GetRoom(cno).enqueue(new Callback<SearchActivateResponse>() {
                @Override
                public void onResponse(Call<SearchActivateResponse> call, Response<SearchActivateResponse> response) {
                    SearchActivateResponse result = response.body();
                    SearchActivateRoom[] searchData = result.getCourse();
                    room_length = searchData.length;

                    for(int i=0; i< searchData.length; i++){
                        room_id[i] = searchData[i].getRoom_id();
                        departure_time[i] = searchData[i].getDeparture_time();
                        mate_gender[i] = searchData[i].getMate_gender();
                    }
                }

                @Override
                public void onFailure(Call<SearchActivateResponse> call, Throwable t) {
                    Toast.makeText(activity_home.this, "매칭방 로드 에러 발생", Toast.LENGTH_SHORT).show();
                    Log.e("매칭방 로드 에러 발생", t.getMessage());
                }
            });
        } catch(NumberFormatException e){

        }catch(Exception e){

        }

//        if(!flag){
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    flag = true;
//                    dialog01.dismiss();
//                }
//            }, 0);
//        }

        // matching 리스트 설정
        ListView matchingList = dialog01.findViewById(R.id.list_matching);
        // 더미 매칭 리스트
        List<String> list = new ArrayList<>();
//        list.add("matching1");
        for(int i=0; i<room_length; i++){
            String addStr = "RoomID:"+String.valueOf(room_id[i])+" 시작:"+departure_time[i]+","+mate_gender[i];
            list.add(addStr);
        }
        // ~더미 매칭 리스트

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        matchingList.setAdapter(arrayAdapter);

//        matchingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                // 매칭 리스트에서 매칭 클릭 시 실행
//                // ListView에서 버튼 넣기는 힘들어보임
//                // 그냥 매칭 클릭하면 참여하는거로 할지 고민중..
//                /*
//                    Matching1  20:00  2/4
//                    ----------------------
//                    Matching2  20:30  1/4
//                    ----------------------
//                */
//                // 이런 느낌으로 리스트 만들어두면 여기서 선택해도 괜찮을듯
//
//                String matchName = (String)parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(), "select: " + matchName, Toast.LENGTH_SHORT).show();
//            }
//        });

        // 기능 구현하기

//        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
//        inputNickname = auto.getString("inputNickname", null);
//
//        service = RetrofitClient.getClient().create(RetrofitInterface.class);
//        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        service.Create(new MakeMatchingRoomData(inputNickname,ts,1,"man",1,37.5582876,127.0001671)).enqueue(new Callback<MakeMatchingRoomResponse>() {
//            @Override
//            public void onResponse(Call<MakeMatchingRoomResponse> call, Response<MakeMatchingRoomResponse> response) {
//                MakeMatchingRoomResponse result = response.body();
//                Toast.makeText(activity_home.this, result.getMsg(), Toast.LENGTH_SHORT).show();
//                TextView courseData = dialog01.findViewById(R.id.courseData);
//
//                courseData.setText("러닝 코스 정보\n"+"Test : "+inputNickname+" "+result.getMsg());
//            }
//
//            @Override
//            public void onFailure(Call<MakeMatchingRoomResponse> call, Throwable t) {
//                Toast.makeText(activity_home.this, "매칭방 생성 에러 발생", Toast.LENGTH_SHORT).show();
//                Log.e("매칭방 생성 에러 발생", t.getMessage());
//            }
//        });


//        // 확인 버튼
//        Button ok_btn = dialog01.findViewById(R.id.btn_ok);
//        ok_btn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //ListView matchName = v.findViewById(R.id.list_matching);
//                dialog01.dismiss();
//            }
//        });

//        // 확인버튼
//        Button ok_btn = dialog01.findViewById(R.id.btn_ok);
//        ok_btn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 여기서 선택한 리스트 데이터 처리
//
//                Log.i("Ts", ""+select_room_id);
//
//
//                flag=false;
//                dialog01.dismiss();
//            }
//        });

        // 나가기 버튼
        Button exit_btn = dialog01.findViewById(R.id.btn_exit);
        exit_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                dialog01.dismiss();
            }
        });
        /*
        // home 화면 리스트 삭제
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,MachingProfileList);

        ListView listview = (ListView) findViewById(R.id.MachingUserList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String strText = (String) parent.getItemAtPosition(position);
            }
        });
*/
    }

    public void mDialog(@NonNull Dialog mDialog, Dialog sDialog) {
        mDialog.show();
        CheckBox manCheck, womanCheck, bothCheck;
        int runTime = mDialog.findViewById(R.id.runTime).getId();   // 소요 시간

        manCheck = mDialog.findViewById(R.id.signMan);
        womanCheck = mDialog.findViewById(R.id.signWoman);
        bothCheck = mDialog.findViewById(R.id.signBoth);


//        int year, month, day, hour, min;
        DatePicker datePicker = (DatePicker)mDialog.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)mDialog.findViewById(R.id.timePicker);
        Button matchBtn = mDialog.findViewById(R.id.btn_matching);
/*
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day= datePicker.getDayOfMonth();
        hour = timePicker.getHour();
        min = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        Long timestamp = calendar.getTimeInMillis();         // 출발 시간 타임스탬프
*/

        matchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 캘린더 값 설정
                int year, month, day, hour, min;

                year = datePicker.getYear();
                month = datePicker.getMonth();
                day= datePicker.getDayOfMonth();
                hour = timePicker.getHour();
                min = timePicker.getMinute();

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);

                String sYear, sMonth, sDay, sHour, sMin;
                Log.i("Get Year", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
                sYear = Integer.toString(year);
                sMonth = Integer.toString(month+1);
                sDay = Integer.toString(day);
                sHour = Integer.toString(hour);
                sMin = Integer.toString(min);

                String ts = sYear+"-"+0+sMonth+"-"+sDay+" "+sHour+":"+sMin+":00";

                EditText et_runTime = mDialog.findViewById(R.id.runTime);// 소요 시간
                String runTime = et_runTime.getText().toString();

                if (manCheck.isChecked()){
                    gender = "남자만";
                }
                if (womanCheck.isChecked()){
                    gender = "여자만";
                }
                if (bothCheck.isChecked()){
                    gender = "";
                }

                // 여기서 api 로 runTime, timestamp 넘겨주면 됨
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                service = RetrofitClient.getClient().create(RetrofitInterface.class);
                service.MatchingResult(new MatchingData(inputNickname,ts,Integer.parseInt(runTime),gender, latitude, longitude)).enqueue(new Callback<MatchingResponse>() {
                    @Override
                    public void onResponse(Call<MatchingResponse> call, Response<MatchingResponse> response) {
                        MatchingResponse result = response.body();
                        Toast.makeText(activity_home.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        sDialog.show();
                    }

                    @Override
                    public void onFailure(Call<MatchingResponse> call, Throwable t) {
                        Toast.makeText(activity_home.this, "매칭 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("매칭 에러 발생", t.getMessage());
                    }
                });
                mDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),activity_home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void rDialog(@NonNull Dialog rDialog) {
        rDialog.show();
        CheckBox manCheck, womanCheck, bothCheck;
        Button recommend, select;

        //이런 형식으로
//      userNickname = findViewById(R.id.userNickname);
        recommend = rDialog.findViewById(R.id.btn_recommend);
        select = rDialog.findViewById(R.id.btn_select);

        EditText et_runTime = rDialog.findViewById(R.id.runTime);// 소요 시간
        String runTime = et_runTime.getText().toString();
        TextView courseName = rDialog.findViewById(R.id.courseName);    // 코스 이름

        recommend.setOnClickListener(new OnClickListener() {
            // 추천코스 버튼 클릭
            @Override
            public void onClick(View v) {
                // 추천 알고리즘으로 해당 유저 기준으로 제일 추천도 높은 코스 바로 올려주면 될듯
                service.GetRecommend(new RecommendData(inputNickname, latitude, longitude)).enqueue(new Callback<RecommendResponse>() {
                    @Override
                    public void onResponse(Call<RecommendResponse> call, Response<RecommendResponse> response) {
                        RecommendResponse result = response.body();
                        recommendNo = result.getData();
                    }

                    @Override
                    public void onFailure(Call<RecommendResponse> call, Throwable t) {
                        Toast.makeText(activity_home.this, "추천 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("추천 에러 발생", t.getMessage());
                    }
                });

                String course = "";     // 여기에 코스 이름 저장
                if(recommendNo>=10){
                    course = ""+recommendNo;
                }else{
                    course = "0"+recommendNo;
                }

                courseName.setText(course);
            }
        });

        select.setOnClickListener(new OnClickListener() {
            // 직접선택 버튼 클릭

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity_home.this);
                alertBuilder.setTitle("Select Matching");

                // 더미 List Adapter 생성
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity_home.this, android.R.layout.select_dialog_item);
                //
                adapter.add("01.경춘선공원, 노원구");
                adapter.add("02.공릉공원, 노원구");
                adapter.add("03.대현공원, 마포구");
                adapter.add("04.세곡천, 강남구");
                adapter.add("05.도곡공원, 강남구");
                adapter.add("06.명일공원, 강동구");
                adapter.add("07.명우공원, 강동구");
                adapter.add("08.마곡하늬공원, 강서구");
                adapter.add("09.월정공원, 강서구");
                adapter.add("10.까치산근린공원, 강서구");
                adapter.add("11.과기대트랙, 노원구");
                adapter.add("12.홍릉근린공원, 동대문구");
                adapter.add("13.삼일공원, 동작구");
                adapter.add("14.월드컵공원, 마포구");
                adapter.add("15.경의선숲길, 용산구");
                adapter.add("16.용산공원, 용산구");
                adapter.add("17.중랑천, 중랑구");
                adapter.add("18.올림픽공원, 송파구");
                adapter.add("19.석촌호수, 송파구");

                //

                // 버튼 생성
                alertBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Adapter 셋팅
                alertBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //
                        courseName.setText(adapter.getItem(id));
                    }
                });
                alertBuilder.show();
            }
        });

        String man, woman, both;
        manCheck = rDialog.findViewById(R.id.signMan);
        womanCheck = rDialog.findViewById(R.id.signWoman);
        bothCheck = rDialog.findViewById(R.id.signBoth);      // gender

        man = manCheck.getText().toString();
        woman = womanCheck.getText().toString();
        both = bothCheck.getText().toString();

//        int year, month, day, hour, min;
        DatePicker datePicker = (DatePicker)rDialog.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)rDialog.findViewById(R.id.timePicker);
        Button newRunningBtn = rDialog.findViewById(R.id.btn_newRunning);
/*
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day= datePicker.getDayOfMonth();
        hour = timePicker.getHour();
        min = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        Log.i("Get Year", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
*/
//        Long timestamp = calendar.getTimeInMillis();// 출발 시간 타임스탬프
//        Log.i("testCode", ""+timestamp);
//        timestamp.toString();

        newRunningBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 캘린더 값 설정
                int year, month, day, hour, min;

                year = datePicker.getYear();
                month = datePicker.getMonth();
                day= datePicker.getDayOfMonth();
                hour = timePicker.getHour();
                min = timePicker.getMinute();

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, min);
                calendar.set(Calendar.SECOND, 0);

                String sYear, sMonth, sDay, sHour, sMin;
                Log.i("Get Hour", Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
                sYear = Integer.toString(year);
                sMonth = Integer.toString(month);
                sDay = Integer.toString(day);
                sHour = Integer.toString(hour);
                sMin = Integer.toString(min);

                String ts = sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMin+":00";

                if (manCheck.isChecked()){
                    gender = "남자만";
                }
                if (womanCheck.isChecked()){
                    gender = "여자만";
                }
                if (bothCheck.isChecked()){
                    gender = "";
                }

                // 여기서 데이터 값 api로 보내주면 됨
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                service = RetrofitClient.getClient().create(RetrofitInterface.class);
                String str_cno = courseName.getText().toString().substring(0,2);
                int cNo = Integer.parseInt(str_cno); // 위도, 경도 배열에 접근하기 위한 인덱스

                service.Create(new MakeMatchingRoomData(auto.getString("inputNickname", null), ts, Integer.parseInt(runTime), gender, lat[cNo-1],lon[cNo-1])).enqueue(new Callback<MakeMatchingRoomResponse>() {
                    @Override
                    public void onResponse(Call<MakeMatchingRoomResponse> call, Response<MakeMatchingRoomResponse> response) {
                        MakeMatchingRoomResponse result = response.body();
                        Toast.makeText(activity_home.this, "매칭방 생성", Toast.LENGTH_SHORT).show();
                        Log.i("매칭방 생성", result.getMsg());
                    }

                    @Override
                    public void onFailure(Call<MakeMatchingRoomResponse> call, Throwable t) {
                        Toast.makeText(activity_home.this, "매칭방 생성 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("매칭방 생성 에러 발생", t.getMessage());
                    }
                });

                rDialog.dismiss();
            }
        });
    }

    public void sDialog(@NonNull Dialog sDialog) {
        sDialog.show();

        String course, departure, run, gender;
        TextView tv_course, tv_departure, tv_run, tv_gender;
        Button btn_cancel, btn_ok;

        tv_course = sDialog.findViewById(R.id.tv_course);
        tv_departure = sDialog.findViewById(R.id.tv_departure);
        tv_run = sDialog.findViewById(R.id.tv_runTime);
        tv_gender = sDialog.findViewById(R.id.tv_gender);
        btn_cancel = sDialog.findViewById(R.id.btn_cancel);
        btn_ok = sDialog.findViewById(R.id.btn_ok);
        Log.i("Ts", ""+room_flag);
        if(room_flag==0){
            tv_course.setText("아직 매칭 안됐습니다.");
            tv_departure.setText("아직 매칭 안됐습니다.");
            tv_run.setText("아직 매칭 안됐습니다.");
            tv_gender.setText("아직 매칭 안됐습니다.");
        } else{ // 여기서 api호출. room_id로 방 정보 가져오기
            try{
                service = RetrofitClient.getClient().create(RetrofitInterface.class);
                service.GetByRoomId(room_flag).enqueue(new Callback<GetRoomResponse>() {
                    @Override
                    public void onResponse(Call<GetRoomResponse> call, Response<GetRoomResponse> response) {
                        GetRoomResponse result = response.body();
                        room_courseNo = result.getCourseNo();
                        room_departure_time = result.getDeparture_time();
                        room_running_time = result.getRunning_time();
                        room_mate_gender = result.getMate_gender();
                        // ==각각의 String 값에 코스이름, 출발시간, 달리기시간, 성별 받아오기==
//                        course = ""+room_courseNo;
//                        departure = room_departure_time;
//                        run = room_running_time+"분";
//                        gender = room_mate_gender;

                        // Textview 값 변경하기
                        tv_course.setText(""+room_courseNo+" (Room ID : "+room_flag+")");
                        tv_departure.setText(room_departure_time);
                        tv_run.setText(room_running_time+"분");
                        Log.i("Ts", room_mate_gender);
                        if(room_mate_gender == "남자만"){
                            tv_gender.setText(room_mate_gender);
                        }else if(room_mate_gender == "여자만"){
                            tv_gender.setText(room_mate_gender);
                        }else{
                            tv_gender.setText("상관없음");
                        }
                        Toast.makeText(activity_home.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<GetRoomResponse> call, Throwable t) {
                        Toast.makeText(activity_home.this, "매칭방 로드 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("매칭방 로드 에러 발생", t.getMessage());
                    }
                });
            } catch (Exception e){

            }

        }


        btn_cancel.setOnClickListener(new OnClickListener() {
            // 매칭 취소 버튼 클릭
            // 참여하고 있는 매칭에서 나오면 됨
            @Override
            public void onClick(View v) {
                Toast.makeText(activity_home.this, "매칭 취소", Toast.LENGTH_SHORT).show();
                // 이 유저의 룸 아이디 지워버림
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                service = RetrofitClient.getClient().create(RetrofitInterface.class);
                String userId = auto.getString("inputId", null);

                service.DeleteRoomId(new ProfileData(userId)).enqueue(new Callback<ProfileResponse>(){
                    //통신 성공시 호출
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response){
                        ProfileResponse result = response.body();
                        Toast.makeText(activity_home.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    //통신 실패시 호출
                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        Toast.makeText(activity_home.this, "삭제 에러 발생", Toast.LENGTH_SHORT).show();
                        Log.e("삭제 에러 발생", t.getMessage());
                    }
                });
                if(room_flag==0){
                    sDialog.dismiss();
                }else{
                    sDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),activity_home.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        btn_ok.setOnClickListener(new OnClickListener() {
            // 그냥 확인(=나가기) 버튼
            @Override
            public void onClick(View v) {
                sDialog.dismiss();

            }
        });
    }
}