package com.pukka.ydepg.launcher.bean.response;

import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;


import java.util.List;

/**
 * Login response body
 */
public class ZJLoginResponse extends BaseResponse {
    private static final String TAG = ZJLoginResponse.class.getSimpleName();

    public  ZJLoginResponse() {}

    @SerializedName("subnetID")
    private String subnetID;

    @SerializedName("isFirstLogin")
    private String isFirstLogin;

    @SerializedName("bossID")
    private String bossID;
    /**
     * Address of the root certificate
     */
    @SerializedName("profiles")
    private List<Profile> profileList;

    @SerializedName("subscriberID")
    private String subscriberID;

    @SerializedName("timeZone")
    private String timeZone;

    @SerializedName("loginOccasion")
    private String loginOccasion;

    @SerializedName("deviceID")
    private String deviceID;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("areaCode")
    private String areaCode;

    @SerializedName("opt")
    private String opt;

    @SerializedName("jSessionID")
    private String jSessionID;

    @SerializedName("csrfToken")
    private String csrfToken;

    @SerializedName("templateName")
    private String templateName;

    @SerializedName("profileID")
    private String profileID;

    @SerializedName("profileSN")
    private String profileSN;

    @SerializedName("transportProtocol")
    private String transportProtocol;

    @SerializedName("loginIP")
    private List<String> loginIPList;

    @SerializedName("RRSAddr")
    private String RRSAddr;

    @SerializedName("userGroup")
    private String userGroup;

    @SerializedName("templateTimeStamp")
    private String templateTimeStamp;

    public String getSubnetID() {
        return subnetID;
    }

    public void setSubnetID(String subnetID) {
        this.subnetID = subnetID;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getBossID() {
        return bossID;
    }

    public void setBossID(String bossID) {
        this.bossID = bossID;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLoginOccasion() {
        return loginOccasion;
    }

    public void setLoginOccasion(String loginOccasion) {
        this.loginOccasion = loginOccasion;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getjSessionID() {
        return jSessionID;
    }

    public void setjSessionID(String jSessionID) {
        this.jSessionID = jSessionID;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getProfileSN() {
        return profileSN;
    }

    public void setProfileSN(String profileSN) {
        this.profileSN = profileSN;
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    public List<String> getLoginIPList() {
        return loginIPList;
    }

    public void setLoginIPList(List<String> loginIPList) {
        this.loginIPList = loginIPList;
    }

    public String getRRSAddr() {
        return RRSAddr;
    }

    public void setRRSAddr(String RRSAddr) {
        this.RRSAddr = RRSAddr;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getTemplateTimeStamp() {
        return templateTimeStamp;
    }

    public void setTemplateTimeStamp(String templateTimeStamp) {
        this.templateTimeStamp = templateTimeStamp;
    }

    @Override
    public String toString() {
        return "ZJLoginResponse{" +
                "subnetID='" + subnetID + '\'' +
                ", isFirstLogin='" + isFirstLogin + '\'' +
                ", bossID='" + bossID + '\'' +
                ", profileList=" + profileList +
                ", subscriberID='" + subscriberID + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", loginOccasion='" + loginOccasion + '\'' +
                ", deviceID='" + deviceID + '\'' +
                ", userToken='" + userToken + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", opt='" + opt + '\'' +
                ", jSessionID='" + jSessionID + '\'' +
                ", csrfToken='" + csrfToken + '\'' +
                ", templateName='" + templateName + '\'' +
                ", profileID='" + profileID + '\'' +
                ", profileSN='" + profileSN + '\'' +
                ", transportProtocol='" + transportProtocol + '\'' +
                ", loginIPList=" + loginIPList +
                ", RRSAddr='" + RRSAddr + '\'' +
                ", userGroup='" + userGroup + '\'' +
                ", templateTimeStamp='" + templateTimeStamp + '\'' +

                '}';
    }
}