package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

/**
 * 优惠券
 * Created by liudo on 2018/9/14.
 */

public class CouponInfo
{
    /**
     * 1.记名优惠券
     * 2.不记名兑换码
     */
    @SerializedName("type")
    private String type;

    /**
     * 优惠券归属类型
     * 1：VSS
     * 2: BOSS
     */
    @SerializedName("couponOriginalType")
    private String couponOriginalType;

    /**
     * 优惠券ID或兑换码
     */
    @SerializedName("code")
    private String code;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getCouponOriginalType()
    {
        return couponOriginalType;
    }

    public void setCouponOriginalType(String couponOriginalType)
    {
        this.couponOriginalType = couponOriginalType;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
