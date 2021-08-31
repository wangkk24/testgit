package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class SendVerifiedCodeRequest {

    /**
    消息唯一标识, 生成规则:
    appKey(6位)+deviceID(8位)+YYMMDDHHMMSS+4位序列号
    本地时间.
    appKey: 获取对应的后6位值.
    deviceID: 预留给Partner标识服务器节点或者终端编号, 建议数字和字母组合使用.
    YYMMDDHHMMSS: 本地时间戳
    4位序列号: 0000 – 9999
    */
    @SerializedName("messageID")
    private String messageID;

    //用户标识
    @SerializedName("userId")
    private String userId;

    /**
    用户标示类型
    1：手机号，默认
    2：用户类型：比如说BOSS积分支付、手机账号支付、统一账号支付时会校验userId是否在VRS有效，无效则不发送验证码
    */
    @SerializedName("userType")
    private Integer userType;

    /**
    场景类型
    1：推荐有礼
    2：BOSS积分支付(2020.04增加)
    3：手机账号支付（2020.05增加）
    4：统一账号支付童锁（2020.05增加）
    */
    @SerializedName("sceneType")
    private Integer sceneType;

    /**
     当userType=2&&sceneType=2/3/4时必填，实际发送短信的手机号
     */
    @SerializedName("mobileNum")
    private String mobileNum;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getSceneType() {
        return sceneType;
    }

    public void setSceneType(Integer sceneType) {
        this.sceneType = sceneType;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }
}
