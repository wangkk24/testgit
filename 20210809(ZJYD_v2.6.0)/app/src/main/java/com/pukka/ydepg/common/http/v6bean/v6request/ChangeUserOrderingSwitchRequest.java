package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class ChangeUserOrderingSwitchRequest {

    //用户号
    @SerializedName("userId")
    private String userId;

    /**
     * 童锁开关
     * 枚举值：|0|1|
     * 枚举值说明：0关闭 默认值；1开启
     * */
    @SerializedName("orderingSwitch")
    private Integer orderingSwitch;

    //变更渠道，调用方传参，建议按照|EPG|客服|后台|，这样的枚举值传
    @SerializedName("changeChannel")
    private String changeChannel;

    //操作流水号
    @SerializedName("sequenceID")
    private String sequenceID;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getOrderingSwitch() {
        return orderingSwitch;
    }

    public void setOrderingSwitch(Integer orderingSwitch) {
        this.orderingSwitch = orderingSwitch;
    }

    public String getChangeChannel() {
        return changeChannel;
    }

    public void setChangeChannel(String changeChannel) {
        this.changeChannel = changeChannel;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }
}
