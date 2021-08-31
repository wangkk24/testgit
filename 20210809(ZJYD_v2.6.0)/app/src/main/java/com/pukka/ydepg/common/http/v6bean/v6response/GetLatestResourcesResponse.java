package com.pukka.ydepg.common.http.v6bean.v6response;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;

import java.util.List;

/**
 * 检查配置数据有没有最新的，用于定期更新数据
 *
 * @FileName: com.pukka.ydepg.common.http.v6bean.v6response.GetLatestResourcesResponse.java
 * @author: luwm
 * @data: 2018-07-24 10:57
 * @Version V1.0 <描述当前版本功能>
 */
public class GetLatestResourcesResponse extends BaseResponse {
    /**
     * 该桌面上最新配置的策略匹配数据的最后更新时间，如果VSP侧检测资源位未发生变化，
     * 则此字段不返回，终端下次请求时继续沿用以前的资源位版本号。
     */
    @SerializedName("version")
    private String version;
    /**
     * 哪些资源位的数据发生变化。
     */
    @SerializedName("resourceIDs")
    private List<String> resourceIDs;
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getResourceIDs() {
        return resourceIDs;
    }

    public void setResourceIDs(List<String> resourceIDs) {
        this.resourceIDs = resourceIDs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }

    @Override
    public String toString()
    {
        return "GetLatestResourcesResponse{" + "version='" + version + '\'' + ", resourceIDs=" +
                resourceIDs + ", extensionFields=" + extensionFields + '}';
    }
}
