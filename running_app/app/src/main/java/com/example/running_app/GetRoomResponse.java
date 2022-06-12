package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class GetRoomResponse {
    @SerializedName("courseNo")
    public int courseNo;

    @SerializedName("departure_time")
    public String departure_time;

    @SerializedName("running_time")
    public int running_time;

    @SerializedName("mate_gender")
    public String mate_gender;

    @SerializedName("msg")
    private String msg;
    @SerializedName("result")
    public boolean result;

    @SerializedName("start_latitude")
    public double start_latitude;

    @SerializedName("start_longitude")
    public double start_longitude;

    public int getCourseNo(){return courseNo;}
    public String getDeparture_time(){return departure_time;}
    public int getRunning_time(){return running_time;}
    public String getMate_gender(){return mate_gender;}
    public String getMsg(){return msg;}
    public boolean getResult(){return result;}

    public double getStart_latitude() {
        return start_latitude;
    }

    public double getStart_longitude() {
        return start_longitude;
    }
}
