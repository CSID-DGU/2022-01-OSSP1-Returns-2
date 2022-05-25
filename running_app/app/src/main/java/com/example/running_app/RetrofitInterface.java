package com.example.running_app;

import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;

public interface RetrofitInterface {
    @POST("/user/login")
    Call<LoginResponse> Login(@Body LoginData data);

    @POST("/user/join")
    Call<SignupResponse> Signup(@Body SignupData data);

    @POST("/user/profile")
    Call<ProfileResponse> Profile(@Body ProfileData data);

    @POST("/matching/create")
    Call<MakeMatchingRoomResponse> Create(@Body MakeMatchingRoomData data);
}
