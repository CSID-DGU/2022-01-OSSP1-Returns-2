package com.example.running_app;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProfileResponse {
    @SerializedName("nickname")
    private String nickname;

    @SerializedName("running_count")
    private int running_count;

    @SerializedName("average_face")
    private String average_face;

    @SerializedName("average_distance")
    private Double average_distance;

    public String getNickname() {
        return nickname;
    }
    public int getRunning_count(){
        return running_count;
    }
    public String getAverage_face(){
        return average_face;
    }
    public Double getAverage_distance(){ return average_distance; }
}