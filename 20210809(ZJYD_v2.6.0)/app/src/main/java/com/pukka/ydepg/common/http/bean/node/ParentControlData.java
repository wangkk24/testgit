package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

public class ParentControlData {

    @SerializedName("gender")
    private String gender;


    @SerializedName("birthday")
    private String birthday;

    @SerializedName("switch")
    private String  onOrOff;

    @SerializedName("alltime")
    private String alltime;

    @SerializedName("singletime")
    private String singletime;

    @SerializedName("name")
    private String name;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOnOrOff() {
        return onOrOff;
    }

    public void setOnOrOff(String onOrOff) {
        this.onOrOff = onOrOff;
    }

    public String getAlltime() {
        return alltime;
    }

    public void setAlltime(String alltime) {
        this.alltime = alltime;
    }

    public String getSingletime() {
        return singletime;
    }

    public void setSingletime(String singletime) {
        this.singletime = singletime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
