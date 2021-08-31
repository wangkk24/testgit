package com.pukka.ydepg.common.http.bean.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liudo on 2018/9/17.
 */

public class ProductInfo implements Serializable
{

    @SerializedName("productID")
    private  String productID;

    @SerializedName("prodType")
    private String prodType;


    @SerializedName("productName")
    private  String productName;

    @SerializedName("productExpTime")
    private  String productExpTime;

    @SerializedName("doneDate")
    private  String doneDate;

    @SerializedName("productEffTime")
    private String productEffTime;

    @SerializedName("prodTypeName")
    private  String prodTypeName;

    public String getProductID()
    {
        return productID;
    }

    public void setProductID(String productID)
    {
        this.productID = productID;
    }

    public String getProdType()
    {
        return prodType;
    }

    public void setProdType(String prodType)
    {
        this.prodType = prodType;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductExpTime()
    {
        return productExpTime;
    }

    public void setProductExpTime(String productExpTime)
    {
        this.productExpTime = productExpTime;
    }

    public String getDoneDate()
    {
        return doneDate;
    }

    public void setDoneDate(String doneDate)
    {
        this.doneDate = doneDate;
    }

    public String getProductEffTime()
    {
        return productEffTime;
    }

    public void setProductEffTime(String productEffTime)
    {
        this.productEffTime = productEffTime;
    }

    public String getProdTypeName()
    {
        return prodTypeName;
    }

    public void setProdTypeName(String prodTypeName)
    {
        this.prodTypeName = prodTypeName;
    }
}
