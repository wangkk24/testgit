package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;

public class QueryAdvertTokenResponse extends BaseResponse
{

    @SerializedName("AccessToken")
    private String accessToken;

    @SerializedName("ApplyType")
    private String applyType;

    @SerializedName("CreateTime")
    private String createTime;

    @SerializedName("Expires")
    private String expires;

    @SerializedName("Scope")
    private String scope;

    @SerializedName("AppKey")
    private String appKey;

    @SerializedName("UserID")
    private String userID;

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getApplyType()
    {
        return applyType;
    }

    public void setApplyType(String applyType)
    {
        this.applyType = applyType;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getExpires()
    {
        return expires;
    }

    public void setExpires(String expires)
    {
        this.expires = expires;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }
}
