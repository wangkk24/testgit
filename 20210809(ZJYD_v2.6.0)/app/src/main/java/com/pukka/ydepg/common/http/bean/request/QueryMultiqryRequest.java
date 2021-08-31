package com.pukka.ydepg.common.http.bean.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liudo on 2018/9/17.
 */

public class QueryMultiqryRequest
{
    /**
     * 消息唯一标识, 生成规则:
     *appKey(6位)+deviceID(8位)+YYMMDDHHMMSS+4位序列号
     *本地时间.
     *
     */
    @SerializedName("messageID")
    private String messageID;

    /**
     * 用户号码
     */
    @SerializedName("billID")
    private String billID;


    @SerializedName("validType")
    private String validType;

    public String getMessageID()
    {
        return messageID;
    }

    public void setMessageID(String messageID)
    {
        this.messageID = messageID;
    }

    public String getBillID()
    {
        return billID;
    }

    public void setBillID(String billID)
    {
        this.billID = billID;
    }

    public String getValidType()
    {
        return validType;
    }

    public void setValidType(String validType)
    {
        this.validType = validType;
    }
}
