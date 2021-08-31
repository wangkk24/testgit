package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.PersonalDataVersion;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */
public class OnLineHeartbeatResponse extends BaseResponse{

    //用户合法标志。取值范围：true：合法  false：非法
    @SerializedName("userValid")
    private String userValid;

    /**
     * 心跳周期，单位：秒。客户端请务必要按照该间隔调用心跳接口。如果随意高频调用，将会严重影响VSP性能。
     * 心跳接口调用间隔默认900，即15分钟。
     * 如果VSP连续两个心跳时长仍未收到终端的心跳消息，即认为终端已经离线，将会释放会话
     */
    @SerializedName("nextCallInterval")
    private String nextCallInterval;

    //个性化数据的版本号
    @SerializedName("personalDataVersions")
    private PersonalDataVersion personalDataVersions;

    //用户区域的外部编号
    @SerializedName("areaCode")
    private String areaCode;

    //用户组ID
    @SerializedName("userGroup")
    private String userGroup;

    //订户ID
    @SerializedName("subscriberID")
    private String subscriberID;

    //用户设置的in/out数据是否采集属性 该字段终端可不关注，当前不推荐使用。
    @SerializedName("opt")
    private String opt;

    //CDN验证终端合法性的密钥
    @SerializedName("validInfo4Stream")
    private String validInfo4Stream;

    //CDN验证终端合法性的随机数
    @SerializedName("validKey4Stream")
    private String validKey4Stream;

    //用户模板最近更新的时间，取值为距离1970年1月1号的毫秒数
    @SerializedName("templateTimeStamp")
    private String templateTimeStamp;

    //是否支持实时采集用户页面浏览轨迹，取值包括：0：不支持 1：支持 默认值为0。
    @SerializedName("isSupportedUserLogCollect")
    private String isSupportedUserLogCollect;

    //用户频道列表过滤条件 当调用EPG的相关频道列表查询接口时，传入该值可提高性能
    @SerializedName("userFilter")
    private String userFilter;

    //用户VOD列表过滤条件 当调用EPG的相关VOD列表查询接口时，传入该值可提高性能
    private String userVODListFilter;

    //用户VOD详情过滤条件 当调用EPG的查询VOD详情接口时，传入该值可提高性能
    @SerializedName("userVODDetailFilter")
    private String userVODDetailFilter;

    //用户节目单列表过滤条件 当调用EPG的相关节目单列表查询接口时，传入该值可提高性能
    @SerializedName("userPlaybillListFilter")
    private String userPlaybillListFilter;

    //用户节目单详情过滤条件 当调用EPG的查询节目单详情接口时，传入该值可提高性能
    @SerializedName("userPlaybillDetailFilter")
    private String userPlaybillDetailFilter;

    //当前登录用户的令牌，由VSP平台在认证成功后自动生成，过期自动刷新
    @SerializedName("userToken")
    private String userToken;

    //扩展信息
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;








    public String getUserValid() {
        return userValid;
    }

    public void setUserValid(String userValid) {
        this.userValid = userValid;
    }

    public String getNextCallInterval() {
        return nextCallInterval;
    }

    public void setNextCallInterval(String nextCallInterval) {
        this.nextCallInterval = nextCallInterval;
    }

    public PersonalDataVersion getPersonalDataVersions() {
        return personalDataVersions;
    }

    public void setPersonalDataVersions(PersonalDataVersion personalDataVersions) {
        this.personalDataVersions = personalDataVersions;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }

    public String getValidInfo4Stream() {
        return validInfo4Stream;
    }

    public void setValidInfo4Stream(String validInfo4Stream) {
        this.validInfo4Stream = validInfo4Stream;
    }

    public String getValidKey4Stream() {
        return validKey4Stream;
    }

    public void setValidKey4Stream(String validKey4Stream) {
        this.validKey4Stream = validKey4Stream;
    }

    public String getTemplateTimeStamp() {
        return templateTimeStamp;
    }

    public void setTemplateTimeStamp(String templateTimeStamp) {
        this.templateTimeStamp = templateTimeStamp;
    }

    public String getIsSupportedUserLogCollect() {
        return isSupportedUserLogCollect;
    }

    public void setIsSupportedUserLogCollect(String isSupportedUserLogCollect) {
        this.isSupportedUserLogCollect = isSupportedUserLogCollect;
    }

    public String getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(String userFilter) {
        this.userFilter = userFilter;
    }

    public String getUserVODListFilter() {
        return userVODListFilter;
    }

    public void setUserVODListFilter(String userVODListFilter) {
        this.userVODListFilter = userVODListFilter;
    }

    public String getUserVODDetailFilter() {
        return userVODDetailFilter;
    }

    public void setUserVODDetailFilter(String userVODDetailFilter) {
        this.userVODDetailFilter = userVODDetailFilter;
    }

    public String getUserPlaybillListFilter() {
        return userPlaybillListFilter;
    }

    public void setUserPlaybillListFilter(String userPlaybillListFilter) {
        this.userPlaybillListFilter = userPlaybillListFilter;
    }

    public String getUserPlaybillDetailFilter() {
        return userPlaybillDetailFilter;
    }

    public void setUserPlaybillDetailFilter(String userPlaybillDetailFilter) {
        this.userPlaybillDetailFilter = userPlaybillDetailFilter;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    @Override
    public String toString() {
        return "OnLineHeartbeatResponse is as followed:>>>"  +
                "\n\tresultCode                = " + getResult().getRetCode() +
                "\n\tresultMsg                 = " + getResult().getRetMsg()  +
                "\n\tuserValid                 = " + userValid                +
                "\n\tnextCallInterval          = " + nextCallInterval         +
                "\n\tpersonalDataVersions      = " + personalDataVersions     +
                "\n\tareaCode                  = " + areaCode                 +
                "\n\tuserGroup                 = " + userGroup                +
                "\n\tsubscriberID              = " + subscriberID             +
                "\n\topt                       = " + opt                      +
                "\n\tvalidInfo4Stream          = " + validInfo4Stream         +
                "\n\tvalidKey4Stream           = " + validKey4Stream          +
                "\n\ttemplateTimeStamp         = " + templateTimeStamp        +
                "\n\tisSupportedUserLogCollect = " + isSupportedUserLogCollect+
                "\n\tuserFilter                = " + userFilter               +
                "\n\tuserVODListFilter         = " + userVODListFilter        +
                "\n\tuserVODDetailFilter       = " + userVODDetailFilter      +
                "\n\tuserPlaybillListFilter    = " + userPlaybillListFilter   +
                "\n\tuserPlaybillDetailFilter  = " + userPlaybillDetailFilter +
                "\n\tuserToken                 = " + userToken                +
                "\n\textensionFields           = " + extensionFields;
    }
}
