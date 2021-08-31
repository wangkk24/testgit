package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.bean.node.User;
import com.pukka.ydepg.launcher.bean.node.NamedParameter;

public class ConvertFeeToScoreRequest {

    //用户信息
    @SerializedName("userId")
    private User userId;

    //费用(货币最小单位，比如说浙江移动视频项目为分)
    @SerializedName("fee")
    private String fee;

    //币种Currency code，参考ISO 4217规范定义（不填默认为CNY）。如：CNY
    @SerializedName("currency")
    private String currency;

    //扩展字段
    @SerializedName("extentionInfo")
    private NamedParameter extentionInfo;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public NamedParameter getExtentionInfo() {
        return extentionInfo;
    }

    public void setExtentionInfo(NamedParameter extentionInfo) {
        this.extentionInfo = extentionInfo;
    }
}
