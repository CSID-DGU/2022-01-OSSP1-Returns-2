package com.example.running_app;


import com.google.gson.annotations.SerializedName;

public class RunningResultData {

    @SerializedName("courseNo")
    public String courseNo;

    @SerializedName("time")
    public String time;

    @SerializedName("distance")
    public double distance;

    @SerializedName("run_rate")
    public float run_rate;

    public RunningResultData(String courseNo, String time, double distance, float run_rate){
        this.courseNo = courseNo;
        this.time = time;
        this.distance = distance;
        this.run_rate = run_rate;
    }
}
