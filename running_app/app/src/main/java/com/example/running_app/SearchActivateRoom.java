package com.example.running_app;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class SearchActivateRoom {
//    rome_id : result[i].rome_id,
//    nickname : result[i].nickname,
//    departure_time : result[i].departure_time,
//    running_time : result[i].running_time,
//    mate_gender : result[i].mate_gender,
//    mate_level : result[i].mate_level,
//    start_latitude : result[i].start_latitude,
//    start_longitude : result[i].start_longitude
    @SerializedName("room_id")
    public int room_id;

    @SerializedName("nickname")
    public String nickname;

//    @SerializedName("departure_time")
//    public Timestamp departure_time;

    @SerializedName("running_time")
    public int running_time;

    @SerializedName("mate_gender")
    public String mate_gender;

    @SerializedName("mate_level")
    public int mate_level;

    @SerializedName("start_latitude")
    public double start_latitude;

    @SerializedName("start_longitude")
    public double start_longitude;

    public int getRoom_id(){return room_id;}
    public String getNickname(){return nickname;}
//    public Timestamp getDeparture_time(){return departure_time;}
    public int getRunning_time(){return running_time;}
    public String getMate_gender(){return mate_gender;}
    public int getMate_level(){return mate_level;}
    public double getStart_latitude(){return start_latitude;}
    public double getStart_longitude(){return start_longitude;}
}
