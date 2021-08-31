package com.pukka.ydepg.launcher.bean.request;

import com.google.gson.annotations.SerializedName;

/**
 * Login request body
 *
 */
public class LoginRequest extends BaseRequest
{
    /**
     * The identity of subscriber
     */
    @SerializedName("subscriberID")
    private String subscriberId;

    /**
     * Terminal model
     */
    @SerializedName("deviceModel")
    private String deviceModel;


    public LoginRequest(String deviceModel)
    {
        this.deviceModel = deviceModel;
    }

    public String getSubscriberId()
    {
        return subscriberId;
    }

    public LoginRequest()
    {

    }

    public LoginRequest setSubscriberId(String subscriberId)
    {
        this.subscriberId = subscriberId;
        return this;
    }


    public String getDeviceModel()
    {
        return deviceModel;
    }

    public LoginRequest setDeviceModel(String deviceModel)
    {
        this.deviceModel = deviceModel;
        return this;
    }

    @Override
    public String toString()
    {
        return "LoginRequest{" +
                "subscriberId='" + subscriberId + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                '}';
    }
}