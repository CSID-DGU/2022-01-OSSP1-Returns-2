package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class ProfileData {
    @SerializedName("id")
    public String id;

    public ProfileData(String id){
        this.id = id;
    }
    public String getId(){return id;}
}
