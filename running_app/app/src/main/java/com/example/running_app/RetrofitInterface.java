package com.example.running_app;

import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;

public interface RetrofitInterface {
    @POST("/user/join")
    Call<SignupResponse> Signup(@Body SignupData data);

//    @POST("/user/login")
//    Call<LoginResponse> Login(@Body LoginData data);
}
