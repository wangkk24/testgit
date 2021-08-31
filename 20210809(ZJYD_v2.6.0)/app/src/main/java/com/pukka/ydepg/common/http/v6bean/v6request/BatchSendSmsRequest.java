package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class BatchSendSmsRequest {

    /**
    消息唯一标识, 生成规则:
    appKey(6位)+deviceID(8位)+YYMMDDHHMMSS+4位序列号
    本地时间.
    appKey: 获取对应的后6位值.
    deviceID: 预留给Partner标识服务器节点或者终端编号, 建议数字和字母组合使用.
    YYMMDDHHMMSS: 本地时间戳
    4位序列号: 0000 – 9999
    */
    @SerializedName("from")
    private String from;

    //用户标识
    @SerializedName("to")
    private String to;

    /**
    用户标示类型
    1：手机号，默认
    2：用户类型：比如说BOSS积分支付、手机账号支付、统一账号支付时会校验userId是否在VRS有效，无效则不发送验证码
    */
    @SerializedName("body")
    private String body;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
