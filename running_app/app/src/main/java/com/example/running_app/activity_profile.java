package com.example.running_app;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

public class activity_profile extends AppCompatActivity{
    TextView userID, runCountKr, runCountNum, averPaceKr, averPaceNum, averKmKr, averKmNum;
    EditText introduce;
    Button homeButton, matchingButton, profileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //기입 항목
        introduce = (EditText) findViewById(R.id.introduce);
        introduce.setText("EditText is changed.");


        //하단 버튼
        homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        // 아직 매칭 페이지는 구현 안해서 경로 MainActivity로 잡아놓음.
        matchingButton = findViewById(R.id.matchingButton);
        matchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), activity_profile.class);
            }
        });
    }
}
