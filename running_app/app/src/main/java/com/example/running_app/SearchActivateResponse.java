package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class SearchActivateResponse {
    @SerializedName("data")
    public SearchActivateRoom[] rooms;

    @SerializedName("result")
    public boolean result;

    @SerializedName("msg")
    public String msg;

    public SearchActivateRoom[] getCourse(){return rooms;}
    public boolean getResult(){return result;}
    public String getMsg(){return msg;}
}
