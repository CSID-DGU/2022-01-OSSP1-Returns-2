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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class activity_signup extends AppCompatActivity {
    TextView back;
    EditText id,nickname,pw,pw2,career,email;
    CheckBox man,woman;
    String activity_location;
    String[] activity_locations = {"강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구",
    "도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구",
    "종로구","중구","중랑구"};
    Button pwcheckButton, submitButton;
    RetrofitInterface service;
    private ProgressBar mProgressView;

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
        career = findViewById(R.id.signCareer);
        email = findViewById(R.id.signmail);
        man = findViewById(R.id.signMan);
        woman = findViewById(R.id.signWoman);
        mProgressView = (ProgressBar) findViewById(R.id.join_progress);

        RetrofitInterface service = RetrofitClient.getClient().create(RetrofitInterface.class);

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
//        submitButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, activity_login.class);
//            startActivity(intent);
//        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
    }

    private void attemptSignup(){
        id.setError(null);
        nickname.setError(null);
        pw.setError(null);
        career.setError(null);
        email.setError(null);
        man.setError(null);
        woman.setError(null);
        boolean cancel = false;
        View focusView = null;

        String mid = id.getText().toString();
        String mnickname = nickname.getText().toString();
        String mpw = pw.getText().toString();
        String memail = email.getText().toString();
        String mman = man.getText().toString();
        String mwoman = woman.getText().toString();
        String mcareer = career.getText().toString();
        String gender = "";

        if (mid.isEmpty()){
            id.setError("id를 입력해주세요.");
            focusView = id;
            cancel = true;
        }
        if (mnickname.isEmpty()){
            id.setError("nickname을 입력해주세요.");
            focusView = nickname;
            cancel = true;
        }
        if (mpw.isEmpty()){
            id.setError("pw를 입력해주세요.");
            focusView = pw;
            cancel = true;
        }
        if (memail.isEmpty()){
            id.setError("email을 입력해주세요.");
            focusView = email;
            cancel = true;
        } else if(!isEmailValid(memail)){
            email.setError("@를 포함한 유효한 이메일을 입력하세요.");
            focusView = email;
            cancel = true;
        }
        if (mman.isEmpty() && mwoman.isEmpty()){
            man.setError("성별을 입력하세요.");
            cancel = true;
        } else{
            if(mman.isEmpty()){
                gender = mwoman;
            }else{
                gender = mman;
            }
        }

        if (cancel){
            focusView.requestFocus();
        } else{
            startSignup(new SignupData(mid, mnickname, memail, mpw, gender, Integer.parseInt(mcareer),activity_location));
            showProgress(true);
        }
    }

    private void startSignup(SignupData data){
        service.Signup(data).enqueue(new Callback<SignupResponse>(){
            //통신 성공시 호출
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response){
                SignupResponse result = response.body();
                Toast.makeText(activity_signup.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                showProgress(false);
//                if(result.getResult()){
//                    Log.e("test", "test");
//                    finish();
//                }
                if(result.getResult()){
                    Log.e("test", result.getMsg());
                }
            }
            //통신 실패시 호출
            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(activity_signup.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
                showProgress(false);
            }
        });
    }
    private boolean isEmailValid(String memail){
        return memail.contains("@");
    }
    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
