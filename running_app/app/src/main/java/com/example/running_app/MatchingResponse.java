package com.example.running_app;


import com.google.gson.annotations.SerializedName;

public class MatchingResponse {
    @SerializedName("result")
    private boolean result;

    @SerializedName("msg")
    private String msg;

    public boolean getResult(){return result;}
    public String getMsg(){return msg;}
}
