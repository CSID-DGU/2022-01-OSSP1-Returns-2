package com.example.running_app;

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

public class activity_signup extends AppCompatActivity {
    TextView back;
    EditText id,nickname,pw,pw2,age,career,email;
    CheckBox man,woman;
    String activity_location;
    String[] activity_locations = {"강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구",
    "도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구",
    "종로구","중구","중랑구"};
    Button pwcheckButton, submitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        //기입 항목
        id = findViewById(R.id.signID);
        nickname = findViewById(R.id.signNickName);
        pw = findViewById(R.id.signPW);
        pw2 = findViewById(R.id.signPW2);
        age = findViewById(R.id.signAge);
        career = findViewById(R.id.signCareer);
        email = findViewById(R.id.signmail);
        man = findViewById(R.id.signMan);
        woman = findViewById(R.id.signWoman);

        //드롭다운 박스
        @SuppressLint("WrongViewCast") Spinner spinner = findViewById(R.id.sign_activate_location);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_item,activity_locations);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity_location =  spinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //비밀번호 학인 버튼
        pwcheckButton = findViewById(R.id.pwcheckbutton);
        pwcheckButton.setOnClickListener(v -> {
            if(pw.getText().toString().equals(pw2.getText().toString())){
                pwcheckButton.setText("일치");
            } else {
                Toast.makeText(activity_signup.this,"비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });
        //회원가입 완료 버튼
        submitButton = findViewById(R.id.signupbutton);
        submitButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


    }
}
