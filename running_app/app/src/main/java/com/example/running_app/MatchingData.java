package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class MatchingData {
    @SerializedName("nickname")
    public String nickname;

    @SerializedName("departure_time")
    public String departure_time;

    @SerializedName("running_time")
    public int running_time;

    @SerializedName("mate_gender")
    public String mate_gender;

    @SerializedName("user_lat")
    public double user_lat;

    @SerializedName("user_lng")
    public double user_lng;

    public MatchingData(String nickname, String departure_time, int running_time, String mate_gender, double user_lat, double user_lng){
        this.nickname = nickname;
        this.departure_time = departure_time;
        this.running_time = running_time;
        this.mate_gender = mate_gender;
        this.user_lat = user_lat;
        this.user_lng = user_lng;

    }
}
