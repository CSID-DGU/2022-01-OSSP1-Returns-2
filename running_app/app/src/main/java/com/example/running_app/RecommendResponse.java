package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class RecommendResponse {

    @SerializedName("result")
    private boolean result;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private int data;

    public boolean getResult(){return result;}
    public String getMsg(){return msg;}
    public int getData(){return data;}

}
