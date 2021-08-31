package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.ssp.AdvertContent;

import java.util.List;

public class QueryAdvertContentResponse extends BaseResponse {
    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("resultMsg")
    private String resultMsg;

    @SerializedName("ads")
    private List<AdvertContent> advertContents;

    public String getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }

    public String getResultMsg()
    {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg)
    {
        this.resultMsg = resultMsg;
    }

    public List<AdvertContent> getAdvertContents()
    {
        return advertContents;
    }

    public void setAdvertContents(List<AdvertContent> advertContents)
    {
        this.advertContents = advertContents;
    }
}