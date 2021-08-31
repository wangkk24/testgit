package com.pukka.ydepg.common.report.ubd.extension;

public class DesktopTypeData {
    //[序号1][不可为空]TV用户的标识,TV用户的标识
    private String userID;

    //[序号3]本地安装版本 Ex "epgVersion":"ZJYD_v2.0.0"
    private String epgVersion;

    //start/switch
    private String action;

    //normal/child/simple
    private String desktopType;



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEpgVersion() {
        return epgVersion;
    }

    public void setEpgVersion(String epgVersion) {
        this.epgVersion = epgVersion;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }
}
