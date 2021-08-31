package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wgy on 2017/4/24.
 */

public class Verimatrix {

    /**
     * company :
     * serverAddr :
     * serverPort :
     * VKSAddr :
     * CSMIP :
     * CSMPort :
     * multirights_widevine :
     * multiRights_playready :
     */

    /**
     * 申请VeriMatrix系统License时提供的公司信息，用于机顶盒与VeriMatrix系统的通信认证
     */
    @SerializedName("company")
    private String company;

    /**
     * VCI服务器的IP地址/域名，IPTV终端访问VMX CA的VCI地址，该服务器用于管理Client设备和服务器之间的安全通信接口。
     * IP地址支持V4或V6，平台根据终端接入的网络地址类型返回对应的服务器地址。
     */
    @SerializedName("serverAddr")
    private String serverAddr;

    /**
     * VCI服务使用的业务端口，IPTV终端访问VMX CA的VCI端口
     */
    @SerializedName("serverPort")
    private String serverPort;

    /**
     * VKS服务器的IP地址/域名，IPTV终端访问VMX CA的VKS地址，该服务器用于生成并管理频道密钥。
     * IP地址支持V4或V6，平台根据终端接入的网络地址类型返回对应的服务器地址
     */
    @SerializedName("VKSAddr")
    private String VKSAddr;

    /**
     * CSM的IP地址，OTT终端访问VMX CA的地址，VMX3.1以上版本才支持返回。
     * 该服务器用于设备的认证和授权，密钥的生成，管理和下发。
     * IP地址支持V4或V6，平台根据终端接入的网络地址类型返回对应的服务器地址。
     */
    @SerializedName("CSMIP")
    private String CSMIP;

    /**
     * CSM的端口，OTT终端访问VMX CA的端口，VMX3.1以上版本才支持返回
     */
    @SerializedName("CSMPort")
    private String CSMPort;

    /**
     * Verimatrix提供的Widevine服务器
     */
    @SerializedName("multirights_widevine")
    private MultirightsWidevine multirights_widevine;

    /**
     * Verimatrix提供的Playready服务器
     */
    @SerializedName("multiRights_playready")
    private MultiRightsPlayready multiRights_playready;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getVKSAddr() {
        return VKSAddr;
    }

    public void setVKSAddr(String VKSAddr) {
        this.VKSAddr = VKSAddr;
    }

    public String getCSMIP() {
        return CSMIP;
    }

    public void setCSMIP(String CSMIP) {
        this.CSMIP = CSMIP;
    }

    public String getCSMPort() {
        return CSMPort;
    }

    public void setCSMPort(String CSMPort) {
        this.CSMPort = CSMPort;
    }

    public MultirightsWidevine getMultirights_widevine() {
        return multirights_widevine;
    }

    public void setMultirights_widevine(MultirightsWidevine multirights_widevine) {
        this.multirights_widevine = multirights_widevine;
    }

    public MultiRightsPlayready getMultiRights_playready() {
        return multiRights_playready;
    }

    public void setMultiRights_playready(MultiRightsPlayready multiRights_playready) {
        this.multiRights_playready = multiRights_playready;
    }
}
