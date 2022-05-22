package com.example.running_app;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL= "http://10.0.2.2:5000";
    private static Retrofit retrofit = null;
    private RetrofitClient(){
    }

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) //요청을 보낼 base url을 설정
                    .addConverterFactory(GsonConverterFactory.create()) //JSON 파싱 위한 팩토리 추가.
                    .build();
        }
        return retrofit;
    }
}
