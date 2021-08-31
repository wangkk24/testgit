package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 营销订购信息
 * Created by liudo on 2018/9/14.
 */

public class PromSubInfo
{

    /**
     * 营销活动id
     * BOSS观影券场景下不需要填写该值
     */
    @SerializedName("promotionId")
    private String promotionId;
    /**
     * 优惠券列表
     */

    @SerializedName("couponList")
    private List<CouponInfo> couponList;

    /**
     * M选N套餐场景下，自选产品
     */
    @SerializedName("alacarteProds")
    private List<SubProdInfo> alacarteProds;

    /**
     * 买X送Y套餐场景下，赠送产品选择
     */
    @SerializedName("presentProds")
    private List<SubProdInfo> presentProds;

    public String getPromotionId()
    {
        return promotionId;
    }

    public void setPromotionId(String promotionId)
    {
        this.promotionId = promotionId;
    }

    public List<CouponInfo> getCouponList()
    {
        return couponList;
    }

    public void setCouponList(List<CouponInfo> couponList)
    {
        this.couponList = couponList;
    }

    public List<SubProdInfo> getAlacarteProds()
    {
        return alacarteProds;
    }

    public void setAlacarteProds(List<SubProdInfo> alacarteProds)
    {
        this.alacarteProds = alacarteProds;
    }

    public List<SubProdInfo> getPresentProds()
    {
        return presentProds;
    }

    public void setPresentProds(List<SubProdInfo> presentProds)
    {
        this.presentProds = presentProds;
    }
}
