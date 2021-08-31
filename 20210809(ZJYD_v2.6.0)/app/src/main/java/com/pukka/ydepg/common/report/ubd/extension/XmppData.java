package com.pukka.ydepg.common.report.ubd.extension;

public class XmppData {

    //设备类型 0手机 1机顶盒
    public static final String DEVICE_STB = "1";

    public interface OptType {
        String PULL       = "0";   //拉屏
        String PUSH_LIVE  = "1";   //直播甩屏
        String VOICE      = "10";  //语音搜索
        String PUSH_VOD   = "13";  //点播甩屏
        String SWITCH     = "14";  //EPG模板切换
        String VIDEO_CALL = "15";  //视频通话(当前无此场景)
        String BIND       = "16";  //绑定
        String UNBIND     = "17";  //解绑
    }

    //1.流水号	唯一编号(时间戳+MAC)
    private String streamNo;

    //2.操作时间	格式为:YYYYMMDDHHMMSS
    private String optTime;

    //3.操作类型	枚举值范围见#interface OptType
    private String optType;

    //4.机顶盒账号
    private String stbUserId;

    //5.手机帐号，在XMPP操作场景中是发送消息的手机号；在绑定和解绑场景中是绑定或解绑的手机号
    private String mobilePhone;

    //6.设备类型	枚举值范围[0：手机 1：机顶盒]
    private String deviceType = DEVICE_STB;

    //7.发送XMPP的消息整体进行UrlEnCode处理
    private String params;


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

    public String getStbUserId() {
        return stbUserId;
    }

    public void setStbUserId(String stbUserId) {
        this.stbUserId = stbUserId;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}