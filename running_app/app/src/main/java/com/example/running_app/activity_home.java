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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_home extends AppCompatActivity implements OnMapReadyCallback {

    static final String[] MachingProfileList = {"해당 매칭 정보"};
    private GoogleMap googleMap;
    Button matchingButton, newRunningButton, running_btn, home_btn, profile_btn;
    Dialog dialog01;

    Button logoutButton;
    String inputNickname;
    RetrofitInterface service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        inputNickname = auto.getString("inputNickname", null);
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
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
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

        //아직 새로운 매칭 버튼 누르면 페이지 열리는 거 구현 못함.


        // dialog 생성
        dialog01 = new Dialog(activity_home.this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.custom_dialog);


        //매칭 버튼
        matchingButton = findViewById(R.id.matchingButton);
        matchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog01();
            }
        });

        //러닝 생성 버튼
        newRunningButton = findViewById(R.id.newRunningButton);
        newRunningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog01();
            }
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
        LatLng latLng = new LatLng(37.5582876,127.0001671);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("여기");
        markerOptions.snippet("여기엔 간단한 설명");
        googleMap.addMarker(markerOptions);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
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

    public void showDialog01() {
        dialog01.show();
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
                //여기에 기능 구현
                dialog01.dismiss();
            }
        });

    }
}