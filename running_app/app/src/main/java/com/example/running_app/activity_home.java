package com.example.running_app;

import android.app.Dialog;
import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.Toast;

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
    Button matching_btn, newRunning_btn, running_btn, home_btn, profile_btn;
    Dialog dialogMarker, dialogNewRunning, dialogMatching;

    Button logout_btn;
    String inputNickname;
    RetrofitInterface service;

    public static double[] lat = new double[19];
    public static double[] lon = new double[19];
    public static String[] course_name_String = new String[19];
    public static String[] course_loc = new String[19];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        inputNickname = auto.getString("inputNickname", null);

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
        //logout버튼 구현
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

        // dialog 생성
        dialogMarker = new Dialog(activity_home.this);
        dialogMarker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMarker.setContentView(R.layout.custom_dialog);

        dialogNewRunning = new Dialog(activity_home.this);
        dialogNewRunning.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNewRunning.setContentView(R.layout.newrunning_dialog);

        dialogMatching = new Dialog(activity_home.this);
        dialogMatching.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMatching.setContentView(R.layout.matching_dialog);


        //매칭 버튼
        matching_btn = findViewById(R.id.matchingButton);
        matching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mDialog(dialogMatching); }
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
        LatLng latLng0 = new LatLng(37.5582876,127.0001671);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng0,11));
        MarkerOptions markerOptions0 = new MarkerOptions().position(latLng0).title("동국대");
        markerOptions0.snippet("동국대에서 뛰기");
        googleMap.addMarker(markerOptions0);


        //자동 마커 추가 기능
        for(int i=0; i<19; i++){
            LatLng latLng = new LatLng(lat[i], lon[i]);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(course_name_String[i]);
            googleMap.addMarker(markerOptions);
        }

//        //코스 1 37.627338,127.077278
//        LatLng latLng1 = new LatLng(37.627338,127.077278);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,11));
//        MarkerOptions markerOptions1 = new MarkerOptions().position(latLng1).title("코스1");
//        markerOptions1.snippet("서울특별시 노원구 공릉2동");
//        googleMap.addMarker(markerOptions1);
//
//        //코스 2 37.623154,127.077866
//        LatLng latLng2 = new LatLng(37.623154,127.077866);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng2,11));
//        MarkerOptions markerOptions2 = new MarkerOptions().position(latLng2).title("코스2");
//        markerOptions2.snippet("서울특별시 노원구 공릉2동");
//        googleMap.addMarker(markerOptions2);
//
//        //코스 3 37.537663,126.949066
//        LatLng latLng3 = new LatLng(37.537663,126.949066);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng3,11));
//        MarkerOptions markerOptions3 = new MarkerOptions().position(latLng3).title("코스3");
//        markerOptions3.snippet("서울특별시 마포구 도화동");
//        googleMap.addMarker(markerOptions3);
//
//        //코스 4 37.465908,127.093651
//        LatLng latLng4 = new LatLng(37.465908,127.093651);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng4,11));
//        MarkerOptions markerOptions4 = new MarkerOptions().position(latLng4).title("코스4");
//        markerOptions4.snippet("서울특별시 강남구 세곡동");
//        googleMap.addMarker(markerOptions4);
//
//        //코스 5 37.487965,127.048233
//        LatLng latLng5 = new LatLng(37.487965,127.048233);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng5,11));
//        MarkerOptions markerOptions5 = new MarkerOptions().position(latLng5).title("코스5");
//        markerOptions5.snippet("서울특별시 강남구 도곡1동");
//        googleMap.addMarker(markerOptions5);
//
//        //코스 6 37.554485,127.157036
//        LatLng latLng6 = new LatLng(37.554485,127.157036);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng6,11));
//        MarkerOptions markerOptions6 = new MarkerOptions().position(latLng6).title("코스6");
//        markerOptions6.snippet("서울특별시 강동구 상일동");
//        googleMap.addMarker(markerOptions6);
//
//        //코스 7 37.55201,127.142372
//        LatLng latLng7 = new LatLng(37.55201,127.142372);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng7,11));
//        MarkerOptions markerOptions7 = new MarkerOptions().position(latLng7).title("코스7");
//        markerOptions7.snippet("서울특별시 강동구 명일1동");
//        googleMap.addMarker(markerOptions7);
//
//        //코스 8 37.559193,126.824783
//        LatLng latLng8 = new LatLng(37.559193,126.824783);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng8,11));
//        MarkerOptions markerOptions8 = new MarkerOptions().position(latLng8).title("코스8");
//        markerOptions8.snippet("서울특별시 강서구 발산1동");
//        googleMap.addMarker(markerOptions8);
//
//        //코스 9 37.534462,126.838135
//        LatLng latLng9 = new LatLng(37.534462,126.838135);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng9,11));
//        MarkerOptions markerOptions9 = new MarkerOptions().position(latLng9).title("코스9");
//        markerOptions9.snippet("서울특별시 강서구 화곡1동");
//        googleMap.addMarker(markerOptions9);
//
//        //코스 10 37.53994,126.844475
//        LatLng latLng10 = new LatLng(37.53994,126.844475);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng10,11));
//        MarkerOptions markerOptions10 = new MarkerOptions().position(latLng10).title("코스10");
//        markerOptions10.snippet("서울특별시 강서구 화곡본동");
//        googleMap.addMarker(markerOptions10);
//
//        //코스 11 37.649628,127.078674
//        LatLng latLng11 = new LatLng(37.649628,127.078674);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng11,11));
//        MarkerOptions markerOptions11 = new MarkerOptions().position(latLng11).title("코스11");
//        markerOptions11.snippet("서울특별시 노원구 중계본동");
//        googleMap.addMarker(markerOptions11);
//
//        //코스 12 37.589363,127.051239
//        LatLng latLng12 = new LatLng(37.589363,127.051239);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng12,11));
//        MarkerOptions markerOptions12 = new MarkerOptions().position(latLng12).title("코스12");
//        markerOptions12.snippet("서울특별시 동대문구 회기동");
//        googleMap.addMarker(markerOptions12);
//
//        //코스 13 37.488174,126.976784
//        LatLng latLng13 = new LatLng(37.488174,126.976784);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng13,11));
//        MarkerOptions markerOptions13 = new MarkerOptions().position(latLng13).title("코스13");
//        markerOptions13.snippet("서울특별시 동작구 사당2동");
//        googleMap.addMarker(markerOptions13);
//
//        //코스 14 37.566872,126.896408
//        LatLng latLng14 = new LatLng(37.566872,126.896408);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng14,11));
//        MarkerOptions markerOptions14 = new MarkerOptions().position(latLng14).title("코스14");
//        markerOptions14.snippet("서울특별시 마포구 성산2동");
//        googleMap.addMarker(markerOptions14);
//
//        //코스 15 37.535442,126.961449
//        LatLng latLng15 = new LatLng(37.535442,126.961449);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng15,11));
//        MarkerOptions markerOptions15 = new MarkerOptions().position(latLng15).title("코스15");
//        markerOptions15.snippet("서울특별시 용산구 원효로1동");
//        googleMap.addMarker(markerOptions15);
//
//        //코스 16 37.530254,126.994637
//        LatLng latLng16 = new LatLng(37.530254,126.994637);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng16,11));
//        MarkerOptions markerOptions16 = new MarkerOptions().position(latLng16).title("코스16");
//        markerOptions16.snippet("서울특별시 용산구 이태원1동");
//        googleMap.addMarker(markerOptions16);
//
//        //코스 17 37.604881,127.087692
//        LatLng latLng17 = new LatLng(37.604881,127.087692);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng17,11));
//        MarkerOptions markerOptions17 = new MarkerOptions().position(latLng17).title("코스17");
//        markerOptions17.snippet("서울특별시 중랑구 상봉1동");
//        googleMap.addMarker(markerOptions17);
//
//        //코스 18 37.510139,127.127907
//        LatLng latLng18 = new LatLng(37.510139,127.127907);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng18,11));
//        MarkerOptions markerOptions18 = new MarkerOptions().position(latLng18).title("코스18");
//        markerOptions18.snippet("서울특별시 송파구 오금동");
//        googleMap.addMarker(markerOptions18);
//
//        //코스 19 37.510033,127.105072
//        LatLng latLng19 = new LatLng(37.510033,127.105072);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng19,11));
//        MarkerOptions markerOptions19 = new MarkerOptions().position(latLng19).title("코스19");
//        markerOptions19.snippet("서울특별시 송파구 잠실6동");
//        googleMap.addMarker(markerOptions19);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            checkLocationPermissionWithRationale();
        }

        // 마커 클릭에 대한 이벤트 설정
       googleMap.setOnMarkerClickListener(this);

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // 마커 클릭하면 실행
        showDialog(dialogMarker);

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

    public void showDialog(Dialog dialog01) {
        // 이게 지도에서 마커 누르면 나오는 팝업창
        // custom_dialog.xml 띄우면 됨
        dialog01.show();
        String courseName = "example course name";


        // 코스 이름 불러와서 courseName에 저장하는 코드 작성해야함


        // courseData 텍스트뷰 값 설정
        TextView courseData;
        courseData = dialog01.findViewById(R.id.runningCourseData);
        courseData.setText(courseName);

        // matching 리스트 설정
        ListView matchingList = dialog01.findViewById(R.id.list_matching);
        // 더미 매칭 리스트
        List<String> list = new ArrayList<>();
        list.add("matching1");
        list.add("matching2");
        list.add("matching3");
        list.add("matching4");
        list.add("matching5");
        list.add("matching6");
        // ~더미 매칭 리스트

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        matchingList.setAdapter(arrayAdapter);

        matchingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                // 매칭 리스트에서 매칭 클릭 시 실행
                // ListView에서 버튼 넣기는 힘들어보임
                // 그냥 매칭 클릭하면 참여하는거로 할지 고민중..
                /*
                    Matching1  20:00  2/4
                    ----------------------
                    Matching2  20:30  1/4
                    ----------------------
                 */
                // 이런 느낌으로 리스트 만들어두면 여기서 선택해도 괜찮을듯
                String matchName = (String)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "select: " + matchName, Toast.LENGTH_SHORT).show();
            }
        });

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


        // 나가기 버튼
        Button exitBtn = dialog01.findViewById(R.id.btn_exit);

        exitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 나가기 버튼
                //
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

    public void mDialog(Dialog mDialog) {
        mDialog.show();

        int runTime = mDialog.findViewById(R.id.runTime).getId();   // 소요 시간

        String gender, man, woman, both;
        man = mDialog.findViewById(R.id.signMan).toString();
        woman = mDialog.findViewById(R.id.signWoman).toString();
        both = mDialog.findViewById(R.id.signBoth).toString();

        if(man.isEmpty()){
            if(woman.isEmpty()) { gender = both;}
            else { gender = woman; }
        }
        else{ gender = man; }                           // gender : 성별 값

        int year, month, day, hour, min;
        DatePicker datePicker = (DatePicker)mDialog.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)mDialog.findViewById(R.id.timePicker);
        Button matchBtn = mDialog.findViewById(R.id.btn_matching);

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



        matchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 여기서 api 로 runTime, timestamp 넘겨주면 됨

                mDialog.dismiss();
            }
        });
    }
    public void rDialog(Dialog rDialog) {
        rDialog.show();
        CheckBox manCheck, womanCheck, bothCheck;
        //이런 형식으로
//        userNickname = findViewById(R.id.userNickname);

        EditText et_runTime = rDialog.findViewById(R.id.runTime);// 소요 시간
        String runTime = et_runTime.getText().toString();
        String courseName = rDialog.findViewById(R.id.courseName).toString();    // 코스 이름

        String gender, man, woman, both;
        manCheck = rDialog.findViewById(R.id.signMan);
        womanCheck = rDialog.findViewById(R.id.signWoman);
        bothCheck = rDialog.findViewById(R.id.signBoth);      // gender

        man = manCheck.getText().toString();
        woman = womanCheck.getText().toString();
        both = bothCheck.getText().toString();

        if(man.isEmpty()){
            if(woman.isEmpty()) { gender = null;}
            else { gender = woman; }
        }
        else{ gender = man; }

        int year, month, day, hour, min;
        DatePicker datePicker = (DatePicker)rDialog.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker)rDialog.findViewById(R.id.timePicker);
        Button newRunningBtn = rDialog.findViewById(R.id.btn_newRunning);

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
//        Long timestamp = calendar.getTimeInMillis();// 출발 시간 타임스탬프
//        Log.i("testCode", ""+timestamp);
//        timestamp.toString();
        String ts = "2022-05-28 18:00:00.0";
        java.sql.Timestamp realTs = java.sql.Timestamp.valueOf(ts);

        newRunningBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 여기서 데이터 값 api로 보내주면 됨
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                service = RetrofitClient.getClient().create(RetrofitInterface.class);

                service.Create(new MakeMatchingRoomData(auto.getString("inputNickname", null), realTs, Integer.parseInt(runTime), gender, 37.5582876,127.0001671)).enqueue(new Callback<MakeMatchingRoomResponse>() {
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
}