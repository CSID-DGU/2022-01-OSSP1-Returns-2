package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class CourseResponse {
    @SerializedName("data")
    public Course[] course;

    @SerializedName("result")
    public boolean result;

    @SerializedName("msg")
    public String msg;

    public Course[] getCourse(){return course;}
    public boolean getResult(){return result;}
    public String getMsg(){return msg;}
}


