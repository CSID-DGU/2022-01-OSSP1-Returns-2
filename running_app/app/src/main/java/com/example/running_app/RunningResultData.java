package com.example.running_app;


import com.google.gson.annotations.SerializedName;

public class RunningResultData {

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("courseNo")
    public String courseNo;

    @SerializedName("time")
    public String time;

    @SerializedName("distance")
    public double distance;

    @SerializedName("run_rate")
    public double run_rate;

    @SerializedName("course_rate")
    public double course_rate;

    @SerializedName("room_id")
    public int room_id;

    public RunningResultData(String nickname, int room_id, String courseNo, String time, double distance, double run_rate, double course_rate){
        this.room_id = room_id;
        this.nickname = nickname;
        this.courseNo = courseNo;
        this.time = time;
        this.distance = distance;
        this.run_rate = run_rate;
        this.course_rate = course_rate;
    }
}
