package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class RunningResultResponse {

    @SerializedName("result")
    public boolean result;

    @SerializedName("msg")
    public String msg;

    public String getMsg(){return msg;}
    public boolean getResult(){return result;}
}
