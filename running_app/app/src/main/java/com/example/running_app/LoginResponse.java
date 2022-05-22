package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("result")
    private String result;

    @SerializedName("msg")
    private String msg;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("user_location_latitude")
    private String user_location_latitude;

    @SerializedName("user_location_longitude")
    private String user_location_longitude;

    @SerializedName("gender")
    private String gender;

    @SerializedName("career")
    private int career;

    @SerializedName("activity_place")
    private String activity_place;

    @SerializedName("average_face")
    private String average_face;

    @SerializedName("running_type")
    private String running_type;

    @SerializedName("match_with_course")
    private String match_with_course;

    @SerializedName("match_with_track")
    private String match_with_track;

    @SerializedName("id")
    private String id;

    public String getMsg(){return msg;}
    public String getResult(){return result;}
}
