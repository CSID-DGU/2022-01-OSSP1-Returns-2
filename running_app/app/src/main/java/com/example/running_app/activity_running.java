package com.example.running_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class activity_running extends AppCompatActivity {

    //멤버 변수화
    TextView runningTime, runningDistance;
    Button start_btn, end_btn, running_btn, home_btn, profile_btn;

    //상태를 표시하는 상수 지정.
    //각각의 숫자는 독립적인 개별 '상태' 의미

    public static final int INIT = 0; //처음
    public static final int RUN = 1; //실행중
    public static final int PAUSE = 2; //정지

    //상태값을 저장하는 변수
    //INIT은 초기값이다. 그걸 status 안에 넣는다. (0을 넣은 것)
    public static int status = INIT;

    //기록할때 순서 체크를 위한 변수
    private int cnt = 1;

    //타이머 시간 값을 저장할 변수
    private long baseTime,pauseTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_running);


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
                Intent intent = new Intent(getApplicationContext(),activity_home.class);
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
        runningTime = (TextView)findViewById(R.id.time);
        start_btn = findViewById(R.id.runningStart);
        end_btn = findViewById(R.id.runningEnd);
        // runningDistance = findViewById(R.id.distance);

        start_btn.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.runningStart :
                    start_btn.setText("일시 중단");
                    StaButton();
            }
        }
    };

    private void StaButton(){
        switch(status) {
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
        }
    }

    private String getTime(){
        //경과된 시간 체크

        long nowTime = SystemClock.elapsedRealtime();
        //시스템이 부팅된 이후의 시간?
        long overTime = nowTime - baseTime;

        long h = overTime/1000/3600;
        long m = overTime/1000/60;
        long s = (overTime/1000)%60;

        String recTime = String.format("%02d:%02d:%02d", h, m,s);

        return recTime;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            runningTime.setText(getTime());

            handler.sendEmptyMessage(0);
        }
    };
}