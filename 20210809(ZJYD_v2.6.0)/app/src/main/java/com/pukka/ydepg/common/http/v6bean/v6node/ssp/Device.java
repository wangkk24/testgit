package com.pukka.ydepg.common.http.v6bean.v6node.ssp;

public class Device {

    private String type;

    private String make;

    private String model;

    //IMEI/STBID   (32位全小写MD5离散值)
    private String didmd5;

    //AndroidID   (32位全小写MD5离散值)
    private String dpidmd5;

    //MAC   (32位全小写MD5离散值)
    private String macmd5;



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDidmd5() {
        return didmd5;
    }

    public void setDidmd5(String didmd5) {
        this.didmd5 = didmd5;
    }

    public String getDpidmd5() {
        return dpidmd5;
    }

    public void setDpidmd5(String dpidmd5) {
        this.dpidmd5 = dpidmd5;
    }

    public String getMacmd5() {
        return macmd5;
    }

    public void setMacmd5(String macmd5) {
        this.macmd5 = macmd5;
    }
}
