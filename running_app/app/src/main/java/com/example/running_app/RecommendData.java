package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class RecommendData {

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    public RecommendData(String nickname, double latitude, double longitude){
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
