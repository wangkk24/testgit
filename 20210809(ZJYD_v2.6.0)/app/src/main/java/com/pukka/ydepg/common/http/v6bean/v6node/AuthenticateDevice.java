package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class AuthenticateDevice {

    /**
     * physicalDeviceID :
     * terminalID :
     * STBSN :
     * deviceModel :
     * terminalVendor :
     * OSVersion :
     * STBVersion :
     * softwareVersion :
     * CADeviceInfos :
     * authTerminalID :
     * VUID :
     * extensionFields :
     */

    /**
     * 终端MAC地址，此属性不仅标识物理地址也用于CA设备。
     * 对于STB终端，取值必须填写STB设备的MAC地址
     * 对于支持播放功能的OTT设备，取值必须填写为CA客户端插件的caDeviceId（如VMX插件的caDeviceId）。
     * 说明：对于登录自助服务Portal的场景，终端以不绑定设备的方式登录，不需要填写此参数。
     */
    @SerializedName("physicalDeviceID")
    private String physicalDeviceID;

    /**
     * 终端的唯一标识
     */
    @SerializedName("terminalID")
    private String terminalID;

    /**
     * STB的设备外部编号
     */
    @SerializedName("STBSN")
    private String STBSN;

    /**
     * 终端型号。对于需要绑定设备登录的场景，此参数必填。对于自助服务Portal方式登录，不需填写。
     * VSP根据终端型号从配置的终端型号中查找确定终端所属领域。用户可以在VMPortal中的System->DeviceModel菜单查看对应的系统配置
     */
    @SerializedName("deviceModel")
    private String deviceModel;

    /**
     * 设备厂商型号，该参数由终端上报。比如iPhone对应的deviceModel是OTT，terminalVendor是iPhone 4S。
     * 当使用SilverLight注册时填写Silverlight+版本号，如Silverlight4.1。
     */
    @SerializedName("terminalVendor")
    private String terminalVendor;

    /**
     * 终端操作系统版本号，该参数由终端上报
     */
    @SerializedName("OSVersion")
    private String OSVersion;

    /**
     * STB版本，该参数由终端上报。
     * 用户网关(VSP)服务器根据该字段找到用户匹配的VSP模板，只有STB终端需要，其他终端不需要携带该参数
     */
    @SerializedName("STBVersion")
    private String STBVersion;

    /**
     * 终端应用的软件版本号，该参数由终端上报
     */
    @SerializedName("softwareVersion")
    private String softwareVersion;

    /**
     * CA设备信息。
     * 如果不携带该参数，默认为Verimatrix CA设备。在AuthenticateDevice对象中，本字段对应的CADeviceInfo对象中的参数有两点特殊说明如下：
     * caDeviceId默认为设备的mac地址（mac参数）。
     * caDeviceType由VSP根据终端型号（deviceModel参数）识别后确定
     */
    @SerializedName("CADeviceInfos")
    private List<CADeviceInfo> CADeviceInfos;

    /**
     * 终端的唯一标识，免密登录场景下必传。该参数是终端自带的参数
     */
    @SerializedName("authTerminalID")
    private String authTerminalID;

    /**
     * 设备向Verimatrix或MultiDRM注册后返回的ID，针对Verimatrix该字段仅在Verimatrix3.8及以后的版本中支持
     */
    @SerializedName("VUID")
    private String VUID;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getPhysicalDeviceID() {
        return physicalDeviceID;
    }

    public void setPhysicalDeviceID(String physicalDeviceID) {
        this.physicalDeviceID = physicalDeviceID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getSTBSN() {
        return STBSN;
    }

    public void setSTBSN(String STBSN) {
        this.STBSN = STBSN;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getTerminalVendor() {
        return terminalVendor;
    }

    public void setTerminalVendor(String terminalVendor) {
        this.terminalVendor = terminalVendor;
    }

    public String getOSVersion() {
        return OSVersion;
    }

    public void setOSVersion(String OSVersion) {
        this.OSVersion = OSVersion;
    }

    public String getSTBVersion() {
        return STBVersion;
    }

    public void setSTBVersion(String STBVersion) {
        this.STBVersion = STBVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public List<CADeviceInfo> getCADeviceInfos() {
        return CADeviceInfos;
    }

    public void setCADeviceInfos(List<CADeviceInfo> CADeviceInfos) {
        this.CADeviceInfos = CADeviceInfos;
    }

    public String getAuthTerminalID() {
        return authTerminalID;
    }

    public void setAuthTerminalID(String authTerminalID) {
        this.authTerminalID = authTerminalID;
    }

    public String getVUID() {
        return VUID;
    }

    public void setVUID(String VUID) {
        this.VUID = VUID;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
