package com.pukka.ydepg.common.http.v6bean.v6request;


import com.google.gson.annotations.SerializedName;

/**动态桌面请求
 */
public class QueryLauncherRequest
{
    @SerializedName("version")
    private String version;

    @SerializedName("deviceModel")
    private String deviceModel;

    @SerializedName("userToken")
    private String userToken;

    @SerializedName("terminalVersion")
    private String terminalVersion;

    public String getTerminalVersion() {
        return terminalVersion;
    }

    public void setTerminalVersion(String terminalVersion) {
        this.terminalVersion = terminalVersion;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getDeviceModel()
    {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel)
    {
        this.deviceModel = deviceModel;
    }

    public String getUserToken()
    {
        return userToken;
    }

    public void setUserToken(String userToken)
    {
        this.userToken = userToken;
    }

    @Override
    public String toString()
    {
        return "QueryLauncherRequest{" +
                "version='" + version + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", userToken='" + userToken + '\'' +
                '}';
    }
}