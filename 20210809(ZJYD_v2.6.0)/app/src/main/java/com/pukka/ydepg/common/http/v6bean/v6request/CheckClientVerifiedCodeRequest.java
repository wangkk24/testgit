package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class CheckClientVerifiedCodeRequest {
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
    @SerializedName("account")
    private String account;

    /**
     用户标示类型
     1：手机号，默认
     */
    @SerializedName("verifiedCodeType")
    private String verifiedCodeType;

    /**
     场景类型
     1：推荐有礼
     2：BOSS积分支付(2020.04添加)
     */
    @SerializedName("verifiedCode")
    private String verifiedCode;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVerifiedCodeType() {
        return verifiedCodeType;
    }

    public void setVerifiedCodeType(String verifiedCodeType) {
        this.verifiedCodeType = verifiedCodeType;
    }

    public String getVerifiedCode() {
        return verifiedCode;
    }

    public void setVerifiedCode(String verifiedCode) {
        this.verifiedCode = verifiedCode;
    }
}
