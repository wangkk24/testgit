package com.pukka.ydepg.common.report.ubd.extension;

import com.pukka.ydepg.common.report.ubd.UBDConstant;

import static com.pukka.ydepg.common.report.ubd.UBDConstant.MANUAL_RECOMMEND_TYPE;

public class RecommendData {

    public interface RecommendType{
        String CVI_TYPE  = "0";
        String HAND_TYPE = MANUAL_RECOMMEND_TYPE;
        String IVOP_TYPE = "1";
    }

    //1.流水号	唯一编号(时间戳+MAC)
    private String streamNo;

    //2.操作时间	格式为:YYYYMMDDHHMMSS
    private String optTime;

    //3.操作类型	枚举值范围见#interface OptType
    private String optType;

    //4.机顶盒账号
    private String stbUser;

    //6.设备类型	枚举值范围[0：手机 1：机顶盒]
    private String deviceType = UBDConstant.DEVICE_STB;

    //推荐内容ID 以逗号（;）分隔
    private String recommendId;

    //appointedId 以逗号（;）分隔
    private String appointedId;

    //推荐方式 0：CVI智能推荐 ，1：手工推荐
    private String recommendType;

    private String contentID;

    private String sceneId;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

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

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(String recommendId) {
        this.recommendId = recommendId;
    }

    public String getAppointedId() {
        return appointedId;
    }

    public void setAppointedId(String appointedId) {
        this.appointedId = appointedId;
    }

    public String getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(String recommendType) {
        this.recommendType = recommendType;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }
}