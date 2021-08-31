package com.pukka.ydepg.common.report.ubd.extension;

public class VersionData {

    //[序号1][不可为空]TV用户的标识,TV用户的标识
    private String userID;

    //[序号2]我的页面展示的"业务账号" Ex "billID":"vhzyh370184338"
    private String billID;

    //[序号3]本地安装版本 Ex "epgVersion":"ZJYD_v2.0.0"
    private String epgVersion;

    //[序号4]Ex "stbModel":"E900V21C"
    private String stbModel;

    //[序号5]Ex "stbID":"0042010008028910160254935992B43A"
    private String stbID;

    //[序号6]用户所属地区码
    private String areaID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getEpgVersion() {
        return epgVersion;
    }

    public void setEpgVersion(String epgVersion) {
        this.epgVersion = epgVersion;
    }

    public String getStbModel() {
        return stbModel;
    }

    public void setStbModel(String stbModel) {
        this.stbModel = stbModel;
    }

    public String getStbID() {
        return stbID;
    }

    public void setStbID(String stbID) {
        this.stbID = stbID;
    }

    public String getAreaID() {
        return areaID;
    }

    public void setAreaID(String areaID) {
        this.areaID = areaID;
    }
}
