package com.pukka.ydepg.common.http.v6bean.v6node;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: QueryDynamicRecmContent.java
 * @author: yh
 * @date: 2017-04-24 11:39
 */

public class QueryDynamicRecmContent {


    /**
     * recmScenarios : ["RecmScenario"]
     * network : 123
     * CPs : [""]
     * extensionFields : [""]
     */

    /**
     * 终端接入的网络类型，取值包括：

     1：WLAN
     2：Cellular
     如果为空，则表示不处理此字段。

     */
    @SerializedName("network")
    private String network;
    /**
     * 推荐位参数。
     *
     * 说明：
     如一个页面涉及多个独立推荐位，该接口支持批量查询，分别指定每个推荐位的推荐参数。
     推荐批量查询接口，接口响应时延/性能会随着推荐位的个数增加而降低。所以应尽量在1个接口传递较少的批量个数。
     为保证接口性能，V6基线版本默认在1次推荐接口请求中批量查询推荐位数量最大为2(后台参数可配置)，如在1次推荐接口请求中批量查询推荐位数量超过2，则接口返回批量查询数量超过系统限制错误码。

     */
    @SerializedName("recmScenarios")
    private List<RecmScenario> recmScenarios;
    /**
     * 推荐内容范围，该字段主要面向国内牌照方CP自行定制UI场景，在本UI推荐结果只推荐该牌照方CP编号对应的内容列表。取值VSP平台CP内部ID。

     该字段主要应用于国内解决方案。针对海外解决方案该字段不需使用，默认为空，则不需按CP条件过滤只返回属于该CP 的内容。

     默认为空。

     */
    @SerializedName("CPs")
    private List<String> CPs;

    /**
     * 扩展信息。

     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public List<RecmScenario> getRecmScenarios() {
        return recmScenarios;
    }

    public void setRecmScenarios(List<RecmScenario> recmScenarios) {
        this.recmScenarios = recmScenarios;
    }

    public List<String> getCPs() {
        return CPs;
    }

    public void setCPs(List<String> CPs) {
        this.CPs = CPs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
