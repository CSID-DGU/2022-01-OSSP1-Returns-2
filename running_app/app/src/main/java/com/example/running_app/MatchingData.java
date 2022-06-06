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

    public MatchingData(String nickname, String departure_time, int running_time, String mate_gender){
        this.nickname = nickname;
        this.departure_time = departure_time;
        this.running_time = running_time;
        this.mate_gender = mate_gender;

    }
}
