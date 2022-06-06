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

    public RunningResultData(String nickname, String courseNo, String time, double distance, double run_rate, double course_rate){
        this.nickname = nickname;
        this.courseNo = courseNo;
        this.time = time;
        this.distance = distance;
        this.run_rate = run_rate;
        this.course_rate = course_rate;
    }
}
