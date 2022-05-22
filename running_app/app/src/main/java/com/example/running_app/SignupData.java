package com.example.running_app;

import com.google.gson.annotations.SerializedName;

public class SignupData {
    /**
     {
        "id" :
        "nickname" : String
        "email" : String
        "password" : String
        "gender" : String
        "career" : int
        "activity_place" : String
     }
     **/
    @SerializedName("id")
    public String id;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("gender")
    public String gender;

    @SerializedName("career")
    public int career;

    @SerializedName("activity_place")
    public String activity_place;

    //생성자
    public SignupData(String id, String nickname, String email, String password, String gender, int career, String activity_place){
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.career = career;
        this.activity_place = activity_place;
    }

    //getMethod
    public String getId(){
        return id;
    }
    public String getNickname(){
        return nickname;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getGender(){
        return gender;
    }
    public int getCareer(){
        return career;
    }
    public String getActivity_place(){
        return activity_place;
    }
}
