package com.pukka.ydepg.common.http.v6bean.v6request;

import com.google.gson.annotations.SerializedName;

public class QueryAdvertTokenRequest
{

    @SerializedName("app_key")
    private String appKey;

    @SerializedName("app_secret")
    private String appSecret;

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }
}
