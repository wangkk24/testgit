package com.pukka.ydepg.common.report.ubd.extension;

public class UpgradeData {

    //[序号1][不可为空]TV用户的标识,TV用户的标识
    private String userID;

    //[序号2]我的页面展示的"业务账号" Ex "billID":"vhzyh370184338"
    private String billID;

    //[序号3]本地安装版本 Ex "epgVersion":"ZJYD_v2.0.0"
    private String epgVersion;

    //[序号4]Ex "stbModel":"E900V21C"
    private String stbModel;

    //[序号5]服务器升级版本:"ZJYD_v2.1.0"
    private String upgradeVersion;

    //[序号6]是否强制升级 0:非强制  1:强制
    private String force;

    //[序号7]升级失败原因
    private String reason;

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

    public String getUpgradeVersion() {
        return upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
