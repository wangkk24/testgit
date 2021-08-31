package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean;

public class LoginRequest {
    private String deviceModel;
    private String subnetID;

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setSubnetID(String subnetID) {
        this.subnetID = subnetID;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getSubnetID() {
        return subnetID;
    }
}
