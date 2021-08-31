package com.pukka.ydepg.common.http.bean.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liudo on 2018/9/27.
 */

public class VssResult
{

    /**
     * retCode :
     * retMsg :
     */

    @SerializedName("resultCode")
    private String retCode;

    @SerializedName("description")
    private String retMsg;

    public String getRetMsg()
    {
        return retMsg;
    }

    public void setRetMsg(String retMsg)
    {
        this.retMsg = retMsg;
    }

    public String getRetCode()
    {
        return retCode;
    }

    public void setRetCode(String retCode)
    {
        this.retCode = retCode;
    }
}
