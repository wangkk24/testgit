package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: Device.java
 * @author: yh
 * @date: 2017-04-20 12:19
 */

public class Device {


    /**
     * ID : ID
     * name : name
     * deviceType : deviceType
     * deviceModel : deviceModel
     * status : 0
     * onlineState : 0
     * physicalDeviceID : physicalDeviceID
     * lastOfflineTime : lastOfflineTime
     * CADeviceInfos : CADeviceInfos
     * channelNamespace : channelNamespace
     * deviceToken : deviceToken
     * terminalVendor : terminalVendor
     * videoCodec : videoCodec
     * definition : definition
     * fps : fps
     * extensionFields : extensionFields
     * customFields : customFields
     */

    /**
     * 逻辑设备ID。
     */
    @SerializedName("ID")
    private String ID;

    /**
     *设备名称。
     */
    @SerializedName("name")
    private String name;

    /**
     *设备类型的编号，0~3是系统预置的枚举值，>=5是管理员自定义的设备类型。取值范围：
     0：STB
     1：PC Client
     2：OTT（包括PC Plugin、iOS和Andriod device）
     3：Mobile（注意这里的Mobile是MTV解决方案的设备）
     >=5: 自定义的设备类型
     说明：
     PC Client需要在PC本地安装APP。
     PC Plugin不需要在PC本地安装APP，通过IE浏览。
     */
    @SerializedName("deviceType")
    private String deviceType;

    /**
     *终端对应的设备型号，只有设备登录过且上报了终端型号，该属性才有值。
     */
    @SerializedName("deviceModel")
    private String deviceModel;

    /**
     *设备状态，取值包括:
     1：激活
     0：暂停
     默认值为1。
     */
    @SerializedName("status")
    private String status;

    /**
     *设备在线标识。
     取值范围：
     0：不在线
     1：在线
     2：待机
     3：休眠
     默认值为0
     */
    @SerializedName("onlineState")
    private String onlineState;

    /**
     *终端设备物理地址。
     对于STB设备，此参数为MAC地址。
     对于OTT设备，此参数为客户端生成的UUID独立设备编码。
     */
    @SerializedName("physicalDeviceID")
    private String physicalDeviceID;

    /**
     *终端上次下线时间。
     取值为距离1970年1月1号的毫秒数。
     如果设备从未登录过则不会返回。
     */
    @SerializedName("lastOfflineTime")
    private String lastOfflineTime;

    /**
     *CA设备信息。
     */
    @SerializedName("CADeviceInfos")
    private List<CADeviceInfo> CADeviceInfos;

    /**
     *设备对应的设备型号关联的频道命名空间。
     */
    @SerializedName("channelNamespace")
    private String channelNamespace;

    /**
     *对于XMPP方式，为从Push Server获取的设备token；
     对于代理模式，为APNS/GCM/MPNS分配的token。
     如果设备不在线，则不返回此属性。
     */
    @SerializedName("deviceToken")
    private String deviceToken;

    /**
     *设备厂商型号，比如iPhone对应的deviceModel是iPhone，terminalVendor是iPhone 4S。
     当使用SilverLight注册时填写Silverlight+版本号，如Silverlight4.1。
     */
    @SerializedName("terminalVendor")
    private String terminalVendor;

    /**
     *视频编码格式，取值包括：
     H.263
     H.264
     H.265
     如果终端支持多种编码格式，使用英文逗号分隔。
     */
    @SerializedName("videoCodec")
    private String videoCodec;

    /**
     *高清标清标识。取值范围：
     0：SD
     1：HD
     2：4K
     如果终端支持多种definition，使用英文逗号分隔。
     */
    @SerializedName("definition")
    private String definition;

    /**
     *内容帧率，单位是fps，取值范围(0,256]，默认值是30。
     */
    @SerializedName("fps")
    private String fps;

    /**
     *
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    /**
     *
     */
    @SerializedName("customFields")
    private List<NamedParameter> customFields;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public String getPhysicalDeviceID() {
        return physicalDeviceID;
    }

    public void setPhysicalDeviceID(String physicalDeviceID) {
        this.physicalDeviceID = physicalDeviceID;
    }

    public String getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(String lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public List<CADeviceInfo> getCADeviceInfos() {
        return CADeviceInfos;
    }

    public void setCADeviceInfos(List<CADeviceInfo> CADeviceInfos) {
        this.CADeviceInfos = CADeviceInfos;
    }

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getTerminalVendor() {
        return terminalVendor;
    }

    public void setTerminalVendor(String terminalVendor) {
        this.terminalVendor = terminalVendor;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    public List<NamedParameter> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<NamedParameter> customFields) {
        this.customFields = customFields;
    }
}
