package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liudo on 2018/9/17.
 */

public class OfferInfo implements Serializable
{
    @SerializedName("offerName")
    private String offerName;

    @SerializedName("offerRoleID")
    private String offerRoleID;

    @SerializedName("offerRoleName")
    private  String offerRoleName;

    @SerializedName("doneDate")
    private String doneDate;

    @SerializedName("offerID")
    private  String offerID="";

    @SerializedName("offerTypeName")
    private  String offerTypeName;

    @SerializedName("offerEffTime")
    private  String offerEffTime;

    @SerializedName("doneCode")
    private  String doneCode;

    @SerializedName("offerExpTime")
    private  String offerExpTime;

    @SerializedName("offerType")
    private  String offerType;


    @SerializedName("offerGroupClass")
    private  String offerGroupClass;


    @SerializedName("offerInstID")
    private String offerInstID;

    @SerializedName("planReqRelat")
    private String planReqRelat;

    @SerializedName("productList")
    private List<ProductInfo> productList;

    public String getOfferName()
    {
        return offerName;
    }

    public void setOfferName(String offerName)
    {
        this.offerName = offerName;
    }

    public String getOfferRoleID()
    {
        return offerRoleID;
    }

    public void setOfferRoleID(String offerRoleID)
    {
        this.offerRoleID = offerRoleID;
    }

    public String getOfferRoleName()
    {
        return offerRoleName;
    }

    public void setOfferRoleName(String offerRoleName)
    {
        this.offerRoleName = offerRoleName;
    }

    public String getDoneDate()
    {
        return doneDate;
    }

    public void setDoneDate(String doneDate)
    {
        this.doneDate = doneDate;
    }

    public String getOfferID()
    {
        return offerID;
    }

    public void setOfferID(String offerID)
    {
        this.offerID = offerID;
    }

    public String getOfferTypeName()
    {
        return offerTypeName;
    }

    public void setOfferTypeName(String offerTypeName)
    {
        this.offerTypeName = offerTypeName;
    }

    public String getOfferEffTime()
    {
        return offerEffTime;
    }

    public void setOfferEffTime(String offerEffTime)
    {
        this.offerEffTime = offerEffTime;
    }

    public String getDoneCode()
    {
        return doneCode;
    }

    public void setDoneCode(String doneCode)
    {
        this.doneCode = doneCode;
    }

    public String getOfferExpTime()
    {
        return offerExpTime;
    }

    public void setOfferExpTime(String offerExpTime)
    {
        this.offerExpTime = offerExpTime;
    }

    public String getOfferType()
    {
        return offerType;
    }

    public void setOfferType(String offerType)
    {
        this.offerType = offerType;
    }

    public String getOfferGroupClass()
    {
        return offerGroupClass;
    }

    public void setOfferGroupClass(String offerGroupClass)
    {
        this.offerGroupClass = offerGroupClass;
    }

    public String getOfferInstID()
    {
        return offerInstID;
    }

    public void setOfferInstID(String offerInstID)
    {
        this.offerInstID = offerInstID;
    }

    public String getPlanReqRelat()
    {
        return planReqRelat;
    }

    public void setPlanReqRelat(String planReqRelat)
    {
        this.planReqRelat = planReqRelat;
    }

    public List<ProductInfo> getProductList()
    {
        return productList;
    }

    public void setProductList(List<ProductInfo> productList)
    {
        this.productList = productList;
    }
}
