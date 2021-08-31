package com.pukka.ydepg.launcher.bean.request;

import com.google.gson.annotations.SerializedName;

/**
 * Login request body
 *
 */
public class ZJLoginRequest
{
    /**
     * The identity of subscriber
     */
    @SerializedName("userToken")
    private volatile String userToken;

    public ZJLoginRequest(String userToken)
    {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString()
    {
        return "ZJLoginRequest{" +
                "userToken='" + userToken + '\'' +
                '}';
    }
}