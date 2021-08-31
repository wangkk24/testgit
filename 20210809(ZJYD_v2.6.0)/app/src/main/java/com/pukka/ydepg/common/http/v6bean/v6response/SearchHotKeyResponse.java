package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Result;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wgy on 2017/4/25.
 */

public class SearchHotKeyResponse extends BaseResponse{

    /**
     * result :
     * total :
     * hotKeys :
     * extensionFields :
     */

    /**
     * 支持查询的搜索热词总数
     */
    @SerializedName("total")
    private String total;

    /**
     * 搜索热词关键字信息。有结果时返回。每个热词长度小于等于128
     */
    @SerializedName("hotKeys")
    private List<String> hotKeys;

    /**
     * 扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<String> getHotKeys() {
        return hotKeys;
    }

    public void setHotKeys(List<String> hotKeys) {
        this.hotKeys = hotKeys;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
