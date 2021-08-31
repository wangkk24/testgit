package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.CAInfo;
import com.pukka.ydepg.common.http.v6bean.v6node.Device;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PageTracker;
import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.UserLoginHistoryInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class AuthenticateResponse {

    /**
     * result :
     * userToken :
     * deviceID :
     * deviceName :
     * devices :
     * CA :
     * subscriberID :
     * profileID :
     * profileSN :
     * profiles :
     * isFirstLogin :
     * loginOccasion :
     * timeZone :
     * DSTTime :
     * areaCode :
     * templateName :
     * templateTimeStamp :
     * userGroup :
     * subnetID :
     * bossID :
     * bandWidth :
     * transportProtocol :
     * provisioningType :
     * loginIP :
     * location :
     * RRSAddr :
     * pageTrackers :
     * pictureURL :
     * antiTamperURI :
     * csrfToken :
     * isTriplePlay :
     * lockedNum :
     * waitUnlockTime :
     * remainLockedNum :
     * pwdResetTime :
     * STBRCUSubscribed :
     * opt :
     * hasCollectUserPreference :
     * userLoginHistoryInfo :
     * VUID :
     * network :
     * jSessionID :
     * authToken :
     * extensionFields :
     */

    /**
     *返回结果
     */
    @SerializedName("result")
    private Result result;

    /**
     *当前登录用户的令牌，由VSP平台在认证成功后自动生成
     */
    @SerializedName("userToken")
    private String userToken;

    /**
     *用户使用的物理设备对应的逻辑设备ID，当用户创建CPVR任务会用到该参数
     */
    @SerializedName("deviceID")
    private String deviceID;

    /**
     *返回当前设备的设备名称，只有认证请求携带了设备物理信息，才支持返回
     */
    @SerializedName("deviceName")
    private String deviceName;

    /**
     *用户的设备列表。
     在返回设备绑定达到上限时返回
     */
    @SerializedName("devices")
    private List<Device> devices;

    /**
     *Envision Video 系统可以支持对接多家CA厂商，每个CA厂商需要下发给终端的参数不完全相同。

     认证成功后返回。

     请参见“CAInfo”类型

     */
    @SerializedName("CA")
    private CAInfo CA;

    /**
     *订户ID。

     当登录成功或设备绑定达到上限时返回

     */
    @SerializedName("subscriberID")
    private String subscriberID;

    /**
     *如果请求参数userType为1或者支持Profile自动登录，返回用户对应的profileID。

     说明：
     Profile自动登录包括如下场景：当前登录设备带默认Profile；游客登录；订户只有一个Profile
     */
    @SerializedName("profileID")
    private String profileID;

    /**
     *如果请求参数userType为1或者支持Profile自动登录，返回用户对应的profileSn。

     说明：
     Profile自动登录包括如下场景：当前登录设备带默认Profile；游客登录；订户只有一个Profile

     */
    @SerializedName("profileSN")
    private String profileSN;

    /**
     *订户所有的User Profile信息
     */
    @SerializedName("profiles")
    private List<Profile> profiles;

    /**
     *如果userId是订户或者设备Id，表示订户是否是首次登录IPTV系统，如果userId是Profile的loginName，表示Profile是否是首次登录IPTV系统。

     取值范围：

     0：非首次登录
     1：首次登录
     */
    @SerializedName("isFirstLogin")
    private String isFirstLogin;

    /**
     *用户登录状态标识。

     认证成功后返回。

     取值范围：

     0：正常登录
     1：放通登录
     默认值为0。

     如果系统配置为支持放通，用户开机认证时，如果数据库或VSP故障，用户网关(VSP)服务器也会让用户认证通过，此时用户的登录状态即放通登录

     */
    @SerializedName("loginOccasion")
    private String loginOccasion;

    /**
     *终端所属时区。

     格式为：GMT+/-N。

     有些地方的时区并不是整数，N以“hh:ss”时间形式表示。如：“GMT+03:30”。

     说明：
     终端和平台支持UTC时区和本地时区，参考认证接口的UTCEnable字段。
     如果用本地时区的话，终端可以上报用户指定的时区，如果用户不指定的话，平台会根据用户归属的区域查询此区域的本地时区，区域是开户时指定的。
     */
    @SerializedName("timeZone")
    private String timeZone;

    /**
     *终端所在时区的夏令时时段。

     如果时区不支持夏令时可不携带该参数，否则返回“夏令时UTC开始时间，夏令时UTC结束时间”。

     取值为距离1970年1月1号的毫秒数。

     例如：“20070401080000,20071028065959”。

     */
    @SerializedName("DSTTime")
    private String DSTTime;

    /**
     *用户区域的外部编号，认证成功后返回。

     用户认证成功后需要写入终端

     */
    @SerializedName("areaCode")
    private String areaCode;

    /**
     *如果终端上报templatename，返回终端上报的取值，否则：

     如果是Profile认证的，平台首先查询Profile设置的模板名，如果Profile未设置模板则返回运营商给订户分配的默认模板。
     如果是订户/设备认证的，平台首先查询Admin Profile设置的模板名，如果Admin Profile未设置模板则返回运营商给订户分配的默认模板。
     如果是容灾用户，平台默认返回default
     */
    @SerializedName("templateName")
    private String templateName;

    /**
     *用户模板最近更新的时间，取值为距离1970年1月1号的毫秒数
     */
    @SerializedName("templateTimeStamp")
    private String templateTimeStamp;

    /**
     *用户组ID。

     用户认证成功后需要将认证响应中的usergroup写入终端，并在后续认证请求中传给用户网关(VSP)服务器，以便Envision Video 系统容灾后，仍然能够为用户提供服务

     */
    @SerializedName("userGroup")
    private String userGroup;

    /**
     *订户归属的子网运营商ID，终端首次开机登录成功后终端需要保存到本地，当下次开机时携带给VSP，以便Envision Video 系统容灾后，仍然能够为用户提供服务
     */
    @SerializedName("subnetID")
    private String subnetID;

    /**
     *订户归属的BOSS编号
     */
    @SerializedName("bossID")
    private String bossID;

    /**
     *用户带宽，认证成功后返回。

     单位：kbit/s

     */
    @SerializedName("bandWidth")
    private String bandWidth;

    /**
     *用户在观看单播时，终端根据TransportProtocol属性表示的协议来接收媒体流，对于不支持该属性的终端按照出厂设置处理。认证成功后返回。

     取值范围：

     -1：使用出厂设置
     0：TCP
     1：UDP
     2：RTP over TCP
     3：RTP over UDP
     默认值为-1

     */
    @SerializedName("transportProtocol")
    private String transportProtocol;

    /**
     *认证成功后，返回订户开通的业务类型。

     基线版本，取值包括：

     IPTV
     SAT-only
     Hybrid-IP
     OTT
     一个设备支持多种业务类型（使用英文逗号分隔），为兼容OTE的Hybrid-IP，Hybrid-IP特指”IPTV,DVB”。

     */
    @SerializedName("provisioningType")
    private List<String> provisioningType;

    /**
     *订户同一个时刻在其他地方登录的IP地址，支持IPv4和IPv6格式
     */
    @SerializedName("loginIP")
    private List<String> loginIP;

    /**
     *用户地理位置，为两位的国家码，遵从ISO 3166-1。仅在认证成功时返回
     */
    @SerializedName("location")
    private String location;

    /**
     *区域RRS地址，格式为http://rrsip:rrsport
     */
    @SerializedName("RRSAddr")
    private String RRSAddr;

    /**
     *采集用户页面浏览轨迹参数
     */
    @SerializedName("pageTrackers")
    private List<PageTracker> pageTrackers;

    /**
     *防盗链图片地址
     */
    @SerializedName("pictureURL")
    private List<String> pictureURL;

    /**
     *支持防篡改检查的接口，key为接口名，比如Play、Authorization等。

     value是需要做为数据一致性校验的属性名，入参和出参使用分号分隔，多个入参/出参之间使用英文逗号分隔。如果接口不需要校验入参或出参，使用空串代替

     */
    @SerializedName("antiTamperURI")
    private List<NamedParameter> antiTamperURI;

    /**
     *为了防止跨站攻击，服务器侧生成临时Token
     */
    @SerializedName("csrfToken")
    private String csrfToken;

    /**
     *是否是TriplePlay用户。

     取值范围：

     0：非TriplePlay用户
     1：TriplePlay用户
     默认值为0。

     是否需要携带该参数取决于局点需求

     */
    @SerializedName("isTriplePlay")
    private String isTriplePlay;

    /**
     *连续登录失败多少次锁定账号，如果局点不需要此功能，该字段将返回0
     */
    @SerializedName("lockedNum")
    private String lockedNum;

    /**
     *等待解锁的时间，单位：秒
     */
    @SerializedName("waitUnlockTime")
    private String waitUnlockTime;

    /**
     *还剩余尝试登录次数
     */
    @SerializedName("remainLockedNum")
    private String remainLockedNum;

    /**
     *密码重置时间，取值为距离1970年1月1号的毫秒数
     */
    @SerializedName("pwdResetTime")
    private String pwdResetTime;

    /**
     *用户是否订购了iPad遥控器业务。

     取值范围：

     0：未订购
     1：已订购
     默认值为0

     */
    @SerializedName("STBRCUSubscribed")
    private String STBRCUSubscribed;

    /**
     *表示用户是否同意平台采集自己的行为数据，取值包括：

     0：opt in
     1：opt out
     默认值为0

     */
    @SerializedName("opt")
    private String opt;

    /**
     *是否反馈用户初始偏好，取值范围：

     0：未反馈
     1：反馈
     默认值为0

     */
    @SerializedName("hasCollectUserPreference")
    private String hasCollectUserPreference;

    /**
     *本次认证以前的认证日志信息以及当前其它地方有效的登录信息（如果有的话）。

     */
    @SerializedName("userLoginHistoryInfo")
    private UserLoginHistoryInfo userLoginHistoryInfo;

    /**
     *设备向Verimatrix或MultiDRM注册后返回的ID，针对Verimatrix该字段仅在Verimatrix3.8及以后的版本中支持。

     说明：
     Verimatrix保护类型MulitRights的一种保护方式，该种保护方式下，向CA绑定设备时，会返回VUID，播放器请求播放时，携带该字符串到Verimatrix

     */
    @SerializedName("VUID")
    private String VUID;

    /**
     *设备所处网络。当前取值为如下字符串：

     “MultiRights” （对接Verimatrix MultiRights ）
     “MultiDRM ”（对接华为自研MultiDRM，当前不支持空字符串(即null)）
     */
    @SerializedName("network")
    private String network;

    /**
     *如果认证成功，返回会话ID
     */
    @SerializedName("jSessionID")
    private String jSessionID;

    /**
     *免密登录流程所需的认证token。

     当请求消息中的authenticateBasic中的authType值为1且用户登录认证成功时，返回此字段

     */
    @SerializedName("authToken")
    private String authToken;

    /**
     *
     */
    @SerializedName("sqmSessionId")
    private String sqmSessionId;

    @SerializedName("edsHttpsURLs")
    private List<String> edsHttpsURLs;

    @SerializedName("edsHttpURLs")
    private List<String> edsHttpURLs;

    public List<String> getEdsHttpsURLs() {
        return edsHttpsURLs;
    }

    public void setEdsHttpsURLs(List<String> edsHttpsURLs) {
        this.edsHttpsURLs = edsHttpsURLs;
    }

    public List<String> getEdsHttpURLs() {
        return edsHttpURLs;
    }

    public void setEdsHttpURLs(List<String> edsHttpURLs) {
        this.edsHttpURLs = edsHttpURLs;
    }

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public CAInfo getCA() {
        return CA;
    }

    public void setCA(CAInfo CA) {
        this.CA = CA;
    }

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getProfileSN() {
        return profileSN;
    }

    public void setProfileSN(String profileSN) {
        this.profileSN = profileSN;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getLoginOccasion() {
        return loginOccasion;
    }

    public void setLoginOccasion(String loginOccasion) {
        this.loginOccasion = loginOccasion;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDSTTime() {
        return DSTTime;
    }

    public void setDSTTime(String DSTTime) {
        this.DSTTime = DSTTime;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateTimeStamp() {
        return templateTimeStamp;
    }

    public void setTemplateTimeStamp(String templateTimeStamp) {
        this.templateTimeStamp = templateTimeStamp;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getSubnetID() {
        return subnetID;
    }

    public void setSubnetID(String subnetID) {
        this.subnetID = subnetID;
    }

    public String getBossID() {
        return bossID;
    }

    public void setBossID(String bossID) {
        this.bossID = bossID;
    }

    public String getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(String bandWidth) {
        this.bandWidth = bandWidth;
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    public List<String> getProvisioningType() {
        return provisioningType;
    }

    public void setProvisioningType(List<String> provisioningType) {
        this.provisioningType = provisioningType;
    }

    public List<String> getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(List<String> loginIP) {
        this.loginIP = loginIP;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRRSAddr() {
        return RRSAddr;
    }

    public void setRRSAddr(String RRSAddr) {
        this.RRSAddr = RRSAddr;
    }

    public List<PageTracker> getPageTrackers() {
        return pageTrackers;
    }

    public void setPageTrackers(List<PageTracker> pageTrackers) {
        this.pageTrackers = pageTrackers;
    }

    public List<String> getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(List<String> pictureURL) {
        this.pictureURL = pictureURL;
    }

    public List<NamedParameter> getAntiTamperURI() {
        return antiTamperURI;
    }

    public void setAntiTamperURI(List<NamedParameter> antiTamperURI) {
        this.antiTamperURI = antiTamperURI;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getIsTriplePlay() {
        return isTriplePlay;
    }

    public void setIsTriplePlay(String isTriplePlay) {
        this.isTriplePlay = isTriplePlay;
    }

    public String getLockedNum() {
        return lockedNum;
    }

    public void setLockedNum(String lockedNum) {
        this.lockedNum = lockedNum;
    }

    public String getWaitUnlockTime() {
        return waitUnlockTime;
    }

    public void setWaitUnlockTime(String waitUnlockTime) {
        this.waitUnlockTime = waitUnlockTime;
    }

    public String getRemainLockedNum() {
        return remainLockedNum;
    }

    public void setRemainLockedNum(String remainLockedNum) {
        this.remainLockedNum = remainLockedNum;
    }

    public String getPwdResetTime() {
        return pwdResetTime;
    }

    public void setPwdResetTime(String pwdResetTime) {
        this.pwdResetTime = pwdResetTime;
    }

    public String getSTBRCUSubscribed() {
        return STBRCUSubscribed;
    }

    public void setSTBRCUSubscribed(String STBRCUSubscribed) {
        this.STBRCUSubscribed = STBRCUSubscribed;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getHasCollectUserPreference() {
        return hasCollectUserPreference;
    }

    public void setHasCollectUserPreference(String hasCollectUserPreference) {
        this.hasCollectUserPreference = hasCollectUserPreference;
    }

    public UserLoginHistoryInfo getUserLoginHistoryInfo() {
        return userLoginHistoryInfo;
    }

    public void setUserLoginHistoryInfo(UserLoginHistoryInfo userLoginHistoryInfo) {
        this.userLoginHistoryInfo = userLoginHistoryInfo;
    }

    public String getVUID() {
        return VUID;
    }

    public void setVUID(String VUID) {
        this.VUID = VUID;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getjSessionID() {
        return jSessionID;
    }

    public void setjSessionID(String jSessionID) {
        this.jSessionID = jSessionID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public String getSqmSessionId() {
        return sqmSessionId;
    }

    public void setSqmSessionId(String sqmSessionId) {
        this.sqmSessionId = sqmSessionId;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
