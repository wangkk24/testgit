package com.pukka.ydepg.common;

/**
 * 应用设置界面对应的配置数据
 *
 * @author yinxing 00176760
 */
import com.google.gson.annotations.SerializedName;

public class AppConfig
{
    @SerializedName("versionCode")
    private int versionCode;
    @SerializedName("edsUrl")
    private String edsURL;
    @SerializedName("epgURL")
    private String sparedEpgUrl;
    @SerializedName("edsUrlHttp")
    private String edsURLHttp;
    @SerializedName("vspURL")
    private String sparedVspURL;
    @SerializedName("useHttps")
    private boolean useHTTPS;
    @SerializedName("shareURL")
    private String shareURL;
    @SerializedName("registerURL")
    private String registerURL;
    @SerializedName("queryAdvAndUpdateURL")
    private String queryAdvAndUpdateURL;
    @SerializedName("wxServiceURL")
    private String wxServiceURL;
    @SerializedName("xmppIP")
    private String xmppIP;
    @SerializedName("xmppPort")
    private int xmppPort;
    @SerializedName("caasName")
    private String caasName;
    @SerializedName("caasPassword")
    private String caasPassword;

    public int getVersionCode()
    {
        return versionCode;
    }

    public void setVersionCode(int versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getEdsURL() {
        return edsURL;
    }

    public void setEdsURL(String edsURL)
    {
        this.edsURL = edsURL;
    }

    public String getSparedEpgUrl()
    {
        return sparedEpgUrl;
    }

    public String getEdsURLHttp() {
        return edsURLHttp;
    }

    public void setEdsURLHttp(String edsURLHttp) {
        this.edsURLHttp = edsURLHttp;
    }

    public String getSparedVspURL() {
        return sparedVspURL;
    }

    public void setSparedVspURL(String sparedVspURL) {
        this.sparedVspURL = sparedVspURL;
    }

    public void setSparedEpgUrl(String sparedEpgUrl)
    {
        this.sparedEpgUrl = sparedEpgUrl;
    }

    public boolean isUseHTTPS()
    {
        return useHTTPS;
    }

    public void setUseHTTPS(boolean useHTTPS)
    {
        this.useHTTPS = useHTTPS;
    }

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public String getQueryAdvAndUpdateURL() {
        return queryAdvAndUpdateURL;
    }

    public void setQueryAdvAndUpdateURL(String queryAdvAndUpdateURL) {
        this.queryAdvAndUpdateURL = queryAdvAndUpdateURL;
    }

    public String getRegisterURL() {
        return this.registerURL;
    }

    public void setRegisterURL(String registerURL) {
        this.registerURL = registerURL;
    }

    public String getWxServiceURL() {
        return wxServiceURL;
    }

    public void setWxServiceURL(String wxServiceURL) {
        this.wxServiceURL = wxServiceURL;
    }

    public String getXmppIP() {
        return xmppIP;
    }

    public void setXmppIP(String xmppIP) {
        this.xmppIP = xmppIP;
    }

    public int getXmppPort() {
        return xmppPort;
    }

    public void setXmppPort(int xmppPort) {
        this.xmppPort = xmppPort;
    }

    public String getCaasName() {
        return caasName;
    }

    public String getCaasPassword() {
        return caasPassword;
    }

    public void setCaasName(String caasName) {
        this.caasName = caasName;
    }

    public void setCaasPassword(String caasPassword) {
        this.caasPassword = caasPassword;
    }
}