package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class SignupResponse {
    /**
     {
        "result" : boolean
        "msg" : String
     }
     **/
    @SerializedName("code")
    private int code;

    @SerializedName("result")
    public boolean result;

    @SerializedName("msg")
    public String msg;

    //생성자
    public SignupResponse(boolean result, String msg){
        this.result = result;
        this.msg = msg;
    }

    //getMethod
    public int getCode(){
        return code;
    }
    public boolean getResult(){
        return result;
    }
    public String getMsg(){
        return msg;
    }
}
