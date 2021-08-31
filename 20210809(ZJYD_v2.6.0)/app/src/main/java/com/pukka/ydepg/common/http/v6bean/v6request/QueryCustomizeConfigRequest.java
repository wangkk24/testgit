package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6request.QueryLocationRequest.java
 * @author: yh
 * @date: 2017-09-06 09:10
 */

public class QueryCustomizeConfigRequest {


    /**
     * key :
     * queryType : 0,2,4,5,7
     * isFuzzyQuery : 0
     * deviceModel :
     * language : English
     * standard : ISO 639-1
     * extensionFields : ["",""]
     */

    /**
     * 参数名称，如果携带多个参数名称则以英文逗号分隔；如果不指定，则查询终端或模板所有的参数信息。
     * 当queryType为3时，该参数必选，表示集成部件名。
     */
    @SerializedName("key")
    private String key;

    /**
     *
     查询类型，支持查询多个类型，以英文逗号分隔，取值范围：
     •0：查询终端的配置参数
     •1：查询模板的配置参数
     •2：查询VSP EPG参数
     •3：查询第三方系统集成参数[字段预留]
     •4：查询内容级别体系
     •5：查询可用语种列表
     •6：查询ISO编码列表
     •7：查询货币类型字母编码

     说明：
     1.当queryType为多个的时候，忽略key值(包括queryType为3的这种场景)。
     2.终端在查询多个配置信息时请审视需要的配置参数，如果某种类型的参数在业务中不需要，请从请求参数中过滤掉，以保证接口性能要求。
     */
    @SerializedName("queryType")
    private String queryType;

    /**
     当queryType取值为0和1时，可以指定查询类型，取值包括：
     •0：精确查询
     •1：模糊查询
     默认值为0。
     */
    @SerializedName("isFuzzyQuery")
    private int isFuzzyQuery;

    /**
     * 当queryType =0时，表示终端型号。如果终端未携带，默认取用户会话里的终端型号。
     */
    @SerializedName("deviceModel")
    private String deviceModel;

    /**
     *

     当queryType=6时，全称语种，该字段为必填项，平台当前支持如下语种包括（客户端传的值需要与以下选项全匹配，区分大小写），终端可以定制其他语种：
     •English
     •Chinese
     •Arabic
     •Hungarian
     •German

     默认值为English。

     */
    @SerializedName("language")
    private String language;

    /**
     * 当queryType=6时，ISO标准，该字段为必填项，平台当前支持的选项包括，终端可以定制其他ISO标准：
     •ISO 3166-1 alpha-2
     •ISO 639-1
     •ISO 639-2
     •ISO 639-3

     ISO 639-3目前仅支持英文和德文。

     */
    @SerializedName("standard")
    private String standard;


    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public int getIsFuzzyQuery() {
        return isFuzzyQuery;
    }

    public void setIsFuzzyQuery(int isFuzzyQuery) {
        this.isFuzzyQuery = isFuzzyQuery;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
