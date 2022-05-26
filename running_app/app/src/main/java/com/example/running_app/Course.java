package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class Course {

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("courseNo")
    public String courseNo;

    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public String getCourseNo(){return courseNo;}
}
