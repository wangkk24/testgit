package com.pukka.ydepg.moudule.vrplayer.vrplayer.autoupdate.bean;

public class LoginRouteResponse extends SrvResponse{
    private String subnetId;
    private String vspURL;
    private String vspHttpsURL;

    public LoginRouteResponse() {
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getSubnetId() {
        return this.subnetId;
    }

    public void setVspURL(String vspURL) {
        this.vspURL = vspURL;
    }

    public String getVspURL() {
        return this.vspURL;
    }

    public void setVspHTTPSURL(String vspHTTPSURL) {
        this.vspHttpsURL = vspHTTPSURL;
    }

    public String getVspHTTPSURL() {
        return this.vspHttpsURL;
    }
}
