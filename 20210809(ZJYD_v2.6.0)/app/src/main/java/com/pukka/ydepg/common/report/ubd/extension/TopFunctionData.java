package com.pukka.ydepg.common.report.ubd.extension;

public class TopFunctionData {

    public final static String BTN_NORMAL = "0";     //头部功能普通按键类型
    public final static String BTN_LAMP   = "1";     //头部功能跑马灯按键类型

    //[序号1]头部按键展示文本
    private String displayName;

    //[序号2]按键类型 0:普通按键 1:跑马灯
    private String buttonType;

    //[序号3]userID
    private String userID;

    //[序号4]版本号
    private String desktopVersion;

    //[序号3]时间戳 YYYYMMDD HH24:MM:SS 即订购成功时的时间戳
    private String timestamp;




    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDesktopVersion() {
        return desktopVersion;
    }

    public void setDesktopVersion(String version) {
        this.desktopVersion = version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
