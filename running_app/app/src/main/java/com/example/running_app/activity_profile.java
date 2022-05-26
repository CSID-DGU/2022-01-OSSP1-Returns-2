package com.example.running_app;

import android.app.Activity;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_profile extends AppCompatActivity{
    TextView userNickname, runCountKr, runCountNum, averPaceKr, averPaceNum, averKmKr, averKmNum;
//    EditText introduce;
    Button running_btn, home_btn, profile_btn;
    RetrofitInterface service;
    String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userNickname = findViewById(R.id.userNickname);
        runCountNum = findViewById(R.id.runCountNum);
        averPaceNum = findViewById(R.id.averPaceNum);
        averKmNum = findViewById(R.id.averKmNum);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        service = RetrofitClient.getClient().create(RetrofitInterface.class);
        userId = auto.getString("inputId", null);

        service.Profile(new ProfileData(userId)).enqueue(new Callback<ProfileResponse>(){
            //통신 성공시 호출
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response){
                ProfileResponse result = response.body();
//                Toast.makeText(activity_profile.this, result.getNickname(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("inputNickname", result.getNickname());
                autoLogin.commit();

                userNickname.setText(result.getNickname());
                runCountNum.setText(result.getRunning_count()+" 회");
                averPaceNum.setText(result.getAverage_face());
                averKmNum.setText(result.getAverage_distance()+" Km");
            }
            //통신 실패시 호출
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(activity_profile.this, "프로필 뷰 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("프로필 뷰 에러 발생", t.getMessage());
            }
        });

        //기입 항목 없앨 것 같음.
//        //기입 항목
//        introduce = (EditText) findViewById(R.id.introduce);
//        introduce.setText("EditText is changed.");


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

        // home 버튼
        home_btn = findViewById(R.id.home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),activity_home.class);
                startActivity(intent);
                finish();
            }
        });

        //프로필 화면에서 프로필이 다시 안 열려도 됨.
//        //프로필 버튼
//        profile_btn = findViewById(R.id.profile_btn);
//        profile_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), activity_profile.class);
//                startActivity(intent);
//            }
//        });
    }
}
