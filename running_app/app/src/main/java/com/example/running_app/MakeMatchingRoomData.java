package com.example.running_app;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class MakeMatchingRoomData {
    @SerializedName("nickname")
    public String nickname;

    @SerializedName("departure_time")
    public String departure_time;

    @SerializedName("running_time")
    public int running_time;

    @SerializedName("mate_gender")
    public String mate_gender;

//    @SerializedName("mate_level")
//    public int mate_level;

    @SerializedName("start_latitude")
    public double start_latitude;

    @SerializedName("start_longitude")
    public double start_longitude;

    public MakeMatchingRoomData(String nickname, String departure_time, int running_time, String mate_gender, double start_latitude, double start_longitude){
        this.nickname = nickname;
        this.departure_time = departure_time;
        this.running_time = running_time;
//        this.mate_level = mate_level;
        this.mate_gender = mate_gender;
        this.start_latitude = start_latitude;
        this.start_longitude = start_longitude;
    }
}
