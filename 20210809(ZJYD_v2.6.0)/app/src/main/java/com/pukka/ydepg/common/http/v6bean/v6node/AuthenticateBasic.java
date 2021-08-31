package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: AuthenticateBasic.java
 * @author: yh
 * @date: 2017-04-24 11:13
 */

public class AuthenticateBasic {

    /**
     * userID :
     * userType :
     * authType :
     * authToken :
     * clientPasswd :
     * timeZone :
     * channelNamespace :
     * connectType :
     * bizdomain :
     * preSharedKeyID :
     * cnonce :
     * pageTrackerUIType :
     * lang :
     * isSupportWebpImgFormat :
     * needPosterTypes :
     * asDefaultProfile :
     * extensionFields :
     */

    /**
     *用户登录终端时输入的用户唯一标识，VSP平台根据此字段可以获取用户归属的订户，目前VSP平台支持以下几种取值：

     订户ID
     逻辑设备ID
     Profile的loginName，loginName属性说明请参见Profile。

     */
    @SerializedName("userID")
    private String userID;

    /**
     *终端输入的userID对应的用户标识类型，取值包括：

     0：订户ID，此时只校验订户密码(去掉主Profile密码的校验)。
     1：Profile的loginName，此时只校验User Profile密码。
     2：逻辑设备ID，此时只校验逻辑设备密码
     3：Guest用户，Guest用户认证可以不携带userID，但是如果终端上报了，VSP会以终端上报的为准。

     */
    @SerializedName("userType")
    private String userType;

    /**
     *认证方式，取值包括：

     0：基于密码认证，密码类型可以是订户密码、设备密码、Profile密码以及第三方认证系统分配的密码。
     1：免密登录用户名/密码认证（免密登录初次用户名/密码登录）。
     2：免密登录认证（免密登录再次登录认证）。
     注意：
     authType为2的时候，authToken为必填参数。

     authType为1或2的时候，userID为loginName必传，userType为1。

     */
    @SerializedName("authType")
    private String authType;

    /**
     *终端保存的免密登录流程所需的认证token。

     当authType值为2时，此字段必填。

     */
    @SerializedName("authToken")
    private String authToken;

    /**
     *由终端传入明文密码。

     */
    @SerializedName("clientPasswd")
    private String clientPasswd;

    /**
     *终端所属时区。

     支持夏令时的终端，格式类似Asia/Shanghai。
     仅支持多时区无夏令的终端，格式可以类似Asia/Shanghai或者GMT+03:30
     */
    @SerializedName("timeZone")
    private String timeZone;

    /**
     *默认频道查询使用的命名空间，如果不指定，VSP将按照终端型号绑定的频道命名空间处理。

     */
    @SerializedName("channelNamespace")
    private String channelNamespace;

    /**
     *OTT设备的网络接入方式，取值包括：

     1：WLAN
     2：Cellular
     默认值为1。

     */
    @SerializedName("connectType")
    private String connectType;

    /**
     *用户所属领域，多值使用英文逗号分隔，取值范围：

     0：IPTV
     1：MTV
     2：WebTV
     默认值为0。

     说明：
     该参数仅国内局点支持。

     */
    @SerializedName("bizdomain")
    private String bizdomain;

    /**
     *终端和用户网关(VSP)支持数据防篡改时，上报preSharedKey的编号。

     说明：
     数据防篡改的配置请参见VSP的VSC配置项antiTamperEnable，取值范围如下：

     No
     Yes
     默认值为No。

     VSP平台的系统参数配置请参考《 产品文档》，可以从Support官方网站获取对应版本的产品文档资料。

     */
    @SerializedName("preSharedKeyID")
    private String preSharedKeyID;

    /**
     *终端生成的随机字符串，用于生成敏感数据加密密钥。

     终端和用户网关(VSP) Server之间传输敏感数据时，不能明文传输，需要采用此加密密钥加密后传输，VSP保存该加密密钥，后续对敏感数据进行加密和解密时使用。

     */
    @SerializedName("cnonce")
    private String cnonce;

    /**
     *表示接入用户页面轨迹采集SDK的应用UI类型，对华为UI，默认数值如：HW_STB_VSPUI、HW_PC、HW_iPhone、HW_iPad、HW_AndroidPhone、HW_AndroidPad；对第三方UI，页面上操作员填写名称，如Youpeng_VSPUI。具体数值由多屏UI接入用户页面轨迹采集SDK时在网管进行统一分配。

     对应PageTracker中的type。

     */
    @SerializedName("pageTrackerUIType")
    private String pageTrackerUIType;

    /**
     *当前终端页面显示内容的语种属性。

     如果语种是订户共享的且语种还未设置，VSP将终端上报的locale作为订户语种。

     采用统一为ISO639-1缩写，如en。

     */
    @SerializedName("lang")
    private String lang;

    /**
     *终端支持图片格式

     0：返回当前gif、png、jpg格式图片
     1：只返回Webp格式图片
     2：两种格式文件均返回
     默认为0。

     */
    @SerializedName("isSupportWebpImgFormat")
    private String isSupportWebpImgFormat;

    /**
     *海报类型的枚举数组，具体取值如下：

     0：缩略图
     1 ：海报(对应DT的main)
     2 ：剧照
     3 ：图标
     4 ：标题图
     5 ：广告图
     6 ：草图
     7 ：背景图(对应DT的channelFallback)
     9 ：频道图片
     10： 频道黑白图片
     12 ：频道名字图片
     */
    @SerializedName("needPosterTypes")
    private List<String> needPosterTypes;

    /**
     *是否将该profile设置为默认profile

     0：否
     1：是
     默认值为0。

     如果接口请求中不传入该参数，则表示VSP平台保持当前设备上的默认profile不变化

     */
    @SerializedName("asDefaultProfile")
    private String asDefaultProfile;

    /**
     *扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public AuthenticateBasic() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getClientPasswd() {
        return clientPasswd;
    }

    public void setClientPasswd(String clientPasswd) {
        this.clientPasswd = clientPasswd;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getChannelNamespace() {
        return channelNamespace;
    }

    public void setChannelNamespace(String channelNamespace) {
        this.channelNamespace = channelNamespace;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getBizdomain() {
        return bizdomain;
    }

    public void setBizdomain(String bizdomain) {
        this.bizdomain = bizdomain;
    }

    public String getPreSharedKeyID() {
        return preSharedKeyID;
    }

    public void setPreSharedKeyID(String preSharedKeyID) {
        this.preSharedKeyID = preSharedKeyID;
    }

    public String getCnonce() {
        return cnonce;
    }

    public void setCnonce(String cnonce) {
        this.cnonce = cnonce;
    }

    public String getPageTrackerUIType() {
        return pageTrackerUIType;
    }

    public void setPageTrackerUIType(String pageTrackerUIType) {
        this.pageTrackerUIType = pageTrackerUIType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getIsSupportWebpImgFormat() {
        return isSupportWebpImgFormat;
    }

    public void setIsSupportWebpImgFormat(String isSupportWebpImgFormat) {
        this.isSupportWebpImgFormat = isSupportWebpImgFormat;
    }

    public List<String> getNeedPosterTypes() {
        return needPosterTypes;
    }

    public void setNeedPosterTypes(List<String> needPosterTypes) {
        this.needPosterTypes = needPosterTypes;
    }

    public String getAsDefaultProfile() {
        return asDefaultProfile;
    }

    public void setAsDefaultProfile(String asDefaultProfile) {
        this.asDefaultProfile = asDefaultProfile;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
