package com.pukka.ydepg.common.report.ubd.extension;

public class SMSData {

    //1.流水号	唯一编号(时间戳+MAC)
    private String streamNo;

    //2.操作时间	格式为:YYYYMMDDHHMMSS
    private String optTime;

    //3.操作类型	枚举值范围见#interface OptType
    private String optType;

    //4.机顶盒账号
    private String stbUser;

    //5.手机帐号，在XMPP操作场景中是发送消息的手机号；在绑定和解绑场景中是绑定或解绑的手机号
    private String mobilePhone;

    //6.设备类型	枚举值范围[0：手机 1：机顶盒]
    private String deviceType = "1";

    public String getStreamNo() {
        return streamNo;
    }

    public void setStreamNo(String streamNo) {
        this.streamNo = streamNo;
    }

    public String getOptTime() {
        return optTime;
    }

    public void setOptTime(String optTime) {
        this.optTime = optTime;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getStbUser() {
        return stbUser;
    }

    public void setStbUser(String stbUser) {
        this.stbUser = stbUser;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
