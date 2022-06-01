package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class SearchActivateData {
    @SerializedName("courseNo")
    public int courseNo;

    public SearchActivateData(int courseNo){
        this.courseNo = courseNo;
    }

    public int getCourseNo() {
        return courseNo;
    }
}
