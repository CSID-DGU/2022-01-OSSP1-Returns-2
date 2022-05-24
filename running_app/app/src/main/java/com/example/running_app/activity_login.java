package com.example.running_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class activity_login extends AppCompatActivity {

    EditText id,pw;
    Button login, signup;
    RetrofitInterface service;
    String loginId, loginPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //첫화면이라 뒤로가기 불가능

        // 기입 정보
        id = findViewById(R.id.tv_id);
        pw = findViewById(R.id.tv_pw);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줌.
        loginId = auto.getString("inputId", null);
        loginPw = auto.getString("inputPw", null);

        service = RetrofitClient.getClient().create(RetrofitInterface.class);

        // 회원가입 버튼
        signup = findViewById(R.id.btn_signup);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_signup.class);
            startActivity(intent);
        });

        // 로그인 버튼
        login = findViewById(R.id.btn_login);
        //        login.setOnClickListener(v -> {
//            Intent intent = new Intent(this, activity_home.class);
//            startActivity(intent);
//        });

        // autologin구현
        if (loginId!=null && loginPw!=null){
            if(loginId.length()!=0 && loginPw.length()!=0){ // 값이 존재할 경우
                Toast.makeText(activity_login.this, loginId+"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_login.this, activity_home.class);
                startActivity(intent);
                finish();
            }
        } else if(loginId==null && loginPw==null){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }


    }

    private void attemptLogin(){
        String mid = id.getText().toString();
        String mpassword = pw.getText().toString();
        startLogin(new LoginData(mid, mpassword));
    }

    private void startLogin(LoginData data){
        service.Login(data).enqueue(new Callback<LoginResponse>(){
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response){
                LoginResponse result = response.body();
                Toast.makeText(activity_login.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                if (result.getResult()) {
                    Intent intent = new Intent(getApplicationContext(), activity_home.class);
                    startActivity(intent);
                    if(id.getText().toString()!=null && pw.getText().toString()!=null){
                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", id.getText().toString());
                        autoLogin.putString("inputPw", pw.getText().toString());
                        autoLogin.commit();
                        Toast.makeText(activity_login.this, id.getText().toString()
                                +"님 환영합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t){
                Toast.makeText(activity_login.this, "로그인 에러", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러", t.getMessage());
            }
        });
    }
}
