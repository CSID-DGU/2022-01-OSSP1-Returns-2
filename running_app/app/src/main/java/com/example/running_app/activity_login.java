package com.example.running_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activity_login extends AppCompatActivity {

    EditText id,pw;
    Button login, signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //첫화면이라 뒤로가기 불가능

        // 기입 정보
        id = findViewById(R.id.tv_id);
        pw = findViewById(R.id.tv_pw);

        // 회원가입 버튼
        signup = findViewById(R.id.btn_signup);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_signup.class);
            startActivity(intent);
        });

        // 로그인 버튼
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_home.class);
            startActivity(intent);
        });

    }
}
