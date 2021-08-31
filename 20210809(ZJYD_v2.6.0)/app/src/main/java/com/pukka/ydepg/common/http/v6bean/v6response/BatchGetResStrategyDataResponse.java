package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.pukka.ydepg.common.http.v6bean.v6node.StrategyData;

import java.util.List;

/**
 * 资源位详情获取信息
 *
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.BatchGetResStrategyDataResponse.java
 * @author: luwm
 * @data: 2018-07-24 11:04
 * @Version V1.0 <描述当前版本功能>
 */
public class BatchGetResStrategyDataResponse extends BaseResponse{
    @SerializedName("resourceDatas")
    private List<StrategyData> resourceDatas;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<StrategyData> getResourceDatas() {
        return resourceDatas;
    }

    public void setResourceDatas(List<StrategyData> resourceDatas) {
        this.resourceDatas = resourceDatas;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    @Override
    public String toString() {
        return "BatchGetResStrategyDataResponse{" +
                "resourceDatas=" + resourceDatas +
                ", extensionFields=" + extensionFields +
                '}';
    }
}
