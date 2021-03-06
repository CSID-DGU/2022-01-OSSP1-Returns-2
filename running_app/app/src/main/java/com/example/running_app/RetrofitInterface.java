package com.example.running_app;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("/user/login")
    Call<LoginResponse> Login(@Body LoginData data);

    @POST("/user/join")
    Call<SignupResponse> Signup(@Body SignupData data);

    @POST("/user/profile")
    Call<ProfileResponse> Profile(@Body ProfileData data);

    @POST("/matching/create")
    Call<MakeMatchingRoomResponse> Create(@Body MakeMatchingRoomData data);

    @GET("/course/selectALL")
    Call<CourseResponse> GetCourse();

    @GET("/matching/courseActivate")
    Call<SearchActivateResponse> GetRoom(@Query("courseNo") int courseNo);

    @POST("/result/runningResult")
    Call<RunningResultResponse> RunResult(@Body RunningResultData data);

    @POST("/matching/matching")
    Call<MatchingResponse> MatchingResult(@Body MatchingData data);

    @POST("/user/delete")
    Call<ProfileResponse> DeleteRoomId(@Body ProfileData data);

    @GET("/matching/room")
    Call<GetRoomResponse> GetByRoomId(@Query("room_id") int room_id);

    @POST("/course/courseRecommend")
    Call<RecommendResponse> GetRecommend(@Body RecommendData data);
}
