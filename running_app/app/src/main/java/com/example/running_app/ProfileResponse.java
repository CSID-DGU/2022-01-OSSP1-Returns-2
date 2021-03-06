package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("nickname")
    private String nickname;

    @SerializedName("running_count")
    private int running_count;

    @SerializedName("average_face")
    private String average_face;

    @SerializedName("average_distance")
    private Double average_distance;

    @SerializedName("room_id")
    private int room_id;

    @SerializedName("msg")
    private String msg;

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
    public int getRoom_id(){return room_id;}
    public String getMsg(){return msg;}
}