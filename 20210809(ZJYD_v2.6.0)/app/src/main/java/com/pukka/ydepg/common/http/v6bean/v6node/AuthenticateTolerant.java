package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class AuthenticateTolerant {

    /**
     * areaCode :
     * templateName :
     * userGroup :
     * subnetID :
     * bossID :
     * extensionFields :
     */

    /**
     * 区域的外部编号。
     * 首次认证时，不需要携带该参数。
     * 用户认证成功后需要将认证响应中的areaCode写入终端，并在后续认证请求中传给VSP服务器，
     * 以便Envision Video 系统容灾后（如数据库故障，VSP服务器宕机），仍然能够为用户提供服务
     */
    @SerializedName("areaCode")
    private String areaCode;

    /**
     * 模板名称。
     * 首次认证时，不需要携带该参数。
     * 用户认证成功后需要将认证响应中的templateName写入终端，并在后续认证请求中传给用户网关(VSP)服务器，
     * 以便Envision Video 系统容灾后（如数据库故障，VSP服务器宕机），仍然能够为用户提供服务。
     * 如果终端不需要VSP平台提供模板管理功能，忽略此参数。
     */
    @SerializedName("templateName")
    private String templateName;

    /**
     * 用户组ID。
     * 首次认证时，不需要携带该参数。
     * 用户认证成功后需要将认证响应中的usergroup写入终端，并在后续认证请求中传给用户网关(VSP)服务器，
     * 以便Envision Video 系统容灾后（如数据库故障，VSP服务器宕机），仍然能够为用户提供服务。
     */
    @SerializedName("userGroup")
    private String userGroup;

    /**
     * 订户归属的子网运营商ID。
     * 首次认证时，不需要携带该参数。
     * 用户认证成功后需要将认证响应中的subnetId写入终端，并在后续认证请求中传给用户网关(VSP)服务器，
     * 以便Envision Video 系统容灾后（如数据库故障，VSP服务器宕机），仍然能够为用户提供服务。
     */
    @SerializedName("subnetID")
    private String subnetID;

    /**
     * 订户归属的BOSS编号。
     * 首次认证时，不需要携带该参数。
     * 用户认证成功后需要将认证响应中的bossID写入终端，并在后续认证请求中传给用户网关(VSP)服务器，
     * 以便Envision Video 系统容灾后（如数据库故障，VSP服务器宕机），仍然能够为用户提供服务
     */
    @SerializedName("bossID")
    private String bossID;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

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

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
